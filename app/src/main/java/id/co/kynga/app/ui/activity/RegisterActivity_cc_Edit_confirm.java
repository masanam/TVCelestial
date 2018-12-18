package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.AccountCCModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;


public class RegisterActivity_cc_Edit_confirm extends Activity {
	public static RegisterActivity_cc_Edit_confirm instance;

	private ImageButton btnBack, btnCancel, btnSave, btnDelete;
	private EditText textFullname, textCC, textMM, textYY, textCVV ;
	private ImageButton btnAdd;
	private String phone_number;
	private TextView textRespon, textTitle;
	private Spinner spinner1;
	DatePicker picker;
	private ImageButton displayDate, imgBtnDate;
	private LinearLayout linearLayoutDatePicker;
	private static Typeface tf_roboto;

	private String fullName, phoneNumber, ccNumber, ccNumberOri, mm, yy, cvv;
	private boolean textCCKlik = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_register_cc_edit_confirm);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		Bundle bundle = getIntent().getExtras();
		fullName = bundle.getString("fullName") != null ? bundle.getString("fullName") : "";
		ccNumber = bundle.getString("ccNumber") != null ? bundle.getString("ccNumber") : "";
		ccNumberOri = ccNumber.replaceAll("-", "");
		mm = bundle.getString("mm") != null ? bundle.getString("mm") : "";
		yy = bundle.getString("yy") != null ? bundle.getString("yy") : "";
		cvv = bundle.getString("cvv") != null ? bundle.getString("cvv") : "";
		phoneNumber = bundle.getString("phoneNumber") != null ? bundle.getString("phoneNumber") : "";
/*
		fullName = intent.getStringExtra("fullName");
		phoneNumber = intent.getStringExtra("phoneNumber");
		ccNumber = intent.getStringExtra("ccNumber");
		mm = intent.getStringExtra("mm");
		yy = intent.getStringExtra("yy");
		cvv = intent.getStringExtra("cvv");
*/
		//GlobalController.showToast(fullName, 20000);

		btnBack = (ImageButton) findViewById(R.id.btnBack);
		textFullname = (EditText) findViewById(R.id.textFullname);
		textCC = (EditText) findViewById(R.id.textCC);
		textMM = (EditText) findViewById(R.id.textMM);
		textYY = (EditText) findViewById(R.id.textYY);
		textCVV = (EditText) findViewById(R.id.textCVV);
		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		btnSave = (ImageButton) findViewById(R.id.btnSave);
		btnDelete = (ImageButton) findViewById(R.id.btnDelete);

		textFullname.setText(fullName);
		//textCC.setText(ccNumber);
		textCC.setText("**** **** **** "+
				ccNumberOri.substring(12,16));
		textMM.setText(mm);
		textYY.setText(yy);
		textCVV.setText(cvv);
		setEventListener();
		textCC.addTextChangedListener(new RegisterActivity_cc.FourDigitCardFormatWatcher());
	}

	private void setEventListener() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSubmitEdit();
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doComingSoon();
			}
		});
		textCC.requestFocus();
		textCC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				textCC.requestFocus();
				textCC.getText().clear();
				textCCKlik  =true;
			}
		});

	}
	private void doComingSoon() {
		GlobalController.showComingSoon(this);
	}

	private void doBack() {
		//AppController.openLoginActivity(this);
		finish();
		this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

	private void doSubmitEdit() {
		final String fullname2 = textFullname.getText().toString();
		//final String ccNumber = textCC.getText().toString();
		//final String ccNumber2 = textCC.getText().toString().replaceAll("-", "");
		if (textCCKlik==true){
			ccNumberOri = textCC.getText().toString().replaceAll("-", "");
		}
		final String mm2 = textMM.getText().toString();
		final String yy2 = textYY.getText().toString();
		final String cvv2 = textCVV.getText().toString();
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
		if (!GlobalController.isNotNull(fullName)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textFullname.setError("mandatory");
			return;
		}

		if (!GlobalController.isNotNull(ccNumber)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textCC.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(mm)) {
			//GlobalController.showWarning(this, R.string.error_cc_exp_month_blank);
			textMM.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(yy)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textMM.setError("mandatory");
			return;
		}
		if (!GlobalController.isNotNull(cvv)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			textCVV.setError("mandatory");
			return;
		}
		//TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		//String IMEI = telephonyManager.getDeviceId();
		//final String imei = telephonyManager.getDeviceId();
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		//GlobalController.showToast(ccNumberOri, 20000);
		GlobalController.showLoading(this);
		URLController.editAccountCC(phoneNumber, fullname2, ccNumberOri, mm2,yy2, cvv2, new URLManager.URLCallback() {
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
						if(response.contains("successfully")){
							GlobalController.showSuccessMessage(instance);
						}else{
							GlobalController.showWarning(instance,"FAIL");
						}
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}
}