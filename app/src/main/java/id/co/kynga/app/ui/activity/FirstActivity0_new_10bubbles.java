package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;

import static id.co.kynga.app.KyngaApp.context;

public class FirstActivity0_new_10bubbles extends Activity {

    public static FirstActivity0_new_10bubbles instance;
    private ImageView button_register, button_mobile_remote, button_kynga_app, button_customer_service;
    private ImageView button_happentv, button_kuree, button_ticket, button_mytenant, button_ar, button_more;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkLogin();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        setContentView(R.layout.activity_first0_new_10bubbles);
        instance = this;

        button_register = (ImageView) findViewById(R.id.button_register);
        button_mobile_remote = (ImageView) findViewById(R.id.button_mobile_remote);
        button_kynga_app = (ImageView) findViewById(R.id.button_kynga_app);
        button_happentv = (ImageView) findViewById(R.id.button_happentv);
        button_kuree = (ImageView) findViewById(R.id.button_kuree);
        button_ticket = (ImageView) findViewById(R.id.button_ticket);
        button_mytenant= (ImageView) findViewById(R.id.button_mytenant);
        button_ar= (ImageView) findViewById(R.id.button_ar);
        button_more= (ImageView) findViewById(R.id.button_more);
        button_customer_service = (ImageView) findViewById(R.id.button_customer_service);


        if (!RequestActivity.isAllPermitted()) {
            AppController.openRequestActivity(instance);
            finish();
            //checkLogin();
        }else{
            handler.postDelayed(runnable, 4000);
            //checkLogin();
            //checkRegister();
        }
/*
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AppController.openRegisterActivityNew2(instance);
                AppController.openChooseBoxOrMobileActivity(instance);
                finish();
            }
        });
        button_mobile_remote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_kynga_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_happentv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_kuree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_mytenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
        button_customer_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalController.showWarning(instance,"Click Register Button first");
            }
        });
*/

    }
/*
    @Override
    protected void onResume() {
        //this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        //setContentView(R.layout.activity_first0_new_10bubbles);
        if (!RequestActivity.isAllPermitted()) {
            AppController.openRequestActivity(instance);
        }else{
            checkLogin();
            //checkRegister();
        }
        super.onResume();
    }
    */

    private void checkLogin() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = telephonyManager.getDeviceId();
        GlobalController.showLoading(this);
        URLController.checkLogin(IMEI, new URLManager.URLCallback() {
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
                        //GlobalController.showToast(response, 20000);

                        if (response.contains("Imei Is Login")){
                            //tujuannya agar bisa langsung pakai app
                            AppController.checkSession(instance, true);
                        }
                        if (response.contains("Imei Not Found")){
                            SessionController.close();
                            final Intent intent = new Intent(context, ChooseBoxOrMobileActivity.class);
                            startActivity(intent);
                        }
                        if (response.contains("Please Login")){
                            SessionController.close();
                            final Intent intent = new Intent(context, ChooseBoxOrMobileActivity.class);
                            startActivity(intent);
                        }
                        finish();

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

/*
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.KEYCODE_BACK) {
            //finish();
            //Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
            //this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            //finish();
            return true;//must return true, other wise menu will dissapear suddently
        }

        return super.dispatchKeyEvent(event);
    }
*/
}
