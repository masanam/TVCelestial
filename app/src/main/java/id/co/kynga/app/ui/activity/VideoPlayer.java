package id.co.kynga.app.ui.activity;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.ForceLoginDialog;
import id.co.kynga.app.util.ForceLoginDialog.LoginListener;
import id.co.kynga.app.util.PreferenceUtil;
import id.co.kynga.app.util.ResumeDialog;
import id.co.kynga.app.util.Utils;
import id.co.kynga.app.widget.NoelPlayer;
import id.co.kynga.app.widget.NoelPlayer.NoelPlayerCallback;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.exoplayer.AspectRatioFrameLayout;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.chunk.Format;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.keycode;
import static id.co.kynga.app.KyngaApp.context;

public class VideoPlayer extends FragmentActivity implements NoelPlayerCallback, LoginListener {
	public static final String CONTENT_TYPE_EXTRA = "content_type";
	public static final int TYPE_DASH = 0;
	public static final int TYPE_SS = 1;
	public static final int TYPE_HLS = 2;
	public static final int TYPE_OTHER = 3;
	private NoelPlayer player;
	private AspectRatioFrameLayout videoFrame;
	private FrameLayout mainFrame;
	private String crid;
	private String title;
	private String url;
	private int contentType;
	private ProgressBar pb;
	private static final int MODE_STANDARD = 1;
	private static final int MODE_FULL_WITH_ASPECT_RATIO = 2;
	private static final int MODE_FULLSCREEN = 3;
	private int currentMode = 1;
	private TextView textBuffer;
	private Uri contentUri;
	private PreferenceUtil pref;
	private Handler handler = new Handler();
	private boolean isWarned;
	private ForceLoginDialog fDialog;
	private String asalClick;
	private ImageButton image_play, image_trailer;

	private SharedPreferences sharedpreferences;
	public static final String mypreference = "mypref";
	public static final String currentPosition= "currentPosition";
	private String cpPosition;
	private Handler handlerPlayControl = new Handler();
	private Long longStartPlay;
	private boolean isPlaying;

	private String link;
	public static VideoPlayer instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_player);
		//To accomodate resume player
		instance = this;
		Intent intent0 = getIntent();
		link = intent0.getStringExtra(DataVariable.KEY_URL);
		//shared prefference to collect current player position
		sharedpreferences = getSharedPreferences(mypreference,
				Context.MODE_PRIVATE);

		if (sharedpreferences.contains(link)) {
			cpPosition = sharedpreferences.getString(link, "");
			longStartPlay = Long.valueOf(cpPosition);
			//GlobalController.showToast(cpPosition, 20000);
		}else
		{
			cpPosition ="0";
			longStartPlay = Long.valueOf(cpPosition);
		}
		// Played 5 seconds before
		if (Long.valueOf(cpPosition) > 5000){
			longStartPlay = Long.valueOf(cpPosition)-5000;
		}

		//Toast.makeText(context.getApplicationContext(), "test", Toast.LENGTH_LONG).show();
		//setContentView(R.layout.video_player_with_resume);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		pref = new PreferenceUtil();
		isWarned = false;
		player = (NoelPlayer) findViewById(R.id.noelPlayer1);
		videoFrame = (AspectRatioFrameLayout) findViewById(R.id.videoFrame);
		mainFrame = (FrameLayout) findViewById(R.id.root);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		textBuffer = (TextView) findViewById(R.id.textDebug);
		image_trailer = (ImageButton) findViewById(R.id.image_trailer);
		Intent intent = getIntent();
		crid = intent.getStringExtra(DataVariable.CRID);
		title = intent.getStringExtra(DataVariable.KEY_TITLE);
		url = intent.getStringExtra(DataVariable.KEY_URL);
		contentType = intent.getIntExtra(CONTENT_TYPE_EXTRA, 2);
		asalClick = intent.getStringExtra("asalClick");
		player.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (player.isControllerShowing()) {
					player.hideController();
				} else {
					player.showController();
				}
			}
		});

		fDialog = new ForceLoginDialog();
		fDialog.setListener(this);
		loginAuth();
		pb.setVisibility(View.GONE);
		isPlaying = false;
		//simpleExoPlayerView.setUseController(false);
		videoFrame.setVisibility(View.GONE);
		playVideoAwal();

		//longStartPlay = Long.valueOf(0);
		//simpleExoPlayerView.setUseController(false);
		//simpleExoPlayerView.setVisibility(View.GONE);
	}

	public void showDialog(){
		ResumeDialog resumeDialog = new ResumeDialog(this);
		resumeDialog.setListener(new ResumeDialog.ResumeListener() {

			@Override
			public void onRestart() {
				longStartPlay = Long.valueOf(0);
				isPlaying =true;
				pb.setVisibility(View.VISIBLE);
				videoFrame.setVisibility(View.VISIBLE);
				isPlaying=true;
				if (asalClick.equals("play")) {
					playVideo(url, title, contentType);
				}
				/*
				if (asalClick.equals("trailer")) {
					playVideo(crid, title, contentType);
				}
				*/
				pb.setVisibility(View.GONE);
			}

			@Override
			public void onResume() {
				isPlaying =true;
				pb.setVisibility(View.VISIBLE);
				videoFrame.setVisibility(View.VISIBLE);
				isPlaying=true;
				if (asalClick.equals("play")) {
					playVideo(url, title, contentType);
				}
				/*
				if (asalClick.equals("trailer")) {
					playVideo(crid, title, contentType);
				}
				*/
				pb.setVisibility(View.GONE);
			}
		});
		resumeDialog.show();
		//errorShown = true;
	}

	public void playVideoAwal(){
		//Toast.makeText(getApplicationContext(), longStartPlay.toString(), Toast.LENGTH_LONG).show();
		if (asalClick.equals("play")) {
			if (longStartPlay == 0){
				pb.setVisibility(View.VISIBLE);
				videoFrame.setVisibility(View.VISIBLE);
				isPlaying=true;
				//player.start();
				playVideo(url, title, contentType);
				pb.setVisibility(View.GONE);
				//Toast.makeText(getApplicationContext(), String.valueOf(isPlaying), Toast.LENGTH_LONG).show();
			}
			else {
				showDialog();
			}
		}
		if (asalClick.equals("trailer")) {
				longStartPlay = Long.valueOf(0);
				pb.setVisibility(View.VISIBLE);
				videoFrame.setVisibility(View.VISIBLE);
				isPlaying=true;
				//player.start();
				playVideo(crid, title, contentType);
				pb.setVisibility(View.GONE);
			/*
			else {
				showDialog();
			}
			*/
		}

		image_trailer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				player.pause();

			}

		});
	}

	public void alertDialog() {
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VideoPlayer.this);
		//builder.setTitle(String.valueOf(keyCode));
		builder.setMessage("RESUME or RESTART?");
		builder.setNegativeButton("RESUME", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				player.start();
			}
		});

		builder.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				playVideoAwal();
			}
		});
		android.app.AlertDialog alert11 = builder.create();
		alert11.show();
	}

	private void playVideo(String src, String mTitle, int type) {
		if (player.isInitialized()) {
			player.clearSurface();
			player.release();
		}
		switch (type) {
			case TYPE_HLS:
				contentUri = Uri.parse(src);
				break;
			case TYPE_OTHER:
				contentUri = Uri.parse(src);
				break;
			default:
				contentUri = Uri.parse("");
		}
		player.setContentUri(this, contentUri);
		player.setType(TYPE_HLS);
		player.enableControler();
		//player.setControlAnchorView(mainFrame);
		player.setPlayerCallback(this);
		player.startPlayback();
		player.seekTo(longStartPlay);
		player.enableControler();
		player.setControlAnchorView(mainFrame);
	}

	@Override
	public void onError(Exception e) {
		Toast.makeText(this, "Player Error", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDownstreamFormatChanged(int sourceId, Format format,
										  int trigger, long mediaTimeMs) {
	}

	@Override
	public void onVideoSizeChanged(int width, int height,
								   int unappliedRotationDegrees, float pixelWidthHeightRatio) {
	}

	@Override
	public void onStateChanged(boolean playWhenReady, int playbackState) {
		String text = "";
		switch (playbackState) {
			case ExoPlayer.STATE_BUFFERING:
				//text += "Let the show begin..." + player.getBufferPercentage() / 100 + " %";
				text += "Let the show begin...";
				pb.setVisibility(View.VISIBLE);

				break;
			case ExoPlayer.STATE_ENDED:
				text += "ended";
				pb.setVisibility(View.GONE);
				finish();
				break;
			case ExoPlayer.STATE_IDLE:
				text += "idle";
				pb.setVisibility(View.GONE);

				break;
			case ExoPlayer.STATE_PREPARING:
				text += "Let the show begin...";
				pb.setVisibility(View.VISIBLE);


				break;
			case ExoPlayer.STATE_READY:
				pb.setVisibility(View.GONE);
				textBuffer.setVisibility(View.GONE);

				text += "ready";
				break;
			default:
				text += "";
				break;
		}
		textBuffer.setText(text);
	}

	@Override
	public void onBitrateChange(String variantUrl, boolean isAuto) {

	}

	@Override
	public void switchFullScreen(int w, int h, float aspect) {
		toggleFullScreen(w, h, aspect);
	}

	private void toggleFullScreen(int width, int height, float a) {
		DisplayMetrics metrics;
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		switch (currentMode) {
			case MODE_STANDARD:
				videoFrame.setAspectRatio((screenWidth * a) / screenHeight);
				currentMode = MODE_FULL_WITH_ASPECT_RATIO;
				break;
			case MODE_FULL_WITH_ASPECT_RATIO:
				videoFrame.setAspectRatio(0);
				currentMode = MODE_FULLSCREEN;
				break;
			case MODE_FULLSCREEN:
				videoFrame.setAspectRatio((width * a) / height);
				currentMode = MODE_STANDARD;
				break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		player.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		/*
		//alertDialog();
		//player.start();
		if (longStartPlay == 0){
			//GlobalController.showLoading(instance);
			//simpleExoPlayerView.setUseController(true);
			//simpleExoPlayerView.setVisibility(View.VISIBLE);
			//initializePlayer();
			isPlaying=true;
			//handlerPlayControl.postDelayed(runnable1, 2000);
			player.start();
		}
		else {
			showDialog();
		}
		*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		player.release();
	}

	private void loginAuth() {
		handler.postDelayed(authRunnable, 20000);
	}

	private Runnable authRunnable = new Runnable() {

		@Override
		public void run() {
			checkSession();
		}

	};

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
						Log.d("Validation", arg0);
						if (!isWarned) {
							validateUser(arg0);
						}
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
				params.put("macID", macId);
				params.put("sessionValue", session);
				params.put("action", ACTION);
				params.put("signature", signature);
				return params;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(
				0,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		KyngaApp.getInstance().addToRequestQueue(request);
	}

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
						}

					}
				}
				if (BASIC < 0) {
					fDialog.show(getSupportFragmentManager(), "Login");
					isWarned = true;
					stopPlayer();
				}
			} else if (status.equals("E102")) {
				fDialog.show(getSupportFragmentManager(), "Login");
				isWarned = true;
				stopPlayer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		finish();
	}

	@Override
	public void onLogged() {
		player.start();
		isWarned = false;
	}

	private void stopPlayer() {
		player.pause();
	}

	@Override
	public void onShow(View v) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == 4 && isPlaying == true) {
			String n = String.valueOf(player.getCurrentPosition());
			SharedPreferences.Editor editor = sharedpreferences.edit();
			//editor.putString(currentPosition, n);
			editor.putString(link, n);
			editor.commit();
			//Toast.makeText(context.getApplicationContext(), n, Toast.LENGTH_LONG).show();
			finish();
		}
		if(keyCode == 4 && isPlaying == false && !sharedpreferences.contains(link)){
			String n = "0";
			SharedPreferences.Editor editor = sharedpreferences.edit();
			//editor.putString(currentPosition, n);
			editor.putString(link, n);
			editor.commit();
			longStartPlay = Long.valueOf(0);
			//Toast.makeText(context.getApplicationContext(), n, Toast.LENGTH_LONG).show();
			//initializePlayer();
			finish();
		}
		//GlobalController.showToast("test", 20000);
		//return false; // bisa false bisa true

		return super.onKeyDown(keyCode, event);
	}

	// To enable OK remote button
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER ) {
			player.showController();
			/*
			//Toast.makeText(context.getApplicationContext(), "Test tombol OK", Toast.LENGTH_LONG).show();
			if (player.isControllerShowing()) {
				player.hideController();
			} else {
				player.showController();
			}
			*/
			//return false;//must return true, other wise menu will dissapear suddently
		}

		return super.dispatchKeyEvent(event);
	}



}
