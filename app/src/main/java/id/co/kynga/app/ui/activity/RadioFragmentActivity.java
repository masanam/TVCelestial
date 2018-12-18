package id.co.kynga.app.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.analytics.GoogleAnalytics;
import id.co.kynga.app.R;
import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.general.controller.GoogleAnalyticsController;
import id.co.kynga.app.ui.adapter.RadioAdapter;
import id.co.kynga.app.ui.adapter.RadioCategories;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.ForceLoginDialog;
import id.co.kynga.app.util.ForceLoginDialog.LoginListener;
import id.co.kynga.app.util.PreferenceUtil;
import id.co.kynga.app.util.Utils;
import id.co.kynga.app.widget.VerticalSeekBar;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class RadioFragmentActivity extends FragmentActivity implements OnPreparedListener {
	private static final float VISUALIZER_HEIGHT_DIP = 50f;
	private RadioAdapter adapter;
	private RadioCategories rCategories;
	private ArrayList<HashMap<String, String>> dataAdapter, radiocategories;
	private ListView lv;
	private RadioListAdapter rAdapter;
	private GetRadioList gRadioList = null;
	private String categoryName;
	private ImageView rotateImage;
	private Visualizer mVisualizer;
	private Equalizer mEqualizer;
	private LinearLayout mLinearLayout;
	private LinearLayout visualizer;
	private VisualizerView mVisualizerView;
	private MediaPlayer player;
	private TextView tName;
	private ImageView imgPlay, lrotate;
	private String u = "";
	private PreferenceUtil pref;
	private ForceLoginDialog fDialog;
	LayoutInflater inflater;
	private boolean isWarned;
	private Handler handler = new Handler();
	private LinearLayout prLayout;
	private View selected = null;
	AudioManager am;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.radio_activity);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		pref = new PreferenceUtil();
		u = pref.getPreference(DataVariable.PHONE_NUMBER);
		lv = (ListView) findViewById(R.id.listView1);
		imgPlay = (ImageView) findViewById(R.id.imagePlay);
		rotateImage = (ImageView) findViewById(R.id.animation);
		lrotate = (ImageView) findViewById(R.id.logoRotate);
		tName = (TextView) findViewById(R.id.tRadioName);
		mLinearLayout = (LinearLayout) findViewById(R.id.eq_layout);
		visualizer = (LinearLayout) findViewById(R.id.visualizer);
		prLayout = (LinearLayout) findViewById(R.id.presetLayout);
		rCategories = new RadioCategories(this);
		radiocategories = rCategories.getData();
		if (radiocategories.size() > 0) {
			setup_header(radiocategories);
			categoryName = radiocategories.get(0).get("name");
			adapter = new RadioAdapter(this, radiocategories.get(0).get("id"), radiocategories.get(0).get("name"));
			dataAdapter = adapter.getData();
			if (dataAdapter.size() > 0) {
				rAdapter = new RadioListAdapter(dataAdapter);
				lv.setAdapter(rAdapter);
			}
		}
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		player = new MediaPlayer();
		setupVisualizerFxAndUI();
		setupEqualizerFxAndUI();
		setupPreset();
		mVisualizer.setEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
			                        int position, long id) {
				HashMap<String, String> mRadio = dataAdapter.get(position);
				String name = mRadio.get("title");
				String url = mRadio.get("playUrl");
				tName.setText(name);
				playMusic(url);
				GoogleAnalyticsController.hitEvent("RADIO", categoryName, name, u);
			}

		});

		if (dataAdapter.size() > 0) {
			HashMap<String, String> firstData = dataAdapter.get(0);
			String fUrl = firstData.get("playUrl");
			String n = firstData.get("title");
			tName.setText(n);
			playMusic(fUrl);
			GoogleAnalyticsController.hitEvent("RADIO", categoryName, n, u);
		}
		imgPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (player == null) {
					return;
				}
				if (player.isPlaying()) {
					player.pause();
				} else {
					player.start();
				}
				updatePausePlay();
			}

		});
		fDialog = new ForceLoginDialog();
		fDialog.setListener(new LoginListener() {

			@Override
			public void onClose() {
				finish();
			}

			@Override
			public void onLogged() {
				if (player != null) {
					player.start();
				}
			}
		});
	}

	public void startAnimation() {
		AnimationSet anim = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(600);
		rotate.setInterpolator(new DecelerateInterpolator());
		rotate.setRepeatCount(RotateAnimation.INFINITE);
		anim.addAnimation(rotate);
		rotateImage.setVisibility(View.VISIBLE);
		rotateImage.startAnimation(anim);
	}

	public void startPlayerAnimation() {
		AnimationSet anim = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(600);
		rotate.setInterpolator(new DecelerateInterpolator());
		rotate.setRepeatCount(RotateAnimation.INFINITE);
		anim.addAnimation(rotate);
		lrotate.startAnimation(anim);
	}

	private void setup_header(final ArrayList<HashMap<String, String>> data) {
		LinearLayout l1 = (LinearLayout) findViewById(R.id.radio_header);
		l1.removeAllViews();
		for (int i = 0; i < data.size(); i++) {
			final int j = i;
			int button_id = Integer.parseInt(data.get(i).get("id"));
			//final int firstId = Integer.parseInt(data.get(0).get("id")),lastId = Integer.parseInt(data.get(data.size() - 1).get("id"));
			LayoutInflater buttonInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View vi = buttonInflater.inflate(R.layout.radio_header, null);
			Button mybutton = (Button) vi.findViewById(R.id.radio_group);
			mybutton.setId(button_id);
			mybutton.setText(data.get(i).get("name"));
			mybutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String radioId = data.get(j).get("id");
					String menuName = data.get(j).get("name");
					String[] params = new String[]{radioId, menuName};
					if (gRadioList != null) {
						gRadioList.cancel(true);
					}
					gRadioList = new GetRadioList();
					gRadioList.execute(params);
					categoryName = menuName;
				}

			});
			l1.addView(vi);
		}
	}

	@SuppressLint("InflateParams")
	private class RadioListAdapter extends BaseAdapter {
		ArrayList<HashMap<String, String>> radios;

		public RadioListAdapter(ArrayList<HashMap<String, String>> c) {
			radios = c;
		}

		@Override
		public int getCount() {
			return radios.size();
		}

		@Override
		public Object getItem(int position) {
			return radios.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = convertView;
			if (convertView == null) {
				view = inflater.inflate(R.layout.list_radio_item, null);
			}
			HashMap<String, String> rmap = radios.get(position);
			TextView tRadio = (TextView) view.findViewById(R.id.textRadioName);
			TextView cRadio = (TextView) view.findViewById(R.id.textRadioCategory);
			tRadio.setText(rmap.get("title"));
			cRadio.setText(categoryName);
			return view;
		}
	}

	private class GetRadioList extends AsyncTask<String[], Void, ArrayList<HashMap<String, String>>> {
		@Override
		protected void onPreExecute() {
			startAnimation();
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String[]... arg0) {
			String id = arg0[0][0];
			String name = arg0[0][1];
			adapter = new RadioAdapter(RadioFragmentActivity.this, id, name);
			dataAdapter = adapter.getData();
			return dataAdapter;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> data) {
			rAdapter = new RadioListAdapter(data);
			lv.setAdapter(rAdapter);
			rAdapter.notifyDataSetChanged();
			rotateImage.clearAnimation();
			rotateImage.setVisibility(View.GONE);
		}
	}

	class VisualizerView extends View {
		private byte[] mBytes;
		private float[] mPoints;
		private Rect mRect = new Rect();

		private Paint mForePaint = new Paint();

		public VisualizerView(Context context) {
			super(context);
			init();
		}

		private void init() {
			mBytes = null;

			mForePaint.setStrokeWidth(1f);
			mForePaint.setAntiAlias(true);
			mForePaint.setColor(Color.rgb(0, 128, 255));
		}

		public void updateVisualizer(byte[] bytes) {
			mBytes = bytes;
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (mBytes == null) {
				return;
			}

			if (mPoints == null || mPoints.length < mBytes.length * 4) {
				mPoints = new float[mBytes.length * 4];
			}

			mRect.set(0, 0, getWidth(), getHeight());

			for (int i = 0; i < mBytes.length - 1; i++) {
				mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
				mPoints[i * 4 + 1] = mRect.height() / 2
						+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
				mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
				mPoints[i * 4 + 3] = mRect.height() / 2
						+ ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
			}

			canvas.drawLines(mPoints, mForePaint);
		}
	}

	private void setupVisualizerFxAndUI() {
		mVisualizerView = new VisualizerView(this);
		mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));
		visualizer.addView(mVisualizerView);

		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(player.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
			                                  int samplingRate) {
				mVisualizerView.updateVisualizer(bytes);
			}

			public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
			}
		}, Visualizer.getMaxCaptureRate() / 2, true, false);
	}

	@SuppressLint("InflateParams")
	private void setupEqualizerFxAndUI() {
		mEqualizer = new Equalizer(0, player.getAudioSessionId());
		mEqualizer.setEnabled(true);
		mLinearLayout.removeAllViews();
		TextView eqTextView = new TextView(this);
		eqTextView.setText("Equalizer:");
		//mLinearLayout.addView(eqTextView);

		short bands = mEqualizer.getNumberOfBands();

		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

		for (short i = 0; i < bands; i++) {
			int id = i + 1;
			final short band = i;
			View row = inflater.inflate(R.layout.equalizer_item, null);
			TextView minDbTextView = (TextView) row.findViewById(R.id.tMinDb);
			minDbTextView.setText((minEQLevel / 100) + " dB");
			TextView maxDbTextView = (TextView) row.findViewById(R.id.tMaxDb);
			maxDbTextView.setText((maxEQLevel / 100) + " dB");
			VerticalSeekBar bar = (VerticalSeekBar) row.findViewById(R.id.verticalSeekBar1);
			bar.setId(id);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(mEqualizer.getBandLevel(band));

			if (i > 0) {
				bar.setNextFocusDownId(id - 1);
			}
			if (i < bands - 1) {
				bar.setNextFocusUpId(id + 1);
			}
			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
				                              boolean fromUser) {
					mEqualizer.setBandLevel(band, (short) (progress + minEQLevel));
					// seekBar.setProgress(progress);
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
				}
			});
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			layoutParams.weight = VISUALIZER_HEIGHT_DIP;
			row.setLayoutParams(layoutParams);
			mLinearLayout.addView(row);


		}
	}

	@SuppressWarnings("deprecation")
	private void setupPreset() {
		/*
         * Setting Preset
         */
		short presets = mEqualizer.getNumberOfPresets();
		prLayout.removeAllViews();
		for (short p = 0; p < presets; p++) {
			final short preset = p;
			String presetName = mEqualizer.getPresetName(preset);
			final Button pButton = new Button(RadioFragmentActivity.this);
			pButton.setText(presetName);
			pButton.setBackgroundResource(R.drawable.selector_bg);
			pButton.setTextColor(getResources().getColor(R.color.white));
			pButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mEqualizer.usePreset(preset);
					setupEqualizerFxAndUI();
					if (selected != null) {
						((Button) selected).setTextColor(getResources().getColor(R.color.white));
					}
					((Button) v).setTextColor(getResources().getColor(R.color.Chocolate));
					selected = v;
				}

			});
			prLayout.addView(pButton);
		}
	}

	private void playMusic(String url) {
		if (url.equals("")) {
			return;
		}
		startPlayerAnimation();
		handler.removeCallbacks(checkRunnable);
		try {
			if (player != null) {
				player.reset();
				imgPlay.setImageResource(R.drawable.ic_circle_play_selector);
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setDataSource(this, Uri.parse(url));
				player.setOnPreparedListener(this);
				player.prepareAsync();
				//player.start();
			} else {
				player = new MediaPlayer();
				imgPlay.setImageResource(R.drawable.ic_circle_play_selector);
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setDataSource(this, Uri.parse(url));
				player.setOnPreparedListener(this);
				player.prepareAsync();
				//player.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

		} catch (SecurityException e) {
			e.printStackTrace();

		} catch (IllegalStateException e) {
			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		player.start();
		updatePausePlay();
		lrotate.clearAnimation();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (player != null && player.isPlaying()) {
			player.pause();
			updatePausePlay();
		}
		handler.removeCallbacks(checkRunnable);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (player != null) {
			player.release();
		}

	}

	private void updatePausePlay() {
		if (player.isPlaying()) {
			imgPlay.setImageResource(R.drawable.ic_pause_circle_selector);
		} else {
			imgPlay.setImageResource(R.drawable.ic_circle_play_selector);
		}
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
						if (!isWarned) {
							validateUser(arg0);
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<>();
				params.put("phoneNumber", phone);
				params.put("macID", macId);
				params.put("sessionValue", session);
				params.put("action", ACTION);
				params.put("signature", signature);
				return params;
			}
		};
		KyngaApp.getInstance().addToRequestQueue(request);
	}

	private void validateUser(String cred) {
		try {
			JSONObject jObject = new JSONObject(cred);
			final String status = jObject.getString("code");
			if (status.equals("00")) {
				JSONArray packages = jObject.getJSONArray("package");
				if (packages.length() > 0) {
					for (int i = 0; i < packages.length(); i++) {
						JSONObject packageItem = packages.getJSONObject(i);
						JSONObject valObj = packageItem.getJSONObject("value");
						String pName = packageItem.getString("name");
						String pResult = valObj.getString("result");
						if (pName.equals(DataVariable.PACKAGE_BASIC)) {
							final int BASIC = Integer.valueOf(pResult);
							if (BASIC < 0) {

								fDialog.show(getSupportFragmentManager(), "Login");
								stopPlayer();
								isWarned = true;
								return;
							}
						}
					}
				}

			} else if (status.equals("E102")) {
				fDialog.show(getSupportFragmentManager(), "Login");
				stopPlayer();
				isWarned = true;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stopPlayer() {
		if (player != null) {
			player.pause();

		}
	}

	private Runnable checkRunnable = new Runnable() {
		@Override
		public void run() {
			checkSession();
		}
	};
}