package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import id.co.kynga.app.R;

/**
 * Created by Asus on 4/10/2017.
 */

public class HomeARActivity extends Activity implements  View.OnClickListener{

public HomeARActivity instance;
    private static final String LOGTAG = "MAIN";

    static String EXTRA_MESSAGE_DATA = "data";



    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ar);
        instance = this;

        (findViewById(R.id.btn_brachiosaurus)).setOnClickListener(this);
        (findViewById(R.id.btn_mammouth)).setOnClickListener(this);
        (findViewById(R.id.btn_pteranodon)).setOnClickListener(this);
        (findViewById(R.id.btn_triceratops)).setOnClickListener(this);
        (findViewById(R.id.btn_tyrannosaurus)).setOnClickListener(this);
        (findViewById(R.id.btn_velociraptor)).setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_brachiosaurus:
                gotoAR("data-3.txt");
                break;
            case R.id.btn_mammouth:
                gotoAR("data-4.txt");
                break;
            case R.id.btn_pteranodon:
                gotoAR("data-5.txt");
                break;
            case R.id.btn_triceratops:
                gotoAR("data-6.txt");
                break;
            case R.id.btn_tyrannosaurus:
                gotoAR("data-1.txt");
                break;
            case R.id.btn_velociraptor:
                gotoAR("data-2.txt");
                break;
            default:
                break;
        }
    }



    public void gotoAR(String data)
    {
        Intent i = new Intent(this, StartArActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra(EXTRA_MESSAGE_DATA, data);
        startActivity(i);

    }
}
