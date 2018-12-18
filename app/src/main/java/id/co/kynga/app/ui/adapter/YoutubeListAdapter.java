package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.model.YoutubeContent;

/**
 * Modified by Aji Subastian
 */

public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.ViewHolder> {

    private static final String DEFAULT_IMAGE = "http://kynga.co.id/images/logo.png";
    private Context context;
    private List<YoutubeContent> youtubeContentList;
    private OnYoutubeContentClickListener listener;
    private int posSelected = -1;

    public YoutubeListAdapter(List<YoutubeContent> youtubeContentList) {
        setContentList(youtubeContentList);
    }

    public void setContentList(List<YoutubeContent> youtubeContentList) {
        this.youtubeContentList = youtubeContentList;
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
        View view = inflater.inflate(R.layout.grid_youtube_item, parent, false);

        return new YoutubeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        YoutubeContent youtubeContent = youtubeContentList.get(position);

        if (youtubeContent != null) {
            String title = youtubeContent.Title;
            title = title.isEmpty() ? "" : title;

            String image = youtubeContent.ImageURL;

            holder.youtubeTitle.setText(title);
            holder.youtubeImage.setTag(image);

            if(!TextUtils.isEmpty(image))
                Picasso.with(context)
                        .load(image)
                        .placeholder(R.drawable.bg_logo)
                        .fit()
                        .into(holder.youtubeImage);
        }

    }

    @Override
    public int getItemCount() {
        return youtubeContentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView youtubeTitle;
        ImageView youtubeImage;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setBackgroundResource(R.drawable.selector_bg);

            youtubeTitle = (TextView) itemView.findViewById(R.id.youtube_video_title);
            youtubeImage = (ImageView) itemView.findViewById(R.id.youtube_video_image);
        }

        @Override
        public void onClick(View v) {
            listener.onYoutubeContentClick(v, getAdapterPosition());
        }
    }

    public interface OnYoutubeContentClickListener {
        void onYoutubeContentClick(View v, int position);
    }

    public void setOnYoutubeContentClickListener(OnYoutubeContentClickListener listener){
        this.listener = listener;
    }
}