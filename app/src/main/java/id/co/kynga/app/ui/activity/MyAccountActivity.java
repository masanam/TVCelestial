package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BubblePackageCategoryModel;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.model.ResponseModel;

public class MyAccountActivity extends Activity {
	public static MyAccountActivity instance;
	private ImageButton btnMyAccount, btnPurchase, btnRegisterCc, btnTopup, btnHistory, btnRegBox;
	private TextView textFullname, textHandphone, textEmail, textAddress, textBalance, textPackage;
	private String fullName, handPhone, email, address, macAddress;
	private long balance, longPackage;
	private BubblePackageModel game_model;
	private BubblePackageCategoryModel game_category_model;
	private TextView textRespon;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//this.overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_my_account);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		setInitial();
		super.onResume();
	}


	private void setInitial() {
		Intent intent0 = getIntent();
		fullName = intent0.getStringExtra("fullName");
		handPhone = intent0.getStringExtra("phoneNumber");
		email = intent0.getStringExtra("email");
		address = intent0.getStringExtra("adress");
		macAddress = intent0.getStringExtra("MacAddress");
		balance = intent0.getLongExtra("balance",0);
		longPackage = intent0.getLongExtra("package",0);

		//GlobalController.showToast(macAddress, 20000);

		btnTopup = (ImageButton) findViewById(R.id.btnTopup);
		btnRegisterCc = (ImageButton) findViewById(R.id.btnRegisterCc);
		btnMyAccount = (ImageButton) findViewById(R.id.btnMyAccount);
		btnRegBox = (ImageButton) findViewById(R.id.btnRegBox);
		btnPurchase= (ImageButton) findViewById(R.id.btnPurchase);
		btnHistory= (ImageButton) findViewById(R.id.btnHistory);

		textRespon = (TextView) findViewById(R.id.textRespon);
		textFullname = (TextView) findViewById(R.id.textFullname);
		textHandphone = (TextView) findViewById(R.id.textHandphone);
		textEmail = (TextView) findViewById(R.id.textEmail);
		textAddress = (TextView) findViewById(R.id.textAddress);
		textBalance = (TextView) findViewById(R.id.textBalance);
		textPackage = (TextView) findViewById(R.id.textPackage);

		textFullname.setText(fullName);
		textHandphone.setText(handPhone);
		textEmail.setText(email);
		textAddress.setText(address);
		textBalance.setText(String.valueOf(balance));
		textPackage.setText(String.valueOf(longPackage));

		//checkVAForBalance() is used to trigger check Balance in Server API
		checkVAForBalance();
		checkBalance();

		setEventListener();

		if(macAddress.equalsIgnoreCase("Mobile")){
			btnRegBox.setVisibility(View.VISIBLE);
		}else{
			btnRegBox.setVisibility(View.INVISIBLE);
		}

	}

	private void setEventListener() {
		btnMyAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				MyAccountActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			}
		});
		btnPurchase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//GlobalController.showComingSoon(instance);
				//return;
				doPurchase();
			}
		});
		btnRegisterCc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doRegisterCc();
			}
		});
		btnTopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openTopupPage1Activity(instance);
				//AppController.openTopupEwalletActivity(instance);
				//final Intent intent = new Intent(context, MainActivity_refresh.class);
				//startActivity(intent);
			}
		});
		btnHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				GlobalController.showComingSoon(instance);
			}
		});
		btnRegBox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openRegisterActivityNew2_Box(instance);
			}
		});
	}

	private void doPurchase() {
		//final UserModel user_model = SessionController.getUser();
		//final String phoneNumber = user_model.PhoneNumber;
		//final String  phoneNumber = SessionController.getPhoneNumberVer();
		final String  phoneNumber = handPhone;

		GlobalController.showLoading(this);
		//URLController.checkGame(user_model.PhoneNumber, new URLManager.URLCallback() {
		URLController.activePackage(phoneNumber , new URLManager.URLCallback() {
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
						//final Intent intent = new Intent(context, PackageActivity.class);
						//startActivity(intent);
						textRespon.setText(response);
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


	private void setValidation(final String response) { //convert response to model
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			//game_model = new BubblePackageModel(response_model.Result);
			game_category_model = new BubblePackageCategoryModel((response_model.Result));
			//game_model = new BubblePackageModel((response_model.Result));
			//GlobalController.showToast(va_model.list.get(0).va_number, 1000);
			//AppController.openVAStatusReadActivity(instance, banner_model);

			//	GlobalController.showToast(game_model.list.size());
			AppController.openPackageActivity(instance, game_category_model);
			//AppController.openG(instance, active_package_category_model);
			//finish();

		}else{
			GlobalController.showWarning(instance,response_model.Message);
		}
	}

	private void doRegisterCc() {
		AppController.openRegisterCcActivity(instance);
	}

	private void checkVAForBalance() {
		//final UserModel user_model = SessionController.getUser();
		//final String  phoneNumber = SessionController.getPhoneNumberVer();
		final String  phoneNumber =handPhone;
		//GlobalController.showLoading(this);
		//GlobalController.showToast(phoneNumber, 20000);
		URLController.checkVA(phoneNumber, new URLManager.URLCallback() {
			//URLController.banner(new URLManager.URLCallback() {
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
						//response from Midtrans / Veritrans
						//textRespon.setText(response);
						//setVAValidation(response);
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
						//ignore fail
						GlobalController.closeLoading();
						//GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}


	private void checkBalance() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		//GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.balance(phoneNumber, new URLManager.URLCallback() {
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
						/*
						long intResponse = Long.valueOf(response);
						String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(intResponse);
						textBalance.setText(strResponse2);
						*/
						/*
						//response from Midtrans / Veritrans
						if (response.contains("capture") && response.contains("accept")){
							GlobalController.showMessage(instance, "Transaction success");
						}
						else
						{
							GlobalController.showMessage(instance, "Transaction fail");
						}
						*/
						setValidationCheckBalance(response);
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
						//GlobalController.closeLoading();
						GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}

	private void setValidationCheckBalance(final String response) { //convert response to model
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			//game_model = new BubblePackageModel(response_model.Result);
			//game_category_model = new BubblePackageCategoryModel((response_model.Result));
			//game_model = new BubblePackageModel((response_model.Result));
			//GlobalController.showToast(va_model.list.get(0).va_number, 1000);
			//AppController.openVAStatusReadActivity(instance, banner_model);

			//	GlobalController.showToast(game_model.list.size());
			//AppController.openPackageActivity(instance, game_category_model);
			//AppController.openG(instance, active_package_category_model);
			//finish();
			long intResponse = Long.valueOf(response_model.Result);
			String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(intResponse);
			textBalance.setText(strResponse2);
		}else{
			GlobalController.showWarning(instance,"Fail query balance");
		}
	}
/*
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_BACK) {
			//finish();
			MyAccountActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			return true;//must return true, other wise menu will dissapear suddently
		}

		return super.dispatchKeyEvent(event);
	}
*/
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}


}

