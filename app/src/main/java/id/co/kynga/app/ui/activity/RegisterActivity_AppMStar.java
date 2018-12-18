package id.co.kynga.app.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.RegisterModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.VersionModel;

public class RegisterActivity_AppMStar extends CommonActivity {
	public static RegisterActivity_AppMStar instance;

	private ImageButton btn_back;
	private EditText txt_fullname;
	private EditText txt_email;
	private ImageButton btn_register;
	private String phone_number;
	private TextView textRespon;

	private SharedPreferences sharedpreferences;
	public static final String mypreference = "mypref";
	private String appVersion, currentVersion;

	private VersionModel version_model;
	private static int versionNumber;
	private CheckBox checkBox;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_register_app_mstar);
		setInitial();
	}

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

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		phone_number = getIntent().getStringExtra("PhoneNumber");
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		txt_fullname = (EditText) findViewById(R.id.txt_fullname);
		txt_email = (EditText) findViewById(R.id.txt_email);
		btn_register = (ImageButton) findViewById(R.id.btn_register);
		textRespon = (TextView) findViewById(R.id.textRespon);
		setEventListener();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});
		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
						if(b)
						{
							checkBox.setTag("Y");
							doSubmit();
						}
						if(checkBox.isChecked()==false)
						{
							checkBox.setTag("N");
							Toast.makeText(RegisterActivity_AppMStar.this, "Please Agreed Terms of Use", Toast.LENGTH_LONG).show();
						}
					}
				});

			}
		});
	}

	private void doBack() {
		AppController.openLoginActivityAppMStar(this);
		finish();
	}

	private void doSubmit() {
		final String fullname = txt_fullname.getText().toString();
		final String email = txt_email.getText().toString();
		if (!GlobalController.isNotNull(fullname)) {
			GlobalController.showWarning(this, R.string.error_fullname_blank);
			return;
		} else if (!GlobalController.isEmail(email)) {
			GlobalController.showWarning(this, R.string.error_email);
			return;
		}
		GlobalController.showLoading(this);
		URLController.registerAppMStar(phone_number, fullname, email, new URLManager.URLCallback() {
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

	private void setValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final RegisterModel register_model = new RegisterModel(response_model.Result);

			//checkVersionApp();
			SessionControllerAppMStar.open(register_model);
			AppController.checkSessionAppMStar(this, true);
		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showWarning(this, response_model.Message);
		}
	}

	private void exit() {
		GlobalController.showQuestion(this, R.string.message_exit_confirm, new GlobalController.AlertCallback() {
			@Override
			public void didAlertButton1() {
				if (LoginActivity.instance == null) {
					AppController.openLoginActivityAppMStar(instance);
				}
				finish();
			}

			@Override
			public void didAlertButton2() {
			}
		});
	}

	private void checkVersionApp() {
		//TelephonyManager telephonyManager = (TelephonyManager) MStarApp.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
		//String IMEI = telephonyManager.getDeviceId();
		GlobalController.showLoading(this);
		URLController.checkVersionAppMStar(new URLManager.URLCallback() {
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
						//setValidationVersion(response);
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

	private void setValidationVersion(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			version_model = new VersionModel(response_model.Result);
			versionNumber = Integer.parseInt(version_model.list.get(0).VersionNumber);
			SharedPreferences.Editor editor_app_mstar = sharedpreferences.edit();
			//editor.putString(currentPosition, n);
			editor_app_mstar.putString(appVersion, String.valueOf(versionNumber));
			editor_app_mstar.commit();
		}
	}
}