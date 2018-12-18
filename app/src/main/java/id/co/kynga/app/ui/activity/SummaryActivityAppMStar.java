package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.demo.PlayerActivity;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.model.VideoTrailerModel;
import id.co.kynga.app.ui.view.TrailerView;

import static id.co.kynga.app.KyngaApp.context;

public class SummaryActivityAppMStar extends CommonActivity {
	private ImageButton btn_back;
	private TextView lbl_title;
	private RecyclingImageView img_poster;
	private TextView lbl_name;
	private TextView lbl_summary;
	private LinearLayout lay_more;
	private ImageButton btn_play;

	private VideoModel video_model;
	private String title;
	private String videoCategoryId;
	private String clickOrigin;
	public static SummaryActivityAppMStar instance;
	private Tracker mTracker;
	private static final String TAG = "VideoActivity";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary_app_mstar);
		//GlobalController.showToast("test--000", 20000);
		setInitial();
		/*
		btn_play = (ImageButton) findViewById(R.id.btn_play);
		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//doPlay();
				Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
				//intent.putExtra("Title", title);
				//intent.putExtra(VideoModel.TAG, video_model);
				startActivity(intent);
			}
		});
		*/
	}

/*
	private void setInitial() {
		if (getIntent().hasExtra(VideoModel.TAG)) {
			video_model = getIntent().getParcelableExtra(VideoModel.TAG);
		}
		if (getIntent().hasExtra("Title")) {
			title = getIntent().getStringExtra("Title");
		}
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		img_poster = (RecyclingImageView) findViewById(R.id.img_poster);
		lbl_name = (TextView) findViewById(R.id.lbl_name);
		lbl_summary = (TextView) findViewById(R.id.lbl_summary);
		lay_more = (LinearLayout) findViewById(R.id.lay_more);
		btn_play = (ImageButton) findViewById(R.id.btn_play);
		populateData();
		setEventListener();
	}
*/
	private void setInitial() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		img_poster = (RecyclingImageView) findViewById(R.id.img_poster);
		lbl_name = (TextView) findViewById(R.id.lbl_name);
		lbl_summary = (TextView) findViewById(R.id.lbl_summary);
		lay_more = (LinearLayout) findViewById(R.id.lay_more);
		btn_play = (ImageButton) findViewById(R.id.btn_play);

		if (getIntent().hasExtra(VideoModel.TAG)) {
			video_model = getIntent().getParcelableExtra(VideoModel.TAG);
		}
		if (getIntent().hasExtra("Title")) {
			title = getIntent().getStringExtra("Title");
		}
		if (getIntent().hasExtra("videoCategoryId")) {
			videoCategoryId = getIntent().getStringExtra("videoCategoryId");
		}
		if (getIntent().hasExtra("clickOrigin")) {
			clickOrigin = getIntent().getStringExtra("clickOrigin");
		}
		if (getIntent().hasExtra(VideoModel.TAG) && getIntent().hasExtra("Title") ) {
			populateData();
			setEventListener();
		}
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				/*
				Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
				//intent.putExtra("Title", title);
				//intent.putExtra(VideoModel.TAG, video_model);
				startActivity(intent);
				*/
				doPlay();
			}
		});
	}

	public static void openVideoActivity(
			final Activity activity,
			final VideoModel video_model) {
		if (activity == null) {
			return;
		}
		final Intent intent = new Intent(context, PlayerActivity.class);
		//intent.putExtra(VideoModel.TAG, video_model);
		intent.setData(Uri.parse(video_model.LinkURL));
		intent.putExtra(PlayerActivity.EXTENSION_EXTRA, "");
		intent.setAction(PlayerActivity.ACTION_VIEW);
		intent.putExtra("subscription", "pay");
		activity.startActivity(intent);
	}

	private void doPlay() {
/*
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		Log.i(TAG, "Setting screen name: " + "Video");
		mTracker.setScreenName("Screen~" + "Video " +title);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
*/
		if (videoCategoryId.equals("0")) {
			//AppController.openVideoActivityNotFree(this, video_model, clickOrigin);
			AppController.openVideoActivity(this, video_model);
		}
		else{
			AppController.openVideoActivity(this, video_model);
		}
	}

	private void populateData() {
		lbl_title.setText(title);
		ImageLoader.getInstance().displayImage(video_model.ImageURL, img_poster, ImageController.getOption(true));
		lbl_name.setText(video_model.Title);
		lbl_summary.setText(video_model.Summary);
		if (video_model.video_trailer_model.list.size() > 0) {
			final TrailerView vw_trailer = new TrailerView(this, video_model.video_trailer_model, new TrailerView.TrailerViewCallback() {
				@Override
				public void didTrailerViewActioned(VideoTrailerModel video_trailer_model) {
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 10, 0, 0);
			vw_trailer.getView().setLayoutParams(lp);
			lay_more.addView(vw_trailer.getView());
		}
	}
}