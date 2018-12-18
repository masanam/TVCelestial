package id.co.kynga.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ResponseModel;

public class PolicyActivityAppMStar extends CommonActivity {
	public static PolicyActivityAppMStar instance;

	private ImageButton btn_back;
	private WebView web_view;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_policy);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		web_view = (WebView) findViewById(R.id.web_view);
		setEventListener();
		web_view.getSettings().setJavaScriptEnabled(true);
		web_view.setBackgroundColor(0x00000000);
		web_view.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void populateData() {
		GlobalController.showLoading(this);
		URLController.faqAppMStar(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						setValidation(response);
					}
				});
			}

			@Override
			public void didURLFailed() {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
					}
				});
			}
		});
	}

	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			try {
				final JSONObject json = new JSONObject(response_model.Result);
				web_view.loadDataWithBaseURL("", json.getString("Text"), "text/html", "UTF-8", "");
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
		}
	}
}