package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.VAModel;


public class TopupActivity_page1 extends Activity {
	public static TopupActivity_page1 instance;

	private ImageButton btnBack, btnPanahBCA, btnPanahMandiri, btnPanahPermata;
	private EditText txt_amount, textFullname, textCC, textMM, textYY, textCVV ;
	private ImageButton btnBCASubmit, btnMandiriSubmit, btnPermataSubmit;
	private LinearLayout layoutPermataText, layoutPermataButton;
	private LinearLayout layoutBCAText, layoutBCAButton;
	private LinearLayout layoutMandiriText, layoutMandiriButton;
	private LinearLayout layoutEWalletButton;
	private boolean klikBank=false;
	private String clickOrigin;
	private TextView textRespon, textBalance;
	private VAModel va_model;
	private ImageButton btnBCACheckVA, btnMandiriCheckVA, btnPermataCheckVA;
	private Button btnCekBalance;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_topup_page1);
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
		btnBack = (ImageButton) findViewById(R.id.btnBack);
		btnCekBalance = (Button) findViewById(R.id.btnCekBalance);

		textBalance = (TextView) findViewById(R.id.textBalance);
		txt_amount = (EditText) findViewById(R.id.text_amount);
		btnPanahBCA = (ImageButton) findViewById(R.id.btnPanahBCA);
		btnBCASubmit = (ImageButton) findViewById(R.id.btnBCASubmit);
		btnPanahMandiri = (ImageButton) findViewById(R.id.btnPanahMandiri);
		btnMandiriSubmit = (ImageButton) findViewById(R.id.btnMandiriSubmit);
		btnPanahPermata = (ImageButton) findViewById(R.id.btnPanahPermata);
		btnPermataSubmit = (ImageButton) findViewById(R.id.btnPermataSubmit);

		btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_bawah);
		btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_bawah);
		btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_bawah);


		layoutBCAText = (LinearLayout)findViewById(R.id.layoutBCAText);
		layoutMandiriText = (LinearLayout)findViewById(R.id.layoutMandiriText);
		layoutPermataText = (LinearLayout) findViewById(R.id.layoutPermataText);
		layoutBCAText.setVisibility(View.GONE);
		layoutMandiriText.setVisibility(View.GONE);
		layoutPermataText.setVisibility(View.GONE);

		layoutBCAButton = (LinearLayout)findViewById(R.id.layoutBcaButton);
		layoutMandiriButton = (LinearLayout)findViewById(R.id.layoutMandiriButton);
		layoutPermataButton = (LinearLayout) findViewById(R.id.layoutPermataButton);
		layoutEWalletButton = (LinearLayout) findViewById(R.id.layoutEWalletButton);

		btnBCACheckVA = (ImageButton) findViewById(R.id.btnBCACheckVA);
		btnMandiriCheckVA = (ImageButton) findViewById(R.id.btnMandiriCheckVA);
		btnPermataCheckVA = (ImageButton) findViewById(R.id.btnPermataCheckVA);

		textRespon = (TextView) findViewById(R.id.textRespon);

		setMargins (layoutBCAButton, 20, 0, 20, 10);
		setMargins (layoutMandiriButton, 20, 0, 20, 10);
		setMargins (layoutPermataButton, 20, 0, 20, 10);
		setEventListener();
	}

	private void setEventListener() {
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doBack();
			}
		});
		txt_amount.addTextChangedListener(onTextChangedListener());
		btnPanahBCA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutBCAText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutBCAButton, 20, 0, 20, 0);
				}else if(klikBank==true) {
					btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutBCAText.setVisibility(View.GONE);
					//setMargins (layoutBCAButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		layoutBCAButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutBCAText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutBCAButton, 20, 0, 20, 0);
				}else if(klikBank==true) {
					btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutBCAText.setVisibility(View.GONE);
					//setMargins (layoutBCAButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		btnBCASubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromBCA";
				doPaymentBCA();
			}
		});
		btnBCACheckVA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromCheckVABcaPermata";
				checkVA();
			}
		});


		btnPanahMandiri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutMandiriText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutMandiriButton, 20, 0, 20, 0);
				}else if(klikBank==true) {
					btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutMandiriText.setVisibility(View.GONE);
					//setMargins (layoutMandiriButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		layoutMandiriButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setPanahBawah();
				hideLayoutBankText();
				if(klikBank==false) {
					btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutMandiriText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutMandiriButton, 20, 0, 20, 0);
					//setMargin0();
				}else if(klikBank==true) {
					btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutMandiriText.setVisibility(View.GONE);
					//setMargins (layoutMandiriButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		btnMandiriSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromMandiri";
				doPaymentMandiri();
			}
		});

		btnMandiriCheckVA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromCheckVAMandiri";
				checkVA();
			}
		});


		btnPanahPermata.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutPermataText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutPermataButton, 20, 0, 20, 0);
				}else if(klikBank==true) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutPermataText.setVisibility(View.GONE);
					//setMargins (layoutPermataButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		layoutPermataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutPermataText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutPermataButton, 20, 0, 20, 0);
					//setMargin0();
				}else if(klikBank==true) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutPermataText.setVisibility(View.GONE);
					//setMargins (layoutPermataButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
			}
		});
		btnPermataSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromPermata";
				doPaymentPermata();
			}
		});
		btnPermataCheckVA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromCheckVABcaPermata";
				checkVA();
			}
		});
		btnCekBalance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				clickOrigin = "fromCheckVABcaPermata";
				checkVAForBalance();
				//Be carefull.. double checkVA make double payment...!!!!!!!!!!
				GlobalController.showMessage(instance, "Check again if Balance is not update");
				//checkVAForBalance();
				checkBalance();
			}
		});

		layoutEWalletButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openTopupEwalletActivity(instance);
				/*hideLayoutBankText();
				setPanahBawah();
				if(klikBank==false) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_atas);
					layoutPermataText.setVisibility(View.VISIBLE);
					setMargin10();
					setMargins (layoutPermataButton, 20, 0, 20, 0);
					//setMargin0();
				}else if(klikBank==true) {
					btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_bawah);
					layoutPermataText.setVisibility(View.GONE);
					//setMargins (layoutPermataButton, 30, 0, 30, 10);
					setMargin10();
				}
				if(klikBank==false){
					klikBank=true;
				}else{
					klikBank=false;
				}
				*/
			}
		});

		checkVAForBalance();
		checkBalance();

		/*
		linearLayoutPermataButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(klikPermata==false) {
					showTextPermata();
				}else if(klikPermata==true) {
					hideTextPermata();
				}
				if(klikPermata==false){
					klikPermata=true;
				}else{
					klikPermata=false;
				}
			}
		});
		*/
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

	private void setMargin0(){
		setMargins (layoutBCAButton, 20, 0, 20, 0);
		setMargins (layoutMandiriButton, 20, 0, 20, 0);
		setMargins (layoutPermataButton, 20, 0, 20, 0);
	}
	private void setMargin10(){
		setMargins (layoutBCAButton, 20, 0, 20, 10);
		setMargins (layoutMandiriButton, 20, 0, 20, 10);
		setMargins (layoutPermataButton, 20, 0, 20, 10);
		setMargins (layoutBCAText, 20, 0, 20, 10);
		setMargins (layoutMandiriText, 20, 0, 20, 10);
		setMargins (layoutPermataText, 20, 0, 20, 10);
	}

	private void setPanahAtas(){
		btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_atas);
		btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_atas);
		btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_atas);
	}

	private void setPanahBawah(){
		btnPanahBCA.setBackgroundResource(R.drawable.icon_panah_bawah);
		btnPanahMandiri.setBackgroundResource(R.drawable.icon_panah_bawah);
		btnPanahPermata.setBackgroundResource(R.drawable.icon_panah_bawah);
	}

	private void setMargins (View view, int left, int top, int right, int bottom) {
		if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			p.setMargins(left, top, right, bottom);
			view.requestLayout();
		}
	}
/*
	private void hideTextPermata(){
		linearLayoutPermataText.animate()
				.translationYBy(linearLayoutPermataText.getHeight())
				.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
	}
	private void showTextPermata(){
			linearLayoutPermataText.animate()
					.translationYBy(-linearLayoutPermataText.getHeight())
					.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
	}
*/
	private void doBack() {
		//AppController.openLoginActivity(this);
		finish();
		this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}

	private void doPaymentBCA() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		String cc_amount = txt_amount.getText().toString();
		String new_cc_amount = cc_amount.replaceAll(",", "");
		if (!GlobalController.isNotNull(cc_amount)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			txt_amount.setError("mandatory");
			return;
		}
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.payment2(phoneNumber ,new_cc_amount, "bca", new URLManager.URLCallback() {
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
						textRespon.setText(response);
						checkVA();
						//setValidationBCA(response); response doesn't have sign [ dan ], so no need validation
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

	private void checkVA() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		GlobalController.showLoading(this);
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

						textRespon.setText(response);
/*
						Intent intent = new Intent(getApplicationContext(), VAstatusBCA_Permata.class);
						startActivity(intent);
						finish();
*/
						setVAValidation(response);
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
						//GlobalController.showRequestFailedWarning(instance);
						GlobalController.showMessage(instance,"Fail checking Virtual Account, or click Submit button first");
					}
				});
			}
		});
	}

	private void setVAValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			va_model = new VAModel(response_model.Result);
			//GlobalController.showToast(va_model.list.get(0).va_number, 1000);
			//AppController.openVAStatusReadActivity(instance, banner_model);
			if(clickOrigin.equals("fromMandiri") || clickOrigin.equals("fromCheckVAMandiri")) {
				AppController.openVAStatusMandiri(instance, va_model);
				//finish();
			}else{
				AppController.openVAStatusBCA_Permata(instance, va_model);
				//finish();
			}
		}else{
			GlobalController.showWarning(instance,"Fail checking Virtual Account, or click Submit button first");
		}
	}

	private void doPaymentMandiri() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		String cc_amount = txt_amount.getText().toString();
		String new_cc_amount = cc_amount.replaceAll(",", "");
		if (!GlobalController.isNotNull(cc_amount)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			txt_amount.setError("mandatory");
			return;
		}
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.payment3(phoneNumber,new_cc_amount, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//GlobalController.closeLoading();
						//GlobalController.showToast(response, 20000);
						//response from Midtrans / Veritrans
						textRespon.setText(response);
						checkVA();
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

	private void doPaymentPermata() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		String cc_amount = txt_amount.getText().toString();
		String new_cc_amount = cc_amount.replaceAll(",", "");
		if (!GlobalController.isNotNull(cc_amount)) {
			//GlobalController.showWarning(this, R.string.error_cc_number_blank);
			txt_amount.setError("mandatory");
			return;
		}
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.payment2(phoneNumber,new_cc_amount, "permata", new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//GlobalController.closeLoading();
						//GlobalController.showToast(response, 20000);
						//response from Midtrans / Veritrans
						textRespon.setText(response);
						checkVA();
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

/*
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.KEYCODE_BACK) {
			//finish();
			//this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			return false;
		}
		return super.dispatchKeyEvent(event);
	}
*/
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
				txt_amount.removeTextChangedListener(this);
				try {
					String originalString = s.toString();
					Long longval;
					if (originalString.contains(",")) {
						originalString = originalString.replaceAll(",", "");
					}
					longval = Long.parseLong(originalString);

					DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
					formatter.applyPattern("#,###,###,###");
					String formattedString = formatter.format(longval);
					//setting text after format to EditText
					txt_amount.setText(formattedString);
					txt_amount.setSelection(txt_amount.getText().length());
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}

				txt_amount.addTextChangedListener(this);
			}
		};
	}

	private void hideLayoutBankText(){
		layoutBCAText.setVisibility(View.GONE);
		layoutMandiriText.setVisibility(View.GONE);
		layoutPermataText.setVisibility(View.GONE);
	}

	private void checkVAForBalance() {
		//final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
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

						textRespon.setText(response);

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
						//Ignore fail..!!!!
						GlobalController.closeLoading();
						//GlobalController.showRequestFailedWarning(instance);
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
			GlobalController.showWarning(instance,response_model.Message);
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
	}
}