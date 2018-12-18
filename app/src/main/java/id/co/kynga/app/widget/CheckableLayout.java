package id.co.kynga.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * CheckableLayout
 *
 * @author Aji Subastian
 */

public class CheckableLayout extends LinearLayout implements Checkable {

    private boolean checked;

    public CheckableLayout(Context context) {
        super(context);
    }

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        this.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        this.checked = !this.checked;
    }
}
