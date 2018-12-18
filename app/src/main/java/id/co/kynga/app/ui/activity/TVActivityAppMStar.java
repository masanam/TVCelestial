package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecUtil;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.exoplayer.DashRendererBuilder;
import id.co.kynga.app.exoplayer.DemoPlayer;
import id.co.kynga.app.exoplayer.ExtractorRendererBuilder;
import id.co.kynga.app.exoplayer.HlsRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingTestMediaDrmCallback;
import id.co.kynga.app.exoplayer.WidevineTestMediaDrmCallback;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.TVModel;
import id.co.kynga.app.ui.adapter.TVListAdapter;

public class TVActivityAppMStar extends CommonActivity implements
		SurfaceHolder.Callback,
		DemoPlayer.Listener,
		DemoPlayer.CaptionListener,
		AudioCapabilitiesReceiver.Listener{

	public static TVActivityAppMStar instance;

	private static final CookieManager defaultCookieManager;

	static {
		defaultCookieManager = new CookieManager();
		defaultCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
	}

	private ImageButton btn_back;
	private TextView lbl_title;
	private View vw_shutter;
	private AspectRatioFrameLayout lay_video;
	private SurfaceView vw_surface;
	private TextView lbl_message;
	private DemoPlayer player;
	private GridView gvw_main;

	private AudioCapabilitiesReceiver audio_capabilities_receiver;
	private boolean player_needs_prepare;
	private Uri uri;
	private int content_type;

	private TVListAdapter tv_list_adapter;
	private ArrayList<TVModel> tv_list;
	private TVModel tv_selected;

	private int current_width;
	private int current_height;
	private float aspect_ratio;

	private Handler handler = new Handler();//untuk timer
	private ImageButton btn_landscape, btn_portrait;

	private Tracker mTracker;
	private static final String TAG = "TVActivity";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_tv_app_mstar);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setInitial();
	}

	public void landscape (View view){
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public void portrait (View view){
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		stopPlayer();
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setContentView(R.layout.activity_tv_app_mstar);
			setLandscapeInitial();
			//GlobalController.showToast("test", 20000);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			setContentView(R.layout.activity_tv_app_mstar);
			setPotraitInitial();
			//GlobalController.showToast("test", 20000);
		}
		populateData();
		startPlayer();

	}

	@Override
	public void onStart() {
		super.onStart();
		if (Util.SDK_INT > 23) {
			startPlayer();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Util.SDK_INT <= 23 || player == null) {
			startPlayer();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Util.SDK_INT <= 23) {
			stopPlayer();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (Util.SDK_INT > 23) {
			stopPlayer();
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		stopPlayer();
		setIntent(intent);
	}

	@Override
	public void onDestroy() {
		instance = null;
		super.onDestroy();
		audio_capabilities_receiver.unregister();
		stopPlayer();
	}

	@Override
	public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
		if (player == null) {
			return;
		}
		boolean backgrounded = player.getBackgrounded();
		boolean play_when_ready = player.getPlayWhenReady();
		stopPlayer();
		preparePlayer(play_when_ready);
		player.setBackgrounded(backgrounded);
	}

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		switch(playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				lbl_message.setVisibility(View.VISIBLE);
				lbl_message.setText(GlobalController.getString(R.string.message_player_buffering));
				break;
			case ExoPlayer.STATE_ENDED:
				break;
			case ExoPlayer.STATE_IDLE:
				break;
			case ExoPlayer.STATE_PREPARING:
				lbl_message.setVisibility(View.VISIBLE);
				lbl_message.setText(GlobalController.getString(R.string.message_player_preparing));
				break;
			case ExoPlayer.STATE_READY:
				lbl_message.setVisibility(View.GONE);
				if (GlobalController.isLandscape()) {
					setFullscreen();
				} else {
					setNormalScreen();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onError(Exception e) {
		lbl_message.setVisibility(View.VISIBLE);
		if (e instanceof UnsupportedDrmException) {
			UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
			lbl_message.setText(getString(Util.SDK_INT < 18 ? R.string.error_drm_not_supported
					: unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
					? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown));
		} else if (e instanceof ExoPlaybackException
				&& e.getCause() instanceof MediaCodecTrackRenderer.DecoderInitializationException) {
			MediaCodecTrackRenderer.DecoderInitializationException decoderInitializationException =
					(MediaCodecTrackRenderer.DecoderInitializationException) e.getCause();
			if (decoderInitializationException.decoderName == null) {
				if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
					lbl_message.setText(getString(R.string.error_querying_decoders));
				} else if (decoderInitializationException.secureDecoderRequired) {
					lbl_message.setText(getString(R.string.error_no_secure_decoder,
							decoderInitializationException.mimeType));
				} else {
					lbl_message.setText(getString(R.string.error_no_decoder,
							decoderInitializationException.mimeType));
				}
			} else {
				lbl_message.setText(getString(R.string.error_instantiating_decoder,
						decoderInitializationException.decoderName));
			}
		} else {
			lbl_message.setText(getString(R.string.error_player));
		}
		player_needs_prepare = true;
	}

	@Override
	public void onVideoSizeChanged(
			int width,
			int height,
			int unappliedRotationDegrees,
			float pixelWidthAspectRatio) {
		vw_shutter.setVisibility(View.GONE);
		lay_video.setAspectRatio(
				height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
		aspect_ratio = pixelWidthAspectRatio;
		current_width = width;
		current_height = height;

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
	public void onCues(List<Cue> cues) {
	}

	private DemoPlayer.RendererBuilder getRendererBuilder() {
		final String user_agent = Util.getUserAgent(this, "TV");
		switch (content_type) {
			case Util.TYPE_SS:
				return new SmoothStreamingRendererBuilder(this, user_agent, uri.toString(),
						new SmoothStreamingTestMediaDrmCallback());
			case Util.TYPE_DASH:
				return new DashRendererBuilder(this, user_agent, uri.toString(),
						new WidevineTestMediaDrmCallback());
			case Util.TYPE_HLS:
				return new HlsRendererBuilder(this, user_agent, uri.toString());
			case Util.TYPE_OTHER:
				return new ExtractorRendererBuilder(this, user_agent, uri);
			default:
				throw new IllegalStateException("Unsupported type: " + content_type);
		}
	}

	private void setInitial() {
		if (getIntent().getParcelableExtra(TVModel.TAG) != null) {
			final TVModel tv_model = getIntent().getParcelableExtra(TVModel.TAG);
			tv_list = new ArrayList<>(tv_model.list);
			if (tv_list.size() > 0) {
				tv_selected = tv_list.get(0);
			}
		}
		if (GlobalController.isLandscape()) {
			setLandscapeInitial();
		} else {
			setPotraitInitial();
		}
		final CookieHandler current_handler = CookieHandler.getDefault();
		if (current_handler != defaultCookieManager) {
			CookieHandler.setDefault(defaultCookieManager);
		}
		audio_capabilities_receiver = new AudioCapabilitiesReceiver(this, this);
		audio_capabilities_receiver.register();
		populateData();
		//handler.postDelayed(runnable, 0);//runnable dijalankan 1 detik mendatang
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			btn_landscape.animate().alpha(0.0f).setDuration(5000);

		}
	};

	private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			//btn_portrait.setVisibility(View.GONE);
			btn_portrait.animate().alpha(0.0f).setDuration(5000);

		}
	};

	private void setPotraitInitial() {
		btn_landscape = (ImageButton) findViewById(R.id.btn_landscape);
		//btn_landscape.setVisibility(View.GONE);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		gvw_main = (GridView) findViewById(R.id.gvw_main);
		vw_shutter = findViewById(R.id.vw_shutter);
		lay_video = (AspectRatioFrameLayout) findViewById(R.id.lay_video);
		vw_surface = (SurfaceView) findViewById(R.id.vw_surface);
		lbl_message = (TextView) findViewById(R.id.lbl_message);
		vw_surface.getHolder().addCallback(this);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		lbl_title.setTypeface(null, Typeface.BOLD);
		setPotraitEventListener();
	}

	private void setPotraitEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		tv_list_adapter = new TVListAdapter(tv_list, new TVListAdapter.TVListAdapterCallback() {
			@Override
			public void didTVListAdapterActioned(TVModel tv_model) {
				lbl_title.setText(tv_model.Title);
				lbl_message.setVisibility(View.GONE);
				stopPlayer();
				tv_selected = tv_model;
				startPlayer();
			}
		});
		gvw_main.setAdapter(tv_list_adapter);
		vw_surface.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//GlobalController.showToast("test", 20000);
				//btn_landscape.setVisibility(View.VISIBLE);
				btn_landscape.animate().alpha(1.0f).setDuration(1000);
				//btn_landscape.animate().alpha(0.0f).setDuration(5000);
				handler.postDelayed(runnable, 5000);
			}
		});
		btn_landscape.animate().alpha(0.0f).setDuration(10000);
	}


	private void setLandscapeInitial() {
		setTheme(R.style.AppFullscreenTheme);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		btn_portrait = (ImageButton) findViewById(R.id.btn_portrait);
		vw_shutter = findViewById(R.id.vw_shutter);
		lay_video = (AspectRatioFrameLayout) findViewById(R.id.lay_video);
		vw_surface = (SurfaceView) findViewById(R.id.vw_surface);
		lbl_message = (TextView) findViewById(R.id.lbl_message);
		vw_surface.getHolder().addCallback(this);
		vw_surface.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btn_portrait.animate().alpha(1.0f).setDuration(1000);
				handler.postDelayed(runnable2, 5000);
			}
		});
		btn_portrait.animate().alpha(0.0f).setDuration(10000);
	}

	private void populateData() {
		lbl_title.setText(tv_selected.Title);
/*
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		Log.i(TAG, "Setting screen name: " + "TV");
		mTracker.setScreenName("Screen~" + "TV " +tv_selected.Title);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		*/
	}

	private void preparePlayer(final boolean playWhenReady) {
		if (player == null) {
			player = new DemoPlayer(getRendererBuilder());
			player.addListener(this);
			player.setCaptionListener(this);
			player_needs_prepare = true;
			player.seekTo(0L);
		}
		if (player_needs_prepare) {
			player.prepare();
			player_needs_prepare = false;
		}
		vw_surface.getHolder().setFormat(PixelFormat.TRANSPARENT);
		vw_surface.getHolder().setFormat(PixelFormat.OPAQUE);
		player.setSurface(vw_surface.getHolder().getSurface());
		player.setPlayWhenReady(playWhenReady);
	}

	private void stopPlayer() {
		if (player != null) {
			player.release();
			player = null;
		}
	}

	private void startPlayer() {
		if (tv_selected == null) {
			return;
		}
		uri = Uri.parse(tv_selected.LinkURL);
		final String last_path_segment = uri.getLastPathSegment();
		content_type = Util.inferContentType(last_path_segment);
		if (player == null) {
			preparePlayer(true);
		} else {
			player.setBackgrounded(false);
		}
	}

	private void setFullscreen() {
		lay_video.setAspectRatio((current_width * aspect_ratio) / current_height);
	}

	private void setNormalScreen() {
		lay_video.setAspectRatio((lay_video.getWidth() * aspect_ratio) / lay_video.getHeight());
	}
}