package id.co.kynga.app.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.model.CategoryModel;
import id.co.kynga.app.model.ContentModel;

public class ContentView {
	public interface ContentViewCallback {
		void didContentViewActioned(final ContentModel content_model);
	}

	private LayoutInflater inflater;
	private View view;
	private TextView lbl_title;
	private LinearLayout lay_content;

	private CategoryModel category_model;

	private ContentViewCallback content_view_callback;

	public ContentView(
			final Activity activity,
			final CategoryModel category_model,
			final ContentViewCallback content_view_callback) {
		this.category_model = category_model;
		this.content_view_callback = content_view_callback;
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
		lbl_title.setText(category_model.Title);
		for (final ContentModel content_model : category_model.Content.list) {
			final PosterView vw_poster = new PosterView(inflater, content_model, new PosterView.PosterViewCallback() {
				@Override
				public void didPosterViewActioned(final Object object) {
					if (content_view_callback == null) {
						return;
					}
					if (object instanceof ContentModel) {
						content_view_callback.didContentViewActioned((ContentModel) object);
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