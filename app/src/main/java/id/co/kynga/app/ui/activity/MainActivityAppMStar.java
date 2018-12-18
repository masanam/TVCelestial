package id.co.kynga.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Timer;
import java.util.TimerTask;

import id.co.kynga.app.BuildConfig;
import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.MeCallback;
import id.co.kynga.app.general.controller.MenuController;
import id.co.kynga.app.general.controller.RequestController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BannerModel;
import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.CategoryModelAppMStar;
import id.co.kynga.app.model.ContentModel;
import id.co.kynga.app.model.ContentModelAppMStar;
import id.co.kynga.app.model.GameCategoryModel;
import id.co.kynga.app.model.GameModel;
import id.co.kynga.app.model.MeModel;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.TVModel;
import id.co.kynga.app.model.TypeModel;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.model.VersionModel;
import id.co.kynga.app.model.VideoCategoryModel;
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.model.YoutubePlaylistModel;
import id.co.kynga.app.ui.adapter.BannerListAdapter;
import id.co.kynga.app.ui.adapter.BannerListAdapterAppMStar;
import id.co.kynga.app.ui.view.ContentView;
import id.co.kynga.app.ui.view.ContentViewAppMStar;
import id.co.kynga.app.util.UpdateToPlayStoreDialog;

import static id.co.kynga.app.KyngaApp.context;

public class MainActivityAppMStar extends CommonActivity {
	public static MainActivityAppMStar instance;

	private static boolean opened = false;

	private ImageButton btn_menu;
	private DrawerLayout lay_drawer;
	private RelativeLayout lay_menu;
	private ViewPager vwp_banner;
	private LinearLayout lay_banner_loading;
	private LinearLayout lay_main;
	private LinearLayout lay_main_loading;

	private MenuController menu_controller;
	private BannerModelAppMStar banner_model;
	private CategoryModelAppMStar category_model;
	private Timer tmr_banner;
	private Timer tmr_finish;
	private boolean exit_pressed;
	private GameModel game_model;
	private GameCategoryModel game_category_model;
	private static String globalResponse="Not Click Yet";
	private VersionModel version_model;
	private static int versionNumber;

	private Tracker mTracker;
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_main_app_mstar);
		setInitial();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (lay_drawer.isDrawerOpen(lay_menu)) {
				closeDrawer();
			} else {
				finish();
				Intent mpdIntent = new Intent(MainActivityAppMStar.this, FirstActivity_10bubbles.class);
				startActivity(mpdIntent);
				//exit();
			}
			return false;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			doMenu();
			return false;
		}
		return true;
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

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		opened = false;
		closeBannerTimer();
		instance = null;
		super.onDestroy();
	}

	public static boolean isOpened() {
		return opened;
	}

	public void onBannerClicked(final BannerModelAppMStar banner_model) {
		/*
		if (banner_model.Type == TypeModel.NoneType) {
		} else if (banner_model.Type == TypeModel.WebType) {
			AppController.openWebActivity(this, banner_model.Title, banner_model.LinkURL);
			//AppController.openWebActivity(this, banner_model.Title,  "https://dev.playtagg.com/#/mstar/welcome/" + SessionController.getToken());
		} else if (banner_model.Type == TypeModel.VideoType) {
		} else if (banner_model.Type == TypeModel.TVType) {
		} else if (banner_model.Type == TypeModel.YoutubeType) {
		} else if (banner_model.Type == TypeModel.YoutubeChannelType) {
		} else if (banner_model.Type == TypeModel.RadioType) {
		} else if (banner_model.Type == TypeModel.VAVType) {
		}
		*/
	}

	public void showMessage(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GlobalController.showMessage(instance, message);
			}
		});
	}

	public void openDrawer() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lay_drawer.openDrawer(lay_menu);
			}
		});
	}

	public void closeDrawer() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				lay_drawer.closeDrawers();
			}
		});
	}

	private void setInitial() {
/*
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();

		Log.i(TAG, "Setting screen name: " + "MainActivity");
		mTracker.setScreenName("Screen~" + "MainActivity");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		// [END screen_view_hit]
*/
		banner_model = getIntent().getParcelableExtra(BannerModelAppMStar.TAG);
		category_model = getIntent().getParcelableExtra(CategoryModelAppMStar.TAG);

		btn_menu = (ImageButton) findViewById(R.id.btn_menu);
		lay_drawer = (DrawerLayout) findViewById(R.id.lay_drawer);
		lay_menu = (RelativeLayout) findViewById(R.id.lay_menu);
		vwp_banner = (ViewPager) findViewById(R.id.vwp_banner);
		lay_banner_loading = (LinearLayout) findViewById(R.id.lay_banner_loading);
		lay_main = (LinearLayout) findViewById(R.id.lay_main);
		lay_main_loading = (LinearLayout) findViewById(R.id.lay_main_loading);

		setEventListener();
		int width = getResources().getDisplayMetrics().widthPixels / 2;
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) lay_menu.getLayoutParams();
		params.width = width;
		lay_menu.setLayoutParams(params);
		menu_controller = new MenuController();

		if (getIntent().hasExtra(Config.default_notification_message)) {
			AppController.checkMessageAppMStar(getIntent().getStringExtra(Config.default_notification_message));
		}
		populateData();
	}

	private void setEventListener() {
		btn_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doMenu();
			}
		});
		lay_drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				lay_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				lay_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
			}
		});
		/*
		click event on banner is dissabled for temporary
		vwp_banner.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
					if (banner_model != null) {
						openBannerTimer();
					}
				} else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (banner_model != null) {
						closeBannerTimer();
					}
				}
			}
				return false;
		});
		*/
	}

	private void doMenu() {
		if (lay_drawer.isDrawerOpen(lay_menu)) {
			closeDrawer();
		} else {
			openDrawer();
		}
	}

	private void populateData() {
		setBannerLayout();
		setCategoryLayout();
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

	private void setBannerLayout() {
		lay_banner_loading.setVisibility(View.GONE);
		vwp_banner.setAdapter(new BannerListAdapterAppMStar(getSupportFragmentManager(), banner_model.list));
		vwp_banner.setCurrentItem(0);
		openBannerTimer();
	}

	private void setCategoryLayout() {
		lay_main_loading.setVisibility(View.GONE);
		for (final CategoryModelAppMStar category : category_model.list) {
			final ContentViewAppMStar vw_content = new ContentViewAppMStar(this, category, new ContentViewAppMStar.ContentViewCallback() {
				@Override
				public void didContentViewActioned(final ContentModelAppMStar content_model) {
					checkLogin();
					//globalResponse di sini belum terupdate / "Not CLick Yet"???
					//GlobalController.showToast("2  "+globalResponse, 20000);

					int versionCode = BuildConfig.VERSION_CODE;//versioncode aplikasi ini
					if (versionCode < versionNumber){
						showDialogUpdatePlayStore();
					}else if (globalResponse.contains("Imei Is Login") || globalResponse.contains("Not Click Yet") ){
						setContentValidation(content_model);
					}

				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 10, 0, 0);
			vw_content.getView().setLayoutParams(lp);
			lay_main.setHorizontalScrollBarEnabled(false);
			lay_main.addView(vw_content.getView());
		}
	}

	private void setContentValidation(final ContentModelAppMStar content_model) {
		if (content_model.Type == TypeModel.VideoType) {
/*
			Log.i(TAG, "Setting screen name: " + "Video");
			mTracker.setScreenName("Screen~" + "Video");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
*/
			setVideoActivity(content_model);

		} else if (content_model.Type == TypeModel.TVType) {
/*
			Log.i(TAG, "Setting screen name: " + "TV");
			mTracker.setScreenName("Screen~" + "TV");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
*/
			setTVActivity(content_model);
		}
		else if (content_model.Type == TypeModel.YoutubeChannelType) {
			/*
			Log.i(TAG, "Setting screen name: " + "Youtube");
			mTracker.setScreenName("Screen~" + "Youtube");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			*/
			//setYoutubeChannelActivity(content_model);
			Intent intent2 = new Intent(MainActivityAppMStar.this, YoutubeChannelActivityAppMStar_New.class);
			intent2.putExtra("content_model", content_model);
			startActivity(intent2);

		}
		else if (content_model.Type == TypeModel.RadioType) {
			/*
			Log.i(TAG, "Setting screen name: " + "Radio");
			mTracker.setScreenName("Screen~" + "Radio");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			*/
			setRadioActivity(content_model);
		}
		else if (content_model.Type == TypeModel.WebType) {
			/*
			Log.i(TAG, "Setting screen name: " + "WEBview");
			mTracker.setScreenName("Screen~" + "WEBview");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			*/
			setWebActivity(content_model);
		}
		/*else if (content_model.Type == TypeModel.VAVType) {
			Log.i(TAG, "Setting screen name: " + "VAV");
			mTracker.setScreenName("Screen~" + "VAV");
			mTracker.send(new HitBuilders.ScreenViewBuilder().build());
			setVAVActivity(content_model);
		} else {
			GlobalController.showComingSoon(this);
		}
		*/

	}

	private void setTVActivity(final ContentModelAppMStar content_model) {
		//if (content_model.TVCategory.ID == 4 || content_model.TVCategory.ID == 5 || content_model.TVCategory.ID == 6) {
		//IndiaTV == 5
		//KoreaTV == 6
		//CHineseTV==4

		//if (content_model.TVCategory.ID == 4 || content_model.TVCategory.ID == 6 ) {
		if (content_model.TVCategory.TvGroupID == 0 ) {
			GlobalController.showComingSoon(this);
			return;
		}
		GlobalController.showLoading(this);

		if (content_model.TV.ID != 0 ) {
		} else if (content_model.TVCategory.ID != 0) {
			//GlobalController.showToast(String.valueOf(content_model.TVCategory.ID), 20000);
			RequestController.TVCategoryRequestAppMStar(content_model.TVCategory.ID, new RequestController.TVCategoryCallback() {
				@Override
				public void didTVCategorySucceeded(final TVModel tv_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							AppController.openTVActivityAppMStar(instance, tv_model);
							//GlobalController.showToast(String.valueOf(tv_model.list.size()), 20000);
						}
					});
				}

				@Override
				public void didTVCategoryFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} else {
			GlobalController.closeLoading();
			GlobalController.showWarning(this, R.string.error_no_data);
		}

	}

	private void setVideoActivity(final ContentModelAppMStar content_model) {
/*
		GlobalController.showToast(String.valueOf("video.id : "+ content_model.Video.ID) +"\n"+
				"videoGroup.Id : "+
				String.valueOf(content_model.VideoGroup.ID) + "\n"+ "videoCategoryId : "+
				String.valueOf(content_model.VideoCategory.ID), 20000);
*/
		//BlockBuster dan CatcupTV mempunyai content_model.VideoCategory.ID, jadi "Coming Soon"
		//GlobalController.showToast(String.valueOf(content_model.VideoCategory.ID), 20000);
		//GlobalController.showToast(String.valueOf(content_model.VideoCategory.ID), 20000);
		//GlobalController.showToast(String.valueOf(content_model.VideoGroup), 20000);

		//Coming soon for India and Tamil
		/*
		if (content_model.VideoCategory.ID == 1 || content_model.VideoCategory.ID == 2){
			GlobalController.showComingSoon(this);
		return;
		}
		*/
		//GlobalController.showLoading(this);
		if (content_model.Video.ID != 0) {
			/*
			all Video.Id = 0?
			GlobalController.showToast(String.valueOf(content_model.Video.ID) +" "+
					String.valueOf(content_model.VideoGroup.ID) + " "+
					String.valueOf(content_model.VideoCategory.ID), 20000);
			*/
		} else if (content_model.VideoGroup.ID != 0) {
			/*
			GlobalController.showToast(String.valueOf(content_model.Video.ID) +" "+
					String.valueOf(content_model.VideoGroup.ID) + " "+
					String.valueOf(content_model.VideoCategory.ID), 20000);
			*/
			RequestController.VideoGroupRequest(content_model.VideoGroup.ID, new RequestController.VideoGroupCallback() {
				@Override
				public void didVideoGroupSucceeded(final VideoCategoryModel video_category_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							AppController.openVideoGroupActivity(instance, video_category_model);
							//GlobalController.showToast(String.valueOf(video_category_model.list.size()), 20000);
						}
					});
				}

				@Override
				public void didVideoGroupFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} //else if (content_model.VideoCategory.ID != 0) {
		else if (content_model.VideoCategory.ID != 0 && content_model.VideoCategory.VideoGroupID !=0) {
			//Di sini VideoCategory.ID != 0 akan ditampilkan
			/*
			GlobalController.showToast(String.valueOf(content_model.Video.ID) +" "+
					String.valueOf(content_model.VideoGroup.ID) + " "+
					String.valueOf(content_model.VideoCategory.ID), 20000);
			*/
			RequestController.VideoCategoryRequest(content_model.VideoCategory.ID, new RequestController.VideoCategoryCallback() {
				@Override
				public void didVideoCategorySucceeded(final VideoModel video_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							//AppController.openVideoCategoryActivity(instance, video_model, content_model.VideoCategory.Title);

							AppController.openVideoCategoryActivity2(instance, video_model, content_model.VideoCategory.Title
									, String.valueOf(content_model.VideoCategory.ID));

							//GlobalController.showToast(String.valueOf(video_model.list.size()), 20000);
						}
					});
				}

				@Override
				public void didVideoCategoryFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} else {//dulunya untuk
			GlobalController.closeLoading();
			GlobalController.showComingSoon(this);

			/*
			GlobalController.showToast(String.valueOf(content_model.Video.ID) +" "+
					String.valueOf(content_model.VideoGroup.ID) + " "+
					String.valueOf(content_model.VideoCategory.ID), 20000);
			*/
		}
	}

	private void setYoutubeChannelActivity(final ContentModelAppMStar content_model) {

		//final Intent intent = new Intent(MStarApp.getAppContext(), YoutubeChannelActivity.class);
		//intent.putExtra("Title", title);
		//intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
		//startActivity(intent);

		//GlobalController.showToast(content_model.YoutubeChannel.Title.toString(), 20000); live Tv, Video, Infotainemnt, MUSIC & Kid
		//GlobalController.showToast("content_model.Youtube.YoutubeID.toString()", 20000);
		//GlobalController.showComingSoon(this);
/*
		GlobalController.showToast(String.valueOf("Youtube.id : "+ content_model.Youtube.ID) +"\n"+
				"YoutubeChannel.Id : "+
				String.valueOf(content_model.YoutubeChannel.ID), 20000);
*/
		GlobalController.showLoading(this);
		RequestController.YoutubeChannelRequestAppMStar(content_model.YoutubeChannel.ChannelID, new RequestController.YoutubeChannelCallback() {
			@Override
			public void didYoutubeChannelSucceeded(final YoutubePlaylistModel youtube_playlist_model) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//GlobalController.showToast(String.valueOf(youtube_playlist_model.list.size()), 20000);
						AppController.openYoutubeChannelActivityAppMStar(instance, content_model.YoutubeChannel.Title, youtube_playlist_model);
						//GlobalController.closeLoading(); --> closed at next class

						//final Intent intent = new Intent(MainActivity.instance, YoutubeChannelActivityAppMStar.class);
						//intent.putExtra("Title", content_model.YoutubeChannel.Title);
						//intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
						//intent.putExtra(CategoryModel.TAG, category_model);
						//startActivity(intent);
					}
				});
				//GlobalController.closeLoading();
			}

			@Override
			public void didYoutubeChannelFailed() {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
					}
				});
			}
		});

	}

	private void setRadioActivity(final ContentModelAppMStar content_model) {
		//GlobalController.showComingSoon(this);

		GlobalController.showLoading(this);
		if (content_model.Radio.ID != 0) {
		} else if (content_model.RadioCategory.ID != 0) {
			RequestController.RadioCategoryRequestAppMStar(content_model.RadioCategory.ID, new RequestController.RadioCategoryCallback() {
				@Override
				public void didRadioCategorySucceeded(final RadioModel radio_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							AppController.openRadioActivityAppMStar(instance, radio_model);
						}
					});
				}

				@Override
				public void didRadioCategoryFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} else {
			GlobalController.closeLoading();
			GlobalController.showWarning(this, R.string.error_no_data);
		}
	}

	private void setWebActivity(final ContentModelAppMStar content_model) {
		//GlobalController.showComingSoon(this);
		//AppController.openWebActivity(instance, content_model.Title, content_model.LinkURL);
		//AppController.openWebActivity(instance, content_model.Title, "https://dev.playtagg.com/#/mstar/welcome/" + SessionController.getToken());

		if(content_model.LinkURL.contains("playtagg.com")) {
			AppController.openWebActivityAppMStar(instance, content_model.Title, content_model.LinkURL + SessionController.getToken());
		}else{

			if(content_model.Title.contains("Games")){
				checkGame();
				//setGameActivity(content_model);

			}
			//AppController.openWebActivity(instance, content_model.Title, content_model.LinkURL);
		}

		//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
		//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content_model.LinkURL + SessionController.getToken()));
		//startActivity(browserIntent);
	}

	private void checkGame() {
		final UserModel user_model = SessionControllerAppMStar.getUser();
		GlobalController.showLoading(this);
		//GlobalController.showToast(user_model.PhoneNumber, 20000);

		URLController.checkGameAppMStar(user_model.PhoneNumber, new URLManager.URLCallback() {
			//URLController.checkGame(new URLManager.URLCallback() {
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
						setGameValidation(response);
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

	private void setGameValidation(final String response) { //convert response to model
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			//banner_model = new BannerModel(response_model.Result);
			//game_model = new GameModel(response_model.Result);
			game_category_model = new GameCategoryModel((response_model.Result));
			//game_model = new GameModel((response_model.Result));
			//GlobalController.showToast(va_model.list.get(0).va_number, 1000);
			//AppController.openVAStatusReadActivity(instance, banner_model);

			//	GlobalController.showToast(game_model.list.size());
			//AppController.openGameActivity(instance, game_model);
			AppController.openGameActivity(instance, game_category_model);
			//finish();

		}
	}

	private void setCategoryLayoutGame() {
		lay_main_loading.setVisibility(View.GONE);
		for (final CategoryModelAppMStar category : category_model.list) {
			final ContentViewAppMStar vw_content = new ContentViewAppMStar(this, category, new ContentViewAppMStar.ContentViewCallback() {
				@Override
				public void didContentViewActioned(final ContentModelAppMStar content_model) {
					checkLogin();
					//globalResponse di sini belum terupdate / "Not CLick Yet"???
					//GlobalController.showToast("2  "+globalResponse, 20000);
					if(globalResponse.contains("Imei Is Login") || globalResponse.contains("Not Click Yet")  ){
							setContentValidation(content_model);
					}
					//if(globalResponse.contains("Imei Is Login") ){
					//setContentValidation(content_model);
					//}
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 10, 0, 0);
			vw_content.getView().setLayoutParams(lp);
			lay_main.setHorizontalScrollBarEnabled(false);
			lay_main.addView(vw_content.getView());
		}
	}

	private void setVAVActivity(final ContentModelAppMStar content_model) {
		GlobalController.showComingSoon(this);
	}








/*
	private void setTVActivity(final ContentModel content_model) {
		//if (content_model.TVCategory.ID == 4 || content_model.TVCategory.ID == 5 || content_model.TVCategory.ID == 6) {
		//IndiaTV == 5
		//KoreaTV == 6
		//CHineseTV==4

		//if (content_model.TVCategory.ID == 4 || content_model.TVCategory.ID == 6 ) {
		if (content_model.TVCategory.TvGroupID == 0 ) {
			GlobalController.showComingSoon(this);
			return;
		}
		GlobalController.showLoading(this);
		if (content_model.TV.ID != 0 ) {
		} else if (content_model.TVCategory.ID != 0) {
			//GlobalController.showToast(String.valueOf(content_model.TVCategory.ID), 20000);
			RequestController.TVCategoryRequest(content_model.TVCategory.ID, new RequestController.TVCategoryCallback() {
				@Override
				public void didTVCategorySucceeded(final TVModel tv_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							AppController.openTVActivity(instance, tv_model);
							//GlobalController.showToast(tv_model.Title.toString(), 20000);
						}
					});
				}

				@Override
				public void didTVCategoryFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} else {
			GlobalController.closeLoading();
			GlobalController.showWarning(this, R.string.error_no_data);
		}
	}
	*/
/*
	private void setYoutubeChannelActivity(final ContentModel content_model) {

		//final Intent intent = new Intent(MStarApp.getAppContext(), YoutubeChannelActivity.class);
		//intent.putExtra("Title", title);
		//intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
		//startActivity(intent);

		//GlobalController.showToast(content_model.YoutubeChannel.Title.toString(), 20000); live Tv, Video, Infotainemnt, MUSIC & Kid
		//GlobalController.showToast("content_model.Youtube.YoutubeID.toString()", 20000);
		//GlobalController.showComingSoon(this);

		GlobalController.showLoading(this);
		RequestController.YoutubeChannelRequest(content_model.YoutubeChannel.ChannelID, new RequestController.YoutubeChannelCallback() {
			@Override
			public void didYoutubeChannelSucceeded(final YoutubePlaylistModel youtube_playlist_model) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {

						AppController.openYoutubeChannelActivity(instance, content_model.YoutubeChannel.Title, youtube_playlist_model);
						GlobalController.closeLoading();
						final Intent intent = new Intent(MainActivity.instance, YoutubeChannelActivity.class);
						//intent.putExtra("Title", content_model.YoutubeChannel.Title);
						//intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
						//intent.putExtra(CategoryModel.TAG, category_model);
						//startActivity(intent);
						}
				});
				//GlobalController.closeLoading();
			}

			@Override
			public void didYoutubeChannelFailed() {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
					}
				});
			}
		});

	}
	*/
/*
	private void setRadioActivity(final ContentModel content_model) {
		//GlobalController.showComingSoon(this);

		GlobalController.showLoading(this);
		if (content_model.Radio.ID != 0) {
		} else if (content_model.RadioCategory.ID != 0) {
			RequestController.RadioCategoryRequest(content_model.RadioCategory.ID, new RequestController.RadioCategoryCallback() {
				@Override
				public void didRadioCategorySucceeded(final RadioModel radio_model) {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
							AppController.openRadioActivity(instance, radio_model);
						}
					});
				}

				@Override
				public void didRadioCategoryFailed() {
					if (instance == null) {
						return;
					}
					instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							GlobalController.closeLoading();
						}
					});
				}
			});
		} else {
			GlobalController.closeLoading();
			GlobalController.showWarning(this, R.string.error_no_data);
		}
	}
	*/

	private void openBannerTimer() {
		tmr_banner = new Timer();
		tmr_banner.schedule(new OnBannerTick(), Config.default_banner_timer, Config.default_banner_timer);
	}

	private void closeBannerTimer() {
		if (tmr_banner != null) {
			tmr_banner.cancel();
			tmr_banner = null;
		}
	}

	private class OnBannerTick extends TimerTask {
		@Override
		public void run() {
			if (instance == null) {
				return;
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (vwp_banner.getCurrentItem() == (banner_model.list.size() - 1)) {
						vwp_banner.setCurrentItem(0);
					} else {
						vwp_banner.setCurrentItem(vwp_banner.getCurrentItem() + 1, true);
					}
					//GlobalController.showToast(banner_model.list.get(vwp_banner.getCurrentItem()).ImageURL, 20000);
				}
			});
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

	private void checkLogin() {

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String IMEI = telephonyManager.getDeviceId();
		GlobalController.showLoading(this);

		URLController.checkLoginAppMStar(IMEI, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, final String response) {
				if (instance == null) {
					return;
				}
				instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						GlobalController.closeLoading();
						//setValidation(response, phone);
						globalResponse = response;
						if (response.contains("Imei Is Login")){
							//GlobalController.showToast(globalResponse, 20000);
							//tujuannya agar bisa langsung pakai app
							//AppController.checkSession(instance, true);
							//Menonaktifkan sementara checkVersion untuk playstore checkVersionApp();
						}
						if (response.contains("Imei Not Found")){
							SessionController.close();
							final Intent intent = new Intent(context, LoginActivity_AppMStar.class);
							startActivity(intent);
							finish();
						}
						if (response.contains("Please Login")){
							SessionController.close();
							final Intent intent = new Intent(context, LoginActivity_AppMStar.class);
							startActivity(intent);
							finish();
						}
						//finish();
						//GlobalController.showToast(globalResponse, 20000);
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
						setValidationVersion(response);
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
			//GlobalController.showToast(String.valueOf(versionNumber), 20000);
		}
	}

	public void showDialogUpdatePlayStore(){
		UpdateToPlayStoreDialog updateToPlayStoreDialog = new UpdateToPlayStoreDialog(this);
		updateToPlayStoreDialog.setListener(new UpdateToPlayStoreDialog.ResumeListener() {
			@Override
			public void onRestart() {
				finish();
			}

			@Override
			public void onResume() {
				//THe idea is to logout then go to Play Store
				//AppController.signout(instance);
				updateToPlayStore();
			}
		});
		updateToPlayStoreDialog.show();
	}

	private void updateToPlayStore () {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("market://details?id=" + getPackageName()));
		startActivity(i);
	}

}