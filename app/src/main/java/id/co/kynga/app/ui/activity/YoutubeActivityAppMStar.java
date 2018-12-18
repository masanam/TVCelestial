package id.co.kynga.app.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.YoutubeModel;
import id.co.kynga.app.model.YoutubePlaylistModel;
import id.co.kynga.app.ui.adapter.YoutubeListAdapter;
import id.co.kynga.app.ui.adapter.YoutubeListAdapterAppMStar;

public class YoutubeActivityAppMStar extends YoutubeCommonActivity implements YouTubePlayer.OnFullscreenListener {
	public static YoutubeActivityAppMStar instance;

	private ImageButton btn_back;
	private TextView lbl_title;
	private YouTubePlayerView vw_youtube;
	private GridView gvw_main;
	private YouTubePlayer player;
	private YoutubeListAdapterAppMStar youtube_list_adapter;

	private YoutubePlaylistModel youtube_playlist_model;
	private YoutubeModel youtube_model;
	private boolean fullscreen;
	//private android.support.v7.widget.Toolbar toolbar;
	private Tracker mTracker;
	private static final String TAG = "YoutubeActivity";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_youtube_app_mstar);
		//toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		//GlobalController.showToast(String.valueOf(ActivityInfo.CONFIG_ORIENTATION), 20000);
		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setInitial();

		//setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//player.setFullscreen(false);
		//GlobalController.showToast(String.valueOf(ActivityInfo.CONFIG_ORIENTATION), 20000);
		//GlobalController.showToast(String.valueOf(orientation), 20000);

	}

	public void landscape (View view){
		//toolbar.setVisibility(View.GONE);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	public int getScreenOrientation()
	{
		Display getOrient = getWindowManager().getDefaultDisplay();
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if(getOrient.getWidth()==getOrient.getHeight()){
			orientation = Configuration.ORIENTATION_SQUARE;
		} else{
			if(getOrient.getWidth() < getOrient.getHeight()){
				orientation = Configuration.ORIENTATION_PORTRAIT;
			}else {
				orientation = Configuration.ORIENTATION_LANDSCAPE;
			}
		}
		return orientation;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//GlobalController.showToast(String.valueOf(ActivityInfo.CONFIG_ORIENTATION), 20000);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			player.setFullscreen(true);
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			player.setFullscreen(false);
		}

	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	@Override
	public void onInitializationSuccess(
			YouTubePlayer.Provider provider,
			final YouTubePlayer player,
			boolean wasRestored) {
		this.player = player;
		//player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
		player.setOnFullscreenListener(this);
		player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
			@Override
			public void onPlaying() {
			}

			@Override
			public void onPaused() {
			}

			@Override
			public void onStopped() {
			}

			@Override
			public void onBuffering(boolean paramBoolean) {
				ViewGroup ytView = vw_youtube;
				ProgressBar progressBar;
				try {
					ViewGroup child1 = (ViewGroup) ytView.getChildAt(0);
					ViewGroup child2 = (ViewGroup) child1.getChildAt(3);
					progressBar = (ProgressBar) child2.getChildAt(2);
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
		if (!player.isPlaying()) {
			player.loadVideo(youtube_model.YoutubeID);
		}
	}

	@Override
	protected YouTubePlayer.Provider getYouTubePlayerProvider() {
		return vw_youtube;
	}

	@Override
	public void onFullscreen(boolean isFullscreen) {
		fullscreen = isFullscreen;
	}

	private void setInitial() {
		if (getIntent().hasExtra(YoutubePlaylistModel.TAG)) {
			youtube_playlist_model = getIntent().getParcelableExtra(YoutubePlaylistModel.TAG);
		}
		//GlobalController.showToast(String.valueOf(youtube_playlist_model.list.size()), 20000);
		if (getIntent().hasExtra(YoutubeModel.TAG)) {
			youtube_model = getIntent().getParcelableExtra(YoutubeModel.TAG);
		}
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		lbl_title.setTypeface(null, Typeface.BOLD);
		gvw_main = (GridView) findViewById(R.id.gvw_main);
		vw_youtube = (YouTubePlayerView) findViewById(R.id.vw_youtube);
		vw_youtube.initialize(GlobalController.getString(R.string.google_key), this);
		setEventListener();
		populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		gvw_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				doSelect(i);
			}
		});
	}

	private void doSelect(final int index) {
		youtube_model = youtube_playlist_model.Youtube.list.get(index);
		lbl_title.setText(youtube_model.Title);
		if (player != null) {
			player.loadVideo(youtube_model.YoutubeID);
		}
	}

	private void populateData() {
		youtube_list_adapter = new YoutubeListAdapterAppMStar(youtube_playlist_model.Youtube.list);
		gvw_main.setAdapter(youtube_list_adapter);
		lbl_title.setText(youtube_model.Title);
/*
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		Log.i(TAG, "Setting screen name: " + "Youtube");
		mTracker.setScreenName("Screen~" + "Youtube " +youtube_model.Title);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		*/
	}

	private ProgressBar findProgressBar(final View view) {
		if (view instanceof ProgressBar) {
			return (ProgressBar) view;
		} else if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				ProgressBar res = findProgressBar(viewGroup.getChildAt(i));
				if (res != null) return res;
			}
		}
		return null;
	}
}