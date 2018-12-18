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
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.ui.activity.VideoCategoryActivityAppMStar;


public class VideoCategoryListAdapter extends BaseAdapter {
	private class ViewHolder {
		RecyclingImageView img_video;
		TextView lbl_title;
	}

	private LayoutInflater inflater;
	private ArrayList<VideoModel> video_list;

	public VideoCategoryListAdapter(final ArrayList<VideoModel> video_list) {
		this.video_list = video_list;
		inflater = (LayoutInflater) VideoCategoryActivityAppMStar.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return video_list.size();
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
			convertView = inflater.inflate(R.layout.list_video_category_app_mstar, null);
			holder.img_video = (RecyclingImageView) convertView.findViewById(R.id.img_video);
			holder.lbl_title = (TextView) convertView.findViewById(R.id.lbl_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final VideoModel video_model = video_list.get(position);
		ImageLoader.getInstance().displayImage(video_model.ImageURL, holder.img_video, ImageController.getOption(true));
		holder.lbl_title.setText(video_model.Title);
		holder.lbl_title.setSelected(true);
		return convertView;
	}
}