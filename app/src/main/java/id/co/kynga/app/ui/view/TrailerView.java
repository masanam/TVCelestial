package id.co.kynga.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.VideoTrailerModel;

public class TrailerView {
	public interface TrailerViewCallback {
		void didTrailerViewActioned(final VideoTrailerModel video_trailer_model);
	}

	private LayoutInflater inflater;
	private View view;
	private TextView lbl_title;
	private LinearLayout lay_content;

	private VideoTrailerModel video_trailer_model;

	private TrailerViewCallback trailer_view_callback;

	public TrailerView(
			final Activity activity,
			final VideoTrailerModel video_trailer_model,
			final TrailerViewCallback trailer_view_callback) {
		this.video_trailer_model = video_trailer_model;
		this.trailer_view_callback = trailer_view_callback;
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
		lbl_title.setText(GlobalController.getString(R.string.label_trailer));
		for (final VideoTrailerModel video_trailer : video_trailer_model.list) {
			final PosterView vw_poster = new PosterView(inflater, video_trailer, new PosterView.PosterViewCallback() {
				@Override
				public void didPosterViewActioned(final Object object) {
					if (trailer_view_callback == null) {
						return;
					}
					if (object instanceof VideoTrailerModel) {
						trailer_view_callback.didTrailerViewActioned((VideoTrailerModel) object);
					}
				}
			});
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 10, 0);
			vw_poster.getView().setLayoutParams(lp);
			lay_content.addView(vw_poster.getView());
		}
	}
}