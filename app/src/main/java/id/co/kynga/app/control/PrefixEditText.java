package id.co.kynga.app.control;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class PrefixEditText extends AppCompatEditText {
	private ColorStateList mPrefixTextColor;

	public PrefixEditText(Context context) {
		this(context, null);
	}

	public PrefixEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public PrefixEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPrefixTextColor = getTextColors();
	}

	public void setPrefix(String prefix) {
		setCompoundDrawables(new TextDrawable(prefix), null, null, null);
	}

	public void setPrefixTextColor(int color) {
		mPrefixTextColor = ColorStateList.valueOf(color);
	}

	public void setPrefixTextColor(ColorStateList color) {
		mPrefixTextColor = color;
	}

	private class TextDrawable extends Drawable {
		private String mText = "";

		public TextDrawable(String text) {
			mText = text;
			setBounds(0, 0, (int) getPaint().measureText(mText) + 2, (int) getTextSize());
		}

		@Override
		public void draw(Canvas canvas) {
			Paint paint = getPaint();
			paint.setColor(mPrefixTextColor.getColorForState(getDrawableState(), 0));
			int lineBaseline = getLineBounds(0, null);
			canvas.drawText(mText, 0, canvas.getClipBounds().top + lineBaseline, paint);
		}

		@Override
		public void setAlpha(int alpha) {
		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {
		}

		@Override
		public int getOpacity() {
			return 1;
		}
	}
}