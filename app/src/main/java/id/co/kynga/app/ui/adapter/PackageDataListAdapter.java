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
import id.co.kynga.app.model.PackageDataModel;
import id.co.kynga.app.ui.activity.TopupActivity;

public class PackageDataListAdapter extends BaseAdapter {
	private class ViewHolder {
		RecyclingImageView img_operator;
		TextView lbl_name;
		TextView lbl_description;
	}

	private LayoutInflater inflater;
	private ArrayList<PackageDataModel> package_data_list;

	public PackageDataListAdapter(final ArrayList<PackageDataModel> package_data_list) {
		this.package_data_list = package_data_list;
		inflater = (LayoutInflater) TopupActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return package_data_list.size();
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
			convertView = inflater.inflate(R.layout.list_package_data, null);
			holder.img_operator = (RecyclingImageView) convertView.findViewById(R.id.img_operator);
			holder.lbl_name = (TextView) convertView.findViewById(R.id.lbl_name);
			holder.lbl_description = (TextView) convertView.findViewById(R.id.lbl_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final PackageDataModel package_data_model = package_data_list.get(position);
		ImageLoader.getInstance().displayImage(package_data_model.ImageURL, holder.img_operator, ImageController.getOption(true));
		holder.lbl_name.setText(package_data_model.Name);
		holder.lbl_description.setText(package_data_model.Description);
		return convertView;
	}
}