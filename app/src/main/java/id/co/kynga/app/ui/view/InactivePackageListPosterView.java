package id.co.kynga.app.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.BubblePackageModel;

/**
 * Created by macbookpro on 10/1/17.
 */

public class InactivePackageListPosterView {

    public interface GameListPosterViewCallback{
        void didGameListPosterViewActioned(final BubblePackageModel game_model);
    }

    private View view;
    private RelativeLayout lay_poster;
    private RecyclingImageView img_poster;
    private Button btn_action;
    private TextView lbl_title;

    private BubblePackageModel game_model;
    private GameListPosterViewCallback game_list_poster_view_callback;

    public InactivePackageListPosterView(
            final LayoutInflater inflater,
            final BubblePackageModel game_model,
            final GameListPosterViewCallback game_list_poster_view_callback){
        this.game_model = game_model;
        this.game_list_poster_view_callback = game_list_poster_view_callback;
        view = inflater.inflate(R.layout.view_game_poster,null,false);
        setInitial();

    }
    public View getView() {
        return view;
    }

    private void setInitial(){
        lay_poster = (RelativeLayout) view.findViewById(R.id.lay_poster);
        img_poster = (RecyclingImageView) view.findViewById(R.id.img_poster);
        btn_action = (Button) view.findViewById(R.id.btn_action);
        lbl_title = (TextView) view.findViewById(R.id.lbl_title);
        setEventListener();
        populateData();
        lbl_title.setMaxWidth(((LinearLayout) lay_poster.getParent()).getWidth());
    }
    private void setEventListener() {
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAction();
            }
        });
    }

    private void populateData() {
        ImageLoader.getInstance().displayImage(game_model.ImageUrl, img_poster, ImageController.getOption(true));
        //lbl_title.setText(game_model.Title);
        lbl_title.setText("Rp."+game_model.Price +" "+game_model.Period+" days");
    }
    private void doAction() {
        if (game_list_poster_view_callback == null) {
            return;
        }
        game_list_poster_view_callback.didGameListPosterViewActioned(game_model);
    }
}
