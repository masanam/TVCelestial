package id.co.kynga.app.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.YoutubeModel;

public class YoutubePlaylistPosterView {
	public interface YoutubePlaylistPosterViewCallback {
		void didYoutubePlaylistPosterViewActioned(final YoutubeModel youtube_model);
	}

	private View view;
	private RelativeLayout lay_poster;
	private RecyclingImageView img_poster;
	private Button btn_action;
	private TextView lbl_title;

	private YoutubeModel youtube_model;

	private YoutubePlaylistPosterViewCallback youtube_playlist_poster_view_callback;

	public YoutubePlaylistPosterView(
			final LayoutInflater inflater,
			final YoutubeModel youtube_model,
			final YoutubePlaylistPosterViewCallback youtube_playlist_poster_view_callback) {
		this.youtube_model = youtube_model;
		this.youtube_playlist_poster_view_callback = youtube_playlist_poster_view_callback;
		view = inflater.inflate(R.layout.view_youtube_poster_app_mstar, null, false);
		setInitial();
	}

	public View getView() {
		return view;
	}

	private void setInitial() {
		lay_poster = (RelativeLayout) view.findViewById(R.id.lay_poster);
		img_poster = (RecyclingImageView) view.findViewById(R.id.img_poster);
		btn_action = (Button) view.findViewById(R.id.btn_action);
		lbl_title = (TextView) view.findViewById(R.id.lbl_title);
		setEventListener();
		populateData();
		lbl_title.setMaxWidth(((LinearLayout) lay_poster.getParent()).getWidth());
	}

	private void setEventListener() {
		btn_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doAction();
			}
		});
	}

	private void populateData() {
		ImageLoader.getInstance().displayImage(youtube_model.ImageURL, img_poster, ImageController.getOption(true));
		lbl_title.setText(youtube_model.Title);
	}

	private void doAction() {
		if (youtube_playlist_poster_view_callback == null) {
			return;
		}
		youtube_playlist_poster_view_callback.didYoutubePlaylistPosterViewActioned(youtube_model);
	}
}