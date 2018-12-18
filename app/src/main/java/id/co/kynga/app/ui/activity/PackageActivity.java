package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.AppController;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.BubblePackageCategoryModel;
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.model.InactivePackageCategoryModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.ui.adapter.GameListAdapter;
import id.co.kynga.app.ui.view.ActivePackageListView;
import id.co.kynga.app.ui.view.InactivePackageListView;

/**
 * Created by macbookpro on 10/1/17.
 */

public class PackageActivity extends Activity {
    public static PackageActivity instance;

    private ImageButton btn_back;
    private TextView lbl_title;
    private LinearLayout lay_main, lay_main2;
    private GridView gvw_main;

    private BubblePackageCategoryModel game_category_model;
    private InactivePackageCategoryModel game_category_model2;
    private BubblePackageModel game_model;
    private String title = Config.text_blank;
    private boolean init = false;
    private CategoryModel category_model;
    private ArrayList<BubblePackageModel> game_list;
    private GameListAdapter game_list_adapter;
    private String month9;
    private TextView textRespon;
    private int cacah=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        setContentView(R.layout.activity_package);
        init = true;
        category_model = getIntent().getParcelableExtra(CategoryModel.TAG);
        setInitial();
        populateData();
        //checkInactivePackage();
    }


    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
        finish();
    }

    @Override
    protected void onResume() {
        //be careful.. setInitial() and populateData make double package bubble..!!!!
        //setInitial();
        //populateData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setInitial() {
        if (getIntent().hasExtra(BubblePackageCategoryModel.TAG)) {
            game_category_model = getIntent().getParcelableExtra(BubblePackageCategoryModel.TAG);
        }
        //GlobalController.showToast(String.valueOf(game_category_model.list.size()),20000);
     /*  if (getIntent().hasExtra(BubblePackageModel.TAG)) {
            game_model = getIntent().getParcelableExtra(BubblePackageModel.TAG);
        }*/
        if (getIntent().hasExtra("Title")) {
            title = getIntent().getStringExtra("Title");
        }
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        textRespon = (TextView) findViewById(R.id.textRespon);
        //lbl_title = (TextView) findViewById(R.id.lbl_title);
        //lbl_title.setTypeface(null, Typeface.BOLD);
        lay_main = (LinearLayout) findViewById(R.id.lay_main);
        lay_main2 = (LinearLayout) findViewById(R.id.lay_main2);
        setEventListener();
    }

    private void setEventListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Details.this.overridePendingTransition(R.anim.nothing,R.anim.nothing);
                PackageActivity.this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });
    }

    protected void Quit() {
        super.finish();
    }

    private void populateData() {
       // lbl_title.setText(title);

        //game_list_adapter = new GameListAdapter(game_category_model.Bubble.list);
       //gvw_main.setAdapter(game_list_adapter);

        //lbl_title.setText(game_model.gamesTitle);
        //GlobalController.showToast(String.valueOf(game_category_model.list.get(0)),20000);
        for (final BubblePackageCategoryModel game_playlist : game_category_model.list) {
            //GlobalController.showToast(String.valueOf(String.valueOf(cacah)),20000);
            //cacah++;
            /*
            Toast toast = Toast.makeText(this,String.valueOf(game_category_model.list.size()), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            */
            final ActivePackageListView vw_game_list = new ActivePackageListView(instance, game_playlist, new ActivePackageListView.GameListViewCallback() {
                @Override
                public void didGameListViewActioned(
                        final BubblePackageCategoryModel game_category_model,
                        final BubblePackageModel game_model) {
                    //GlobalController.showToast(String.valueOf(game_model.ImageURL), 20000);
                    //  AppController.openYoutubeActivity(instance, game_category_model, game_model);
                    //AppController.openWebActivity(instance, game_model.gamesTitle, game_model.gamesLink);

                }

            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            vw_game_list.getView().setLayoutParams(lp);
            lay_main.addView(vw_game_list.getView());
            //menaruh hasilnya dari bawah ke atas
            //lay_main.addView(vw_youtube_playlist.getView(),0);
        }
        checkInactivePackage();
    }

    private void checkInactivePackage() {
        //final UserModel user_model = SessionController.getUser();
        //final String phoneNumber = user_model.PhoneNumber;
        final String token = SessionController.getToken();
        //GlobalController.showToast(token, 20000);
        GlobalController.showLoading(this);
        URLController.inactivePackage(token , new URLManager.URLCallback() {
            //URLController.activePackage(token , new URLManager.URLCallback() {
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
                        if(response.contains("\"Periode\":\"270\"")){
                            month9="Yes";
                        }else{
                            month9="No";
                        }
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
                        GlobalController.closeLoading();
                        GlobalController.showRequestFailedWarning(instance);
                    }
                });
            }
        });

    }

    private void setValidation(final String response) { //convert response to model
        final ResponseModel response_model = new ResponseModel(response);
        if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
            game_category_model2 = new InactivePackageCategoryModel((response_model.Result));
            //GlobalController.showToast(String.valueOf(game_category_model2.list.size()), 20000);
            populateData2();
        }
        else{
            GlobalController.showWarning(instance,response_model.Message);
        }
    }

    private void populateData2() {
        for (final InactivePackageCategoryModel game_playlist : game_category_model2.list) {
            //GlobalController.showToast(String.valueOf(game_playlist.Bubble.list.size()), 20000);
            final InactivePackageListView vw_game_list2 = new InactivePackageListView(instance, game_playlist, new InactivePackageListView.GameListViewCallback() {
                @Override
                public void didGameListViewActioned(
                        final InactivePackageCategoryModel game_category_model2,
                        final BubblePackageModel game_model) {
                    //GlobalController.showToast(String.valueOf(game_model.ID), 20000);
                    //GlobalController.showToast(String.valueOf(game_category_model2.Bubble.list.size()), 20000);
                    //GlobalController.showToast(String.valueOf(game_category_model2.Bubble.list.get(0).Price), 20000);
                    AppController.openChoosePayMethodActivity(instance, game_category_model2, game_model,month9);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            vw_game_list2.getView().setLayoutParams(lp);
            lay_main2.addView(vw_game_list2.getView());
            //menaruh hasilnya dari bawah ke atas
            //lay_main.addView(vw_youtube_playlist.getView(),0);
        }
    }
/*
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keycode = event.getKeyCode();
        if (keycode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

}
