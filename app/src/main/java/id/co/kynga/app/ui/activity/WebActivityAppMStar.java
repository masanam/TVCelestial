package id.co.kynga.app.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import id.co.kynga.app.R;

public class WebActivityAppMStar extends CommonActivity {
	public static WebActivityAppMStar instance;

	private ImageButton btn_back;
	private TextView lbl_title;
	private ProgressBar pgb_loading;
	private WebView web_view;
	TextView textRespon;
	private ValueCallback<Uri> mUploadMessage;public ValueCallback<Uri[]> uploadMessage;
	public static final int REQUEST_SELECT_FILE = 100;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private Tracker mTracker;
	private static final String TAG = "WebActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
		setContentView(R.layout.activity_web_app_mstar);
		setInitial();
	}

	@Override
	protected void onPause() {
		this.overridePendingTransition(R.anim.hold, R.anim.slide_out_down);
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (web_view.canGoBack()) {
				web_view.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		final String title = getIntent().getExtras().getString("Title");
		final String url  = getIntent().getExtras().getString("URL");
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_title = (TextView) findViewById(R.id.lbl_title);
		lbl_title.setTypeface(null, Typeface.BOLD);
		pgb_loading = (ProgressBar) findViewById(R.id.pgb_loading);
		web_view = (WebView) findViewById(R.id.web_view);
		textRespon = (TextView) findViewById(R.id.textRespon);
		textRespon.setText(url);
		setEventListener();
		lbl_title.setText(title);
		web_view.getSettings().setJavaScriptEnabled(true);
		//Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
		web_view.loadUrl(url);
/*
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		Log.i(TAG, "Setting screen name: " + "WebView");
		mTracker.setScreenName("Screen~" + "WebView " + title);
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
*/
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				web_view.loadUrl("about:blank");
				finish();
			}
		});
		/*Old WebView Parameters
		web_view.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				pgb_loading.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				pgb_loading.setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				super.onReceivedError(view, request, error);
				if (Build.VERSION.SDK_INT >= 21) {
					web_view.loadUrl(request.getUrl().toString());
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				web_view.loadUrl(failingUrl);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		*/
		//Additional parameter======================================================================
		//Agar kalau ada sisi video dimainkan bisa dikill
		web_view.loadUrl("about:blank");
		web_view.setWebViewClient(new myWebClient());
		//web_view.getSettings().setJavaScriptEnabled(true);
		// httpWebView.loadUrl("http://www.google.com"); //ini menyebabkan disuspend???
		//httpWebView.getSettings().setBuiltInZoomControls(true);
		web_view.getSettings().setUseWideViewPort(true);
		web_view.getSettings().setLoadWithOverviewMode(true);
		web_view.setWebChromeClient(new WebChromeClient());
		// setWebViewClient ini bikin progressbar nggak pernah muncul....
/*        httpWebView.setWebViewClient(
                new SSLTolerentWebViewClient()
        );
*/
		web_view.getSettings().setDomStorageEnabled(true);


		//Agar bisa upload photo ke facebook== video nggak boleh dari m.facebook.com
		//httpWebView.getSettings().setSupportZoom(false); --> bikin zoom gak muncul
		web_view.getSettings().setAllowFileAccess(true);
		web_view.getSettings().setAllowFileAccess(true);
		web_view.getSettings().setAllowContentAccess(true);

		web_view.setWebChromeClient(new WebChromeClient() {
			// For 3.0+ Devices (Start)
			// onActivityResult attached before constructor
			protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				// i.setType("video/*");
				//i.setType("*/*"); //Facebook tidak membolehkan upload video dari web mobile
				startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
			}


			// For Lollipop 5.0+ Devices
			public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
				if (uploadMessage != null) {
					uploadMessage.onReceiveValue(null);
					uploadMessage = null;
				}

				uploadMessage = filePathCallback;

				Intent intent = fileChooserParams.createIntent();
				// intent.setType("*/*");
				try {
					startActivityForResult(intent, REQUEST_SELECT_FILE);
				} catch (ActivityNotFoundException e) {
					uploadMessage = null;
					Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
					return false;
				}
				return true;
			}

			//For Android 4.1 only
			protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				mUploadMessage = uploadMsg;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				// i.setType("video/*");
				//  intent.setType("*/*"); //biar keluar file photo dan video
				startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
			}

			protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mUploadMessage = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				// i.setType("image/*");
				// i.setType("video/*");
				i.setType("*/*"); //biar keluar file photo dan video
				startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
			}

		});
	}
	//Additional parameters, taken from Wartaku which can play games correctly
	//flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public class myWebClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
			//super.onReceivedSslError(view, handler, error);

			android.app.AlertDialog.Builder  builder = new android.app.AlertDialog.Builder(WebActivityAppMStar.this);
			builder.setTitle("Invalid/Untrusted certificate");
			builder.setMessage("You're accessing a page with an untrusted or invalid certificate. Do you want to continue?");
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.cancel();
					Toast.makeText(WebActivityAppMStar.this, "Request cancelled", Toast.LENGTH_LONG).show();
				}
			});

			builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					handler.proceed();
				}
			});
			android.app.AlertDialog alert11 = builder.create();
			alert11.show();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			//progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			//progressBar.setVisibility(View.VISIBLE);
			// view.loadUrl(url);
			//  return true;

			//Dintambahkan ini agar bisa share whatsapp
			if(url != null && url.startsWith("whatsapp://"))
			{
				view.getContext().startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
				return true;

			}else
			{
				view.loadUrl(url);
				return true;
			}

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			//progressBar.setVisibility(View.GONE);
			pgb_loading.setVisibility(View.GONE);
			super.onPageFinished(view, url);
			//Ini untuk mengatasi kalau page blank setelah klik google link
			System.out.println("onPageFinished: " + url);
			if ("about:blank".equals(url) && view.getTag() != null) {
				view.loadUrl(view.getTag().toString());
			} else {
				view.setTag(url);
			}

		}
	}

}