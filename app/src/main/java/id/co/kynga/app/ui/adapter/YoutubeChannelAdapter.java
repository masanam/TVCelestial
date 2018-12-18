package id.co.kynga.app.ui.adapter;

import java.util.ArrayList;
import java.util.Map;

import id.co.kynga.app.R;

import id.co.kynga.app.util.DataVariable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class YoutubeChannelAdapter extends BaseAdapter {
	ArrayList<Map<String, String>> channels;
	Context mContext;

	public YoutubeChannelAdapter(Context context, ArrayList<Map<String, String>> c) {
		channels = c;
		mContext = context;
	}

	@Override
	public int getCount() {
		return channels.size();
	}

	@Override
	public Object getItem(int position) {
		return channels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int index = position + 1;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_category, parent, false);
		}
		TextView tv = (TextView) vi.findViewById(R.id.textCategoryName);
		TextView tn = (TextView) vi.findViewById(R.id.textchNumber);
		Map<String, String> rMap = channels.get(position);
		String rName = rMap.get(DataVariable.KEY_TITLE);
		tv.setText(rName);
		tn.setText("" + index);
		return vi;
	}
}