package id.co.kynga.app.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.exoplayer.HlsRendererBuilder;
import id.co.kynga.app.general.controller.GoogleAnalyticsController;
import id.co.kynga.app.ui.adapter.CategoriesAdapter;
import id.co.kynga.app.ui.adapter.ListChannelAdapter;
import id.co.kynga.app.ui.adapter.ListMenuAdapter;
import id.co.kynga.app.exoplayer.DashRendererBuilder;
import id.co.kynga.app.exoplayer.DemoPlayer;
import id.co.kynga.app.exoplayer.DemoPlayer.CaptionListener;
import id.co.kynga.app.exoplayer.DemoPlayer.Listener;
import id.co.kynga.app.exoplayer.DemoPlayer.Id3MetadataListener;
import id.co.kynga.app.exoplayer.EventLogger;
import id.co.kynga.app.exoplayer.ExoPlayerControl;
import id.co.kynga.app.exoplayer.ExoPlayerControl.MediaPlayerControl;
import id.co.kynga.app.exoplayer.ExtractorRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingRendererBuilder;
import id.co.kynga.app.exoplayer.SmoothStreamingTestMediaDrmCallback;
import id.co.kynga.app.exoplayer.WidevineTestMediaDrmCallback;
import id.co.kynga.app.exoplayer.DemoPlayer.RendererBuilder;
import id.co.kynga.app.model.PreferenceEntity;
import id.co.kynga.app.model.User;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.ForceLoginDialog;
import id.co.kynga.app.util.ForceLoginDialog.LoginListener;
import id.co.kynga.app.util.LoaderService;
import id.co.kynga.app.util.NoConnectionDialog;
import id.co.kynga.app.util.NoDataDialog;
import id.co.kynga.app.util.NoDataDialog.NoDataListener;
import id.co.kynga.app.util.PreferenceUtil;
import id.co.kynga.app.util.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class F4ouKActivity extends AppCompatActivity implements
		Callback,
		Listener,
		CaptionListener,
		Id3MetadataListener,
		MediaPlayerControl,
		LoginListener {
	public static final int TYPE_DASH = 0;
	public static final int TYPE_SS = 1;
	public static final int TYPE_HLS = 2;
	public static final int TYPE_OTHER = 3;
	private AspectRatioFrameLayout videoFrame;
	private FrameLayout mainFrame;
	private SurfaceView surfaceView;
	//private ExoPlayerControl controller;
	private EventLogger eventLogger;
	private DemoPlayer player;
	private boolean playerNeedsPrepare;
	private long playerPosition;
	//private boolean enableBackgroundAudio;
	private Uri contentUri;//,alternateContentUri;
	private int contentType = 2;
	private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
	private TextView textNotif, textBuffer;//,no_package;
	private ProgressBar pb;//,gridBar;
	private ListView lCategory, lChannel;
	private CategoriesAdapter fCategories;
	private PreferenceUtil pref;
	private LinearLayout menuLayout;
	private Boolean isMenuShowing = false;
	private String categoryName = "", u = "";
	private ArrayList<String> loadedData = new ArrayList<String>();
	AudioManager am;
	private Timer t, t2;
	private TimerTask mTimerTask, nTimerTask;
	private Handler handler = new Handler();
	private int counter = 0, count = 0, attempt = 0;
	private int keyPageIndex = -1;
	private String[] numKey = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private String KEY = "";
	private Boolean isKeyPressed = false, isNumberPressed = false;
	private TextView tChnNumber, tchName;
	private ImageView banner;
	private int selected = -1;
	private boolean firstLoad = true, isWarned = false;
	private String selectedCategory = "";
	private boolean errorShown = false;//,paused = false;//,isReset = false;
	private String userPackage = "BASIC";
	//private String tvPage= "",tvCode = "";
	private String playUrl = "", tCaption = "", id = "", videoType;
	private ForceLoginDialog fDialog;
	ImageView vSelected = null;
	private DisplayMetrics metrics;
	//private ImageButton i1,i2;
	private String PAGE_CODE, PAGE_URL;
	private PreferenceEntity entity;
	private ArrayList<HashMap<String, String>> dataAdapter, fourKCategories;
	private ListChannelAdapter cAdapter;
	private ListMenuAdapter lAdapter;
	private KyngaApp app;
	private int currentWidth, currentHeight;
	private float aspectRatio;
	private static final int MODE_STANDARD = 1;
	private static final int MODE_FULL_WITH_ASPECT_RATIO = 2;
	private static final int MODE_FULLSCREEN = 3;
	private int currentMode = 1;
	private ExoPlayerControl controller;
//private int playState;
//private LinearLayout lSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.four_k_layout);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//Crittercism.initialize(getApplicationContext(), DataVariable.CRITTERCISM_APP_ID);
		entity = new PreferenceEntity(this);
		surfaceView = (SurfaceView) findViewById(R.id.newTvsurface);
		surfaceView.getHolder().addCallback(this);
		videoFrame = (AspectRatioFrameLayout) findViewById(R.id.videoFrame);
		mainFrame = (FrameLayout) findViewById(R.id.tvMainFrame);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		banner = (ImageView) findViewById(R.id.player_banner);
		pref = new PreferenceUtil();
		User user = User.getInstance();
		u = user.getName();
		controller = new ExoPlayerControl(this);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		controller.setAnchorView(mainFrame);
		controller.setMediaPlayer(this);
		lCategory = (ListView) findViewById(R.id.listCategory);
		lChannel = (ListView) findViewById(R.id.listChannel);
		menuLayout = (LinearLayout) findViewById(R.id.tvMenu);
		pb = (ProgressBar) findViewById(R.id.tvprogress);
		tChnNumber = (TextView) findViewById(R.id.ch_number);
		textNotif = (TextView) findViewById(R.id.textNotif);
		textBuffer = (TextView) findViewById(R.id.textBuffering);
		tchName = (TextView) findViewById(R.id.channelName);
		if (!Utils.isNetworkAvailable()) {
			NoConnectionDialog noConnectionDialog = new NoConnectionDialog(this);
			noConnectionDialog.show();
			errorShown = true;
		}
		surfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isMenuShowing) {
					showMenu();
					endTask();
					doTimerTask();

				} else {
					hideMenu();
					//controller.hide();
				}
				Log.d("Surface", "Clicked");
			}

		});
		lCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, String> mCat = fourKCategories.get(position);
				String cId = mCat.get("id");
				String cName = mCat.get("name");
				getFourKMovies(cId, cName);
				categoryName = cName;

				lAdapter.notifyDataChanged(position);
				lAdapter.notifyDataSetChanged();
				endTask();
				doTimerTask();
				firstLoad = false;
				//controller.show();
			}

		});
		lCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				// TODO Auto-generated method stub

				endTask();
				doTimerTask();
				controller.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
		lChannel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> data, View view, int position,
			                        long arg3) {
				//ArrayList<HashMap<String, String>> dataAdapter = d_adapter.getData();
				if (dataAdapter.size() > 0) {
					// TODO Auto-generated method stub
					HashMap<String, String> movie = new HashMap<String, String>();
					movie = dataAdapter.get(position);
					//isAutoBitrate = true;
					playUrl = movie.get("playUrl");
					//playUrl = movie.get(DataVariable.CRID);
					id = movie.get("id");
					tCaption = movie.get("title");
					videoType = movie.get(DataVariable.VIDEO_TYPE);
					playVideo(playUrl, categoryName, tCaption, u, id, videoType);
					selected = position;
					selectedCategory = categoryName;
					cAdapter.setIndex(position);
					cAdapter.notifyDataSetChanged();
					endTask();
					doTimerTask();
					firstLoad = false;
					isWarned = false;
					attempt = 0;
					//mBitrateLayout.removeAllViews();
				}
			}
		});

		lChannel.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				// TODO Auto-generated method stub
				endTask();
				doTimerTask();
				controller.show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}


		});
		fDialog = new ForceLoginDialog();
		fDialog.setListener(this);

		endTask();
		doTimerTask();
		getData();

	}


	private void startLoaderService(ArrayList<String> loader) {
		Intent inten = new Intent(this, LoaderService.class);
		inten.putStringArrayListExtra(LoaderService.DATA, loader);
		startService(inten);
	}


	public void doTimerTask() {
		t2 = new Timer();
		mTimerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if (counter >= 5) {
							//mTimerTask.cancel();
							endTask();
							if (isMenuShowing) {
								hideMenu();
							}
						} else {

							counter++;
						}

					}
				});
			}
		};


		t2.schedule(mTimerTask, 0, 1000); //
	}


	public void endTask() {
		if (t2 != null) {
			t2.cancel();
			t2.purge();
			mTimerTask = null;
		}
		counter = 0;
	}

	public void countTimer() {
		isKeyPressed = true;
		t = new Timer();
		nTimerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if (count >= 20) {
							//mTimerTask.cancel();

							//keyname.setText(KEY);
							if (!KEY.equals("")) {

								try {
									keyPageIndex = Integer.valueOf(KEY);
									playChannel(keyPageIndex);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
							stopTask();
							isKeyPressed = false;
						} else {

							count++;
						}

					}
				});
			}
		};


		t.schedule(nTimerTask, 0, 100); //
	}


	public void stopTask() {
		if (t != null) {
			t.cancel();
			t.purge();
			nTimerTask = null;
		}
		KEY = "";
		count = 0;
	}

	private void getData() {
		Bundle bundle = getIntent().getExtras();
		PAGE_CODE = bundle.getString("code") != null ? bundle.getString("code") : "";
		PAGE_URL = bundle.getString("paramurl") != null ? bundle.getString("paramurl") : "";
		fCategories = new CategoriesAdapter(this, PAGE_CODE, PAGE_URL);
		fourKCategories = fCategories.getData();
		if (fourKCategories.size() > 0) {
			setup_header(fourKCategories);
			categoryName = fourKCategories.get(0).get("name");
			String fId = fourKCategories.get(0).get("id");
			String fName = fourKCategories.get(0).get("name");
			getFourKMovies(fId, fName);
			for (int d = 0; d < fourKCategories.size(); d++) {
				HashMap<String, String> gData = fourKCategories.get(d);
				String codeName = gData.get("name");
				String url = DataVariable.BASE_URL + "movies/" + gData.get("id");
				loadedData.add(codeName + "|" + url);
			}
			if (loadedData != null) {
				startLoaderService(loadedData);
			}
		} else {

			NoDataDialog noDataDialog = new NoDataDialog(this);
			noDataDialog.setListener(new NoDataListener() {

				@Override
				public void onRetry() {
					// TODO Auto-generated method stub
					getData();
				}

				@Override
				public void onExit() {
					// TODO Auto-generated method stub
					finish();
				}

			});
			noDataDialog.show();
			errorShown = true;
		}
	}


	private void setup_header(ArrayList<HashMap<String, String>> tData) {
		lAdapter = new ListMenuAdapter(this, tData);
		lCategory.setAdapter(lAdapter);
		lAdapter.notifyDataSetChanged();
		HashMap<String, String> nCat = tData.get(0);
		String cId = nCat.get("id");
		String cName = nCat.get("name");


		getFourKMovies(cId, cName);
		endTask();
		doTimerTask();
		if (tData.size() > 1) {
			lCategory.setVisibility(View.VISIBLE);
		} else {
			lChannel.requestFocus();
		}

		for (HashMap<String, String> m : tData) {
			Log.d("Categories", "Id :" + m.get("id") + ", name :" + m.get("name"));
		}
	}


	private void playChannel(int channel) {
		if (dataAdapter.size() > 0) {
			HashMap<String, String> mt = dataAdapter.get(channel - 1);
			String murl = mt.get("playUrl");
			//String murl = mt.get(DataVariable.CRID);
			String mid = mt.get("id");
			String mCaption = mt.get("title");
			videoType = mt.get(DataVariable.VIDEO_TYPE);
			playVideo(murl, categoryName, mCaption, u, mid, videoType);
		}

	}


	private void preparePlayer(boolean playWhenReady) {

		if (player == null) {
			player = new DemoPlayer(getRendererBuilder());
			player.addListener(this);
			player.setCaptionListener(this);
			player.setMetadataListener(this);

			player.seekTo(playerPosition);
			playerNeedsPrepare = true;
			eventLogger = new EventLogger();
			eventLogger.startSession();
			player.addListener(eventLogger);
			player.setInfoListener(eventLogger);
			player.setInternalErrorListener(eventLogger);


		}
		if (playerNeedsPrepare) {
			player.prepare();
			playerNeedsPrepare = false;

		}
		player.setSurface(surfaceView.getHolder().getSurface());
		player.setPlayWhenReady(playWhenReady);
		if (firstLoad) {
			showMenu();
		}
		// validationTimer();
	}


	private void playVideo(
			String src,
			String tv_category,
			String tvname,
			String user,
			String channelID,
			String type) {

		//tBanner.setText("");
		//contentUri = Uri.parse(src);

		if (type.equals("1919")) {
			contentType = TYPE_OTHER;
			//contentUri = Uri.parse("http://localhost:55055/video/mp4/" + src);
			contentUri = Uri.parse(src);

		} else if (type.equals("2828")) {
			contentType = TYPE_HLS;
			//contentUri = Uri.parse("http://localhost:55055/video/hls/" + src + ".m3u8");
			contentUri = Uri.parse(src);
		}

		Log.d("Content URI", contentUri.toString());
		if (player == null) {
			preparePlayer(true);
		} else {
			resetPlayer();
			//contentUri = Uri.parse(src);
			player.setRendererBuilder(getRendererBuilder());
			player.prepare();
			playerNeedsPrepare = (false);
		}
		tchName.setText(tvname);
		GoogleAnalyticsController.hitEvent("TV", tv_category, tvname, user);
		isWarned = false;

	}

	private void resetPlayer() {
		if (player != null) {
			player.stop();
			player.seekTo(0L);

		}
	}

	private void releasePlayer() {
		if (player != null) {
			playerPosition = player.getCurrentPosition();
			player.release();
			player = null;

		}
	}

	@Override
	public void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				if (!isMenuShowing) {
					showMenu();
					endTask();
					doTimerTask();

				}
			} else if (keycode == KeyEvent.KEYCODE_F1) {


			} else if (keycode == KeyEvent.KEYCODE_PAGE_UP) {
				if (keyPageIndex == dataAdapter.size()) {
					keyPageIndex = 0;
				} else {
					keyPageIndex += 1;
				}
				HashMap<String, String> mt = dataAdapter.get(keyPageIndex);
				String murl = mt.get("playUrl");
				//String murl = mt.get(DataVariable.CRID);
				String mid = mt.get("id");
				String mCaption = mt.get("title");
				videoType = mt.get(DataVariable.VIDEO_TYPE);
				playVideo(murl, categoryName, mCaption, u, mid, videoType);
				selected = keyPageIndex;

			} else if (keycode == KeyEvent.KEYCODE_PAGE_DOWN) {
				if (keyPageIndex == 0) {
					keyPageIndex = dataAdapter.size() - 1;
				} else {
					keyPageIndex -= 1;
				}
				HashMap<String, String> mt = dataAdapter.get(keyPageIndex);
				String murl = mt.get("playUrl");
				//String murl = mt.get(DataVariable.CRID);
				String mid = mt.get("id");
				String mCaption = mt.get("title");
				videoType = mt.get(DataVariable.VIDEO_TYPE);
				playVideo(murl, categoryName, mCaption, u, mid, videoType);
				selected = keyPageIndex;
			} else {
				char keyName = event.getDisplayLabel();
				final String key = String.valueOf(keyName);
				for (String k : numKey) {
					if (key.equals(k)) {
						KEY += key;
						isNumberPressed = true;
						tChnNumber.setText(KEY);
					}
				}
				//keyname.setText(KEY);
				if (!isKeyPressed && isNumberPressed) {
					countTimer();
				}
			}


		}
		return super.dispatchKeyEvent(event);

	}


	private RendererBuilder getRendererBuilder() {
		String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

		switch (contentType) {

			case TYPE_SS:
				return new SmoothStreamingRendererBuilder(this, userAgent, contentUri.toString(),
						new SmoothStreamingTestMediaDrmCallback());
			case TYPE_DASH:
				return new DashRendererBuilder(this, userAgent, contentUri.toString(),
						new WidevineTestMediaDrmCallback());
			case TYPE_HLS:
				return new HlsRendererBuilder(this, userAgent, contentUri.toString());
			case TYPE_OTHER:
				return new ExtractorRendererBuilder(this, userAgent, contentUri);
			default:
				throw new IllegalStateException("Unsupported type: " + contentType);
		}
	}


	public void getFourKMovies(String mId, String mName) {
		//String nomadPref = pref.getPreference(mId);
		pb.setVisibility(View.VISIBLE);
		String token = pref.getPreference("token");
		entity = new PreferenceEntity(this);
		entity.open();
		String nomadPref = entity.getPreference(mId);
		if (nomadPref.length() > 0) {
			parseFourKMovie(nomadPref);

		} else {

			StringRequest request = new StringRequest(Request.Method.GET,
					DataVariable.BASE_URL + "movies/" + mId + "?token=" + token,
					new Response.Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							parseFourKMovie(arg0);

						}
					}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
				}
			});
			KyngaApp.getInstance().addToRequestQueue(request);
		}
	}

	private void parseFourKMovie(String nMovie) {
		try {
			JSONArray result = new JSONArray(nMovie);
			if (result.length() > 0) {
				if (dataAdapter != null) {
					dataAdapter.clear();
				} else {
					dataAdapter = new ArrayList<>();
				}

				for (int i = 0; i < result.length(); i++) {
					JSONObject j = result.getJSONObject(i);
					HashMap<String, String> mp = new HashMap<String, String>();
					mp.put("id", j.getString("id"));
					mp.put(DataVariable.KEY_IMAGE, j.getString("img1"));
					mp.put(DataVariable.KEY_TITLE, j.getString("title"));
					//map.put(DataVariable.KEY_CATEGORY, j.getString("categoryCode"));
					mp.put(DataVariable.KEY_URL, j.getString("playUrl"));
					mp.put(DataVariable.KEY_ACTOR, j.getString("actor"));
					mp.put(DataVariable.KEY_YEAR, j.getString("year"));
					mp.put(DataVariable.KEY_CODE, j.getString("code"));
					mp.put(DataVariable.KEY_DESC, j.getString("desc"));
					mp.put(DataVariable.VIDEO_TYPE, j.getString("type"));
					mp.put(DataVariable.CRID, j.getString("crid"));
					mp.put("genre", j.getString("genre"));
					dataAdapter.add(mp);
				}
				if (dataAdapter.size() > 0) {
					playUrl = dataAdapter.get(0).get("playUrl");
					//playUrl = dataAdapter.get(0).get(DataVariable.CRID);
					id = dataAdapter.get(0).get("id");
					tCaption = dataAdapter.get(0).get("title");
					videoType = dataAdapter.get(0).get(DataVariable.VIDEO_TYPE);
					playVideo(playUrl, categoryName, tCaption, u, id, videoType);
					selectedCategory = categoryName;
					//selected = 0;
					cAdapter = new ListChannelAdapter(this, dataAdapter);
					lChannel.setAdapter(cAdapter);
					if (firstLoad) {
						cAdapter.setIndex(0);
					}
					cAdapter.notifyDataSetChanged();
					if (lAdapter != null) {
						lAdapter.notifyDataChanged(0);
					}
					pb.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Nomad movie", "Error :" + e.getMessage());
		}
	}


	public void hideMenu() {
		Animation slide_out = AnimationUtils.loadAnimation(this, R.anim.out_to_left);
		Animation slide_down = AnimationUtils.loadAnimation(this, R.anim.slide_down);
		menuLayout.startAnimation(slide_out);
		//lSetting.startAnimation(get_out);
		//lSetting.setVisibility(View.GONE);

		controller.startAnimation(slide_down);
		menuLayout.setVisibility(View.GONE);
		isMenuShowing = false;
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				controller.hide();
			}

		}, 800);

	}


	public void showMenu() {
		Animation slide_in = AnimationUtils.loadAnimation(this, R.anim.in_from_left);
		Animation slide_up = AnimationUtils.loadAnimation(this, R.anim.draw_from_bottom);
		menuLayout.setVisibility(View.VISIBLE);
		menuLayout.startAnimation(slide_in);
		controller.show();
		controller.startAnimation(slide_up);
		//lSetting.startAnimation(get_in);
		//lSetting.setVisibility(View.VISIBLE);
		lChannel.requestFocus();
		lChannel.setSelection(selected);
		isMenuShowing = true;


	}

	private void switchFullScreen() {
		//metrics = Resources.getSystem().getDisplayMetrics();
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		switch (currentMode) {
			case MODE_STANDARD:
				videoFrame.setAspectRatio((screenWidth * aspectRatio) / screenHeight);
				currentMode = MODE_FULL_WITH_ASPECT_RATIO;
				break;
			case MODE_FULL_WITH_ASPECT_RATIO:
				videoFrame.setAspectRatio(0);
				//videoFrame.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, screenHeight));
				currentMode = MODE_FULLSCREEN;
				break;
			case MODE_FULLSCREEN:
				videoFrame.setAspectRatio((currentWidth * aspectRatio) / currentHeight);
				currentMode = MODE_STANDARD;
				break;
		}


	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (player != null) {
			player.setSurface(holder.getSurface());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
	                           int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (player != null) {
			player.blockingClearSurface();
		}
	}

	@Override
	public void onCues(List<Cue> cues) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		// TODO Auto-generated method stub
		//playState = playbackState;
		String text = "";
		switch (playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				text += "buffering " + player.getBufferedPercentage() / 100 + " %";
				pb.setVisibility(View.VISIBLE);
				controller.setEnabled(false);
				// textBuffer.setVisibility(View.VISIBLE);
				break;
			case ExoPlayer.STATE_ENDED:
				text += "ended";
				pb.setVisibility(View.GONE);
				controller.setEnabled(true);
				break;
			case ExoPlayer.STATE_IDLE:
				text += "idle";
				controller.setEnabled(false);
				break;
			case ExoPlayer.STATE_PREPARING:
				text += "preparing";
				pb.setVisibility(View.VISIBLE);
				banner.setVisibility(View.GONE);
				textNotif.setVisibility(View.GONE);
				controller.setEnabled(false);
				break;
			case ExoPlayer.STATE_READY:
				pb.setVisibility(View.GONE);
				textBuffer.setVisibility(View.GONE);
				controller.setEnabled(true);
				text += "ready";
				break;
			default:
				text += "unknown";
				break;
		}
	}

	@Override
	public void onError(Exception e) {
		// TODO Auto-generated method stub
		if (e instanceof UnsupportedDrmException) {
			// Special case DRM failures.
			UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
			int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
					: unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
					? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
			Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
		}
		if (attempt < 5) {
			preparePlayer(true);
			attempt++;
		} else {

			playerNeedsPrepare = true;
			banner.setVisibility(View.VISIBLE);
			textNotif.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
		}
		playerNeedsPrepare = true;
	}

	@Override
	public void onVideoSizeChanged(int width, int height,
	                               int unappliedRotationDegrees, float pixelWidthHeightRatio) {
		// TODO Auto-generated method stub
		videoFrame.setAspectRatio(
				height == 0 ? 1 : (width * pixelWidthHeightRatio) / height);
		surfaceView.getHolder().setFixedSize(width, height);
		aspectRatio = pixelWidthHeightRatio;
		currentWidth = width;
		currentHeight = height;

	}

	@Override
	public void onPause() {
		super.onPause();
		releasePlayer();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releasePlayer();
	}


	@Override
	public void start() {
		// TODO Auto-generated method stub
		if (player != null) {
			player.setPlayWhenReady(true);
		}
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if (player != null) {
			player.setPlayWhenReady(false);
		}
	}


	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		if (player != null) {
			return player.getDuration();
		}
		return 0;
	}


	@Override
	public long getCurrentPosition() {
		// TODO Auto-generated method stub
		if (player != null) {
			return player.getCurrentPosition();
		}
		return -1;
	}


	@Override
	public void seekTo(long pos) {
		// TODO Auto-generated method stub
		if (player != null) {
			player.seekTo(pos);
		}
	}


	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if (player != null) {
			return player.getPlayWhenReady();
		}
		return false;
	}


	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		if (player != null) {
			return player.getBufferedPercentage();
		}
		return 0;
	}


	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isFullScreen() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void toggleFullScreen() {
		// TODO Auto-generated method stub
		switchFullScreen();
	}


	@Override
	public void onBitrateChange(String variantUrl, boolean isAuto) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		endTask();
		doTimerTask();
		ImageButton cPlay = (ImageButton) controller.findViewById(R.id.pause);
		ImageButton crew = (ImageButton) controller.findViewById(R.id.rew);
		ImageButton cfwd = (ImageButton) controller.findViewById(R.id.ffwd);
		cPlay.setNextFocusUpId(R.id.listChannel);
		crew.setNextFocusUpId(R.id.listChannel);
		cfwd.setNextFocusUpId(R.id.listChannel);
	}

	private void stopPlayer() {
		if (player != null) {
			player.setPlayWhenReady(false);
			isWarned = true;
		}
	}

	private void validationTimer() {

		handler.postDelayed(sessionRunnable, 20000);

	}

	private Runnable sessionRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			checkSession();
		}

	};

	private void validateUser(String cred) {
		String status = "";
		int BASIC = -1;
		try {
			JSONObject jObject = new JSONObject(cred);
			status = jObject.getString("code");
			if (status.equals("00")) {
				JSONArray packages = jObject.getJSONArray("package");
				if (packages.length() > 0) {
					for (int i = 0; i < packages.length(); i++) {
						JSONObject packageItem = packages.getJSONObject(i);
						JSONObject valObj = packageItem.getJSONObject("value");
						String pName = packageItem.getString("name");
						String pResult = valObj.getString("result");
						if (pName.equals(DataVariable.PACKAGE_BASIC)) {
							BASIC = Integer.valueOf(pResult);
							if (BASIC < 0) {
								if (!isWarned) {
									fDialog.show(getSupportFragmentManager(), "Login");
									stopPlayer();
									return;
								}
							}
						}
					}
				}

			} else if (status.equals("E102")) {
				if (!isWarned) {
					fDialog.show(getSupportFragmentManager(), "Login");
					stopPlayer();
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkSession() {
		final String phone = pref.getPreference(DataVariable.PHONE_NUMBER);
		final String macId = Utils.wifiEnabled() ? Utils.getMACAddress("wlan0") : Utils.getMACAddress("eth0");
		final String session = pref.getPreference(DataVariable.SESSION);
		final String ACTION = "checksession";
		String paramSign = phone + macId + session + ACTION;
		final String signature = DataVariable.Md5(paramSign + DataVariable.SECRET_KEY);
		StringRequest request = new StringRequest(Request.Method.POST,
				DataVariable.BILLING_URL,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO Auto-generated method stub
						Log.d("Validation", arg0);
						validateUser(arg0);

					}
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Log.d("Validation", "Error :" + arg0.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", phone);
				//params.put("email", email);
				params.put("macID", macId);
				// params.put("password", password);
				params.put("sessionValue", session);
				params.put("action", ACTION);
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}


	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		finish();
	}


	@Override
	public void onLogged() {
		// TODO Auto-generated method stub
		if (player != null) {
			player.setPlayWhenReady(true);
		}
	}

	@Override
	public void onBackPressed() {
		if (isMenuShowing) {
			hideMenu();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onId3Metadata(List<Id3Frame> id3Frames) {

	}
}