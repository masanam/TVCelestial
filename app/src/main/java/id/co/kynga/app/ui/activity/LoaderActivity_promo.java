package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BannerModel;
import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.CategoryModelAppMStar;
import id.co.kynga.app.model.ResponseModel;


public class LoaderActivity_promo extends CommonActivity {
	public static LoaderActivity_promo instance;

	private Timer tmr_check;
	private Timer tmr_finish;
	private boolean exit_pressed;
	private BannerModelAppMStar banner_model;
	private CategoryModelAppMStar category_model;
	private ImageView imgCilok, imgTutup;
	private int file_size=0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_promo_banner);
		imgCilok = (ImageView) findViewById(R.id.Imageprev);
		//imgCilok.setBackgroundResource(R.drawable.promoisi);

		imgTutup = (ImageView) findViewById(R.id.ImageTutup);
		imgTutup.setBackgroundResource(R.drawable.tutup);
		imgTutup.setVisibility(View.GONE);
		setInitial();
	}
/*
	@Override
	protected void onResume() {
		super.onResume();
		AppController.refreshMe(new MeCallback() {
			@Override
			public void onSuccess(MeModel me_model) {
			}

			@Override
			public void onFailure() {
				AppController.signout(MainActivity.instance);
			}
		});
		if (!RequestActivity.isAllPermitted()) {
			AppController.openRequestActivity(this);
		}
	}
	*/

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
		cancelCheck();
		instance = null;
		super.onDestroy();
	}

	private void setInitial() {
		setBannerPromoRequest();
		//setCategoryRequest();
		//check();
	}

	private void setBannerPromoRequest() {
		URLController.bannerpromoAppMStar(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(final int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//GlobalController.showToast(response, 1000);
						//imgCilok.setImageURI(response.));

						if(response.contains("No data")) {
							Intent intent = new Intent(getApplicationContext(), LoaderActivity.class);
							startActivity(intent);
							finish();

						}

						if(response.contains("Sorry your token is not valid")) {
							AppController.openLoginActivityAppMStar(instance);
							finish();

						}
						if(response.contains("http://")) {
							imgTutup.setVisibility(View.VISIBLE);
							setBannerValidation(response);

						}

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
				//setBannerPromoRequest();
			}
		});
	}

	public void tutupSplash(View view) {
		/*
		// Do something in response to button
		//handler.removeMessages(0);
		Intent intent = new Intent(this, LoaderActivity.class);
		//  intent.putExtra(EXTRA_MESSAGEbahasa, bahasa);
		startActivity(intent);
		finish();
		*/
	}

	private void setCategoryRequest() {
		URLController.category(new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setCategoryValidation(response);
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
			//imgCilok.setImageURI(Uri.parse("http://cdn.mstar.co.id/banner-image/1920x1080-AMAZINGRACE(mstar-web&mobile-apps).jpg"));
			//imgCilok.setImageBitmap(BitmapFactory.decodeStream(new URL("http://cdn.mstar.co.id/banner-image/1920x1080-AMAZINGRACE(mstar-web&mobile-apps).jpg").openConnection().getInputStream()));
			//GlobalController.showToast(banner_model.list.get(0).ImageURL, 1000);
			// Create an object for subclass of AsyncTask


			// Execute the task
			//task.execute(new String[] {"http://cdn.mstar.co.id/banner-image/1920x1080-AMAZINGRACE(mstar-web&mobile-apps).jpg"});
			//if (banner_model.list.get(0).ImageURL.contains("http://")) {
				GetXMLTask task = new GetXMLTask();
				task.execute(banner_model.list.get(0).ImageURL);
				//GlobalController.showToast(String.valueOf(file_size), 1000);
			/*
			}else{
				Intent intent = new Intent(this, LoaderActivity.class);
				startActivity(intent);
				finish();
			}
		*/
		}
	}

	private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url);
			}
			return map;
		}

		// Sets the Bitmap returned by doInBackground
		@Override
		protected void onPostExecute(Bitmap result) {
			//GlobalController.showToast(String.valueOf(sizeOf(result)), 1000);
			if (sizeOf(result) < 10000000) {
				imgCilok.setImageBitmap(result);
			}else{
				/*
				Intent intent = new Intent(getApplicationContext(), LoaderActivity.class);
				startActivity(intent);
				finish();*/
			}


			//Bitmap resizedBitmap = Bitmap.createScaledBitmap(result, 100, 100, false);
			//imgCilok.setImageBitmap(resizedBitmap);
		}

		protected int sizeOf(Bitmap data) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
				return data.getRowBytes() * data.getHeight();
			} else {
				return data.getByteCount();
			}
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;
			//mengecilkan ukuran
			/*
			bmOptions.inJustDecodeBounds = true;
			bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bmOptions.inDither = true;

			//bmOptions.inSampleSize=2;
			*/
			//bmOptions.inSampleSize = calculateInSampleSize(bmOptions,  100,100);


			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.
						decodeStream(stream, null, bmOptions);
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		// Makes HttpURLConnection and returns InputStream
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
					//file_size = httpConnection.getContentLength();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
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