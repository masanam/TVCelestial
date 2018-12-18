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
import id.co.kynga.app.model.pojo.SubCategory;

public class ListTVCategoryAdapter extends RecyclerView.Adapter<ListTVCategoryAdapter.ViewHolder> {
	private Context context;
	private List<SubCategory.Content> categoryList;
	private OnCategoryClickListener listener;
	private int posSelected = -1;

	public ListTVCategoryAdapter(List<SubCategory.Content> categoryList) {
		this.setContentList(categoryList);
	}

	public void setContentList(List<SubCategory.Content> categoryList) {
		this.categoryList = categoryList;
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
		View view = inflater.inflate(R.layout.list_tv_category_channel, parent, false);

		return new ListTVCategoryAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		SubCategory.Content categoryContent = categoryList.get(position);

		if (categoryContent != null && !TextUtils.isEmpty(categoryContent.Title)) {
			holder.tvTitle.setText(categoryContent.Title);
		}

		if (posSelected == position) {
			holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.Chocolate));
		} else {
			holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
		}
	}

	@Override
	public int getItemCount() {
		return categoryList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView tvTitle;

		ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			itemView.setBackgroundResource(R.drawable.selector_bg);
			tvTitle = (TextView) itemView.findViewById(R.id.list_category_channel_title);
		}

		@Override
		public void onClick(View v) {
			listener.onCategoryClick(v, getAdapterPosition());
		}
	}

	public interface OnCategoryClickListener {
		void onCategoryClick(View v, int position);
	}

	public void setOnCategoryClickListener(OnCategoryClickListener listener) {
		this.listener = listener;
	}
}
