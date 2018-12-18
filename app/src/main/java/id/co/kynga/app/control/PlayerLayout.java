package id.co.kynga.app.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class PlayerLayout extends FrameLayout {
	public PlayerLayout(Context context) {
		super(context);
	}

	public PlayerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PlayerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (width * 0.8);
		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}

}