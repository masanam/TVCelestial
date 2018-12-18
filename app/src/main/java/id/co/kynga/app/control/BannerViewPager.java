package id.co.kynga.app.control;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class BannerViewPager extends ViewPager {
	public BannerViewPager(Context context) {
		super(context);
	}

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width / 1.7);

		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}