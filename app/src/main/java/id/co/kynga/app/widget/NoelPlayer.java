package id.co.kynga.app.widget;

import java.io.IOException;
import java.util.List;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.chunk.ChunkSampleSource.EventListener;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.upstream.BandwidthMeter;
import com.google.android.exoplayer.util.Util;
import id.co.kynga.app.R;
import id.co.kynga.app.exoplayer.DashRendererBuilder;
import id.co.kynga.app.exoplayer.DemoPlayer;
import id.co.kynga.app.exoplayer.DemoPlayer.RendererBuilder;
import id.co.kynga.app.exoplayer.ExoPlayerControl;
import id.co.kynga.app.exoplayer.ExtractorRendererBuilder;
import id.co.kynga.app.exoplayer.HlsRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingTestMediaDrmCallback;
import id.co.kynga.app.exoplayer.WidevineTestMediaDrmCallback;

import android.content.Context;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Toast;

public class NoelPlayer extends SurfaceView implements
		SurfaceHolder.Callback,
		OnClickListener,
		DemoPlayer.Listener,
		DemoPlayer.CaptionListener,
		DemoPlayer.Id3MetadataListener,
		DemoPlayer.InfoListener,
		AudioCapabilitiesReceiver.Listener,
		ExoPlayerControl.MediaPlayerControl,
		EventListener {
	public static final int TYPE_DASH = 0;
	public static final int TYPE_SS = 1;
	public static final int TYPE_HLS = 2;
	public static final int TYPE_OTHER = 3;
	private ExoPlayerControl controller;
	private Context mContext;
	private DemoPlayer player;
	private NoelPlayerCallback playerCallback;
	private Uri contentUri;
	private int contentType;
	private boolean playerNeedsPrepare;
	private long playerPosition;
	private boolean enableBackgroundAudio;
	private BandwidthMeter bandwidthMeter;
	private int currentWidth;
	private int currentHeight;
	private float aspectRatio;
	private int attempt = 0;
	private boolean initialized = false;
	private boolean nShowing = false;

	public NoelPlayer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public NoelPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public NoelPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onLoadCanceled(int arg0, long arg1) {
	}

	@Override
	public void onLoadError(int arg0, IOException arg1) {
	}

	@Override
	public void onUpstreamDiscarded(int arg0, long arg1, long arg2) {
	}

	@Override
	public void start() {
		if (player != null) {
			player.setPlayWhenReady(true);
		}
	}

	@Override
	public void pause() {
		if (player != null) {
			player.setPlayWhenReady(false);
		}
	}

	@Override
	public long getDuration() {
		if (player != null) {
			return player.getDuration();
		}
		return 0;
	}

	@Override
	public long getCurrentPosition() {
		if (player != null) {
			return player.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public void seekTo(long pos) {
		if (player != null) {
			player.seekTo(pos);
		}
	}

	@Override
	public boolean isPlaying() {
		if (player != null) {
			return player.getPlayWhenReady();
		}
		return false;
	}

	@Override
	public int getBufferPercentage() {
		if (player != null) {
			return player.getBufferedPercentage();
		}
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public void toggleFullScreen() {
		if (playerCallback != null) {
			playerCallback.switchFullScreen(currentWidth, currentHeight, aspectRatio);
		}
	}

	@Override
	public void onShow() {
		if (playerCallback != null) {
			playerCallback.onShow(controller);
		}
	}

	@Override
	public void onBitrateChange(String variantUrl, boolean isAuto) {
		//Toast.makeText(mContext, "Change rate happened", Toast.LENGTH_LONG).show();
		if (isAuto) {
			preparePlayer(true);
		} else {
			changeVideo(Uri.parse(variantUrl));
		}

	}

	@Override
	public void onAudioCapabilitiesChanged(AudioCapabilities arg0) {
	}

	@Override
	public void onVideoFormatEnabled(Format format, int trigger,
	                                 long mediaTimeMs) {
	}

	@Override
	public void onAudioFormatEnabled(Format format, int trigger,
	                                 long mediaTimeMs) {
	}

	@Override
	public void onDroppedFrames(int count, long elapsed) {
	}

	@Override
	public void onBandwidthSample(int elapsedMs, long bytes,
	                              long bitrateEstimate) {
	}

	@Override
	public void onLoadStarted(int sourceId, long length, int type, int trigger,
	                          Format format, long mediaStartTimeMs, long mediaEndTimeMs) {
	}

	@Override
	public void onLoadCompleted(int sourceId, long bytesLoaded, int type,
	                            int trigger, Format format, long mediaStartTimeMs,
	                            long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs) {
	}

	@Override
	public void onDecoderInitialized(String decoderName,
	                                 long elapsedRealtimeMs, long initializationDurationMs) {
	}

	@Override
	public void onAvailableRangeChanged(int sourceId, TimeRange availableRange) {

	}

	@Override
	public void onCues(List<Cue> cues) {
	}

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		if (playerCallback != null) {
			playerCallback.onStateChanged(playWhenReady, playbackState);
		}
		switch (playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				if (controller != null) {
					controller.setEnabled(false);
				}
				break;
			case ExoPlayer.STATE_ENDED:
				if (controller != null) {
					controller.setEnabled(true);
				}
				break;
			case ExoPlayer.STATE_IDLE:
				if (controller != null) {
					controller.setEnabled(true);
				}
				break;
			case ExoPlayer.STATE_PREPARING:
				if (controller != null) {
					controller.setEnabled(false);
				}
				break;
			case ExoPlayer.STATE_READY:
				if (controller != null) {
					controller.setEnabled(true);
				}
				break;
			default:
				if (controller != null) {
					controller.setEnabled(true);
				}
				break;
		}
	}

	@Override
	public void onError(Exception e) {
		if (e instanceof UnsupportedDrmException) {
			// Special case DRM failures.
			UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
			int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
					: unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
					? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
			Toast.makeText(mContext, stringId, Toast.LENGTH_LONG).show();
		}
		if (attempt < 5) {
			preparePlayer(true);
			attempt++;
		} else {

			playerNeedsPrepare = true;
			if (playerCallback != null) {
				playerCallback.onError(e);
			}
		}
	}

	@Override
	public void onVideoSizeChanged(int width, int height,
	                               int unappliedRotationDegrees, float pixelWidthHeightRatio) {
		if (playerCallback != null) {
			playerCallback.onVideoSizeChanged(width, height,
					unappliedRotationDegrees, pixelWidthHeightRatio);
		}
		currentWidth = width;
		currentHeight = height;
		aspectRatio = pixelWidthHeightRatio;
	}

	@Override
	public void onDownstreamFormatChanged(int sourceId, Format format,
	                                      int trigger, long mediaTimeMs) {
		if (playerCallback != null) {
			playerCallback.onDownstreamFormatChanged(sourceId, format, trigger, mediaTimeMs);
		}
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (player != null) {
			player.setSurface(holder.getSurface());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	                           int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (player != null) {
			player.blockingClearSurface();
		}
	}

	private RendererBuilder getRendererBuilder(Uri uri2) {
		String userAgent = Util.getUserAgent(mContext, "NoelPlayer");
		switch (contentType) {
			case TYPE_SS:
				return new SmoothStreamingRendererBuilder(mContext, userAgent, uri2.toString(),
						new SmoothStreamingTestMediaDrmCallback());
			case TYPE_DASH:
				return new DashRendererBuilder(mContext, userAgent, uri2.toString(),
						new WidevineTestMediaDrmCallback());
			case TYPE_HLS:
				return new HlsRendererBuilder(mContext, userAgent, uri2.toString());
			case TYPE_OTHER:
				return new ExtractorRendererBuilder(mContext, userAgent, uri2);
			default:
				throw new IllegalStateException("Unsupported type: " + contentType);
		}
	}

	private void preparePlayer(boolean playWhenReady) {
		if (player == null) {
			player = new DemoPlayer(getRendererBuilder(contentUri));
			player.addListener(this);
			player.setCaptionListener(this);
			player.setMetadataListener(this);
			player.seekTo(playerPosition);
			playerNeedsPrepare = true;

		}
		if (playerNeedsPrepare) {
			player.prepare();
			playerNeedsPrepare = false;

		}
		getHolder().setFormat(PixelFormat.TRANSPARENT);
		getHolder().setFormat(PixelFormat.OPAQUE);
		player.setSurface(getHolder().getSurface());
		player.setPlayWhenReady(playWhenReady);
		bandwidthMeter = player.getBandwidthMeter();
	}

	private void releasePlayer() {
		if (player != null) {
			playerPosition = player.getCurrentPosition();
			player.release();
			player = null;
		}
	}

	private void changeVideo(Uri uri1) {
		playerPosition = player.getCurrentPosition();
		player.stop();
		player.seekTo(0L);

		//you must change your contentUri before invoke getRendererBuilder();
		player.setRendererBuilder(getRendererBuilder(uri1));
		player.seekTo(playerPosition);
		player.prepare();
		playerNeedsPrepare = false;
	}
	
	/*
	 * public method
	 */

	public void setPlayerCallback(NoelPlayerCallback callback) {
		playerCallback = callback;
	}

	public void setContentUri(Context context, Uri uri) {
		contentUri = uri;
		mContext = context;
		attempt = 0;
		initialized = true;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setType(int type) {
		contentType = type;
	}

	public void enableControler() {
		if (mContext == null) {
			return;
		}
		controller = new ExoPlayerControl(mContext);
		controller.setMediaPlayer(this);
	}

	public void setControlAnchorView(ViewGroup viewGroup) {
		controller.setAnchorView(viewGroup);
	}

	public void clearSurface() {
		getHolder().setFormat(PixelFormat.TRANSPARENT);
	}

	public boolean isControllerShowing() {
		return nShowing;
	}


	public void showController() {
		if (controller != null) {
			Animation slide_up = AnimationUtils.loadAnimation(mContext, R.anim.draw_from_bottom);
			controller.show();
			controller.startAnimation(slide_up);
			nShowing = true;
		}
	}

	public void hideController() {
		if (controller != null) {
			Animation slide_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
			controller.startAnimation(slide_down);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					controller.hide();
				}

			}, 800);
			nShowing = false;
		}
	}

	public void delayController() {
		if (controller != null && !controller.isShowing()) {
			controller.show();
		}
	}

	public void startPlayback() {
		preparePlayer(true);
	}

	public void release() {
		releasePlayer();
	}

	public BandwidthMeter getBandwithMeter() {
		return bandwidthMeter;

	}

	@Override
	public void onId3Metadata(List<Id3Frame> id3Frames) {

	}


	public interface NoelPlayerCallback {
		void onError(Exception e);

		void onDownstreamFormatChanged(int sourceId, Format format,
		                               int trigger, long mediaTimeMs);

		void onVideoSizeChanged(int width, int height,
		                        int unappliedRotationDegrees, float pixelWidthHeightRatio);

		void onStateChanged(boolean playWhenReady, int playbackState);

		void onBitrateChange(String variantUrl, boolean isAuto);

		void switchFullScreen(int w, int h, float aspect);

		void onShow(View v);

	}
}
