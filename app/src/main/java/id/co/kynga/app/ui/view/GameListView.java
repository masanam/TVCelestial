package id.co.kynga.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.model.GameCategoryModel;
import id.co.kynga.app.model.GameModel;

/**
 * Created by macbookpro on 10/1/17.
 */

public class GameListView {

    public interface GameListViewCallback{
        void didGameListViewActioned(
                final GameCategoryModel game_category_model,
                final GameModel game_model);


    }
    private LayoutInflater inflater;
    private View view;
    private TextView lbl_title;
    private LinearLayout lay_content;

    private GameCategoryModel game_category_model;
    private GameListViewCallback game_list_view_callback;

    public GameListView(
            final Activity activity,
            final GameCategoryModel game_category_model,
            final GameListViewCallback game_list_view_callback){
        this.game_category_model = game_category_model;
        this.game_list_view_callback = game_list_view_callback;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_content, null, false);
        setInitial();
    }

    public View getView() {
        return view;
    }
    private void setInitial() {
        lbl_title = (TextView) view.findViewById(R.id.lbl_title);
        lay_content = (LinearLayout) view.findViewById(R.id.lay_content);
        populateData();
    }

    private void populateData() {
        lbl_title.setText(game_category_model.catTitle);

        for (final GameModel game_model : game_category_model.Games.list) {
            final GameListPosterView vw_game_list_poster = new GameListPosterView(inflater, game_model, new GameListPosterView.GameListPosterViewCallback() {
                @Override
                public void didGameListPosterViewActioned(GameModel game_model) {
                    if (game_list_view_callback == null) {
                        return;
                    }
                    game_list_view_callback.didGameListViewActioned(game_category_model, game_model);
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 10, 0);
            vw_game_list_poster.getView().setLayoutParams(lp);
            lay_content.addView(vw_game_list_poster.getView());
        }
    }
}
