package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.VideoView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;

public class SplashVideoCelestialActivity extends Activity {
	public static SplashVideoCelestialActivity instance;

	private VideoView vid_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_splash_video_celestial);
	}


	@Override
	protected void onResume() {
		super.onResume();
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		vid_main = (VideoView) findViewById(R.id.vid_main);
		final String path = "android.resource://" + getPackageName() + "/" + R.raw.splash_celestial;
		final Uri uri = Uri.parse(path);
		vid_main.setVideoURI(uri);
		vid_main.requestFocus();
		vid_main.start();
		vid_main.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
			}
		});
		vid_main.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				//AppController.openLandActivity(instance);
				AppController.openFirstActivity0_new(instance);
				finish();
				//instance.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down); --> bikin hitam
			}
		});
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_BACK) {
			//finish();
			//Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
			//instance.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			//finish();
			return true;//must return true, other wise menu will dissapear suddently
		}

		return super.dispatchKeyEvent(event);
	}

}