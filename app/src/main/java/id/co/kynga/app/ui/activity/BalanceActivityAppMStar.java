package id.co.kynga.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.PackageDataModel;
import id.co.kynga.app.model.ResponseModel2;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.ui.adapter.PackageDataListAdapter;


public class BalanceActivityAppMStar extends CommonActivity {
	public static BalanceActivityAppMStar instance;

	private ImageButton btn_back;
	private TextView lbl_credit;
	private SwipeRefreshLayout lay_refresh;
	private GridView gvw_main;
	private PackageDataListAdapter package_data_list_adapter;

	private ArrayList<PackageDataModel> package_data_list;
	private TextView textRespon;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		this.overridePendingTransition(R.anim.slide_in_up, R.anim.hold);
		setContentView(R.layout.activity_balance);
		setInitial();
	}

	@Override
	protected void onPause() {
		this.overridePendingTransition(R.anim.hold, R.anim.slide_out_down);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lay_refresh = (SwipeRefreshLayout) findViewById(R.id.lay_refresh);
		lbl_credit = (TextView) findViewById(R.id.lbl_credit);
		gvw_main = (GridView) findViewById(R.id.gvw_main);
		textRespon = (TextView) findViewById(R.id.textRespon);
		setEventListener();
		populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		lay_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateData();
			}
		});
		gvw_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				doSelect(position);
			}
		});
	}

	private void doSelect(final int index) {
//		final PackageDataModel package_data_model = package_data_list.get(index);
//		AppController.openTopupSummaryActivity(this, package_data_model);
	}

	private void populateData() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		//lbl_credit.setText(NumberController.formatCurrency(user_model.Credit));

		setRequest();
	}

	private void setRequest() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		if (!lay_refresh.isRefreshing()) {
			lay_refresh.setRefreshing(true);
		}
		//GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);
		URLController.balance(user_model.PhoneNumber, new URLManager.URLCallback() {
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
						long intResponse = Long.valueOf(response);
						String strResponse2 = NumberFormat.getNumberInstance(Locale.US).format(intResponse);
						lbl_credit.setText("Credit: Rp. "+strResponse2);
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
						//setValidation(response, card);
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

		instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lay_refresh.setRefreshing(false);
			}
		});

		/*
		URLController.packageData(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						lay_refresh.setRefreshing(false);
						setValidation(response);
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
						lay_refresh.setRefreshing(false);
					}
				});
			}
		});
		*/

	}

	private void setValidation(final String response) {
		final ResponseModel2 response_model = new ResponseModel2(response);
		//GlobalController.showToast(response_model.Status.toString(), 20000);
		if (response_model.Status == ResponseModel2.StatusType.SucceededStatusType) {
			final PackageDataModel package_data_model = new PackageDataModel(response_model.Result);
			package_data_list = new ArrayList<>(package_data_model.list);
			package_data_list_adapter = new PackageDataListAdapter(package_data_list);
			gvw_main.setAdapter(package_data_list_adapter);
		} else if (response_model.Status == ResponseModel2.StatusType.FailedStatusType) {
		} else if (response_model.Status == ResponseModel2.StatusType.ExpiredStatusType) {
		}
	}
}