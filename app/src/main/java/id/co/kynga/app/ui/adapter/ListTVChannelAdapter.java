package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.model.pojo.ContentPojo;

public class ListTVChannelAdapter extends RecyclerView.Adapter<ListTVChannelAdapter.ViewHolder> {
	private Context context;
	private List<ContentPojo> contentList;
	private OnChannelClickListener listener;
	private int posSelected = -1;

	public ListTVChannelAdapter(List<ContentPojo> contentList) {
		setContentList(contentList);
	}

	public void setContentList(List<ContentPojo> contentList) {
		this.contentList = contentList;
		notifyDataSetChanged();
	}

	public void setSelected(int position){
		this.posSelected = position;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.list_channel, parent, false);

		return new ListTVChannelAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ContentPojo tvChannelContent = contentList.get(position);

		if (tvChannelContent != null && !TextUtils.isEmpty(tvChannelContent.Title)) {
			holder.tvTitle.setText(tvChannelContent.Title);
			String number = String.valueOf(position + 1);
			if (number.length() == 1) {
				number = "00" + number;
			} else if (number.length() == 2) {
				number = "0" + number;
			}
			holder.tvNumber.setText(number);
		}

		if (posSelected == position) {
			holder.tvNumber.setTextColor(ContextCompat.getColor(context, R.color.Chocolate));
			holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.Chocolate));
		} else {
			holder.tvNumber.setTextColor(ContextCompat.getColor(context, R.color.white));
			holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
		}
	}

	@Override
	public int getItemCount() {
		return contentList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView tvNumber;
		TextView tvTitle;

		ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			itemView.setFocusable(true);
			itemView.setBackgroundResource(R.drawable.selector_bg);
			tvNumber = (TextView) itemView.findViewById(R.id.list_channel_number);
			tvTitle = (TextView) itemView.findViewById(R.id.list_channel_title);
		}

		@Override
		public void onClick(View v) {
			listener.onChannelClick(v, getAdapterPosition());
		}
	}

	public interface OnChannelClickListener {
		void onChannelClick(View v, int position);
	}

	public void setOnChannelClickListener(OnChannelClickListener listener) {
		this.listener = listener;
	}

}