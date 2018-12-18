package id.co.kynga.app.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.CategoryModelAppMStar;
import id.co.kynga.app.model.YoutubeModel;
import id.co.kynga.app.model.YoutubePlaylistModel;
import id.co.kynga.app.ui.view.YoutubePlaylistView;


public class YoutubeChannelActivityAppMStar extends CommonActivity {
	public static YoutubeChannelActivityAppMStar instance;

	private ImageButton btn_back;
	private TextView lbl_title;
	private LinearLayout lay_main;

	private YoutubePlaylistModel youtube_playlist_model;
	private String title = Config.text_blank;
	private boolean init = false;
	private CategoryModelAppMStar category_model;
	private Handler handler = new Handler();//untuk timer


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_youtube_channel_app_mstar);
		init = true;
		category_model = getIntent().getParcelableExtra(CategoryModelAppMStar.TAG);
		handler.postDelayed(runnable, 0);
		//setInitial();
		//populateData();
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			setInitial();
			GlobalController.closeLoading();
		}
	};


/*
	@Override
	protected void onResume() {
		super.onResume();
		if (init) {
			init = false;
			populateData();
		}
	}
*/
	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		if (getIntent().hasExtra(YoutubePlaylistModel.TAG)) {
			youtube_playlist_model = getIntent().getParcelableExtra(YoutubePlaylistModel.TAG);
		}
		if (getIntent().hasExtra("Title")) {
			title = getIntent().getStringExtra("Title");
		}
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		lbl_title.setTypeface(null, Typeface.BOLD);
		lay_main = (LinearLayout) findViewById(R.id.lay_main);
		setEventListener();
		populateData();
		handler.removeMessages(0);
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private void populateData() {
		lbl_title.setText(title);
		//for (final YoutubePlaylistModel youtube_playlist : youtube_playlist_model.list) {
		for (final YoutubePlaylistModel youtube_playlist : youtube_playlist_model.list) {
			final YoutubePlaylistView vw_youtube_playlist = new YoutubePlaylistView(instance, youtube_playlist, new YoutubePlaylistView.YoutubePlaylistViewCallback() {
				@Override
				public void didYoutubePlaylistViewActioned(
						final YoutubePlaylistModel youtube_playlist_model,
						final YoutubeModel youtube_model) {
					AppController.openYoutubeActivity(instance, youtube_playlist_model, youtube_model);
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 10, 0, 0);
			vw_youtube_playlist.getView().setLayoutParams(lp);
			lay_main.addView(vw_youtube_playlist.getView());
			//menaruh hasilnya dari bawah ke atas
			//lay_main.addView(vw_youtube_playlist.getView(),0);
		}

	}



}