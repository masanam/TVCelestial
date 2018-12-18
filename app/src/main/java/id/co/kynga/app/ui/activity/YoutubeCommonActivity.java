package id.co.kynga.app.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class YoutubeCommonActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {
	private static final int RECOVERY_DIALOG_REQUEST = 1;

	@Override
	public void onInitializationFailure(
			YouTubePlayer.Provider provider,
			YouTubeInitializationResult errorReason) {
	}

	@Override
	public void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RECOVERY_DIALOG_REQUEST) {
			getYouTubePlayerProvider().initialize(GlobalController.getString(R.string.google_key), this);
		}
	}

	protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();
}