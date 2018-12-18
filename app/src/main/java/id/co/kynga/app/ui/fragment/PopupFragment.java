package id.co.kynga.app.ui.fragment;

import id.co.kynga.app.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class PopupFragment extends DialogFragment {
	private TextView tv;
	OnCancelListener onCancelListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View vi = inflater.inflate(R.layout.reg_failed_fragment, container, false);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		tv = (TextView) vi.findViewById(R.id.textError);
		final String message = getArguments().getString("message");
		tv.setText(message);
		return vi;
	}

	public interface OnCancelListener {
		void onCancel();
	}

	public void setOnCancelListener(OnCancelListener listener) {
		onCancelListener = listener;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (onCancelListener != null) {
			onCancelListener.onCancel();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (onCancelListener != null) {
			onCancelListener.onCancel();
		}
	}
}