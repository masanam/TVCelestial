package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

public class ChooseBoxOrMobileActivity extends Activity implements View.OnClickListener {

    public static ChooseBoxOrMobileActivity instance;
    private ImageView button_my_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        setContentView(R.layout.activity_choose_box_mobile);
        instance = this;

        (findViewById(R.id.btnRegMobile)).setOnClickListener(this);
        (findViewById(R.id.btnRegBox)).setOnClickListener(this);
        (findViewById(R.id.btnSignIn)).setOnClickListener(this);

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
            case R.id.btnRegMobile:
                openRegMobile();
                break;
            case R.id.btnRegBox:
                openRegBox();
                break;
            case R.id.btnSignIn:
                AppController.openLoginActivity(instance);
                finish();
                break;
            default:
                break;
        }
    }



    private void openRegMobile(){
        Intent mpdIntent = new Intent(ChooseBoxOrMobileActivity.this, RegisterActivity_new2_Mobile.class);
        startActivity(mpdIntent);
        finish();
    }


    private void openRegBox(){
        Intent mpdIntent = new Intent(ChooseBoxOrMobileActivity.this, RegisterActivity_new2_box.class);
        startActivity(mpdIntent);
        finish();
    }

    private void openHappen(){

        Intent mpdIntent = new Intent(ChooseBoxOrMobileActivity.this, HappenTvLive.class);
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

    private void openAR(){
        boolean isHooqInstalled = false;
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
        }
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
            //return true;//must return true, other wise menu will dissapear suddently
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
