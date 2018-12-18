package id.co.kynga.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.co.kynga.app.R;
import id.co.kynga.app.util.DataVariable;

public class GridHappenTvAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public GridHappenTvAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(R.layout.grid_happen_item, parent, false);
		}
		TextView title = (TextView) view.findViewById(R.id.happen_title);
		ImageView video_image = (ImageView) view.findViewById(R.id.happen_video_image);

		Map<String, String> movie = data.get(position);
		title.setText(movie.get(DataVariable.KEY_TITLE));
		video_image.setTag(movie.get(DataVariable.KEY_IMAGE));
		Picasso.with(activity)
				.load(movie.get(DataVariable.KEY_IMAGE))
				.placeholder(R.drawable.bg_logo_2)
				.fit()
				.into(video_image);
		return view;
	}
}
