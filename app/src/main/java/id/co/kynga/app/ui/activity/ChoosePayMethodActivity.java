package id.co.kynga.app.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.AccountCCModel;
import id.co.kynga.app.model.BubblePackageCategoryModel;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.model.BuyPackageModel;
import id.co.kynga.app.model.InactivePackageCategoryModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;

import static id.co.kynga.app.KyngaApp.context;

public class ChoosePayMethodActivity extends Activity {
	public static ChoosePayMethodActivity instance;
	private ImageButton btnMyAccount, btnPurchase, btnCc, btnTopup, btnHistory;
	private TextView textFullname, textHandphone, textEmail, textAddress, textBalance, textPackage;
	private String fullName, handPhone, email, address;
	private long balance, longPackage;
	private BubblePackageModel game_model;
	private InactivePackageCategoryModel game_category_model2;
	private BubblePackageCategoryModel game_category_model;
	private ImageButton btn_back;
	private LinearLayout layoutChoosePay, layout1month, layoutTopUpNoBalance, layoutTopUpOk, layoutThanks;
	private LinearLayout layout3month, layout6month, layout9month, layout12month, layoutMonth;
	private LinearLayout layoutCcNoReg, layoutCcReg;
	Animation slideUpAnimation, slideUpAnimation2, slideDownAnimation, in_from_left;
	private ImageView btnClose, img_package, btnClose2, btnClose3, btnCloseThanks, btnCloseCcNoReg, btnCloseCcReg;
	private ImageButton btnNokTopUp, btnOKTopUp, btnOKThanks, btnOkCcNoReg, btnOkCcReg;
	private String month9;
	private TextView textPrice1Month, textPrice3Months, textPrice6Months, textPrice9Months, textPrice12Months;
	private long pricePerMonth3, pricePerMonth6, pricePerMonth9, pricePerMonth12;
	private int  intPackage;
	private TextView textPricePerMonth3, textPricePerMonth6, textPricePerMonth9, textPricePerMonth12;
	private long newPrice1Month, newPrice3Month, newPrice6Month, newPrice9Month, newPrice12Month;
	private long newPricePerMonth3, newPricePerMonth6, newPricePerMonth9, newPricePerMonth12;
	private static long balanceValue;
	private long packagePrice;
	private TextView textYouAboutTopup, textYouAboutTopupNoBalance, textMyAccountRp;
	private String duration;
	private String packageId1Month, packageId3Month, packageId6Month, packageId9Month, packageId12Month;
	private String priceId1Month, priceId3Month, priceId6Month, priceId9Month, priceId12Month;
	private String packageId, priceId;
	private TextView textYouAboutCcNoReg, textYouAboutCcReg, textFromMyCcNo;
	private String cekFullname;
	private String cekTextCc;
	private String cekTextMM;
	private String cekTextYY;
	private String cekTextCVV;
	private final Handler handler = new Handler();
	private BuyPackageModel buyPackageModel;
	private TextView textYouHaveThx;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//this.overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		setContentView(R.layout.activity_choose_pay_method);
		setInitial();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}



	private void setInitial() {
		/*
		Intent intent0 = getIntent();
		fullName = intent0.getStringExtra("fullName");
		handPhone = intent0.getStringExtra("phoneNumber");
		email = intent0.getStringExtra("email");
		address = intent0.getStringExtra("adress");
		balance = intent0.getLongExtra("balance",0);
		longPackage = intent0.getLongExtra("package",0);
		*/
		Intent intent0 = getIntent();
		game_model = intent0.getParcelableExtra("BubblePackageModel");
		game_category_model2 = intent0.getParcelableExtra("InactivePackageCategoryModel");
		month9= intent0.getStringExtra("month9");
		//GlobalController.showToast(String.valueOf(game_model.ImageURL),20000);
		//GlobalController.showToast(game_model.PriceID,20000);
		//GlobalController.showToast(game_model.PackageID,20000);


		in_from_left = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.in_from_left);
		slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_up_animation);
		slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_down_animation);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		img_package =(ImageView)findViewById(R.id.img_package);
		layout9month = (LinearLayout) findViewById(R.id.layout9month);
		if(month9.equals("Yes")){
			layout9month.setVisibility(View.VISIBLE);
		}
		if(month9.equals("No")){
			layout9month.setVisibility(View.GONE);
		}

		textYouHaveThx = (TextView)findViewById(R.id.textYouHaveThx);
		textFromMyCcNo = (TextView)findViewById(R.id.textFromMyCcNo);
		textYouAboutCcReg = (TextView)findViewById(R.id.textYouAboutCcReg);
		textYouAboutCcNoReg = (TextView)findViewById(R.id.textYouAboutCcNoReg);
		textYouAboutTopupNoBalance = (TextView)findViewById(R.id.textYouAboutTopupNoBalance);
		textYouAboutTopup = (TextView)findViewById(R.id.textYouAboutTopup);
		textMyAccountRp = (TextView)findViewById(R.id.textMyAccountRp);
		textPrice1Month = (TextView)findViewById(R.id.textPrice1Month);
		textPrice3Months = (TextView)findViewById(R.id.textPrice3Months);
		textPrice6Months = (TextView)findViewById(R.id.textPrice6Months);
		textPrice9Months = (TextView)findViewById(R.id.textPrice9Months);
		textPrice12Months = (TextView)findViewById(R.id.textPrice12Months);
		textPricePerMonth3 = (TextView)findViewById(R.id.textPricePerMonth3);
		textPricePerMonth6 = (TextView)findViewById(R.id.textPricePerMonth6);
		textPricePerMonth9 = (TextView)findViewById(R.id.textPricePerMonth9);
		textPricePerMonth12 = (TextView)findViewById(R.id.textPricePerMonth12);


		layoutMonth = (LinearLayout) findViewById(R.id.layoutMonth);
		layoutChoosePay = (LinearLayout) findViewById(R.id.layoutChoosePay);
		btnClose = (ImageView) findViewById(R.id.btnClose);
		btnTopup = (ImageButton)findViewById(R.id.btnTopup);
		btnCc = (ImageButton)findViewById(R.id.btnCc);
		layout1month= (LinearLayout) findViewById(R.id.layout1month);
		layout3month= (LinearLayout) findViewById(R.id.layout3month);
		layout6month= (LinearLayout) findViewById(R.id.layout6month);
		layout9month= (LinearLayout) findViewById(R.id.layout9month);
		layout12month= (LinearLayout) findViewById(R.id.layout12month);

		btnClose2 = (ImageView) findViewById(R.id.btnClose2);
		layoutTopUpNoBalance= (LinearLayout) findViewById(R.id.layoutTopUpNobalance);
		btnNokTopUp = (ImageButton) findViewById(R.id.btnNokTopUp);
		btnOKTopUp = (ImageButton) findViewById(R.id.btnOKTopUp);
		layoutTopUpOk = (LinearLayout) findViewById(R.id.layoutTopUpOk);
		btnClose3 = (ImageView) findViewById(R.id.btnClose3);
		layoutThanks = (LinearLayout) findViewById(R.id.layoutThanks);
		btnCloseThanks = (ImageView) findViewById(R.id.btnCloseThanks);
		btnOKThanks = (ImageButton) findViewById(R.id.btnOKThanks);

		btnCloseCcNoReg = (ImageView) findViewById(R.id.btnClosedCcNoReg);
		btnOkCcNoReg = (ImageButton) findViewById(R.id.btnOkCcNoReg);
		layoutCcNoReg = (LinearLayout) findViewById(R.id.layoutCcNoReg);

		btnCloseCcReg = (ImageView) findViewById(R.id.btnClosedCcReg);
		btnOkCcReg = (ImageButton) findViewById(R.id.btnOkCcReg);
		layoutCcReg = (LinearLayout) findViewById(R.id.layoutCcReg);

		Picasso.with(context).load(game_model.ImageUrl).into(img_package);

		intPackage = game_category_model2.Bubble.list.size();
		//GlobalController.showToast(String.valueOf(intPackage),20000);
		if(intPackage==4) {
/*
			textPrice1Month.setText(game_category_model2.Bubble.list.get(3).Price);
			textPrice3Months.setText(game_category_model2.Bubble.list.get(2).Price);
			textPrice6Months.setText(game_category_model2.Bubble.list.get(1).Price);
			textPrice12Months.setText(game_category_model2.Bubble.list.get(0).Price);
*/
			newPrice1Month = Long.valueOf(game_category_model2.Bubble.list.get(3).Price);
			newPrice3Month = Long.valueOf(game_category_model2.Bubble.list.get(2).Price);
			newPrice6Month = Long.valueOf(game_category_model2.Bubble.list.get(1).Price);
			newPrice12Month = Long.valueOf(game_category_model2.Bubble.list.get(0).Price);
			newPricePerMonth3 = Long.valueOf(game_category_model2.Bubble.list.get(2).Price)/3;
			newPricePerMonth6 = Long.valueOf(game_category_model2.Bubble.list.get(1).Price)/6;
			newPricePerMonth12 = Long.valueOf(game_category_model2.Bubble.list.get(0).Price)/12;

			textPrice1Month.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice1Month));
			textPrice3Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice3Month));
			textPrice6Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice6Month));
			textPrice12Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice12Month));
			textPricePerMonth3.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth3));
			textPricePerMonth6.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth6));
			textPricePerMonth12.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth12));

			packageId1Month = game_category_model2.Bubble.list.get(3).PackageID;
			packageId3Month = game_category_model2.Bubble.list.get(2).PackageID;
			packageId6Month = game_category_model2.Bubble.list.get(1).PackageID;
			packageId12Month = game_category_model2.Bubble.list.get(0).PackageID;

			priceId1Month = game_category_model2.Bubble.list.get(3).PriceID;
			priceId3Month = game_category_model2.Bubble.list.get(2).PriceID;
			priceId6Month = game_category_model2.Bubble.list.get(1).PriceID;
			priceId12Month = game_category_model2.Bubble.list.get(0).PriceID;

		}
		if(intPackage==5) {
/*
			textPrice1Month.setText(game_category_model2.Bubble.list.get(4).Price);
			textPrice3Months.setText(game_category_model2.Bubble.list.get(3).Price);
			textPrice6Months.setText(game_category_model2.Bubble.list.get(2).Price);
			textPrice9Months.setText(game_category_model2.Bubble.list.get(1).Price);
			textPrice12Months.setText(game_category_model2.Bubble.list.get(0).Price);
*/
			newPrice1Month = Long.valueOf(game_category_model2.Bubble.list.get(4).Price);
			newPrice3Month = Long.valueOf(game_category_model2.Bubble.list.get(3).Price);
			newPrice6Month = Long.valueOf(game_category_model2.Bubble.list.get(2).Price);
			newPrice9Month = Long.valueOf(game_category_model2.Bubble.list.get(1).Price);
			newPrice12Month = Long.valueOf(game_category_model2.Bubble.list.get(0).Price);

			newPricePerMonth3 = Long.valueOf(game_category_model2.Bubble.list.get(3).Price)/3;
			newPricePerMonth6 = Long.valueOf(game_category_model2.Bubble.list.get(2).Price)/6;
			newPricePerMonth9 = Long.valueOf(game_category_model2.Bubble.list.get(1).Price)/9;
			newPricePerMonth12 = Long.valueOf(game_category_model2.Bubble.list.get(0).Price)/12;

			textPrice1Month.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice1Month));
			textPrice3Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice3Month));
			textPrice6Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice6Month));
			textPrice9Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice9Month));
			textPrice12Months.setText(NumberFormat.getNumberInstance(Locale.US).format(newPrice12Month));
			textPricePerMonth3.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth3));
			textPricePerMonth6.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth6));
			textPricePerMonth9.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth9));
			textPricePerMonth12.setText(NumberFormat.getNumberInstance(Locale.US).format(newPricePerMonth12));

			packageId1Month = game_category_model2.Bubble.list.get(4).PackageID;
			packageId3Month = game_category_model2.Bubble.list.get(3).PackageID;
			packageId6Month = game_category_model2.Bubble.list.get(2).PackageID;
			packageId9Month = game_category_model2.Bubble.list.get(1).PackageID;
			packageId12Month = game_category_model2.Bubble.list.get(0).PackageID;

			priceId1Month = game_category_model2.Bubble.list.get(4).PriceID;
			priceId3Month = game_category_model2.Bubble.list.get(3).PriceID;
			priceId6Month = game_category_model2.Bubble.list.get(2).PriceID;
			priceId9Month = game_category_model2.Bubble.list.get(1).PriceID;
			priceId12Month = game_category_model2.Bubble.list.get(0).PriceID;

		}
		if(intPackage<4) {
			//GlobalController.showToast("Package not complete",20000);
			GlobalController.showWarning(instance,"Package not complete");
			return;
		}

/*
		btnTopup = (ImageButton) findViewById(R.id.btnTopup);
		btnRegisterCc = (ImageButton) findViewById(R.id.btnRegisterCc);
		btnMyAccount = (ImageButton) findViewById(R.id.btnMyAccount);
		btnPurchase= (ImageButton) findViewById(R.id.btnPurchase);
		btnHistory= (ImageButton) findViewById(R.id.btnHistory);

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
*/
		checkVAForBalance();
		checkBalance();
		setEventListener();


	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				ChoosePayMethodActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
			}
		});

		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enableMonthButton();
				layoutChoosePay.setVisibility(View.GONE);
				layoutChoosePay.startAnimation(slideDownAnimation);
			}
		});
		layout1month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableMonthButton();
				packageId = packageId1Month;
				priceId = priceId1Month;
				duration = "1 Month";
				packagePrice = newPrice1Month;
				String strPackagePrice = NumberFormat.getNumberInstance(Locale.US).format(packagePrice);
				textYouAboutTopup.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				//textMyAccountRp.setText("From my account balance Rp. "+strPackagePrice);
				String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(balanceValue);
				textMyAccountRp.setText("From my account balance Rp. "+ strResponse2);

				textYouAboutTopupNoBalance.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcNoReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);

				layoutChoosePay.setVisibility(View.VISIBLE);
				layoutChoosePay.startAnimation(slideUpAnimation);
			}
		});

		layout3month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableMonthButton();
				packageId = packageId3Month;
				priceId = priceId3Month;
				duration = "3 Months";
				packagePrice = newPrice3Month;
				String strPackagePrice = NumberFormat.getNumberInstance(Locale.US).format(packagePrice);
				textYouAboutTopup.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textMyAccountRp.setText("From my account balance Rp. "+strPackagePrice);
				textYouAboutTopupNoBalance.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcNoReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				layoutChoosePay.setVisibility(View.VISIBLE);
				layoutChoosePay.startAnimation(slideUpAnimation);
			}
		});

		layout6month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableMonthButton();
				packageId = packageId6Month;
				priceId = priceId6Month;
				duration = "6 Months";
				packagePrice = newPrice6Month;
				String strPackagePrice = NumberFormat.getNumberInstance(Locale.US).format(packagePrice);
				textYouAboutTopup.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textMyAccountRp.setText("From my account balance Rp. "+strPackagePrice);
				textYouAboutTopupNoBalance.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcNoReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				layoutChoosePay.setVisibility(View.VISIBLE);
				layoutChoosePay.startAnimation(slideUpAnimation);
			}
		});
		layout9month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableMonthButton();
				packageId = packageId9Month;
				priceId = priceId9Month;
				duration = "9 Months";
				packagePrice = newPrice9Month;
				String strPackagePrice = NumberFormat.getNumberInstance(Locale.US).format(packagePrice);
				textYouAboutTopup.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textMyAccountRp.setText("From my account balance Rp. "+strPackagePrice);
				textYouAboutTopupNoBalance.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcNoReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				layoutChoosePay.setVisibility(View.VISIBLE);
				layoutChoosePay.startAnimation(slideUpAnimation);
			}
		});

		layout12month.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableMonthButton();
				packageId = packageId12Month;
				priceId = priceId12Month;
				duration = "12 Months";
				packagePrice = newPrice12Month;
				String strPackagePrice = NumberFormat.getNumberInstance(Locale.US).format(packagePrice);
				textYouAboutTopup.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textMyAccountRp.setText("From my account balance Rp. "+strPackagePrice);
				textYouAboutTopupNoBalance.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcNoReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				textYouAboutCcReg.setText("You are about to purchase "+game_model.Title+" "+duration+" Package for Rp. " +
						strPackagePrice);
				layoutChoosePay.setVisibility(View.VISIBLE);
				layoutChoosePay.startAnimation(slideUpAnimation);
			}
		});

		btnTopup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//GlobalController.showToast(String.valueOf(balanceValue) , 20000);
				dissableTopupCcButton();
				if (balanceValue<packagePrice) {
					layoutTopUpNoBalance.setVisibility(View.VISIBLE);
					layoutTopUpNoBalance.startAnimation(slideUpAnimation);
				}else{
					layoutTopUpOk.setVisibility(View.VISIBLE);
					layoutTopUpOk.startAnimation(slideUpAnimation);

				}

				//layoutTopUpOk.setVisibility(View.VISIBLE);
				//layoutTopUpOk.startAnimation(slideUpAnimation);
				//doPurchase();
			}
		});

		btnCc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dissableTopupCcButton();
				//layoutCcNoReg.setVisibility(View.VISIBLE);
				//layoutCcNoReg.startAnimation(slideUpAnimation);
				//layoutCcReg.setVisibility(View.VISIBLE);
				//layoutCcReg.startAnimation(slideUpAnimation);
				//GlobalController.showComingSoon(instance);
				//doPaymentCC();
				checkAccountCC();
			}
		});

		btnNokTopUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openTopupPage1Activity(instance);
			}
		});

		btnOKTopUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//layoutThanks.setVisibility(View.VISIBLE);
				//layoutThanks.startAnimation(slideUpAnimation);
				doPurchase();
			}
		});

		btnClose2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enableTopupCcButton();
				layoutTopUpNoBalance.setVisibility(View.GONE);
				layoutTopUpNoBalance.startAnimation(slideDownAnimation);
			}
		});
		btnClose3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enableTopupCcButton();
				layoutTopUpOk.setVisibility(View.GONE);
				layoutTopUpOk.startAnimation(slideDownAnimation);
			}
		});
		btnCloseThanks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				ChoosePayMethodActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
				/*
				layoutThanks.setVisibility(View.GONE);
				layoutThanks.startAnimation(slideDownAnimation);
				layoutTopUpOk.setVisibility(View.GONE);
				layoutTopUpOk.startAnimation(slideDownAnimation);
				layoutChoosePay.setVisibility(View.GONE);
				layoutChoosePay.startAnimation(slideDownAnimation);
				layoutCcReg.setVisibility(View.GONE);
				layoutCcReg.startAnimation(slideDownAnimation);
				layoutMonth.setVisibility(View.GONE);
				layoutMonth.startAnimation(slideDownAnimation);
				*/
			}
		});

		btnOKThanks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				ChoosePayMethodActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
				/*
				layoutThanks.setVisibility(View.GONE);
				layoutThanks.startAnimation(slideDownAnimation);
				layoutTopUpOk.setVisibility(View.GONE);
				layoutTopUpOk.startAnimation(slideDownAnimation);
				layoutChoosePay.setVisibility(View.GONE);
				layoutChoosePay.startAnimation(slideDownAnimation);
				layoutCcReg.setVisibility(View.GONE);
				layoutCcReg.startAnimation(slideDownAnimation);
				layoutMonth.setVisibility(View.GONE);
				layoutMonth.startAnimation(slideDownAnimation);
				*/
			}
		});

		btnCloseCcNoReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enableTopupCcButton();
				layoutCcNoReg.setVisibility(View.GONE);
				layoutCcNoReg.startAnimation(slideDownAnimation);
			}
		});

		btnOkCcNoReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AppController.openRegisterCcActivity(instance);
			}
		});

		btnCloseCcReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				enableTopupCcButton();
				layoutCcReg.setVisibility(View.GONE);
				layoutCcReg.startAnimation(slideDownAnimation);
			}
		});

		btnOkCcReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//layoutThanks.setVisibility(View.VISIBLE);
				//layoutThanks.startAnimation(slideUpAnimation);
				//GlobalController.showComingSoon(instance);
				doPaymentCC();
			}
		});
		/*
		btnMyAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				ChoosePayMethodActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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
		*/
	}

	private void dissableTopupCcButton() {
		btnTopup.setEnabled(false);
		btnCc.setEnabled(false);
	}

	private void enableTopupCcButton() {
		btnTopup.setEnabled(true);
		btnCc.setEnabled(true);
	}

	private void dissableMonthButton() {
		layout1month.setEnabled(false);
		layout3month.setEnabled(false);
		layout6month.setEnabled(false);
		layout9month.setEnabled(false);
		layout12month.setEnabled(false);
	}

	private void enableMonthButton() {
		layout1month.setEnabled(true);
		layout3month.setEnabled(true);
		layout6month.setEnabled(true);
		layout9month.setEnabled(true);
		layout12month.setEnabled(true);
	}

	private void doPurchase() {
		final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		//final String  phoneNumber =user_model.PhoneNumber;
		//GlobalController.showLoading(this);
		//GlobalController.showToast(phoneNumber, 20000);
		GlobalController.showLoading(this);
		//URLController.checkGame(user_model.PhoneNumber, new URLManager.URLCallback() {
		URLController.doPurchasePackageByTopup(phoneNumber, packageId,priceId,String.valueOf(packagePrice)
				, new URLManager.URLCallback() {
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


	private void setValidation(final String response) { //convert response to model
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			buyPackageModel = new BuyPackageModel(response_model.Result);
			//game_model = new BubblePackageModel(response_model.Result);
			//game_category_model = new BubblePackageCategoryModel((response_model.Result));
			//game_model = new BubblePackageModel((response_model.Result));
			//GlobalController.showToast(va_model.list.get(0).va_number, 1000);
			//AppController.openVAStatusReadActivity(instance, banner_model);

			//GlobalController.showToast(buyPackageModel.transactionMessage,20000);
			//AppController.openPackageActivity(instance, game_category_model);
			//AppController.openG(instance, active_package_category_model);
			//finish();
			textYouHaveThx.setText("You have purchased "+game_category_model2.Package+" "+duration+" Package. \nYour package is active until \n"+buyPackageModel.transactionMessage);
			layoutThanks.setVisibility(View.VISIBLE);
			layoutThanks.startAnimation(slideUpAnimation);

		}else{
			GlobalController.showWarning(instance,response_model.Message);
		}
	}

	private void doRegisterCc() {
		AppController.openRegisterCcActivity(instance);
	}

	private void checkVAForBalance() {
		final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		//final String  phoneNumber =user_model.PhoneNumber;
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
						GlobalController.closeLoading();
						GlobalController.showRequestFailedWarning(instance);
					}
				});
			}
		});
	}


	private void checkBalance() {
		final UserModel user_model = SessionController.getUser();
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		//final String  phoneNumber =user_model.PhoneNumber;
		//GlobalController.showLoading(this);
		//GlobalController.showToast(phoneNumber, 20000);
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
			balanceValue = Long.valueOf(response_model.Result);
			//GlobalController.showToast(String.valueOf(balanceValue), 20000);
			/*
			long intResponse = Long.valueOf(response_model.Result);
			String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(intResponse);
			textBalance.setText(strResponse2);
			*/
		}else{
			GlobalController.showWarning(instance,response_model.Message);
		}
	}

	private void doPaymentCC() {
		final UserModel user_model = SessionController.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		final String  phoneNumber = SessionController.getPhoneNumberVer();
		//URLController.payment(user_model.PhoneNumber,cekTextCc, cekTextMM, cekTextYY, cekTextCVV,
		URLController.payment(phoneNumber,cekTextCc, cekTextMM, cekTextYY, cekTextCVV,
				String.valueOf(packagePrice), new URLManager.URLCallback() {
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
						setValidationCC(response);
						/*
						if (response.contains("capture") && response.contains("accept")){
							GlobalController.showMessage(instance, "Transaction success");
						}
						else
						{
							GlobalController.showMessage(instance, "Transaction fail");
						}
						*/
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

	private void setValidationCC(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//if payment cc fail, response aonly {}
			doPurchase();
		} else{
			goToRegisterCC();
			//GlobalController.showWarning(this, "Transaction Fail, Please check your Credit Card");
			//process cannot redirect clik OK to other page
			//AppController.openRegisterCcActivity(instance);
			//finish();
		}
		/*else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			GlobalController.showMessage(this, "Transaction Fail");
		}*/
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
						setValidationCekCC(response);
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

	private void setValidationCekCC(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final AccountCCModel accountCC_model = new AccountCCModel(response_model.Result);

			cekFullname = accountCC_model.Fullname;
			cekTextCc = accountCC_model.number;
			//GlobalController.showToast(cekTextCc, 20000);
			cekTextMM = accountCC_model.expired_month;
			cekTextYY = accountCC_model.expired_year;
			cekTextCVV = accountCC_model.cwcv;
			if(cekFullname.length()==0 || cekTextCc.length() ==0 || cekTextMM.length() ==0 || cekTextYY.length() ==0
					|| cekTextCVV.length() ==0){
				//GlobalController.showMessage(this, "CC empty");
				layoutCcNoReg.setVisibility(View.VISIBLE);
				layoutCcNoReg.startAnimation(slideUpAnimation);

			}else{
				//GlobalController.showMessage(this, "CC not empty");
				layoutCcReg.setVisibility(View.VISIBLE);
				layoutCcReg.startAnimation(slideUpAnimation);
				//textFromMyCcNo.setText("From my registered Credit Card "+cekTextCc.substring(0,4)+"-"
				//+cekTextCc.substring(4,8)+"-"+cekTextCc.substring(8,12)+"-****");
				textFromMyCcNo.setText("From my registered Credit Card "+"**** **** **** "+cekTextCc.substring(12,16));
			}

		} else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
			//GlobalController.showWarning(this, response_model.Message);
			layoutCcNoReg.setVisibility(View.VISIBLE);
			layoutCcNoReg.startAnimation(slideUpAnimation);
		}
	}


	private void goToRegisterCC() {
		GlobalController.showQuestion(this, "Transaction Fail, Please check your Credit Card. \n" +
				"Do you want to go to Credit Card Registration page?", new GlobalController.AlertCallback() {
			@Override
			public void didAlertButton1() {
				AppController.openRegisterCcActivity(instance);
				finish();
			}

			@Override
			public void didAlertButton2() {

			}
		});
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

