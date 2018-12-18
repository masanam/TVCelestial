/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.google.android.exoplayer2.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.util.ResumeDialog;
import id.co.kynga.app.util.SubscriptionDialog;

/**
 * An activity that plays media using {@link SimpleExoPlayer}.
 */
public class PlayerActivityNotFree extends Activity implements OnClickListener, ExoPlayer.EventListener,
    MappingTrackSelector.EventListener, PlaybackControlView.VisibilityListener {

  public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
  public static final String DRM_LICENSE_URL = "drm_license_url";
  public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
  public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

  public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
  public static final String EXTENSION_EXTRA = "extension";

  public static final String ACTION_VIEW_LIST =
      "com.google.android.exoplayer.demo.action.VIEW_LIST";
  public static final String URI_LIST_EXTRA = "uri_list";
  public static final String EXTENSION_LIST_EXTRA = "extension_list";

  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  private static final CookieManager DEFAULT_COOKIE_MANAGER;
  static {
    DEFAULT_COOKIE_MANAGER = new CookieManager();
    DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
  }

  private Handler mainHandler;
  private EventLogger eventLogger;
  private SimpleExoPlayerView simpleExoPlayerView;
  private LinearLayout debugRootView;
  private TextView debugTextView;
  private Button retryButton;

  private String userAgent;
  private DataSource.Factory mediaDataSourceFactory;
  private SimpleExoPlayer player;
  private MappingTrackSelector trackSelector;
  private TrackSelectionHelper trackSelectionHelper;
  private DebugTextViewHelper debugViewHelper;
  private boolean playerNeedsSource;

  private boolean shouldAutoPlay;
  private boolean shouldRestorePosition;
  private int playerWindow;
  private long playerPosition;

  private SharedPreferences sharedpreferences;
  public static final String mypreference = "mypref";
  public static final String currentPosition= "currentPosition";
  private String cpPosition;
  private Handler handlerPlayControl = new Handler();
  private Long longStartPlay;
  private boolean isPlaying;

  private Hashtable<String, String> table;
  private SharedPreferences pref;
  private String link;
  public static PlayerActivityNotFree instance;
  private TextView lbl_message;

  private Handler handler = new Handler();//untuk timer
  private String videoID;
  private String clickOrigin = "clickVideo";

  // Activity lifecycle

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //GlobalController.showToast("nomor2", 20000);
    instance = this;

    Intent intent0 = getIntent();
    link = intent0.getData().toString();

    //shared prefference to collect current player position
    sharedpreferences = getSharedPreferences(mypreference,
            Context.MODE_PRIVATE);

    if (sharedpreferences.contains(link)) {
      cpPosition = sharedpreferences.getString(link, "");
      longStartPlay = Long.valueOf(cpPosition);
      //GlobalController.showToast(cpPosition, 20000);
    }else
    {
      cpPosition ="0";
      longStartPlay = Long.valueOf(cpPosition);
    }
    // Played 5 seconds before
    if (Long.valueOf(cpPosition) > 5000){
      longStartPlay = Long.valueOf(cpPosition)-5000;
    }

    shouldAutoPlay = true;
    userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
    mediaDataSourceFactory = buildDataSourceFactory(true);
    mainHandler = new Handler();
    if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
      CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
    }

    setContentView(R.layout.player_activity_exo2);
    View rootView = findViewById(R.id.root);
    rootView.setOnClickListener(this);
    debugRootView = (LinearLayout) findViewById(R.id.controls_root);
    debugTextView = (TextView) findViewById(R.id.debug_text_view);
    retryButton = (Button) findViewById(R.id.retry_button);
    retryButton.setOnClickListener(this);

    simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
    //simpleExoPlayerView.setControllerVisibilityListener(this);
    //simpleExoPlayerView.requestFocus();

    isPlaying = false;
    //longStartPlay = Long.valueOf(0);
    simpleExoPlayerView.setUseController(false);
    simpleExoPlayerView.setVisibility(View.GONE);
    lbl_message = (TextView) findViewById(R.id.lbl_message);
    lbl_message.setVisibility(View.GONE);

    Intent intent = getIntent();
    videoID = intent.getStringExtra("videoID");
    clickOrigin = intent.getStringExtra("clickOrigin");
    checkSubscriptionStatus();

  }

  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      releasePlayer();
      simpleExoPlayerView.setVisibility(View.GONE);
      showDialogSubscription();
    }
  };

  private void checkSubscriptionStatus() {
    final UserModel user_model = SessionControllerAppMStar.getUser();
    GlobalController.showLoading(this);
    URLController.checkSubsctiptionStatusAppMStar(user_model.PhoneNumber, videoID, new URLManager.URLCallback() {
      @Override
      public void didURLSucceeded(int status_code, final String response) {
        if (instance == null) {
          return;
        }
        instance.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            GlobalController.closeLoading();
            //GlobalController.showToast(response, 20000);
            if (!response.contains("unlock")){
              simpleExoPlayerView.setVisibility(View.VISIBLE);
              //GlobalController.showLoading(instance);
              simpleExoPlayerView.setUseController(true);
              longStartPlay = Long.valueOf(0);
              initializePlayer();
              isPlaying =true;
              handler.postDelayed(runnable, 15000);
            }else{
              if (sharedpreferences.contains(link)) {
                showDialog();
              }else
              {
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                //GlobalController.showLoading(instance);
                simpleExoPlayerView.setUseController(true);
                longStartPlay = Long.valueOf(0);
                initializePlayer();
                isPlaying =true;
              }
            }
          }
        });
      }

      @Override
      public void didURLFailed() {
        if (instance == null) {
          return;
        }
        instance.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            GlobalController.closeLoading();
            GlobalController.showRequestFailedWarning(instance);
          }
        });
      }
    });
  }

  public void showDialogSubscription(){
    SubscriptionDialog subscriptionDialog = new SubscriptionDialog(this);
    subscriptionDialog.setListener(new SubscriptionDialog.ResumeListener() {

      @Override
      public void onRestart() {
        finish();
      }

      @Override
      public void onResume() {
        if (clickOrigin.equals("clickMenu")) {
          buyProduct1();
        }else{
          AppController.openSubscription(instance, videoID);
          finish();
        }
      }

    });
    subscriptionDialog.show();
    //errorShown = true;
  }

  @Override
  public void onNewIntent(Intent intent) {
    releasePlayer();
    shouldRestorePosition = false;
    setIntent(intent);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      /*
      Intent intent = getIntent();
      videoID = intent.getStringExtra("videoID");
      clickOrigin = intent.getStringExtra("clickOrigin");
      checkSubscriptionStatus();
      */
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if ((Util.SDK_INT <= 23 || player == null)) {
      /*
      Intent intent = getIntent();
      videoID = intent.getStringExtra("videoID");
      clickOrigin = intent.getStringExtra("clickOrigin");
      checkSubscriptionStatus();
      */
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      initializePlayer();
    } else {
      showToast(R.string.storage_permission_denied);
      finish();
    }
  }

  // OnClickListener methods

  @Override
  public void onClick(View view) {
    if (view == retryButton) {
      initializePlayer();
    } else if (view.getParent() == debugRootView) {
      trackSelectionHelper.showSelectionDialog(this, ((Button) view).getText(),
          trackSelector.getTrackInfo(), (int) view.getTag());
    }
  }

  // PlaybackControlView.VisibilityListener implementation

  @Override
  public void onVisibilityChange(int visibility) {
    debugRootView.setVisibility(visibility);
  }

  // Internal methods

  private void initializePlayer() {
    Intent intent = getIntent();
    if (player == null) {
      boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
      UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA)
          ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
      DrmSessionManager drmSessionManager = null;
      if (drmSchemeUuid != null) {
        String drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
        String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
        Map<String, String> keyRequestProperties;
        if (keyRequestPropertiesArray == null || keyRequestPropertiesArray.length < 2) {
          keyRequestProperties = null;
        } else {
          keyRequestProperties = new HashMap<>();
          for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
            keyRequestProperties.put(keyRequestPropertiesArray[i],
                keyRequestPropertiesArray[i + 1]);
          }
        }
        try {
          drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl,
              keyRequestProperties);
        } catch (UnsupportedDrmException e) {
          int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
              : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                  ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);
          showToast(errorStringId);
          return;
        }
      }

      eventLogger = new EventLogger();
      TrackSelection.Factory videoTrackSelectionFactory =
          new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
      trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
      trackSelector.addListener(this);
      trackSelector.addListener(eventLogger);
      trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
      player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl(),
          drmSessionManager, preferExtensionDecoders);
      player.addListener(this);
      player.addListener(eventLogger);
      player.setAudioDebugListener(eventLogger);
      player.setVideoDebugListener(eventLogger);
      player.setId3Output(eventLogger);
      //player.seekTo(1000000);//Start mulai detik ke 1000
      player.seekTo(longStartPlay);
      simpleExoPlayerView.setPlayer(player);
      if (shouldRestorePosition) {
        if (playerPosition == C.TIME_UNSET) {
          player.seekToDefaultPosition(playerWindow);
        } else {
          player.seekTo(playerWindow, playerPosition);
        }
      }
      player.setPlayWhenReady(shouldAutoPlay);
      debugViewHelper = new DebugTextViewHelper(player, debugTextView);
      debugViewHelper.start();
      playerNeedsSource = true;
    }
    if (playerNeedsSource) {
      String action = intent.getAction();
      Uri[] uris;
      String[] extensions;
      if (ACTION_VIEW.equals(action)) {
        uris = new Uri[] {intent.getData()};
        extensions = new String[] {intent.getStringExtra(EXTENSION_EXTRA)};
      } else if (ACTION_VIEW_LIST.equals(action)) {
        String[] uriStrings = intent.getStringArrayExtra(URI_LIST_EXTRA);
        uris = new Uri[uriStrings.length];
        for (int i = 0; i < uriStrings.length; i++) {
          uris[i] = Uri.parse(uriStrings[i]);
        }
        extensions = intent.getStringArrayExtra(EXTENSION_LIST_EXTRA);
        if (extensions == null) {
          extensions = new String[uriStrings.length];
        }
      } else {
        showToast(getString(R.string.unexpected_intent_action, action));
        return;
      }
      if (Util.maybeRequestReadExternalStoragePermission(this, uris)) {
        // The player will be reinitialized if the permission is granted.
        return;
      }
      MediaSource[] mediaSources = new MediaSource[uris.length];
      for (int i = 0; i < uris.length; i++) {
        mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
      }
      MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
          : new ConcatenatingMediaSource(mediaSources);
      player.prepare(mediaSource, !shouldRestorePosition);
      playerNeedsSource = false;
      updateButtonVisibilities();
    }
  }

  private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
    int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
        : uri.getLastPathSegment());
    switch (type) {
      case Util.TYPE_SS:
        return new SsMediaSource(uri, buildDataSourceFactory(false),
            new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
      case Util.TYPE_DASH:
        return new DashMediaSource(uri, buildDataSourceFactory(false),
            new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
      case Util.TYPE_HLS:
        return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
      case Util.TYPE_OTHER:
        return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
            mainHandler, eventLogger);
      default: {
        throw new IllegalStateException("Unsupported type: " + type);
      }
    }
  }

  private DrmSessionManager buildDrmSessionManager(UUID uuid, String licenseUrl,
      Map<String, String> keyRequestProperties)
      throws UnsupportedDrmException {
    if (Util.SDK_INT < 18) {
      return null;
    }
    HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
        buildHttpDataSourceFactory(false), keyRequestProperties);
    return new StreamingDrmSessionManager<>(uuid,
        FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
  }

  private void releasePlayer() {
    if (player != null) {
      debugViewHelper.stop();
      debugViewHelper = null;
      shouldAutoPlay = player.getPlayWhenReady();
      shouldRestorePosition = false;
      Timeline timeline = player.getCurrentTimeline();
      if (timeline != null) {
        playerWindow = player.getCurrentWindowIndex();
        Timeline.Window window = timeline.getWindow(playerWindow, new Timeline.Window());
        if (!window.isDynamic) {
          shouldRestorePosition = true;
          playerPosition = window.isSeekable ? player.getCurrentPosition() : C.TIME_UNSET;
        }
      }
      player.release();
      player = null;
      trackSelector = null;
      trackSelectionHelper = null;
      eventLogger = null;
    }
  }

  /**
   * Returns a new DataSource factory.
   *
   * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
   *     DataSource factory.
   * @return A new DataSource factory.
   */
  private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
    return new DefaultDataSourceFactory(this, useBandwidthMeter ? BANDWIDTH_METER : null,
        buildHttpDataSourceFactory(useBandwidthMeter));
  }

  /**
   * Returns a new HttpDataSource factory.
   *
   * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
   *     DataSource factory.
   * @return A new HttpDataSource factory.
   */
  private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
    return new DefaultHttpDataSourceFactory(userAgent, useBandwidthMeter ? BANDWIDTH_METER : null);
  }

  // ExoPlayer.EventListener implementation

  @Override
  public void onLoadingChanged(boolean isLoading) {
    // Do nothing.
  }

  @Override
  public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    switch(playbackState) {
      case ExoPlayer.STATE_BUFFERING:
        lbl_message.setVisibility(View.VISIBLE);
        lbl_message.setText(GlobalController.getString(R.string.message_player_buffering));
        break;
      case ExoPlayer.STATE_ENDED:

        break;
      case ExoPlayer.STATE_IDLE:
        break;
      /*
      case ExoPlayer.STATE_PREPARING:
        lbl_message.setVisibility(View.VISIBLE);
        lbl_message.setText(GlobalController.getString(R.string.message_player_preparing));
        break;
      */
      case ExoPlayer.STATE_READY:
        lbl_message.setVisibility(View.GONE);
        /*
        if (GlobalController.isLandscape()) {
          setFullscreen();
        } else {
          setNormalScreen();
        }
        */
        break;
      default:
        break;
    }
    //below is original state indicator
    if (playbackState == ExoPlayer.STATE_ENDED) {
      showControls();
      finish();
    }
    updateButtonVisibilities();
  }

  @Override
  public void onPositionDiscontinuity() {
    // Do nothing.
  }

  @Override
  public void onTimelineChanged(Timeline timeline, Object manifest) {
    // Do nothing.
  }

  @Override
  public void onPlayerError(ExoPlaybackException e) {
    String errorString = null;
    if (e.type == ExoPlaybackException.TYPE_RENDERER) {
      Exception cause = e.getRendererException();
      if (cause instanceof DecoderInitializationException) {
        // Special case for decoder initialization failures.
        DecoderInitializationException decoderInitializationException =
            (DecoderInitializationException) cause;
        if (decoderInitializationException.decoderName == null) {
          if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
            errorString = getString(R.string.error_querying_decoders);
          } else if (decoderInitializationException.secureDecoderRequired) {
            errorString = getString(R.string.error_no_secure_decoder,
                decoderInitializationException.mimeType);
          } else {
            errorString = getString(R.string.error_no_decoder,
                decoderInitializationException.mimeType);
          }
        } else {
          errorString = getString(R.string.error_instantiating_decoder,
              decoderInitializationException.decoderName);
        }
      }
    }
    if (errorString != null) {
      showToast(errorString);
    }
    playerNeedsSource = true;
    updateButtonVisibilities();
    showControls();
  }

  // MappingTrackSelector.EventListener implementation

  @Override
  public void onTracksChanged(MappingTrackSelector.TrackInfo trackInfo) {
    updateButtonVisibilities();
    if (trackInfo.hasOnlyUnplayableTracks(C.TRACK_TYPE_VIDEO)) {
      showToast(R.string.error_unsupported_video);
    }
    if (trackInfo.hasOnlyUnplayableTracks(C.TRACK_TYPE_AUDIO)) {
      showToast(R.string.error_unsupported_audio);
    }
  }

  // User controls

  private void updateButtonVisibilities() {
    debugRootView.removeAllViews();

    retryButton.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);
    debugRootView.addView(retryButton);

    if (player == null) {
      return;
    }

    MappingTrackSelector.TrackInfo trackInfo = trackSelector.getTrackInfo();
    if (trackInfo == null) {
      return;
    }

    int rendererCount = trackInfo.rendererCount;
    for (int i = 0; i < rendererCount; i++) {
      TrackGroupArray trackGroups = trackInfo.getTrackGroups(i);
      if (trackGroups.length != 0) {
        Button button = new Button(this);
        int label;
        switch (player.getRendererType(i)) {
          case C.TRACK_TYPE_AUDIO:
            label = R.string.audio;
            break;
          case C.TRACK_TYPE_VIDEO:
            label = R.string.video;
            break;
          case C.TRACK_TYPE_TEXT:
            label = R.string.text;
            break;
          default:
            continue;
        }
        button.setText(label);
        button.setTag(i);
        button.setOnClickListener(this);
        debugRootView.addView(button);
      }
    }
  }

  private void showControls() {
    debugRootView.setVisibility(View.VISIBLE);
  }

  private void showToast(int messageId) {
    showToast(getString(messageId));
  }

  private void showToast(String message) {
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  private void buyProduct1() {
    final UserModel user_model = SessionControllerAppMStar.getUser();
    GlobalController.showLoading(this);
    //GlobalController.showToast(user_model.PhoneNumber, 20000);
    //GlobalController.showToast(videoID, 20000);
    URLController.buyProduct(user_model.PhoneNumber, videoID, "1",new URLManager.URLCallback() {
      @Override
      public void didURLSucceeded(int status_code, final String response) {
        if (instance == null) {
          return;
        }
        instance.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            GlobalController.closeLoading();
            if (response.contains("You're already")) {
              GlobalController.showMessage(instance, response.substring(23,61));
            }else if(response.contains("Not enough balance")){
              GlobalController.showMessage(instance,response.substring(23,62));
              //finish();
            } else if (response.contains("You're now subscribe")){
              simpleExoPlayerView.setVisibility(View.VISIBLE);
              //GlobalController.showLoading(instance);
              simpleExoPlayerView.setUseController(true);
              longStartPlay = Long.valueOf(0);
              initializePlayer();
              isPlaying =true;
              GlobalController.showMessage(instance,response.substring(23,57));
            }
            //GlobalController.showMessage(instance,response.substring(23,57));
            //textRespon.setText(response);
            //checkVA();
            //setValidationBCA(response); response doesn't have sign [ dan ], so no need validation
            /*
            if (response.contains("unlock")) {
              //GlobalController.showToast(user_model.PhoneNumber, 20000);
              simpleExoPlayerView.setUseController(true);
              simpleExoPlayerView.setVisibility(View.VISIBLE);
              initializePlayer();
              isPlaying = true;
            }
            */
          }
        });
      }

      @Override
      public void didURLFailed() {
        if (instance == null) {
          return;
        }
        instance.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            GlobalController.closeLoading();
            GlobalController.showRequestFailedWarning(instance);
          }
        });
      }
    });
  }

  public void showDialog(){
    ResumeDialog resumeDialog = new ResumeDialog(this);
    resumeDialog.setListener(new ResumeDialog.ResumeListener() {
      @Override
      public void onRestart() {
        //releasePlayer();
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        //GlobalController.showLoading(instance);
        simpleExoPlayerView.setUseController(true);
        longStartPlay = Long.valueOf(0);
        initializePlayer();
        isPlaying =true;
        //GlobalController.closeLoading();
      }
      @Override
      public void onResume() {
        //GlobalController.showToast("testerrrr", 20000);
        simpleExoPlayerView.setVisibility(View.VISIBLE);
        //releasePlayer();
        //GlobalController.showLoading(instance);
        simpleExoPlayerView.setUseController(true);
        //longStartPlay = Long.valueOf(cpPosition);
        initializePlayer();
        isPlaying =true;
        //GlobalController.closeLoading();
      }

    });
    resumeDialog.show();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    //remember, keydown is include volume button..!!!!

    if (keyCode == 4 && isPlaying == true) {
      String n = String.valueOf(player.getCurrentPosition());
      SharedPreferences.Editor editor = sharedpreferences.edit();
      //editor.putString(currentPosition, n);
      editor.putString(link, n);
      editor.commit();
      finish();
    }else if(keyCode == 4 && isPlaying == false && !sharedpreferences.contains(link)){
      String n = "0";
      SharedPreferences.Editor editor = sharedpreferences.edit();
      //editor.putString(currentPosition, n);
      editor.putString(link, n);
      editor.commit();
      longStartPlay = Long.valueOf(0);
      initializePlayer();
    }

    //GlobalController.showToast("test", 20000);

    //return false; // bisa false bisa true

    return super.onKeyDown(keyCode, event);
  }

}
