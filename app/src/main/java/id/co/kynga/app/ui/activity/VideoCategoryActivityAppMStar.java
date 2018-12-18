package id.co.kynga.app.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.ui.adapter.VideoCategoryListAdapter;


public class VideoCategoryActivityAppMStar extends CommonActivity {
	public static VideoCategoryActivityAppMStar instance;

	private ImageButton btn_back;
	private TextView lbl_title;
	private GridView gvw_main;
	private VideoCategoryListAdapter video_category_list_adapter;

	private ArrayList<VideoModel> video_list;
	private String title;
	private boolean init;
	private String videoCategoryId;
	private String clickOrigin;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_video_category_app_mstar);
		init = true;
		setInitial();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (init) {
			init = false;
			populateData();
		}
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		if (getIntent().hasExtra(VideoModel.TAG)) {
			final VideoModel video_model = getIntent().getParcelableExtra(VideoModel.TAG);
			video_list = new ArrayList<>(video_model.list);
		}
		if (getIntent().hasExtra("Title")) {
			title = getIntent().getStringExtra("Title");
		}
		if (getIntent().hasExtra("videoCategoryId")) {
			videoCategoryId = getIntent().getStringExtra("videoCategoryId");
		}
		if (getIntent().hasExtra("clickOrigin")) {
			clickOrigin = getIntent().getStringExtra("clickOrigin");
		}else{
			clickOrigin = "clickVideo";
		}
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		lbl_title.setTypeface(null, Typeface.BOLD);
		gvw_main = (GridView) findViewById(R.id.gvw_main);
		setEventListener();
		lbl_title.setText(title);
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
				final VideoModel video_model = video_list.get(i);
				//AppController.openSummaryActivity(instance, title, video_model);
				//GlobalController.showToast(clickOrigin, 20000);
				AppController.openSummaryActivity2(instance, title, video_model, videoCategoryId, clickOrigin);
			}
		});
	}

	private void populateData() {
		//GlobalController.showToast(String.valueOf(video_list.size()), 20000);
		//Kalau tidak Full Screen maka akan crash
		video_category_list_adapter = new VideoCategoryListAdapter(video_list);
		gvw_main.setAdapter(video_category_list_adapter);
	}
}