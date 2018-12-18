package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;

public class WelcomeActivity extends Activity {

    public static WelcomeActivity instance;
    private Handler handler = new Handler();//untuk timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        setContentView(R.layout.activity_welcome);
        instance = this;
        handler.postDelayed(runnable, 4000);

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            AppController.openFirstActivity(instance);
            handler.removeMessages(0);
            finish();
        }
    };



}
