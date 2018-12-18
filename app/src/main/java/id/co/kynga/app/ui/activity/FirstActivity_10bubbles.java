package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.MyAccountModel;
import id.co.kynga.app.model.ResponseModel;

public class FirstActivity_10bubbles extends Activity implements View.OnClickListener {

    public static FirstActivity_10bubbles instance;
    private ImageView button_my_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        setContentView(R.layout.activity_first_10bubbles);
        instance = this;

        (findViewById(R.id.button_my_account)).setOnClickListener(this);
        (findViewById(R.id.button_mobile_remote)).setOnClickListener(this);
        (findViewById(R.id.button_kynga_app)).setOnClickListener(this);
        (findViewById(R.id.button_customer_service)).setOnClickListener(this);
        (findViewById(R.id.button_ar)).setOnClickListener(this);
        (findViewById(R.id.button_happentv)).setOnClickListener(this);
        (findViewById(R.id.button_ticket)).setOnClickListener(this);
        (findViewById(R.id.button_more)).setOnClickListener(this);
        (findViewById(R.id.button_mytenant)).setOnClickListener(this);
        (findViewById(R.id.button_kuree)).setOnClickListener(this);
        /*
        button_my_account = (ImageView)findViewById(R.id.button_customer_service);
        android.view.ViewGroup.LayoutParams layoutParams = button_my_account.getLayoutParams();
        layoutParams.width = 50;
        layoutParams.height = 50;
        button_my_account.setLayoutParams(layoutParams);
        */
    }



    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_my_account:
                getMyAccount();
                break;
            case R.id.button_mobile_remote:
                GlobalController.showComingSoon(instance);
                break;
            case R.id.button_kynga_app:
                //openMStarApp();
                AppController.openSplashActivityAppMStar(instance);
                //AppController.openKyngaApp(instance);
                break;
            case R.id.button_happentv:
                openHappen();
                break;
            case R.id.button_kuree:
                openKuree();
                break;
            case R.id.button_ticket:
                GlobalController.showComingSoon(instance);
                break;
            case R.id.button_mytenant:
                openMyTenant();
                break;
            case R.id.button_ar:
                openAR();
                break;
            case R.id.button_more:
                GlobalController.showComingSoon(instance);
                break;
            case R.id.button_customer_service:
                GlobalController.showComingSoon(instance);
                break;
            default:
                break;
        }
    }

    private void openMStarApp(){
        boolean isHooqInstalled = false;
        List<PackageInfo> happs = getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < happs.size(); j++) {
            PackageInfo p = happs.get(j);
            if (p.packageName.equals("id.co.mstar.app")) {
                isHooqInstalled = true;
                break;
            }
        }
        if (isHooqInstalled) {
            final Intent i = getPackageManager().getLaunchIntentForPackage("id.co.mstar.app");
            startActivity(i);
        } else {
            GlobalController.showComingSoon(instance);
            /*
            AlertDialog.Builder b = new AlertDialog.Builder(FirstActivity_10bubbles.this);
            b.setTitle("Coming Soon");
            b.setPositiveButton("OK", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dial, int arg1) {
                    dial.dismiss();
                }

            });
            b.create().show();
            */
        }
    }


    private void openKuree(){
        boolean isHooqInstalled = false;
        List<PackageInfo> happs = getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < happs.size(); j++) {
            PackageInfo p = happs.get(j);
            if (p.packageName.equals("com.Pixelhub.FatherAndDaughter")) {
                isHooqInstalled = true;
                break;
            }
        }
        if (isHooqInstalled) {
            final Intent i = getPackageManager().getLaunchIntentForPackage("com.Pixelhub.FatherAndDaughter");
            startActivity(i);
        } else {
            AlertDialog.Builder b = new AlertDialog.Builder(FirstActivity_10bubbles.this);
            b.setTitle("Coming Soon");
            b.setPositiveButton("OK", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dial, int arg1) {
                    dial.dismiss();
                }

            });
            b.create().show();
        }
    }


    private void openHappen(){

        Intent mpdIntent = new Intent(FirstActivity_10bubbles.this, HappenTvLive.class);
        startActivity(mpdIntent);
        /*
        boolean isHooqInstalled = false;
        List<PackageInfo> happs = getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < happs.size(); j++) {
            PackageInfo p = happs.get(j);
            if (p.packageName.equals("net.ossrs.yasea.demo")) {
                isHooqInstalled = true;
                break;
            }
        }
        if (isHooqInstalled) {
            final Intent i = getPackageManager().getLaunchIntentForPackage("net.ossrs.yasea.demo");
            startActivity(i);
        } else {
            AlertDialog.Builder b = new AlertDialog.Builder(FirstActivity_10bubbles.this);
            b.setTitle("Coming Soon");
            b.setPositiveButton("OK", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dial, int arg1) {
                    dial.dismiss();
                }

            });
            b.create().show();
        }
        */
    }
    private void openMyTenant(){
        boolean isHooqInstalled = false;
        List<PackageInfo> happs = getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < happs.size(); j++) {
            PackageInfo p = happs.get(j);
            if (p.packageName.equals("ibase.mytenantworld")) {
                isHooqInstalled = true;
                break;
            }
        }
        if (isHooqInstalled) {
            final Intent i = getPackageManager().getLaunchIntentForPackage("ibase.mytenantworld");
            startActivity(i);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "ibase.mytenantworld")));
        }
    }

    private void openAR(){
        /*boolean isHooqInstalled = false;
        List<PackageInfo> happs = getPackageManager().getInstalledPackages(0);
        for (int j = 0; j < happs.size(); j++) {
            PackageInfo p = happs.get(j);
            if (p.packageName.equals("com.arjuna.ringsar")) {
                isHooqInstalled = true;
                break;
            }
        }
        if (isHooqInstalled) {
            final Intent i = getPackageManager().getLaunchIntentForPackage("com.arjuna.ringsar");
            startActivity(i);
        } else {
            GlobalController.showComingSoon(instance);
        }*/
        Intent arIntent = new Intent(FirstActivity_10bubbles.this, HomeARActivity.class);
        //arIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(arIntent);
    }

    private void getMyAccount() {
    //final UserModel user_model = SessionController.getUser();
    //final String phoneNumber = user_model.PhoneNumber;
        final String  phoneNumber = SessionController.getPhoneNumberVer();
    GlobalController.showLoading(this);
    //URLController.register(phoneNumber, fullname, email, new URLManager.URLCallback() {
    URLController.whoMe(phoneNumber , new URLManager.URLCallback() {
        @Override
        public void didURLSucceeded(int status_code, final String response) {
            if (instance == null) {
                return;
            }
            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GlobalController.closeLoading();
                    setValidation(response);
                    //GlobalController.showToast(response, 20000);
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

    private void setValidation(final String response) {
        final ResponseModel response_model = new ResponseModel(response);
        if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
            final MyAccountModel my_account_model = new MyAccountModel(response_model.Result);
            //SessionController.open(register_model);
            //AppController.checkSession(this, true);
            //GlobalController.showToast(register_model.ID, 20000);
            AppController.openMyAccountActivity(instance, my_account_model);
            //overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
        } else if (response_model.Status == ResponseModel.StatusType.FailedStatusType) {
            GlobalController.showWarning(this, response_model.Message);
        }
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

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }
*/
}
