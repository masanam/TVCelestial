package id.co.kynga.app.ui.activity;

import android.app.Activity;
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
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.AccountCCModel;
import id.co.kynga.app.model.RegisterModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;


public class RegisterActivity_cc_Edit extends Activity {
	public static RegisterActivity_cc_Edit instance;

	private ImageButton btnBack, btnEdit, btnAutoRenew;
	private EditText textFullname, textCC, textMM, textYY, textCVV ;
	private ImageButton btnAdd;
	private String phone_number;
	private TextView textRespon, textTitle;
	private Spinner spinner1;
	DatePicker picker;
	private ImageButton displayDate, imgBtnDate;
	private LinearLayout linearLayoutDatePicker;
	private static Typeface tf_roboto;
	private boolean textCCKlik = false;
	private String ccNumberOri;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_register_cc_edit);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		textFullname = (EditText) findViewById(R.id.textFullname);
		textCC = (EditText) findViewById(R.id.textCC);
		textMM = (EditText) findViewById(R.id.textMM);
		textYY = (EditText) findViewById(R.id.textYY);
		textCVV = (EditText) findViewById(R.id.textCVV);
		btnEdit = (ImageButton) findViewById(R.id.btnEdit);
		btnAutoRenew = (ImageButton) findViewById(R.id.btnAutoRenew);
		setEventListener();
		textCC.addTextChangedListener(new RegisterActivity_cc.FourDigitCardFormatWatcher());
		checkAccountCC();
	}

	private void setEventListener() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});
		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doSubmitEdit();
			}
		});
		btnAutoRenew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				comingSoon();
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

	private void doBack() {
		//AppController.openLoginActivity(this);
		finish();
		this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

	private void comingSoon() {
		GlobalController.showComingSoon(this);
	}


	private void checkAccountCC() {
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		final String  phoneNumber = SessionController.getPhoneNumberVer();

		GlobalController.showLoading(this);
		URLController.accountCC(phoneNumber, new URLManager.URLCallback() {
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

	private void doSubmitEdit() {
		final String fullname = textFullname.getText().toString();
		//final String ccNumber = textCC.getText().toString();
		if (textCCKlik==true){
			ccNumberOri = textCC.getText().toString().replaceAll("-", "");
		}
		//final String ccNumber = textCC.getText().toString().replaceAll("-", "");
		final String mm = textMM.getText().toString();
		final String yy = textYY.getText().toString();
		final String cvv = textCVV.getText().toString();
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
		//if (!GlobalController.isNotNull(ccNumber)) {
			if (!GlobalController.isNotNull(ccNumberOri )) {
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
		final String  phoneNumber = SessionController.getPhoneNumberVer();

		AppController.openEditConfirmActivity(this, phoneNumber, fullname, ccNumberOri, mm, yy, cvv);
		finish();

	}

	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final AccountCCModel accountCC_model = new AccountCCModel(response_model.Result);
			//SessionController.open(register_model);
			//AppController.checkSession(this, true);
			//GlobalController.showToast(accountCC_model.Fullname, 20000);
			textFullname.setText(accountCC_model.Fullname);
			textCC.setText("**** **** **** "+
					accountCC_model.number.substring(12,16));
			//textCC.setText(accountCC_model.number);
			ccNumberOri = accountCC_model.number;
			textMM.setText(accountCC_model.expired_month);
			textYY.setText(accountCC_model.expired_year);
			textCVV.setText(accountCC_model.cwcv);
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}
}