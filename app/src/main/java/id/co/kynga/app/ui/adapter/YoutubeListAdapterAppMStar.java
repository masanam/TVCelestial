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
import id.co.kynga.app.model.YoutubeModel;
import id.co.kynga.app.ui.activity.YoutubeActivity;
import id.co.kynga.app.ui.activity.YoutubeActivityAppMStar;

public class YoutubeListAdapterAppMStar extends BaseAdapter {
	private class ViewHolder {
		RecyclingImageView img_youtube;
		TextView lbl_title;
	}

	private LayoutInflater inflater;
	private ArrayList<YoutubeModel> youtube_list;

	public YoutubeListAdapterAppMStar(final ArrayList<YoutubeModel> youtube_list) {
		this.youtube_list = youtube_list;
		inflater = (LayoutInflater) YoutubeActivityAppMStar.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return youtube_list.size();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_youtube, null);
			holder.img_youtube = (RecyclingImageView) convertView.findViewById(R.id.img_youtube);
			holder.lbl_title = (TextView) convertView.findViewById(R.id.lbl_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final YoutubeModel youtube_model = youtube_list.get(position);
		ImageLoader.getInstance().displayImage(youtube_model.ImageURL, holder.img_youtube, ImageController.getOption(true));
		holder.lbl_title.setText(youtube_model.Title);
		holder.lbl_title.setSelected(true);
		return convertView;
	}
}