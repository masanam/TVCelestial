package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.LoginModel;
import id.co.kynga.app.model.LoginModelSimple;
import id.co.kynga.app.model.ResponseModel;
import me.philio.pinentry.PinEntryView;

import static id.co.kynga.app.KyngaApp.context;

public class VerificationActivity2 extends Activity {
	public static VerificationActivity2 instance;

	private ImageButton btn_back;
	private PinEntryView txt_token;
	private ImageButton btn_submit;
	private ImageButton btn_resend;
	private TextView lbl_error_verify;
	private TextView txt_displaynumber;
	private Handler handler = new Handler();//untuk timer
	//private Handler timerHandler = new Handler();
	private CountDownTimer countDownTimer;
	//int i=0;
	private int counterResend;
	private TextView txt_counter;
	private String phone_number;
	private final long startTime = 60 * 1000;
	private final long interval = 1000;
	//private long millisUntilFinished;
	private static int intSubmit;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_verification2);
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

	public static void checkSMS(final String message) {
		if (instance == null) {
			return;
		}

		//GlobalController.showToast(message, 20000);
		//final String token = message.replace(GlobalController.getString(R.string.message_sms), Config.text_blank);
		if (message.contains(GlobalController.getString(R.string.message_sms)) ) {
			final String token = message.substring(message.length() - 4);
			if(intSubmit==0) {
				instance.txt_token.setText(token);
				instance.doSubmit();
				intSubmit++;
				//GlobalController.showToast(String.valueOf(intSubmit), 20000);
			}
		}
	}


	private void setInitial() {
		intSubmit =0;
		txt_displaynumber = (TextView) findViewById(R.id.txt_displaynumber);
		Bundle bundle = getIntent().getExtras();
		phone_number = bundle.getString("PhoneNumber") != null ? bundle.getString("PhoneNumber") : "";
		txt_displaynumber.setText(phone_number);
		//GlobalController.showToast("PhoneNumber: "+phone_number, 20000);

/*
		if (getIntent().hasExtra("PhoneNumber")) {
			phone_number = getIntent().getStringExtra("PhoneNumber");
			txt_displaynumber.setText(phone_number);
		}
*/
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		txt_token = (PinEntryView) findViewById(R.id.txt_token);
		btn_submit = (ImageButton) findViewById(R.id.btn_submit);
		lbl_error_verify = (TextView) findViewById(R.id.lbl_error_verify);
		btn_resend = (ImageButton) findViewById(R.id.btn_resend);
		//Drawable btn_disable = ContextCompat.getDrawable(this, R.drawable.button_resend_disable);
		//Drawable btn_enable = ContextCompat.getDrawable(this, R.drawable.button_resend_enable);
		//btn_resend.setImageDrawable(btn_disable);

		//btn_resend.setImageDrawable(btn_enable);
		//btn_resend.setBackground(getResources().getDrawable(R.drawable.button_resend_disable));
		//btn_submit.setBackgroundResource(R.drawable.bg_submit);
		btn_resend.setBackgroundResource(R.drawable.button_resend_disable);
		btn_resend.setEnabled(false);

		//btn_resend.setImageDrawable(btn_disable);
		handler.postDelayed(runnable, 60000);
		//timerHandler.postDelayed(runnable2,1000);
		txt_counter = (TextView) findViewById(R.id.txt_counter);
		countDownTimer = new MyCountDownTimer(startTime, interval);
		txt_counter.setText(txt_counter.getText() + String.valueOf(startTime / 1000));
		countDownTimer.start();
		counterResend = 0;
		//if ()
		setEventListener();
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			btn_resend.setBackgroundResource(R.drawable.button_resend_enable);
			btn_resend.setEnabled(true);


			//btn_resend.setImageDrawable(btn_enable);
		}
	};

	private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			btn_resend.setBackgroundResource(R.drawable.button_resend_disable);
			btn_resend.setEnabled(false);

			//btn_resend.setImageDrawable(btn_enable);
		}
	};



	/*private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			//btn_resend.setEnabled(true);
			txt_counter = (TextView)  findViewById(R.id.txt_counter);
			i++;
			txt_counter.setText(Integer.toString(i));

		}
	};*/
	/*private Runnable updateTime = new Runnable() {
		public void run() {
			timeHandler.postDelayed(this, 100);
			currentTimeMillis = currentTimeMillis + 100;
			// whenever currentTimeMillis reaches the value you're waiting for.
		}*/

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				exit();
			}
		});
		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				lbl_error_verify.setVisibility(View.GONE);
				doSubmit();
			}
		});

		btn_resend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				counterResend++;
				if (counterResend <=3) {
					btn_resend.setEnabled(true);

				}
				else{
				GlobalController.showToast(" more than 3", 20000);

				}
				btn_resend.setBackgroundResource(R.drawable.button_resend_disable);
				btn_resend.setEnabled(false);
				handler.postDelayed(runnable, 60000);
				countDownTimer.start();
				//handler.postDelayed(runnable2, 5000);

				Log.w("","resend");
				//GlobalController.showToast("test", 20000);
				//login system doLogin()
				doLogin();

			}
		});

	}


	private void doLogin() {
		/*
		String phone_number = txt_displaynumber.getText().toString();
		if (!GlobalController.isNotNull(phone_number)) {
			GlobalController.showWarning(this, R.string.error_phone_number_blank);
			return;
		}*/
		//phone_number = Config.default_phone_prefix + phone_number;

		//final String phone = phone_number;
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
					setValidation2(response, phone_number);
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


	private void doSubmit() {
		final String token = txt_token.getText().toString();
		GlobalController.showLoading(this);
		URLController.verify(phone_number, token, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//GlobalController.showToast(response,20000);
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
						GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}
//set validation resend
	private void setValidation2(final String response, final String phone_number) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//txt_phone_number.setText(Config.text_blank);
			try {
				final JSONObject json = new JSONObject(response_model.Result);
				if (json.getBoolean("SMSGateway")) {
					SessionController.setPhoneNumberVer(phone_number);
					//AppController.openVerificationActivity(this, phone_number);
					//finish();
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
//set validation checksession
	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			if (GlobalController.isNotNull(response_model.Result)) {
				final LoginModel login_model = new LoginModel(response_model.Result);
				final LoginModelSimple login_model_simple = new LoginModelSimple(response_model.Result);
				//GlobalController.showToast(login_model_simple.PhoneNumber, 20000);
				if(GlobalController.isNotNull(login_model.PhoneNumber) &&
						GlobalController.isNotNull(login_model.Token)&&
						GlobalController.isNotNull(login_model.Fullname)&&
						GlobalController.isNotNull(login_model.Email)) {
					SessionController.open(login_model);
					AppController.checkSession(this, true);
					finish();
				}else if(GlobalController.isNotNull(login_model_simple.PhoneNumber)) {
					AppController.openRegisterActivity(this, login_model_simple.PhoneNumber);
					finish();
				}/*
				GlobalController.showToast("PhoneNumber : "+login_model.PhoneNumber+
						"Token : "+login_model.Token+
						"Fullname : "+login_model.Fullname+
						"Email : "+login_model.Email, 20000);
				*/
				/*
				if(GlobalController.isNotNull(login_model_simple.PhoneNumber)) {
					SessionController.open(login_model_simple);
					AppController.openRegisterActivity(instance, phone_number);
					finish();
				}
				*/
			} else {
				SessionController.close();
				//AppController.openRegisterActivity(instance, "Response Not Match");
				//AppController.openRegisterActivity(instance, phone_number);
				AppController.openRegisterActivityNew2(instance);
				finish();
			}
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			lbl_error_verify.setVisibility(View.VISIBLE);
		}
	}

//countdown
	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {
			btn_resend.setBackgroundResource(R.drawable.button_resend_enable);
			btn_resend.setEnabled(true);
			//handler.postDelayed(runnable2, 5000);
			//handler.postDelayed(runnable, 5000);

			countDownTimer.cancel();
			//countDownTimer.start();

			//btn_resend.setBackgroundResource(R.drawable.button_resend_disable);
			//txt_counter.setText("Time's up!");

		}

		@Override
		public void onTick(long millisUntilFinished) {
			txt_counter.setText("" + millisUntilFinished / 1000);
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
				.withPhoneNumber(phone_number);
		//		.withPhoneNumber("+62" + phone_number);
		Digits.authenticate(auth_config.build());
	}


	private void exit() {
		GlobalController.showQuestion(this, R.string.message_exit_confirm, new GlobalController.AlertCallback() {
			@Override
			public void didAlertButton1() {
				SessionController.close();
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}
}

