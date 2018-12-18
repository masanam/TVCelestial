package id.co.kynga.app.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import id.co.kynga.app.R;
import id.co.kynga.app.control.PosterLayout;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.ContentModel;
import id.co.kynga.app.model.VideoTrailerModel;

public class PosterView {
	public interface PosterViewCallback {
		void didPosterViewActioned(final Object object);
	}

	private View view;
	private PosterLayout lay_poster;
	private RecyclingImageView img_poster;
	private Button btn_action;
	private TextView lbl_title;

	private Object object;

	private PosterViewCallback poster_view_callback;

	public PosterView(
			final LayoutInflater inflater,
			final Object object,
			final PosterViewCallback poster_view_callback) {
		this.object = object;
		this.poster_view_callback = poster_view_callback;
		view = inflater.inflate(R.layout.view_poster, null, false);
		setInitial();
	}

	public View getView() {
		return view;
	}

	private void setInitial() {
		lay_poster = (PosterLayout) view.findViewById(R.id.lay_poster);
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
		String title = Config.text_blank;
		String image_url = Config.text_blank;
		if (object instanceof ContentModel) {
			final ContentModel content_model = (ContentModel) object;
			title = content_model.Title;
			image_url = content_model.ImageURL;
		} else if (object instanceof VideoTrailerModel) {
			final VideoTrailerModel video_trailer_model = (VideoTrailerModel) object;
			image_url = video_trailer_model.ImageURL;
		}
		ImageLoader.getInstance().displayImage(image_url, img_poster, ImageController.getOptionPlaceholder(true));
		lbl_title.setText(title);
	}

	private void doAction() {
		if (poster_view_callback == null) {
			return;
		}
		poster_view_callback.didPosterViewActioned(object);
	}
}