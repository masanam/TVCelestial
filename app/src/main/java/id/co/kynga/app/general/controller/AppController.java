package id.co.kynga.app.general.controller;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.demo.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.model.BubblePackageCategoryModel;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.model.CategoryModelAppMStar;
import id.co.kynga.app.model.GameCategoryModel;
import id.co.kynga.app.model.InactivePackageCategoryModel;
import id.co.kynga.app.model.MeModel;
import id.co.kynga.app.model.MyAccountModel;
import id.co.kynga.app.model.PackageDataModel;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.model.RegisterModel2;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.TVModel;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.model.VAModel;
import id.co.kynga.app.model.VideoCategoryModel;
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.model.YoutubeModel;
import id.co.kynga.app.model.YoutubePlaylistModel;
import id.co.kynga.app.model.pojo.SubCategory;
import id.co.kynga.app.ui.activity.BalanceActivityAppMStar;
import id.co.kynga.app.ui.activity.ChooseBoxOrMobileActivity;
import id.co.kynga.app.ui.activity.ChoosePayMethodActivity;
import id.co.kynga.app.ui.activity.FAQActivity;
import id.co.kynga.app.ui.activity.FirstActivity0_new_10bubbles;
import id.co.kynga.app.ui.activity.FirstActivity_10bubbles;
import id.co.kynga.app.ui.activity.GameActivity;
import id.co.kynga.app.ui.activity.LoaderActivity_promo;
import id.co.kynga.app.ui.activity.LoginActivity_AppMStar;
import id.co.kynga.app.ui.activity.LoginActivity_new2;
import id.co.kynga.app.ui.activity.LogoutActivityAppMStar;
import id.co.kynga.app.ui.activity.MainActivityAppMStar;
import id.co.kynga.app.ui.activity.MyAccountActivity;
import id.co.kynga.app.ui.activity.LoginActivity_new;
import id.co.kynga.app.ui.activity.PackageActivity;
import id.co.kynga.app.ui.activity.PolicyActivityAppMStar;
import id.co.kynga.app.ui.activity.ProfileActivity;
import id.co.kynga.app.ui.activity.RadioActivity2AppMStar;
import id.co.kynga.app.ui.activity.RegisterActivity_AppMStar;
import id.co.kynga.app.ui.activity.RegisterActivity_cc;
import id.co.kynga.app.ui.activity.RegisterActivity_cc_Edit;
import id.co.kynga.app.ui.activity.RegisterActivity_cc_Edit_confirm;
import id.co.kynga.app.ui.activity.RegisterActivity_new;
import id.co.kynga.app.ui.activity.RegisterActivity_new2_box;
import id.co.kynga.app.ui.activity.RequestActivity;
import id.co.kynga.app.ui.activity.SplashActivityAppMStar;
import id.co.kynga.app.ui.activity.SubscriptionFromPlayVideoActivity;
import id.co.kynga.app.ui.activity.SubscriptionListActivity;
import id.co.kynga.app.ui.activity.SummaryActivityAppMStar;
import id.co.kynga.app.ui.activity.TCActivity;
import id.co.kynga.app.ui.activity.TVActivityAppMStar;
import id.co.kynga.app.ui.activity.TopupActivity_ewallet;
import id.co.kynga.app.ui.activity.TopupActivity_page1;
import id.co.kynga.app.ui.activity.VAstatusBCA_Permata;
import id.co.kynga.app.ui.activity.VAstatusMandiri;
import id.co.kynga.app.ui.activity.VerificationActivity2;
import id.co.kynga.app.ui.activity.VerificationActivity2AppMStar;
import id.co.kynga.app.ui.activity.VideoCategoryActivityAppMStar;
import id.co.kynga.app.ui.activity.VideoGroupActivityAppMStar;
import id.co.kynga.app.ui.activity.WebActivity;
import id.co.kynga.app.ui.activity.WebActivityAppMStar;
import id.co.kynga.app.ui.activity.YoutubeActivity;
import id.co.kynga.app.ui.activity.YoutubeActivityAppMStar;
import id.co.kynga.app.ui.activity.YoutubeChannelActivityAppMStar;
import id.co.kynga.app.util.DownloadService;

import static id.co.kynga.app.KyngaApp.context;
import static id.co.kynga.app.ui.activity.LoaderActivity.instance;

public class AppController {

    public static void openVAStatusBCA_Permata(
            final Activity activity,
            //final BannerModel banner_model){
            final VAModel va_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VAstatusBCA_Permata.class);
        //intent.putExtra(BannerModel.TAG, va_model);
        intent.putExtra(VAModel.TAG, va_model);
        //intent.putExtra("TEST", "test");
        //GlobalController.showToast(va_model.list.get(0).va_number, 1000);
        activity.startActivity(intent);
    }

    public static void openVAStatusMandiri(
            final Activity activity,
            //final BannerModel banner_model){
            final VAModel va_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VAstatusMandiri.class);
        //intent.putExtra(BannerModel.TAG, va_model);
        intent.putExtra(VAModel.TAG, va_model);
        //intent.putExtra("TEST", "test");
        //GlobalController.showToast(va_model.list.get(0).va_number, 1000);
        activity.startActivity(intent);
    }

    public static void openBalanceActivityAppMStar(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, BalanceActivityAppMStar.class);
        //final Intent intent = new Intent(MStarApp.getAppContext(), BalanceActivity.class);
        activity.startActivity(intent);
    }

    public static void openFAQActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, FAQActivity.class);
        activity.startActivity(intent);
    }

    public static void openTCActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, TCActivity.class);
        activity.startActivity(intent);
    }
    public static void openPolicyActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, PolicyActivityAppMStar.class);
        activity.startActivity(intent);
    }

    public static void openProfileActivityAppMStar(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, ProfileActivity.class);
        activity.startActivity(intent);
    }


    public static void openWebActivityAppMStar(
            final Activity activity,
            final String title,
            final String url) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, WebActivityAppMStar.class);
        intent.putExtra("Title", title);
        intent.putExtra("URL", url);
        //GlobalController.showToast(url, 1000);
        activity.startActivityForResult(intent, Config.default_web_request_code);

    }

    public static void openWebActivity(
            final Activity activity,
            final String url) {
        final Intent intent = new Intent(KyngaApp.getContext(), WebActivity.class);
        intent.putExtra("URL", url);
        activity.startActivity(intent);
    }

    public static void openChooseBoxOrMobileActivity(final Activity activity) {
        final Intent intent = new Intent(KyngaApp.getContext(), ChooseBoxOrMobileActivity.class);
        activity.startActivity(intent);
    }

    public static void openRequestActivity(final Activity activity) {
        final Intent intent = new Intent(KyngaApp.getContext(), RequestActivity.class);
        activity.startActivity(intent);
    }
    public static void openFirstActivity0_new(final Activity activity) {
        final Intent intent = new Intent(KyngaApp.getContext(), FirstActivity0_new_10bubbles.class);
        activity.startActivity(intent);
    }

    public static void openSplashActivityAppMStar(final Activity activity) {
        final Intent intent = new Intent(KyngaApp.getContext(), SplashActivityAppMStar.class);
        activity.startActivity(intent);
    }

    public static void openGameActivity(
            final Activity activity,
            //final String title, //Games
            final GameCategoryModel game_category_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("Title", "Game");
        intent.putExtra(GameCategoryModel.TAG, game_category_model);
        activity.startActivity(intent);
    }


    /*
        public static void openLandActivity(final Activity activity) {
            final Intent intent = new Intent(KyngaApp.getContext(), LandActivity.class);
            activity.startActivity(intent);
        }
    */

    public static void checkMessageAppMStar(final String message) {
    }

    public static void refreshMeAppMStar() {
        AppController.refreshMeAppMStar(null);
    }

    public static void refreshMeAppMStar(final MeCallback me_callback) {
        if (SessionController.isSession()) {
            URLController.meAppMStar(new URLManager.URLCallback() {
                @Override
                public void didURLSucceeded(int status_code, String response) {
                    final ResponseModel response_model = new ResponseModel(response);
                    if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
                        final MeModel me_model = new MeModel(response_model.Result);
                        boolean updated = false;
                        final UserModel user_model = SessionController.getUser();
                        if (!user_model.PhoneNumber.equals(me_model.PhoneNumber)) {
                            user_model.PhoneNumber = me_model.PhoneNumber;
                            updated = true;
                        }
                        if (!user_model.Fullname.equals(me_model.Fullname)) {
                            user_model.Fullname = me_model.Fullname;
                            updated = true;
                        }
                        if (!user_model.Email.equals(me_model.Email)) {
                            user_model.Email = me_model.Email;
                            updated = true;
                        }
                        if (!user_model.VANumber.equals(me_model.VANumber)) {
                            user_model.VANumber = me_model.VANumber;
                            updated = true;
                        }
                        if (user_model.Credit != me_model.Credit) {
                            user_model.Credit = me_model.Credit;
                            updated = true;
                        }
                        if (updated) {
                            SessionController.update(user_model);
                        }
                        if (me_callback != null) {
                            me_callback.onSuccess(me_model);
                        }
                    } else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
                        if (me_callback != null) {
                            me_callback.onFailure();
                        }
                    } else if (response_model.Status == ResponseModel.StatusType.ExpiredStatusType) {
                        if (me_callback != null) {
                            me_callback.onFailure();
                        }
                    }
                }

                @Override
                public void didURLFailed() {
                }
            });
        } else {
            AppController.signoutAppMStar(instance);
        }
    }

    public static void openSummaryActivity2(
            final Activity activity,
            final String title,
            final VideoModel video_model,
            final String videoCategoryId,
            final String clickOrigin) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, SummaryActivityAppMStar.class);
        intent.putExtra("Title", title);
        intent.putExtra(VideoModel.TAG, video_model);
        intent.putExtra("videoCategoryId", videoCategoryId);
        intent.putExtra("clickOrigin", clickOrigin);
        activity.startActivity(intent);
    }

    public static void openVideoCategoryActivity2_2(
            final Activity activity,
            final VideoModel video_model,
            final String title,
            final String videoCategoryId,
            final String clickOrigin){
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VideoCategoryActivityAppMStar.class);
        intent.putExtra(VideoModel.TAG, video_model);
        intent.putExtra("Title", title);
        intent.putExtra("videoCategoryId", videoCategoryId);
        intent.putExtra("clickOrigin", clickOrigin);
        activity.startActivity(intent);
    }

    public static void openVideoCategoryActivity2(
            final Activity activity,
            final VideoModel video_model,
            final String title,
            final String videoCategoryId){
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VideoCategoryActivityAppMStar.class);
        intent.putExtra(VideoModel.TAG, video_model);
        intent.putExtra("Title", title);
        intent.putExtra("videoCategoryId", videoCategoryId);
        activity.startActivity(intent);
    }

    public static void openSubscription(
            final Activity activity,
            final String videoID) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, SubscriptionFromPlayVideoActivity.class);
        intent.putExtra("videoID", videoID);
        activity.startActivity(intent);
    }

    public static void openRadioActivityAppMStar(
            final Activity activity,
            final RadioModel radio_model) {
        if (activity == null) {
            return;
        }
        Log.i("New Media Player","New Media Player");
        final Intent intent2 = new Intent(context, RadioActivity2AppMStar.class);
        intent2.putExtra(RadioModel.TAG, radio_model);
        activity.startActivity(intent2);
        //final Intent intent = new Intent(MStarApp.getAppContext(), RadioActivity.class);
		/*final Intent intent = new Intent(MStarApp.getAppContext(), RadioActivity2.class);
		intent.putExtra(RadioModel.TAG, radio_model);
		activity.startActivity(intent); */

		/* pilihan media player
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 23) {
			Log.i("Old Media Player","Old Media Player");
			final Intent intent = new Intent(MStarApp.getAppContext(), RadioActivity2_pref.class);
			intent.putExtra(RadioModel.TAG, radio_model);
			activity.startActivity(intent);
		} else {
			Log.i("New Media Player","New Media Player");
			final Intent intent2 = new Intent(MStarApp.getAppContext(), RadioActivity2.class);
			intent2.putExtra(RadioModel.TAG, radio_model);
			activity.startActivity(intent2);
		}
		*/
    }

    public static void openSubscriptionListActivity(final Activity activity) {
        if (instance == null) {
            return;
        };
        //GlobalController.showToast("Signout", 1000);
        final Intent intent = new Intent(context, SubscriptionListActivity.class);
        activity.startActivity(intent);
    }

    public static void openYoutubeActivity(
            final Activity activity,
            final YoutubePlaylistModel youtube_playlist_model,
            final YoutubeModel youtube_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, YoutubeActivityAppMStar.class);
        intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
        intent.putExtra(YoutubeModel.TAG, youtube_model);
        activity.startActivity(intent);
    }

    public static void openYoutubeChannelActivityAppMStar(
            final Activity activity,
            final String title,
            final YoutubePlaylistModel youtube_playlist_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, YoutubeChannelActivityAppMStar.class);
        intent.putExtra("Title", title);
        intent.putExtra(YoutubePlaylistModel.TAG, youtube_playlist_model);
        activity.startActivity(intent);
    }

    public static void openVideoActivity(
            final Activity activity,
            final VideoModel video_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, PlayerActivity.class);
        //intent.putExtra(VideoModel.TAG, video_model);
        intent.setData(Uri.parse(video_model.LinkURL));
        intent.putExtra(PlayerActivity.EXTENSION_EXTRA, "");
        intent.setAction(PlayerActivity.ACTION_VIEW);
        intent.putExtra("subscription", "pay");
        activity.startActivity(intent);
    }

    public static void openVideoGroupActivity(
            final Activity activity,
            final VideoCategoryModel video_category_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VideoGroupActivityAppMStar.class);
        intent.putExtra(VideoCategoryModel.TAG, video_category_model);
        activity.startActivity(intent);
    }

    public static void openVideoCategoryActivity(
            final Activity activity,
            final VideoModel video_model,
            final String title) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, VideoCategoryActivityAppMStar.class);
        intent.putExtra(VideoModel.TAG, video_model);
        intent.putExtra("Title", title);
        activity.startActivity(intent);
    }


    public static void openTVActivityAppMStar(
            final Activity activity,
            final TVModel tv_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, TVActivityAppMStar.class);
        intent.putExtra(TVModel.TAG, tv_model);
        activity.startActivity(intent);
    }

    public static void openMainActivityAppMStar(
            final Activity activity,
            final BannerModelAppMStar banner_model,
            final CategoryModelAppMStar category_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, MainActivityAppMStar.class);
        intent.putExtra(BannerModelAppMStar.TAG, banner_model);
        intent.putExtra(CategoryModelAppMStar.TAG, category_model);
        activity.startActivity(intent);
    }

    public static void openRegisterCcActivity(final Activity activity) {
        Intent start = new Intent(KyngaApp.getContext(), RegisterActivity_cc.class);
        activity.startActivity(start);
    }

    public static void openRegisterActivity_cc_Edit(final Activity activity) {
        Intent start = new Intent(KyngaApp.getContext(), RegisterActivity_cc_Edit.class);
        activity.startActivity(start);
    }

    public static void startDownloadService(final Activity activity) {
        Intent intent = new Intent(KyngaApp.getContext(), DownloadService.class);
        activity.startService(intent);
    }

    public static void openFirstActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, FirstActivity_10bubbles.class);
        activity.startActivity(intent);
    }

    public static void openWelcomeActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(context, WelcomeActivity.class); --> takeout welcome page!
        final Intent intent = new Intent(context, FirstActivity_10bubbles.class);
        activity.startActivity(intent);
    }



    public static void openWelcomeActivityAppMStar(final Activity activity) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(context, WelcomeActivity.class); --> takeout welcome page!
        final Intent intent = new Intent(context, FirstActivity_10bubbles.class);
        activity.startActivity(intent);
    }

    public static void openFirstActivity0(final Activity activity) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(context, FirstActivity0.class);
        final Intent intent = new Intent(context, FirstActivity0_new_10bubbles.class);
        activity.startActivity(intent);
    }

    public static void openLoginActivity(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, LoginActivity_new.class);
        activity.startActivity(intent);
    }

    public static void openLoginActivity2(final Activity activity, final String token, final String phoneNumber) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, LoginActivity_new.class);
        intent.putExtra("token", token);
        intent.putExtra("phoneNumber", phoneNumber);
        activity.startActivity(intent);
    }

    public static void openLoginActivity_New_2(final Activity activity, final String token, final String phoneNumber) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, LoginActivity_new2.class);
        intent.putExtra("token", token);
        intent.putExtra("phoneNumber", phoneNumber);
        activity.startActivity(intent);
    }

    /*
        public static void openMyAccountActivity(final Activity activity) {
            if (activity == null) {
                return;
            }
            final Intent intent = new Intent(context, MyAccountActivity.class);
            activity.startActivity(intent);
        }
    */
    public static void openVerificationActivity(
            final Activity activity,
            final String phone_number) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(MStarApp.getAppContext(), VerificationActivity.class);
        final Intent intent = new Intent(context, VerificationActivity2.class);
        intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }

    public static void openVerificationActivityAppMStar(
            final Activity activity,
            final String phone_number) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(MStarApp.getAppContext(), VerificationActivity.class);
        final Intent intent = new Intent(context, VerificationActivity2AppMStar.class);
        intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }

    public static void openVerificationActivityNew2(
            final Activity activity,
            final RegisterModel2 register_model_2) {
        if (activity == null) {
            return;
        }
        //final Intent intent = new Intent(MStarApp.getAppContext(), VerificationActivity.class);
        final Intent intent = new Intent(context, VerificationActivity2.class);
        intent.putExtra("Token", register_model_2.Token);
        activity.startActivity(intent);
    }

    public static void openEditConfirmActivity(
            final Activity activity,
            final String phone_number,
            final String fullName,
            final String ccNumber,
            final String mm,
            final String yy,
            final String cvv) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, RegisterActivity_cc_Edit_confirm.class);
        intent.putExtra("phoneNumber", phone_number);
        intent.putExtra("fullName", fullName);
        intent.putExtra("ccNumber", ccNumber);
        intent.putExtra("mm", mm);
        intent.putExtra("yy", yy);
        intent.putExtra("cvv", cvv);
        activity.startActivity(intent);
    }

    public static void openTopupPage1Activity(
            final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, TopupActivity_page1.class);
        activity.startActivity(intent);
    }

    public static void openTopupEwalletActivity(final Activity activity) {
        if (activity == null) {
            return;
        }

        final Intent intent = new Intent(context, TopupActivity_ewallet.class);
        activity.startActivity(intent);

    }

    public static void openTopupSummaryActivityAppMStar(final Activity activity, final PackageDataModel package_data_model) {
        if (activity == null) {
            return;
        }
        /*
        final Intent intent = new Intent(context, TopupSummaryActivity.class);
        intent.putExtra(PackageDataModel.TAG, package_data_model);
        activity.startActivityForResult(intent, Config.default_topup_summary_request_code);
        */
    }

    public static void signoutAppMStar(final Activity activity) {
        if (instance == null) {
            GlobalController.showToast("Signout_Null", 2000);
            return;
        };
        GlobalController.showToast("Signout", 2000);
        final Intent intent = new Intent(context, LogoutActivityAppMStar.class);
        activity.startActivity(intent);

		/*
		SessionController.close();
		if (MainActivity.instance != null) {
			AppController.checkSession(MainActivity.instance, true);
		}
		*/
    }

    public static void openLoaderActivity2(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, LoaderActivity_promo.class);
        activity.startActivity(intent);
    }

    public static void checkSession(
            final Activity activity,
            final boolean close_current_activity) {
        if (activity == null) {
            return;
        }
        if (SessionController.isSession()) {
            final UserModel user_model = SessionController.getUser();
            //Crashlytics.setUserEmail(user_model.Email);
            //Crashlytics.setUserName(user_model.Fullname);
            openWelcomeActivity(activity);
        } else {
            //GlobalController.showToast("else", 20000);
            if (SessionController.isPhoneNumberVer()) {
                openVerificationActivity(activity, SessionController.getPhoneNumberVer());
            } else {
                //openLoginActivity(activity);
                //openFirstActivity0(activity);
                //final Intent intent = new Intent(context, RegisterActivity_new2_box.class);
                final Intent intent = new Intent(context, ChooseBoxOrMobileActivity.class);
                activity.startActivity(intent);
            }
        }

        if (close_current_activity) {
            activity.finish();
        }
    }


    public static void checkSessionAppMStar(
            final Activity activity,
            final boolean close_current_activity) {
        if (activity == null) {
            return;
        }

        if (SessionControllerAppMStar.isSession()) {
            final UserModel user_model = SessionControllerAppMStar.getUser();
            //Crashlytics.setUserEmail(user_model.Email);
            //Crashlytics.setUserName(user_model.Fullname);
            //openLoaderActivity(activity);
            openLoaderActivity2(activity);
        } else {
            if (SessionControllerAppMStar.isPhoneNumberVer()) {
                openVerificationActivityAppMStar(activity, SessionControllerAppMStar.getPhoneNumberVer());
            } else {
                openLoginActivityAppMStar(activity);
            }
        }

        if (close_current_activity) {
            activity.finish();
        }
    }

    public static void meAppMStar(final URLManager.URLCallback url_callback) {
        final String url = URLController.getURL3(R.string.url_me);
        URLManager.requestGetAsync(url, URLController.getHeaders(), url_callback);
    }




    public static void openLoginActivityAppMStar(final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, LoginActivity_AppMStar.class);
        activity.startActivity(intent);
    }

    public static void openRegisterActivityAppMStar(
            final Activity activity,
            final String phone_number) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, RegisterActivity_AppMStar.class);
        intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }


    public static void openRegisterActivity(
            final Activity activity,
            final String phone_number) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, RegisterActivity_new.class);
        intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }

    public static void openRegisterActivityNew2(
            final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, RegisterActivity_new2_box.class);
        //intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }

    public static void openRegisterActivityNew2_Box(
            final Activity activity) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, RegisterActivity_new2_box.class);
        //intent.putExtra("PhoneNumber", phone_number);
        activity.startActivity(intent);
    }

    public static void openMyAccountActivity(
            final Activity activity,
            final MyAccountModel my_account_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, MyAccountActivity.class);
        //intent.putExtra(BannerModel.TAG, va_model);
        intent.putExtra("fullName", my_account_model.Fullname);
        intent.putExtra("phoneNumber", my_account_model.PhoneNumber);
        intent.putExtra("email", my_account_model.Email);
        intent.putExtra("adress", my_account_model.Adress);
        intent.putExtra("MacAddress", my_account_model.MacAddress);
        intent.putExtra("balance", my_account_model.Balance);
        intent.putExtra("package", my_account_model.Package);
        //GlobalController.showToast(va_model.list.get(0).va_number, 1000);
        activity.startActivity(intent);
    }

    public static void openChoosePayMethodActivity(
            final Activity activity,
            final InactivePackageCategoryModel game_category_model2,
            final BubblePackageModel game_model,
            final String month9) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, ChoosePayMethodActivity.class);
        intent.putExtra("InactivePackageCategoryModel", game_category_model2);
        intent.putExtra("BubblePackageModel", game_model);
        intent.putExtra("month9", month9);
        activity.startActivity(intent);
    }

    public static void openPackageActivity(
            final Activity activity,
            //final String title, //Games
            final BubblePackageCategoryModel game_category_model) {
        if (activity == null) {
            return;
        }
        final Intent intent = new Intent(context, PackageActivity.class);
        intent.putExtra("Title", "Package");
        intent.putExtra(BubblePackageCategoryModel.TAG, game_category_model);
        activity.startActivity(intent);
    }

	/*
    public static void openMyAccountActivity(
			final Activity activity,
			final String title,
			final String url) {
		if (activity == null) {
			return;
		}
		final Intent intent = new Intent(context, WebActivity.class);
		intent.putExtra("Title", title);
		intent.putExtra("URL", url);
		//GlobalController.showToast(url, 1000);
		activity.startActivityForResult(intent, Config.default_web_request_code);

	}
	*/

    public static void openYoutubeContent(Activity activity, List<SubCategory.Content> contentList) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(KyngaApp.getContext(), YoutubeActivity.class);
        intent.putParcelableArrayListExtra(YoutubeActivity.TAG, (ArrayList<SubCategory.Content>) contentList);
        activity.startActivity(intent);
    }

}