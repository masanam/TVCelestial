package id.co.kynga.app.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PosterLayout extends RelativeLayout {
	public PosterLayout(Context context) {
		super(context);
	}

	public PosterLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PosterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (height / 0.8);

		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}