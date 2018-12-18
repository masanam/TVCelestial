package id.co.kynga.app.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import id.co.kynga.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListChannelAdapter extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	private int positionId = -1;

	public ListChannelAdapter(Context c, ArrayList<HashMap<String, String>> d) {
		data = d;
		context = c;
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

	public void setIndex(int id) {
		positionId = id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_category, parent, false);
		}
		HashMap<String, String> m = data.get(position);
		TextView t = (TextView) vi.findViewById(R.id.textCategoryName);
		TextView number = (TextView) vi.findViewById(R.id.textchNumber);
		t.setText(m.get("title"));
		String chPos = String.valueOf(position + 1);
		if (chPos.length() == 1) {
			chPos = "00" + chPos;
		} else if (chPos.length() == 2) {
			chPos = "0" + chPos;
		}
		number.setText(chPos);
		if (positionId == position) {
			t.setTextColor(context.getResources().getColor(R.color.Chocolate));
			number.setTextColor(context.getResources().getColor(R.color.Chocolate));
		} else {
			t.setTextColor(context.getResources().getColor(R.color.white));
			number.setTextColor(context.getResources().getColor(R.color.white));
		}
		return vi;
	}
}