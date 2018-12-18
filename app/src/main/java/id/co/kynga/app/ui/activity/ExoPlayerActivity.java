package id.co.kynga.app.ui.activity;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.TimeRange;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.chunk.ChunkSampleSource.EventListener;
import com.google.android.exoplayer.chunk.Format;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.exoplayer.DemoPlayer;
import id.co.kynga.app.exoplayer.ExoPlayerControl;
import id.co.kynga.app.exoplayer.ExoPlayerInterface;
import id.co.kynga.app.exoplayer.ExtractorRendererBuilder;
import id.co.kynga.app.exoplayer.DemoPlayer.RendererBuilder;
import id.co.kynga.app.exoplayer.HlsRendererBuilder;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.ForceLoginDialog;
import id.co.kynga.app.util.PreferenceUtil;
import id.co.kynga.app.util.Utils;
import id.co.kynga.app.util.ForceLoginDialog.LoginListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExoPlayerActivity extends FragmentActivity implements
		SurfaceHolder.Callback,
		OnClickListener,
		DemoPlayer.Listener,
		DemoPlayer.CaptionListener,
		DemoPlayer.Id3MetadataListener,
		DemoPlayer.InfoListener,
		AudioCapabilitiesReceiver.Listener,
		ExoPlayerControl.MediaPlayerControl,
		EventListener,
		LoginListener {

	public static final String CONTENT_TYPE_EXTRA = "content_type";
	public static final int TYPE_HLS = 2;
	public static final int TYPE_OTHER = 3;
	private static ExoPlayerInterface.OnBitrateAvailableListener onBitrateAvailableListener;
	private static ExoPlayerInterface.OnTitleListener onTitleListener;
	private int currentWidth, currentHeight;
	private float aspectRatio;
	private static final int MODE_STANDARD = 1;
	private static final int MODE_FULL_WITH_ASPECT_RATIO = 2;
	private static final int MODE_FULLSCREEN = 3;
	private int currentMode = 1;
	private DisplayMetrics metrics;

	private static final CookieManager defaultCookieManager;

	static {
		defaultCookieManager = new CookieManager();
		defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
	}

	private AspectRatioFrameLayout videoFrame;
	private SurfaceView surfaceView;
	private ExoPlayerControl controller;
	private DemoPlayer player;
	private ProgressBar pb;
	private TextView tBuffer;
	private boolean playerNeedsPrepare;

	private long playerPosition;

	private int contentType;
	private String title;
	private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
	private PreferenceUtil pref;
	private ForceLoginDialog fDialog;
	private boolean isWarned = false;
	private Handler handler = new Handler();
	private String crid = "";
	private int attempt = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video_player);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		surfaceView.getHolder().addCallback(this);
		controller = new ExoPlayerControl(this);
		videoFrame = (AspectRatioFrameLayout) findViewById(R.id.videoFrame);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		controller.setAnchorView(videoFrame);
		controller.setMediaPlayer(this);
		tBuffer = (TextView) findViewById(R.id.textDebug);
		audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(this, this);
		audioCapabilitiesReceiver.register();
		pref = new PreferenceUtil();
		CookieHandler currentHandler = CookieHandler.getDefault();
		if (currentHandler != defaultCookieManager) {
			CookieHandler.setDefault(defaultCookieManager);
		}
		surfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				controller.show();
			}

		});
		fDialog = new ForceLoginDialog();
		fDialog.setListener(this);
	}

	@Override
	public void onNewIntent(Intent intent) {
		releasePlayer();
		playerPosition = 0;
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		crid = intent.getStringExtra(DataVariable.CRID);
		title = intent.getStringExtra(DataVariable.KEY_TITLE);
		contentType = intent.getIntExtra(CONTENT_TYPE_EXTRA, 2);
		if (onTitleListener != null) {
			onTitleListener.onTitle(title);
		}
		playerPosition = (long) intent.getIntExtra("position", 0);
		if (player == null) {
			preparePlayer(true);
		} else {
			player.setBackgrounded(false);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		releasePlayer();
		handler.removeCallbacks(sessionRunnable);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		audioCapabilitiesReceiver.unregister();
		releasePlayer();
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
		if (player == null) {
			return;
		}
		boolean backgrounded = player.getBackgrounded();
		boolean playWhenReady = player.getPlayWhenReady();
		releasePlayer();
		preparePlayer(playWhenReady);
		player.setBackgrounded(backgrounded);
	}

	public static void setOnBitrateAvailableListener(ExoPlayerInterface.OnBitrateAvailableListener listener) {
		onBitrateAvailableListener = listener;
	}

	public static void setOnTitleListener(ExoPlayerInterface.OnTitleListener listener) {
		onTitleListener = listener;
	}

	private RendererBuilder getRendererBuilder() {
		String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
		String datasource = "";
		switch (contentType) {
			case TYPE_HLS:
				datasource = "http://localhost:55055/video/hls/" + crid + ".m3u8";
				return new HlsRendererBuilder(this, userAgent, datasource);
			case TYPE_OTHER:
				return new ExtractorRendererBuilder(this, userAgent, Uri.parse(datasource));
			default:
				throw new IllegalStateException("Unsupported type: " + contentType);
		}
	}

	private void preparePlayer(boolean playWhenReady) {
		if (player == null) {
			player = new DemoPlayer(getRendererBuilder());
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
		player.setSurface(surfaceView.getHolder().getSurface());
		player.setPlayWhenReady(playWhenReady);
	}

	private void releasePlayer() {
		if (player != null) {
			playerPosition = player.getCurrentPosition();
			player.release();
			player = null;

		}
	}

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {

		String text = "";
		switch (playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				text += "buffering " + player.getBufferedPercentage() / 100 + " %";
				pb.setVisibility(View.VISIBLE);
				tBuffer.setVisibility(View.VISIBLE);
				break;
			case ExoPlayer.STATE_ENDED:
				text += "ended";
				pb.setVisibility(View.GONE);
				tBuffer.setVisibility(View.VISIBLE);
				break;
			case ExoPlayer.STATE_IDLE:
				text += "idle";
				pb.setVisibility(View.GONE);
				tBuffer.setVisibility(View.VISIBLE);
				break;
			case ExoPlayer.STATE_PREPARING:
				text += "preparing";
				pb.setVisibility(View.VISIBLE);
				tBuffer.setVisibility(View.VISIBLE);
				break;
			case ExoPlayer.STATE_READY:
				pb.setVisibility(View.GONE);
				tBuffer.setVisibility(View.GONE);
				text += "ready";
				break;
			default:
				text += "unknown";
				break;
		}
		tBuffer.setText(text);
	}

	@Override
	public void onError(Exception e) {
		if (e instanceof UnsupportedDrmException) {
			// Special case DRM failures.
			UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
			int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
					: unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
					? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
			Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
		}

		if (attempt < 5) {
			preparePlayer(true);
			attempt++;
		} else {

			playerNeedsPrepare = true;
			pb.setVisibility(View.GONE);
			tBuffer.setText("Playback Error");

		}
		playerNeedsPrepare = true;

	}

	@Override
	public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees,
	                               float pixelWidthAspectRatio) {

		videoFrame.setAspectRatio(
				height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
		aspectRatio = pixelWidthAspectRatio;
		currentWidth = width;
		currentHeight = height;
	}

	@Override
	public void onCues(List<Cue> cues) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (player != null) {
			player.setSurface(holder.getSurface());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (player != null) {
			player.blockingClearSurface();
		}
	}

	@Override
	public void onDownstreamFormatChanged(int arg0, Format arg1, int arg2,
	                                      long arg3) {
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
		// TODO Auto-generated method stub
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
		player.seekTo(pos);
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
		return true;
	}

	@Override
	public void toggleFullScreen() {
		switchFullScreen();
	}

	private void switchFullScreen() {
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		switch (currentMode) {
			case MODE_STANDARD:
				videoFrame.setAspectRatio((screenWidth * aspectRatio) / screenHeight);
				currentMode = MODE_FULL_WITH_ASPECT_RATIO;
				break;
			case MODE_FULL_WITH_ASPECT_RATIO:
				videoFrame.setAspectRatio(0);
				currentMode = MODE_FULLSCREEN;
				break;
			case MODE_FULLSCREEN:
				videoFrame.setAspectRatio((currentWidth * aspectRatio) / currentHeight);
				currentMode = MODE_STANDARD;
				break;
		}


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
	public void onBackPressed() {
		if (controller.isShowing()) {
			controller.hide();
		} else {

			if (player != null) {
				playerPosition = player.getCurrentPosition();
				releasePlayer();
			}

			if (player == null) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
				overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			}

		}
	}

	@Override
	public void onClose() {
		finish();
	}

	@Override
	public void onLogged() {
		isWarned = false;
		preparePlayer(true);
	}


	private void stopPlayer() {
		releasePlayer();
		isWarned = true;
	}

	private Runnable sessionRunnable = new Runnable() {

		@Override
		public void run() {
			checkSession();
		}

	};

	@Override
	public void onBitrateChange(String variantUrl, boolean isAuto) {
	}

	@Override
	public void onShow() {
	}

	private void validateUser(String cred) {
		try {
			JSONObject jObject = new JSONObject(cred);
			final String status = jObject.getString("code");
			if (status.equals("00")) {
				JSONArray packages = jObject.getJSONArray("package");
				if (packages.length() > 0) {
					for (int i = 0; i < packages.length(); i++) {
						JSONObject packageItem = packages.getJSONObject(i);
						JSONObject valObj = packageItem.getJSONObject("value");
						String pName = packageItem.getString("name");
						String pResult = valObj.getString("result");
						if (pName.equals(DataVariable.PACKAGE_BASIC)) {
							final int BASIC = Integer.valueOf(pResult);
							if (BASIC < 0) {
								fDialog.show(getSupportFragmentManager(), "Login");
								stopPlayer();
								isWarned = true;
								return;
							}
						}
					}
				}

			} else if (status.equals("E102")) {
				fDialog.show(getSupportFragmentManager(), "Login");
				stopPlayer();
				isWarned = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkSession() {
		final String phone = pref.getPreference(DataVariable.PHONE_NUMBER);
		final String macId = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		final String session = pref.getPreference(DataVariable.SESSION);
		final String ACTION = "checksession";
		String paramSign = phone + macId + session + ACTION;
		final String signature = DataVariable.Md5(paramSign + DataVariable.SECRET_KEY);
		StringRequest request = new StringRequest(Request.Method.POST,
				DataVariable.BILLING_URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						if (!isWarned) {
							validateUser(arg0);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("phoneNumber", phone);
				params.put("macID", macId);
				params.put("sessionValue", session);
				params.put("action", ACTION);
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}

	@Override
	public void onId3Metadata(List<Id3Frame> id3Frames) {

	}
}