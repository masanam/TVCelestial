package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.model.TVModel;
import id.co.kynga.app.ui.activity.TVActivityAppMStar;

public class TVListAdapter extends BaseAdapter {
	public interface TVListAdapterCallback {
		void didTVListAdapterActioned(final TVModel tv_model);
	}

	private class ViewHolder {
		TextView lbl_title;
	}

	private LayoutInflater inflater;
	private ArrayList<TVModel> tv_list;
	private TVListAdapterCallback tv_list_adapter_callback;

	public TVListAdapter(
			final ArrayList<TVModel> tv_list,
			final TVListAdapterCallback tv_list_adapter_callback) {
		this.tv_list = tv_list;
		this.tv_list_adapter_callback = tv_list_adapter_callback;
		inflater = (LayoutInflater) TVActivityAppMStar.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return tv_list.size();
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
		final TVModel tv_model = tv_list.get(position);
		holder.lbl_title.setText(tv_model.Title);
		holder.lbl_title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (tv_list_adapter_callback != null) {
					tv_list_adapter_callback.didTVListAdapterActioned(tv_model);
				}
			}
		});
		return convertView;
	}
}