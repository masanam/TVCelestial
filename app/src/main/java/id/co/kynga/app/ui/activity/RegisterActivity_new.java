package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import id.co.kynga.app.R;
import id.co.kynga.app.control.SpinnerCustomOnItemSelectedListener;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.RegisterModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;


public class RegisterActivity_new extends Activity {
	public static RegisterActivity_new instance;

	private ImageButton btnScan0, btnScan;
	private EditText textFullname, textAddress;
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

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_register);
		setInitial();
	}
/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
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
		Bundle bundle = getIntent().getExtras();
		phoneNumber = bundle.getString("PhoneNumber") != null ? bundle.getString("PhoneNumber") : "";
		//GlobalController.showToast("PhoneNumber: "+phoneNumber, 20000);

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
		textEmail = (EditText) findViewById(R.id.textEmail);
		textFullname = (EditText) findViewById(R.id.textFullname);
		btn_register = (ImageButton) findViewById(R.id.btn_register);
		setEventListener();
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
	}

	private void doBack() {
		AppController.openLoginActivity(this);
		finish();
	}

	private void doSubmit() {

		final String fullname = textFullname.getText().toString();
		//final String gender = String.valueOf(spinner1.getSelectedItem());
		final String gender=textGender.getText().toString();
		final String dob = textDate.getText().toString();
		final String email = textEmail.getText().toString();
		final String address = textAddress.getText().toString();
		final String macAddress = textQrcode.getText().toString();
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
		final String phoneNumber1 = phoneNumber;
		//GlobalController.showToast(phoneNumber1, 20000);
				GlobalController.showLoading(this);
		//URLController.register(phoneNumber, fullname, email, new URLManager.URLCallback() {
		URLController.register2(fullname, gender, dob,phoneNumber1, email, address, macAddress
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
			final RegisterModel register_model = new RegisterModel(response_model.Result);
			SessionController.open(register_model);
			AppController.checkSession(this, true);
			//GlobalController.showToast(register_model.Token, 20000);
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void exit() {
		GlobalController.showQuestion(this, R.string.message_exit_confirm, new GlobalController.AlertCallback() {
			@Override
			public void didAlertButton1() {
				if (LoginActivity_new.instance == null) {
					AppController.openLoginActivity(instance);
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
		spinner1.setOnItemSelectedListener(new SpinnerCustomOnItemSelectedListener());
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

}