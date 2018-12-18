package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YoutubePlayer extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener,
		OnFullscreenListener {
	private YouTubePlayer player;
	private YouTubePlayerView playerView;
	private String videoId, playlistId;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private int attempt = 0;
	boolean paused = false;
	private int videoType;
	public static final int TYPE_VIDEO = 111;
	public static final int TYPE_PLAYLIST = 222;
	private String[] arrayVideoId;
	private Integer position;
	private String stringPosition;
	private int playerStyle;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.youtube_player);
		playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
		playerView.initialize(GlobalController.getString(R.string.google_api_key), this);
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
	                                    boolean arg2) {
		// TODO Auto-generated method stub
		player = arg1;
		//player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
		//player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
		playerStyle =0;
		player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
		/*player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		int controlFlags = player.getFullscreenControlFlags();
		controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		player.setFullscreenControlFlags(controlFlags);*/
		player.setPlayerStateChangeListener(new PlayerStateChangeListener() {
			@Override
			public void onLoading() {
			}

			@Override
			public void onLoaded(String paramString) {
			}

			@Override
			public void onAdStarted() {
				player.seekToMillis(5000);
			}

			@Override
			public void onVideoStarted() {
			}

			@Override
			public void onVideoEnded() {
			}

			@Override
			public void onError(ErrorReason paramErrorReason) {
				if (attempt < 3) {
					if (videoType == TYPE_VIDEO) {
						player.loadVideo(videoId);
					} else if (videoType == TYPE_PLAYLIST) {
						if (player.hasNext()) {
							player.next();
						}
					}

					attempt++;
				}
			}
		});

		player.setPlaybackEventListener(new PlaybackEventListener() {
			@Override
			public void onPlaying() {
				//player.setFullscreen(true);
				player.setFullscreen(false); //don't change to true! it will make next play not work!!!
				paused = false;
			}

			@Override
			public void onPaused() {
				paused = true;
			}

			@Override
			public void onStopped() {
				paused = true;
			}

			@Override
			public void onBuffering(boolean paramBoolean) {
				ViewGroup ytView = playerView;
				ProgressBar progressBar;
				try {
					ViewGroup child1 = (ViewGroup)ytView.getChildAt(0);
					ViewGroup child2 = (ViewGroup)child1.getChildAt(3);
					progressBar = (ProgressBar)child2.getChildAt(2);
				} catch (Throwable t) {
					progressBar = findProgressBar(ytView);
				}

				int visibility = paramBoolean ? View.VISIBLE : View.INVISIBLE;
				if (progressBar != null) {
					progressBar.setVisibility(visibility);
				}
			}

			@Override
			public void onSeekTo(int paramInt) {
			}

		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			position = Integer.parseInt(bundle.getString("position"));
			arrayVideoId = bundle.getStringArray("arrayVideoId");
			videoId = bundle.getString("videoId");
			playlistId = bundle.getString("playlist");
			videoType = bundle.getInt("video_type", 0);
			playVideo();
		}
	}

	private ProgressBar findProgressBar(View view) {
		if (view instanceof ProgressBar) {
			return (ProgressBar)view;
		} else if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup)view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
				if (res != null) return res;
			}
		}
		return null;
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
	                                    YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			playerView.initialize(GlobalController.getString(R.string.google_api_key), this);
		}
	}

	private void playVideo() {
		if (player == null) {
			return;
		}
		if (videoId != null) {
			//player.loadVideo(videoId);
			//player.loadPlaylist(playlistId,Integer.parseInt(position),10000);
			//player.loadVideo(arrayVideoId[position]);
			List<String> stringList = new ArrayList<String>(Arrays.asList(arrayVideoId));
			player.loadVideos(stringList);
			//Toast.makeText(this, "VideoId", Toast.LENGTH_LONG).show();
		} else if (playlistId != null) {
			//Toast.makeText(this, "PlayList", Toast.LENGTH_LONG).show();
			player.loadPlaylist(playlistId);

		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
				if (player != null) {
					if (paused) {
						player.play();
					} else {
						player.pause();
					}
				}
			} else if (keycode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (videoType == TYPE_PLAYLIST) {
					if (player.hasPrevious()) {
						player.previous();
					}
				}
				/*
				if (videoType == TYPE_VIDEO) {
					if (position > 0) {
						position = position -1;
					}else if (position <= 0) {
						position = arrayVideoId.length-1;
					}
					playVideo();
				}
				*/
				if (videoType == TYPE_VIDEO) {
					if (player.hasPrevious()) {
						player.previous();
					}
				}

			} else if (keycode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				if (videoType == TYPE_PLAYLIST) {
					if (player.hasNext()) {
						player.next();
					}
				}/*
				if (videoType == TYPE_VIDEO) {
					if (position < arrayVideoId.length-1) {
						position = position +1;
					}else if (position >= arrayVideoId.length-1) {
						position = 0;
					}
					playVideo();
				}
				*/
				if (videoType == TYPE_VIDEO) {
					if (player.hasNext()) {
						player.next();
					}
				}
			}
			//=========
			/*
			else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
				if(playerStyle == 0) {
					player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
					playerStyle=1;
					handler.postDelayed(runnable, 20000);
				}else {
					player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
					playerStyle=0;
				}

			}
			*/
		}
		return super.dispatchKeyEvent(event);
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
			handler.removeMessages(0);
		}
	};

	@Override
	public void onFullscreen(boolean arg0) {
	}
/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (player.hasNext()) {
				player.next();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	*/
}