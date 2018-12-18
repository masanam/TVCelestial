package id.co.kynga.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.model.InactivePackageCategoryModel;

/**
 * Created by macbookpro on 10/1/17.
 */

public class InactivePackageListView {

    public interface GameListViewCallback{
        void didGameListViewActioned(
                final InactivePackageCategoryModel game_category_model,
                final BubblePackageModel game_model);


    }
    private LayoutInflater inflater;
    private View view;
    private TextView lbl_title;
    private LinearLayout lay_content;

    private InactivePackageCategoryModel game_category_model;
    private GameListViewCallback game_list_view_callback;

    public InactivePackageListView(
            final Activity activity,
            final InactivePackageCategoryModel game_category_model,
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
        if(game_category_model.Type.equals("2")) {
            lbl_title.setText(game_category_model.Package + " : Add-On" );
        }
        //lbl_title.setText(game_category_model.Package);
        //GlobalController.showToast(String.valueOf(game_category_model.list.size()), 20000);

        for (final BubblePackageModel game_model : game_category_model.Bubble.list) {
            //GlobalController.showToast(game_model.list.get(0).ImageURL, 20000);
            //GlobalController.showToast("test", 20000);
            final InactivePackageListPosterView vw_game_list_poster = new InactivePackageListPosterView(inflater, game_model, new InactivePackageListPosterView.GameListPosterViewCallback() {
                @Override
                public void didGameListPosterViewActioned(BubblePackageModel game_model) {
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
