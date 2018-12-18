package id.co.kynga.app.ui.adapter;

import java.util.ArrayList;
import java.util.Map;

import id.co.kynga.app.util.DataVariable;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import com.squareup.picasso.Picasso;

public class YoutubeListRegionAdapter extends BaseAdapter {
	private ArrayList<Map<String, String>> data;
	private static LayoutInflater inflater = null;
	private Context mContext;

	public YoutubeListRegionAdapter(Context context, ArrayList<Map<String, String>> d) {
		mContext = context;
		data = d;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
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
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.grid_youtube_item, parent, false);
		}
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int screenWidth = metrics.widthPixels;
		int gridSpacing = (int) mContext.getResources().getDimension(R.dimen.list_youtube_channel_margin) * 2;
		int imageWidth = ((screenWidth - gridSpacing) * 3 / 10);
		int imageHeight = imageWidth * 4 / 6;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
		ImageView video_image = (ImageView) vi.findViewById(R.id.youtube_video_image); // thumb image
		video_image.setScaleType(ScaleType.FIT_XY);
		video_image.setLayoutParams(params);
		Map<String, String> movie = data.get(position);
		TextView title = (TextView) vi.findViewById(R.id.youtube_video_title);
		title.setText(movie.get(DataVariable.KEY_TITLE));
		Picasso.with(mContext)
				.load(movie.get(DataVariable.YOUTUBE_KEY_IMAGE))
				.placeholder(R.drawable.bg_logo)
				.resize(imageWidth, imageHeight)
				.into(video_image);

		return vi;
	}
}