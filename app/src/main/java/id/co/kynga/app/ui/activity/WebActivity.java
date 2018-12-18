package id.co.kynga.app.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.co.kynga.app.R;

public class WebActivity extends AppCompatActivity {
	public static WebActivity instance;

	private WebView web_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		setInitial();
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
		final String url  = getIntent().getExtras().getString("URL");
		web_view = (WebView) findViewById(R.id.web_view);
		setEventListener();
		web_view.getSettings().setJavaScriptEnabled(true);
		web_view.loadUrl(url);
	}

	private void setEventListener() {
		web_view.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
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
	}
}