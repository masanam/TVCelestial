package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.BubblePackageModel;
import id.co.kynga.app.ui.activity.PackageActivity;

/**
 * Created by macbookpro on 10/1/17.
 */

public class GameListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BubblePackageModel> game_list;

    private class ViewHolder {
        RecyclingImageView img_game;
        TextView lbl_title;
    }

    public GameListAdapter(final ArrayList<BubblePackageModel> game_list) {
        this.game_list = game_list;
        inflater = (LayoutInflater) PackageActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return game_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_game, null);
            holder.img_game = (RecyclingImageView) convertView.findViewById(R.id.img_game);
            holder.lbl_title = (TextView) convertView.findViewById(R.id.lbl_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final BubblePackageModel game_model = game_list.get(position);
        ImageLoader.getInstance().displayImage(game_model.ImageUrl, holder.img_game, ImageController.getOption(true));
        holder.lbl_title.setText(game_model.Title);
        holder.lbl_title.setSelected(true);
        return convertView;

    }


}
