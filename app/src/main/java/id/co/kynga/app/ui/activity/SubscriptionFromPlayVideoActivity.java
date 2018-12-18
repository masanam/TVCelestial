package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ProductModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.UserModel;

public class SubscriptionFromPlayVideoActivity extends CommonActivity {
	public static SubscriptionFromPlayVideoActivity instance;
	private ImageButton btn_back;

	private ImageButton btn_buy1, btn_buy2;
	private TextView textRespon;
	private ProductModel product_model;
	private static String clickOrigin;
	private TextView textPrice1, textPrice2, textProduct1, textProduct2;
	private String videoID;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_subscription_from_play_video);
		Intent intent = getIntent();
		videoID = intent.getStringExtra("videoID");
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
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_buy1 = (ImageButton) findViewById(R.id.btn_buy1);
		btn_buy2 = (ImageButton) findViewById(R.id.btn_buy2);
		textRespon = (TextView) findViewById(R.id.textRespon);
		textProduct1 = (TextView) findViewById(R.id.textProduct1);
		textProduct2 = (TextView) findViewById(R.id.textProduct2);
		textPrice1 = (TextView) findViewById(R.id.textPrice1);
		textPrice2 = (TextView) findViewById(R.id.textPrice2);

		setEventListener();
		checkProduct();
		//checkSubscriptionStatus();
		//populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		btn_buy1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				buyProduct1();
			}
		});
		btn_buy2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				buyProduct2();
			}
		});
	}

	private void populateData() {
		//txt_phone_number.setPrefix(Config.default_phone_prefix);
		//txt_phone_number.setPrefixTextColor(Color.YELLOW);
	}

	private void checkProduct() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.checkProduct(new URLManager.URLCallback() {
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
						setValidationSubscriptionStatus(response);
						//checkVA();
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

	private void buyProduct2() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		//GlobalController.showToast(videoID, 20000);
		URLController.buyProduct(user_model.PhoneNumber, videoID, "2",new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//GlobalController.showMessage(instance,response);
						if (response.contains("You're already")) {
							GlobalController.showMessage(instance, response.substring(23,61));
						}else if(response.contains("Not enough balance")){
							GlobalController.showMessage(instance,response.substring(23,62));
						} else if (response.contains("You're now subscribe")){
							GlobalController.showMessage(instance,response.substring(23,57));
						}
						textRespon.setText(response);
						//checkVA();
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

	private void buyProduct1() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		//GlobalController.showToast(videoID, 20000);
		URLController.buyProduct(user_model.PhoneNumber, videoID, "1",new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						if (response.contains("You're already")) {
							GlobalController.showMessage(instance, response.substring(23,61));
						}else if(response.contains("Not enough balance")){
							GlobalController.showMessage(instance,response.substring(23,62));
						} else if (response.contains("You're now subscribe")){
							GlobalController.showMessage(instance,response.substring(23,57));
						}

						textRespon.setText(response);
						//checkVA();
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

	private void setValidationSubscriptionStatus(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			product_model = new ProductModel(response_model.Result);
			textProduct1.setText(product_model.list.get(0).product_name);
			textPrice1.setText(product_model.list.get(0).product_price);
			textProduct2.setText(product_model.list.get(1).product_name);
			textPrice2.setText(product_model.list.get(1).product_price);
		}
	}
}