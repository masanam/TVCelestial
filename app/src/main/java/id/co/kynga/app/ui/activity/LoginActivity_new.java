package id.co.kynga.app.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.co.kynga.app.R;

import id.co.kynga.app.control.PrefixEditText;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.util.Utils;

import static id.co.kynga.app.KyngaApp.context;

public class LoginActivity_new extends Activity {
	public static LoginActivity_new instance;
	private ImageButton btn_login;
	private static Typeface tf_roboto;
	private TextView textPhone;
	private String phone_number,countrycode;
	private CountryCodePicker ccp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_login_new);
		getcountrycode();
		setInitial();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		btn_login = (ImageButton) findViewById(R.id.btn_login);
		textPhone = (TextView) findViewById(R.id.textPhone);
		ccp = (CountryCodePicker) findViewById(R.id.ccp);
		//textPhone.addTextChangedListener(onTextChangedListener());
		setTypeFace();
		setEventListener();
		//populateData();
	}

	private void setTypeFace() {
		String fontPath_roboto = "fonts/Roboto-Regular.ttf";
		tf_roboto = Typeface.createFromAsset(getAssets(), fontPath_roboto);
		textPhone.setTypeface(tf_roboto);
		//textView1.setTextSize(20);
		//textPhone.setTextColor(Color.WHITE);
	}

	private void setEventListener() {
		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doLogin();
			}
		});
	}

	private void populateData() {
		//txt_phone_number.setPrefix(Config.default_phone_prefix);
		//txt_phone_number.setPrefixTextColor(Color.YELLOW);
	}

	private void doLogin() {
		phone_number = textPhone.getText().toString();
		if (!GlobalController.isNotNull(phone_number)) {
			GlobalController.showWarning(this, R.string.error_phone_number_blank);
			return;
		}
		if (phone_number.length()<8) {
			GlobalController.showWarning(this, "Phone number is too short");
			return;
		}
		//phone_number = Config.default_phone_prefix + phone_number;
		phone_number = ccp.getSelectedCountryCodeWithPlus() + phone_number;
		final String phone = phone_number;
		GlobalController.showLoading(this);
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
		//URLController.login(phone_number, new URLManager.URLCallback() {
		URLController.login2(IMEI,phone_number, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						setValidation(response, phone_number);
						//GlobalController.showToast(response, 20000);
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
						GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}

	private void setValidation(final String response, final String phone_number) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			textPhone.setText(Config.text_blank);
			try {
				final JSONObject json = new JSONObject(response_model.Result);
				if (json.getBoolean("SMSGateway")) {
					SessionController.setPhoneNumberVer(phone_number);
					AppController.openVerificationActivity(this, phone_number);
					finish();
				} else {
					authenticateNumber(phone_number);
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void authenticateNumber(final String phone_number) {
		final AuthCallback auth_callback = new AuthCallback() {
			@Override
			public void success(DigitsSession session, String phoneNumber) {
				Digits.clearActiveSession();
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AppController.openRegisterActivity(instance, phone_number);
						finish();
					}
				});
			}

			@Override
			public void failure(DigitsException exception) {
			}
		};
		final AuthConfig.Builder auth_config = new AuthConfig.Builder()
				.withAuthCallBack(auth_callback)
				//.withPhoneNumber("+62" + phone_number)
				.withPhoneNumber(phone_number)
				;
		Digits.authenticate(auth_config.build());
	}

	private TextWatcher onTextChangedListener() {
		return new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				if(textPhone.length()>1){
					String stringS = textPhone.getText().toString().substring(0,1);
					if(!stringS.equals("+")){
						GlobalController.showWarning(instance,"You should type +Country Code, e.g. +62");
						textPhone.setText("");
					}
				}
				//textPhone.removeTextChangedListener(this);
				//GlobalController.showToast(textPhone.getText().toString().substring(0,1),20000);
			}
		};
	}

	private void getcountrycode() {
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = URLController.getURL(R.string.url_country_code);
//        String url = DataVariable.happentvlive;
		StringRequest postRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(final String response) {
						// response
						//parseHappenMovie(response);
						cekcountrycode(response);
						Log.d("Response", response);
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				}
		) {
			/**
			 * Passing some request headers
			 */
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				return headers;
			}
		};

		queue.add(postRequest);
	}

	@SuppressLint("LongLogTag")
	private void cekcountrycode(String response) {
		if(response!=null)
		{
			try {
				JSONObject c = new JSONObject(response);
				// Getting JSON Array node
					//String fullname = "";
					String ip = c.getString("ip");
					String country_code = c.getString("country_code");
					Log.i("Resp from ip: ", ip);
					Log.i("Resp from country_code: ", country_code);
					HashMap<String, String> Result2 = new HashMap<>();
					Result2.put("ip", ip);
					Result2.put("country_code", country_code);
					ccp.setCountryForNameCode(country_code);
					Log.i("Resp from countrycode: ", country_code);
			} catch (final JSONException e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(LoginActivity_new.this);
						b.setTitle("Sorry, don't get data");
						b.setPositiveButton("OK", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dial, int arg1) {
								dial.dismiss();
								onBackPressed();
							}
						});
						b.create().show();
					}
				});

			}
		}

		else

		{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Couldn't get json from server. Check LogCat for possible errors!",
							Toast.LENGTH_LONG)
							.show();
				}
			});

		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_BACK) {
			finish();
			//Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
			this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			//finish();
			return true;//must return true, other wise menu will dissapear suddently
		}

		return super.dispatchKeyEvent(event);
	}

}