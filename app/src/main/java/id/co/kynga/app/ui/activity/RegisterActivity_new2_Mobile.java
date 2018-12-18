package id.co.kynga.app.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.co.kynga.app.R;
import id.co.kynga.app.control.SpinnerCustomOnItemSelectedListener2;
import id.co.kynga.app.control.SpinnerCustomOnItemSelectedListener3;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.RegisterModel2;
import id.co.kynga.app.model.ResponseModel;


public class RegisterActivity_new2_Mobile extends Activity {
	public static RegisterActivity_new2_Mobile instance;

	private ImageButton btnScan0, btnScan;
	private EditText textFullname, textAddress, textPhone;
	public static EditText textGender, textDate, textQrcode;
	private EditText textEmail;
	private ImageButton btn_register;
	private String phoneNumber;
	private TextView textRespon, textTitle;
	private Spinner spinner1;
	DatePicker picker;
	private ImageButton displayDate, imgBtnDate;
	private LinearLayout linearLayoutDatePicker;
	private static Typeface tf_roboto;
	private Button btnSignIn;
	private String phoneNumber1;
	private CountryCodePicker ccp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_register_new2_mobile);
		getcountrycode();
		setInitial();
	}
/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//exit();
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return true;
	}
*/
	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		/*
		Bundle bundle = getIntent().getExtras();
		phoneNumber = bundle.getString("PhoneNumber") != null ? bundle.getString("PhoneNumber") : "";
		GlobalController.showToast("PhoneNumber: "+phoneNumber, 20000);
*/
		addListenerOnSpinnerItemSelection();//Spinner
		picker=(DatePicker)findViewById(R.id.datePicker1);
		imgBtnDate=(ImageButton)findViewById(R.id.imgBtnDate);
		displayDate=(ImageButton)findViewById(R.id.btnChangeDate);
		linearLayoutDatePicker = (LinearLayout) findViewById(R.id.linearLayoutDatePicker);
		textDate = (EditText) findViewById(R.id.textDate);
		linearLayoutDatePicker.setVisibility(View.GONE);
		textGender = (EditText) findViewById(R.id.textGender);
		textEmail = (EditText) findViewById(R.id.textEmail);
		textAddress = (EditText) findViewById(R.id.textAddress);
		textQrcode = (EditText) findViewById(R.id.textQrcode);
		textTitle = (TextView) findViewById(R.id.textTitle);
		btnScan0=(ImageButton)findViewById(R.id.btnScan0);
		btnScan=(ImageButton)findViewById(R.id.btnScan);

		displayDate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				//textview1.setText(getCurrentDate());
				linearLayoutDatePicker.setVisibility(View.GONE);
				textDate.setError(null);
				textDate.setText(getCurrentDate());
			}
		});

		btnScan0.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				scanToolbar();
			}
		});

		btnScan.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				scanToolbar();
			}
		});

		imgBtnDate.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				linearLayoutDatePicker.setVisibility(View.VISIBLE);
			}
		});

		//phone_number = getIntent().getStringExtra("PhoneNumber");
		//btn_back = (ImageButton) findViewById(R.id.btn_back);
		/*

		txt_email = (EditText) findViewById(R.id.txt_email);
		btn_register = (ImageButton) findViewById(R.id.btn_register);
		textRespon = (TextView) findViewById(R.id.textRespon);
		setEventListener();
		*/
		textPhone = (EditText) findViewById(R.id.textPhone);
		textEmail = (EditText) findViewById(R.id.textEmail);
		textFullname = (EditText) findViewById(R.id.textFullname);
		btn_register = (ImageButton) findViewById(R.id.btn_register);

		btnSignIn= (Button) findViewById(R.id.btnSignIn);
		btnSignIn.setPaintFlags(btnSignIn.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
		ccp = (CountryCodePicker) findViewById(R.id.ccp);
		setEventListener();
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
						android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(RegisterActivity_new2_Mobile.this);
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

	public String getCurrentDate(){
		StringBuilder builder=new StringBuilder();
		//builder.append("Current Date: ");
		builder.append(picker.getYear()+"-");
		builder.append((picker.getMonth() + 1)+"-");//month is 0 based
		builder.append(picker.getDayOfMonth());

		return builder.toString();
	}

	private void setEventListener() {
		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSubmit();
			}
		});
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openLoginActivity(instance);
			}
		});

		//textPhone.addTextChangedListener(onTextChangedListener());
	}

	private void doBack() {
		AppController.openLoginActivity(this);
		finish();
	}

	private void doSubmit() {
		final String fullname = textFullname.getText().toString();
		final String phoneNumber = ccp.getSelectedCountryCodeWithPlus() + textPhone.getText().toString();
		//final String gender = String.valueOf(spinner1.getSelectedItem());
		final String gender=textGender.getText().toString();
		final String dob = textDate.getText().toString();
		final String email = textEmail.getText().toString();
		final String address = textAddress.getText().toString();
		//final String macAddress = textQrcode.getText().toString();
		final String macAddress = "Mobile";
/*
		if (!GlobalController.isNotNull(fullname)) {
			GlobalController.showWarning(this, R.string.error_fullname_blank);
			return;
		} else if (!GlobalController.isEmail(email)) {
			GlobalController.showWarning(this, R.string.error_email);
			return;
		} else if (!GlobalController.isNotNull(gender)) {
			GlobalController.showWarning(this, R.string.error_gender);
			return;
		}
*/
		if (!GlobalController.isNotNull(fullname)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textFullname.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(phoneNumber)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textPhone.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(gender)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textGender.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(dob)) {
			//GlobalController.showWarning(this, R.string.error_cc_exp_month_blank);
			textDate.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(email)) {
			//GlobalController.showWarning(this, R.string.error_cc_exp_year_blank);
			textEmail.setError("mandatory");
			return;
		}
		if (!GlobalController.isEmail(email)) {
			//GlobalController.showWarning(this, R.string.error_email);
			textEmail.setError("not valid");
			return;
		}
		if (!GlobalController.isNotNull(address)) {
			//GlobalController.showWarning(this, R.string.error_cc_exp_year_blank);
			textAddress.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(macAddress)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textQrcode.setError("mandatory");
			return;
		}
		/*
		final String fullname = "Muqorobin";
		final String email = "muqorobin2@yahoo.com";
		final String gender = "Male";
		final String dob = "1980-1-1";
		final String phoneNumber = "+628118003935";
		final String address = "Pondok Bambu";
		final String macAddress = "abcdefghijklmn";
		*/
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		//String IMEI = telephonyManager.getDeviceId();
		final String imei = telephonyManager.getDeviceId();
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		phoneNumber1 = phoneNumber;

				GlobalController.showLoading(this);
		//URLController.register(phoneNumber, fullname, email, new URLManager.URLCallback() {
		URLController.registerNew(fullname, gender, dob,phoneNumber1, email, address, macAddress
				,imei , new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//GlobalController.showToast(response, 20000);
						setValidation(response);
						//GlobalController.showToast(response, 20000);
						//textRespon.setText(response);
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

	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final RegisterModel2 register_model2 = new RegisterModel2(response_model.Result);
			//SessionController.open(register_model2,phoneNumber);
			//AppController.checkSession(this, true);
			//GlobalController.showToast(register_model.Token, 20000);
			AppController.openLoginActivity_New_2(instance,register_model2.Token,phoneNumber1 );
			//AppController.openVerificationActivityNew2(instance,register_model2 );
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void exit() {
		GlobalController.showQuestion(this, R.string.message_exit_confirm, new GlobalController.AlertCallback() {
			@Override
			public void didAlertButton1() {
				if (LoginActivity_new.instance == null) {
					AppController.openFirstActivity0(instance);
				}
				finish();
			}

			@Override
			public void didAlertButton2() {
			}
		});
	}


	public void addListenerOnSpinnerItemSelection(){
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setOnItemSelectedListener(new SpinnerCustomOnItemSelectedListener3());
	}

	public static void checkSpinner(final String message) {
		if (instance == null) {
			return;
		}
		textGender.setError(null);
		textGender.setText(message);
	}

	private void setTypeFace() {
		String fontPath_roboto = "fonts/Roboto-Regular.ttf";
		tf_roboto = Typeface.createFromAsset(getAssets(), fontPath_roboto);
		textTitle.setTypeface(tf_roboto);
		//textView1.setTextSize(20);
		//textPhone.setTextColor(Color.WHITE);
	}
/*
	public void scanToolbar(View view) {
		new IntentIntegrator(this).setCaptureActivity(QRcode_ToolbarCaptureActivity.class).initiateScan();
	}
	*/
	private void scanToolbar() {
		new IntentIntegrator(this).setCaptureActivity(QRcode_ToolbarCaptureActivity.class).initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if(result != null) {
			if(result.getContents() == null) {
				//Log.d("MainActivity", "Cancelled scan");
				Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
			} else {
				//Log.d("MainActivity", "Scanned");
				//Toast.makeText(this, "OOOOOOO Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
				String qrCodeNoPercent = result.getContents().replace("%","");
				textQrcode.setText(qrCodeNoPercent);
			}
		} else {
			// This is important, otherwise the result will not be passed to the fragment
			super.onActivityResult(requestCode, resultCode, data);
		}
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

}