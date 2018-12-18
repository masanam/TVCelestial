package id.co.kynga.app.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.MeCallback;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.CategoryModelAppMStar;
import id.co.kynga.app.model.MeModel;
import id.co.kynga.app.model.ResponseModel;

public class LoaderActivity extends CommonActivity {
	public static LoaderActivity instance;

	private Timer tmr_check;
	private Timer tmr_finish;
	private boolean exit_pressed;
	private BannerModelAppMStar banner_model;
	private CategoryModelAppMStar category_model;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_loader);
		setInitial();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppController.refreshMeAppMStar(new MeCallback() {
			@Override
			public void onSuccess(MeModel me_model) {
			}

			@Override
			public void onFailure() {
				AppController.signoutAppMStar(MainActivityAppMStar.instance);
			}
		});
		if (!RequestActivity.isAllPermitted()) {
			AppController.openRequestActivity(this);
		}
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
		cancelCheck();
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		setBannerRequest();
		setCategoryRequest();
		check();
	}

	private void setBannerRequest() {
		URLController.bannerAppMStar(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(final int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//GlobalController.showToast(response, 1000);
						setBannerValidation(response);
					}
				});
			}

			@Override
			public void didURLFailed() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				setBannerRequest();
			}
		});
	}

	private void setCategoryRequest() {
		URLController.categoryAppMStar(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setCategoryValidation(response);
						//GlobalController.showToast(response, 1000);
					}

				});
			}

			@Override
			public void didURLFailed() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				setCategoryRequest();
			}
		});
	}

	private void setBannerValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			banner_model = new BannerModelAppMStar(response_model.Result);
			//GlobalController.showToast(banner_model.list.get(0).ImageURL, 1000);
		}
	}

	private void setCategoryValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			category_model = new CategoryModelAppMStar(response_model.Result);
		}
	}

	private void check() {
		tmr_check = new Timer();
		tmr_check.schedule(new CheckTimer(), 2000, 2000);
	}

	private void cancelCheck() {
		if (tmr_check != null) {
			tmr_check.cancel();
			tmr_check = null;
		}
	}

	private void exit() {
		if (!exit_pressed) {
			exit_pressed = true;
			tmr_finish = new Timer();
			tmr_finish.schedule(new FinishTimer(), 2000, 2000);
			GlobalController.showToast(GlobalController.getString(R.string.message_exit), 1000);
		} else {
			finish();
		}
	}

	private void setSuccess() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AppController.openMainActivityAppMStar(instance, banner_model, category_model);
				finish();
			}
		});
	}

	private class CheckTimer extends TimerTask {
		@Override
		public void run() {
			if (banner_model != null && category_model != null) {
				tmr_check.cancel();
				tmr_check = null;
				setSuccess();
			}
		}
	}

	private class FinishTimer extends TimerTask {
		@Override
		public void run() {
			if (exit_pressed) {
				exit_pressed = !exit_pressed;
				tmr_finish.cancel();
				tmr_finish = null;
			}
		}
	}
}