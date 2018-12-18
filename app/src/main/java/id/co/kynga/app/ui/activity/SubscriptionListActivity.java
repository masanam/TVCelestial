package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.ProductModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.SubscriptionModel;
import id.co.kynga.app.model.UserModel;

public class SubscriptionListActivity extends CommonActivity {
	public static SubscriptionListActivity instance;
	private ImageButton btn_back;

	private ImageButton btn_buy1, btn_buy2;
	private TextView textRespon;
	private ProductModel product_model;
	private static String clickOrigin;
	private TextView textPrice1, textPrice2, textProduct1, textProduct2;
	private String videoID;

	private ListView mListView1_1, mListView1_2, mListView1_3, mListView1_4,mListView1_5,mListView1_6,mListView1_7;
	private ListView mListView1_8, mListView1_9, mListView1_10, mListView1_11,mListView1_12,mListView1_13,mListView1_14;
	private ListView mListView1_15, mListView1_16;

	private static ArrayList<String> daftarPemesanList = new ArrayList<String>();
	private static ArrayList<String> daftarNoHpList = new ArrayList<String>();
	private static ArrayList<String> daftarNamaKlinikList = new ArrayList<String>();

	private SubscriptionModel subscription_model;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_subscription_list);

		if (!daftarPemesanList.isEmpty()){
			daftarPemesanList.clear();
		}
		if (!daftarNoHpList.isEmpty()){
			daftarNoHpList.clear();
		}
		if (!daftarNamaKlinikList.isEmpty()){
			daftarNamaKlinikList.clear();
		}

		//Intent intent = getIntent();
		//videoID = intent.getStringExtra("videoID");
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
		textRespon = (TextView) findViewById(R.id.textRespon);
		setEventListener();
		//checkProduct();
		checkSubscriptionList();
		//populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private void populateData() {
		//txt_phone_number.setPrefix(Config.default_phone_prefix);
		//txt_phone_number.setPrefixTextColor(Color.YELLOW);
	}

	private void checkSubscriptionList() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.checkSubsctiptionList(user_model.PhoneNumber, new URLManager.URLCallback() {
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
						//textRespon.setText(response);
						setValidationSubscriptionStatus(response);
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
			subscription_model = new SubscriptionModel(response_model.Result);
			int intNumber=0;
			for (int i = 0;i< subscription_model.list.size();i++){
			//for (int i = subscription_model.list.size()-1 ;i>= 0;i--){
				//if(subscription_model.list.get(i).bank.contains("bca")||va_model.list.get(i).bank.contains("permata")) {
					//daftarPemesanList.add(String.valueOf(intNumber + 1).toString());
					daftarPemesanList.add(subscription_model.list.get(i).title);
					daftarNoHpList.add(subscription_model.list.get(i).end_date);
					daftarNamaKlinikList.add(subscription_model.list.get(i).lock_status);

					intNumber +=1;
				//}
			}

			mListView1_1 = (ListView) findViewById(R.id.listView1_1);
			mListView1_2 = (ListView) findViewById(R.id.listView1_2);
			mListView1_3= (ListView) findViewById(R.id.listView1_3);

			mListView1_1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarPemesanList));
			mListView1_2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarNoHpList));
			mListView1_3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daftarNamaKlinikList));

			ListUtils.setDynamicHeight(mListView1_1);
			ListUtils.setDynamicHeight(mListView1_2);
			ListUtils.setDynamicHeight(mListView1_3);
		}
	}

public static class ListUtils {
	public static void setDynamicHeight(ListView mListView) {
		ListAdapter mListAdapter = mListView.getAdapter();
		if (mListAdapter == null) {
			// when adapter is null
			return;
		}
		int height = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		for (int i = 0; i < mListAdapter.getCount(); i++) {
			View listItem = mListAdapter.getView(i, null, mListView);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			height += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = mListView.getLayoutParams();
		params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
		mListView.setLayoutParams(params);
		mListView.requestLayout();
	}
}

}