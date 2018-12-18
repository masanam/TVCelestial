package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.ui.activity.RadioActivity2AppMStar;

public class RadioListAdapter extends BaseAdapter {
	public interface RadioListAdapterCallback {
		void didRadioListAdapterActioned(final RadioModel radio_model);
	}

	private class ViewHolder {
		TextView lbl_title;
	}

	private LayoutInflater inflater;
	private ArrayList<RadioModel> radio_list;
	private RadioListAdapterCallback radio_list_adapter_callback;

	public RadioListAdapter(
			final ArrayList<RadioModel> radio_list,
			final RadioListAdapterCallback radio_list_adapter_callback) {
		this.radio_list = radio_list;
		this.radio_list_adapter_callback = radio_list_adapter_callback;
		inflater = (LayoutInflater) RadioActivity2AppMStar.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return radio_list.size();
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
			convertView = inflater.inflate(R.layout.list_tv, null);
			holder.lbl_title = (TextView) convertView.findViewById(R.id.lbl_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RadioModel radio_model = radio_list.get(position);
		holder.lbl_title.setText(radio_model.Title);
		holder.lbl_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (radio_list_adapter_callback != null) {
					radio_list_adapter_callback.didRadioListAdapterActioned(radio_model);
				}
			}
		});
		return convertView;
	}
}