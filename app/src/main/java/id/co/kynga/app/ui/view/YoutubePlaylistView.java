package id.co.kynga.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.model.YoutubeModel;
import id.co.kynga.app.model.YoutubePlaylistModel;

public class YoutubePlaylistView {
	public interface YoutubePlaylistViewCallback {
		void didYoutubePlaylistViewActioned(
				final YoutubePlaylistModel youtube_playlist_model,
				final YoutubeModel youtube_model);
	}

	private LayoutInflater inflater;
	private View view;
	private TextView lbl_title;
	private LinearLayout lay_content;

	private YoutubePlaylistModel youtube_playlist_model;

	private YoutubePlaylistViewCallback youtube_playlist_view_callback;

	public YoutubePlaylistView(
			final Activity activity,
			final YoutubePlaylistModel youtube_playlist_model,
			final YoutubePlaylistViewCallback youtube_playlist_view_callback) {
		this.youtube_playlist_model = youtube_playlist_model;
		this.youtube_playlist_view_callback = youtube_playlist_view_callback;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.view_content, null, false);
		setInitial();
	}

	public View getView() {
		return view;
	}

	private void setInitial() {
		lbl_title = (TextView) view.findViewById(R.id.lbl_title);
		lay_content = (LinearLayout) view.findViewById(R.id.lay_content);
		populateData();
	}

	private void populateData() {
		lbl_title.setText(youtube_playlist_model.Title);
		for (final YoutubeModel youtube_model : youtube_playlist_model.Youtube.list) {
			final YoutubePlaylistPosterView vw_youtube_playlist_poster = new YoutubePlaylistPosterView(inflater, youtube_model, new YoutubePlaylistPosterView.YoutubePlaylistPosterViewCallback() {
				@Override
				public void didYoutubePlaylistPosterViewActioned(YoutubeModel youtube_model) {
					if (youtube_playlist_view_callback == null) {
						return;
					}
					youtube_playlist_view_callback.didYoutubePlaylistViewActioned(youtube_playlist_model, youtube_model);
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 10, 0);
			vw_youtube_playlist_poster.getView().setLayoutParams(lp);
			lay_content.addView(vw_youtube_playlist_poster.getView());
		}
	}
}