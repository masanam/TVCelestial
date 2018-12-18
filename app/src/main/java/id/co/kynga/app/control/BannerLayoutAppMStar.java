package id.co.kynga.app.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BannerLayoutAppMStar extends RelativeLayout {
	public BannerLayoutAppMStar(Context context) {
		super(context);
	}

	public BannerLayoutAppMStar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BannerLayoutAppMStar(Context context, AttributeSet attrs) {
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