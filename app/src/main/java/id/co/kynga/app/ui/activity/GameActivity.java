package id.co.kynga.app.ui.activity;

import android.graphics.Typeface;
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
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.GameCategoryModel;
import id.co.kynga.app.model.GameModel;
import id.co.kynga.app.ui.adapter.GameListAdapter;
import id.co.kynga.app.ui.view.GameListView;

/**
 * Created by macbookpro on 10/1/17.
 */

public class GameActivity extends CommonActivity {
    public static GameActivity instance;

    private ImageButton btn_back;
    private TextView lbl_title;
    private LinearLayout lay_main;
    private GridView gvw_main;

    private GameCategoryModel game_category_model;
    private GameModel game_model;
    private String title = Config.text_blank;
    private boolean init = false;
    private CategoryModel category_model;
    private ArrayList<GameModel> game_list;
    private GameListAdapter game_list_adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_game);
        init = true;
        category_model = getIntent().getParcelableExtra(CategoryModel.TAG);
        setInitial();
        populateData();

    }


    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setInitial() {
        if (getIntent().hasExtra(GameCategoryModel.TAG)) {
            game_category_model = getIntent().getParcelableExtra(GameCategoryModel.TAG);
        }
     /*  if (getIntent().hasExtra(GameModel.TAG)) {
            game_model = getIntent().getParcelableExtra(GameModel.TAG);
        }*/
        if (getIntent().hasExtra("Title")) {
            title = getIntent().getStringExtra("Title");
        }
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(null, Typeface.BOLD);
        lay_main = (LinearLayout) findViewById(R.id.lay_main);
        setEventListener();
    }

    private void setEventListener() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             instance.finish();
                //onDestroy();
                    //android.os.Process.killProcess(android.os.Process.myPid());
                // onPause();
                    //Quit();
               // super.finish();

            }
        });
    }

    protected void Quit() {
        super.finish();
    }

    private void populateData() {
        lbl_title.setText(title);

        //game_list_adapter = new GameListAdapter(game_category_model.Games.list);
       // GlobalController.showToast(game_category_model.list.size());
      // gvw_main.setAdapter(game_list_adapter);

        //lbl_title.setText(game_model.gamesTitle);

        for (final GameCategoryModel game_playlist : game_category_model.list) {
            final GameListView vw_game_list = new GameListView(instance, game_playlist, new GameListView.GameListViewCallback() {

                @Override
                public void didGameListViewActioned(
                        final GameCategoryModel game_category_model,
                        final GameModel game_model) {
                   // GlobalController.showToast("click", 20000);
                    //  AppController.openYoutubeActivity(instance, game_category_model, game_model);
                    AppController.openWebActivityAppMStar(instance, game_model.gamesTitle, game_model.gamesLink);

                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            vw_game_list.getView().setLayoutParams(lp);
            lay_main.addView(vw_game_list.getView());
            //menaruh hasilnya dari bawah ke atas
            //lay_main.addView(vw_youtube_playlist.getView(),0);
        }
    }


}
