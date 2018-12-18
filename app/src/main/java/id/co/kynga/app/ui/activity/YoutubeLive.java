package id.co.kynga.app.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.R;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.squareup.picasso.Picasso;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.GoogleAnalyticsController;
import id.co.kynga.app.ui.adapter.YoutubeAdapter;
import id.co.kynga.app.ui.adapter.YoutubeListRegionAdapter;
import id.co.kynga.app.model.PreferenceEntity;
import id.co.kynga.app.model.User;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.LoaderService;
import id.co.kynga.app.util.PreferenceUtil;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class YoutubeLive extends YouTubeFailureRecoveryActivity implements OnFullscreenListener {
	private YouTubePlayerView youTubePlayerView;
	private YouTubePlayer player;
	private YoutubeAdapter yAdapter;
	private Button btn_fullscreen;
	private ArrayList<Map<String, String>> regionAdapter;
	private ArrayList<Map<String, String>> channelAdapter;
	private YoutubeListRegionAdapter cAdapter;
	private ListView regionList;
	private String categoryName;

	private Handler handler = new Handler();
	private PreferenceUtil pref;
	private String token;
	private String u;
	private boolean firstVideo = true;
	private ArrayList<String> loaderData = new ArrayList<String>();
	private PreferenceEntity entity;
	private boolean fullscreen;
	private LinearLayout channelLayout;
	private HorizontalScrollView hScroll;
	private LinearLayout mLayout, initLayout, initError;
	private Button bCancel, bRetry;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private ImageView loading;
	private ArrayList<View> vi_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.youtube_live);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		showLoading();
		showContent();
	}

	private void showContent() {
		youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubePlayerView.initialize(GlobalController.getString(R.string.google_api_key), this);
		btn_fullscreen = (Button) findViewById(R.id.btn_fullscreen);
		channelLayout = (LinearLayout) findViewById(R.id.channelScroll);
		hScroll = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		regionList = (ListView) findViewById(R.id.lRegion);
		mLayout = (LinearLayout) findViewById(R.id.mainLayout);
		initLayout = (LinearLayout) findViewById(R.id.initialize_layout);
		initError = (LinearLayout) findViewById(R.id.initialize_error);
		bCancel = (Button) findViewById(R.id.bInitCancel);
		bRetry = (Button) findViewById(R.id.bInitRetry);
		entity = new PreferenceEntity(this);
		User user = User.getInstance();
		u = user.getName();
		//tchNumber = (TextView)findViewById(R.id.textChNumber);
		pref = new PreferenceUtil();
		token = pref.getPreference("token");
		youTubePlayerView.requestFocus();
		regionList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				Map<String, String> rMap = regionAdapter.get(position);
				String pId = rMap.get("id");
				String pName = rMap.get("title");
				categoryName = pName;
				getYoutubeContent(pId);
			}

		});


		bCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}

		});
		bRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setupData();
			}
		});

		btn_fullscreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				fullscreen = true;
				player.setFullscreen(true);
			}
		});
	}

	private void showLoading() {
		loading = (ImageView) findViewById(R.id.youtube_loader);
		AnimationSet anim = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(600);
		rotate.setInterpolator(new DecelerateInterpolator());
		rotate.setRepeatCount(RotateAnimation.INFINITE);
		anim.addAnimation(rotate);
		loading.startAnimation(anim);
	}

	private void setupData() {
		showLoading();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String ytPage = bundle.getString("youtube_page");
			String ytCode = bundle.getString("ytcode");
			yAdapter = new YoutubeAdapter(this, ytPage, ytCode);
			regionAdapter = yAdapter.getData();
			categoryName = ytPage;
			if (regionAdapter.size() > 0) {
				cAdapter = new YoutubeListRegionAdapter(this, regionAdapter);
				regionList.setAdapter(cAdapter);
				Map<String, String> fMap = regionAdapter.get(0);
				String fId = fMap.get("id");
				String fName = fMap.get("title");
				categoryName = fName;
				getYoutubeContent(fId);
				for (Map<String, String> m : regionAdapter) {
					String prefPlaylist = m.get("id") + "|" + DataVariable.BASE_URL + "youtubeVideoList/" + m.get("id");
					loaderData.add(prefPlaylist);
				}

				if (loaderData.size() > 0) {
					startLoaderService(loaderData);
				}
			}
		}
	}

	private void startLoaderService(ArrayList<String> data) {
		Intent inten = new Intent(this, LoaderService.class);
		inten.putStringArrayListExtra(LoaderService.DATA, data);
		startService(inten);
	}

	private void getYoutubeContent(String youtubeId) {
		entity.open();
		String playlistPref = entity.getPreference(youtubeId);
		if (!playlistPref.equals("") || playlistPref.length() > 0) {
			parseYoutubeContent(playlistPref);
		} else {
			StringRequest request = new StringRequest(Method.GET,
					DataVariable.BASE_URL + "youtubeVideoList/" + youtubeId + "?token=" + token,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							parseYoutubeContent(arg0);
						}
					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					if (loading != null) {
						loading.clearAnimation();
					}
					initError.setVisibility(View.VISIBLE);
				}
			});
			KyngaApp.getInstance().addToRequestQueue(request);
		}
	}

	private void parseYoutubeContent(String content) {
		channelAdapter = new ArrayList<>();
		try {
			JSONArray jArray = new JSONArray(content);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataVariable.YOUTUBE_KEY_ID, json_data.getString(DataVariable.YOUTUBE_KEY_ID));
				map.put(DataVariable.YOUTUBE_KEY_IMAGE, json_data.getString(DataVariable.KEY_IMAGE));
				map.put(DataVariable.YOUTUBE_KEY_TITLE, json_data.getString(DataVariable.YOUTUBE_KEY_TITLE));
				channelAdapter.add(map);
			}
		} catch (JSONException e) {
		}
		if (channelAdapter.size() > 0) {
			setUpChannelList(channelAdapter);
			if (firstVideo) {
				Map<String, String> firstMap = channelAdapter.get(0);
				String firstId = firstMap.get("id");
				String firstTitle = firstMap.get("title");
				playVideo(categoryName, firstTitle, u, firstId);
				firstVideo = false;
			}
			if (loading != null) {
				loading.clearAnimation();
			}
			initLayout.setVisibility(View.GONE);
			mLayout.setVisibility(View.VISIBLE);
		} else {
			if (loading != null) {
				loading.clearAnimation();
			}
			initError.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (fullscreen) {
				player.setFullscreen(false);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		if (fullscreen) {
			player.setFullscreen(false);
		} else {
			super.onBackPressed();
		}
	}

	private void playVideo(String c, String name, String u, final String id) {
		GoogleAnalyticsController.hitEvent("YoutubeLive", c, name, u);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (player != null) {
					player.loadVideo(id);
					youTubePlayerView.requestFocus();
				}
			}

		}, 1000);

	}


	@Override
	public void onStop() {
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
		super.onStop();
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
										boolean arg2) {
		// TODO Auto-generated method stub
		this.player = arg1;
		player.setPlayerStyle(PlayerStyle.CHROMELESS);
		player.setOnFullscreenListener(this);
		/*player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		int controlFlags = player.getFullscreenControlFlags();
		controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
		player.setFullscreenControlFlags(controlFlags);*/
		player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
			@Override
			public void onLoading() {
			}

			@Override
			public void onLoaded(String paramString) {
			}

			@Override
			public void onAdStarted() {
			}

			@Override
			public void onVideoStarted() {
			}

			@Override
			public void onVideoEnded() {
			}

			@Override
			public void onError(YouTubePlayer.ErrorReason paramErrorReason) {
			}
		});
		setupData();
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return youTubePlayerView;
	}

	@Override
	public void onInitializationFailure(YouTubePlayer.Provider provider,
										YouTubeInitializationResult errorReason) {
		if (loading != null) {
			loading.clearAnimation();
		}
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onFullscreen(boolean arg0) {
		fullscreen = arg0;
	}

	private void setUpChannelList(ArrayList<Map<String, String>> channels) {
		channelLayout.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int gridMargin = 2;
		int imageWidth = ((screenWidth - gridMargin) * 7 / 10) / 3;
		int imageHeight = imageWidth * 4 / 6;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
		params.setMargins(5, 0, 5, 0);
		vi_list = new ArrayList<>();
		for (final Map<String, String> c : channels) {
			final View vi = inflater.inflate(R.layout.youtube_grid_item, null);
			vi_list.add(vi);
			ImageView video_image = (ImageView) vi.findViewById(R.id.youtube_video_image);
			video_image.setScaleType(ScaleType.FIT_CENTER);
			video_image.setLayoutParams(params);
			TextView title = (TextView) vi.findViewById(R.id.youtube_video_title);
			title.setText(c.get(DataVariable.KEY_TITLE));
			Picasso.with(this)
					.load(c.get(DataVariable.YOUTUBE_KEY_IMAGE))
					.placeholder(R.drawable.bg_logo)
					.resize(imageWidth, imageHeight)
					.into(video_image);
			vi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String cId = c.get("id");
					String cName = c.get("title");
					playVideo(categoryName, cName, u, cId);
				}

			});
			vi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean b) {
					if (b) {
						for (View v : vi_list) {
							v.setSelected(false);
						}
						vi.setSelected(true);

					}
				}
			});
			channelLayout.addView(vi);
		}
	}
}