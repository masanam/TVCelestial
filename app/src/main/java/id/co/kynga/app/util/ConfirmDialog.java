package id.co.kynga.app.util;

import id.co.kynga.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ConfirmDialog extends Dialog {
	private Button pCancel, pOK;
	OnPurchaseOkListener pListener;

	public ConfirmDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.confirm_dialog);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setWindowAnimations(R.style.MyAnimation2);
		pCancel = (Button) findViewById(R.id.bPurchaseCancel);
		pOK = (Button) findViewById(R.id.bPurchaseOk);
		pCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ConfirmDialog.this.dismiss();

			}

		});
		pOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pListener != null) {
					pListener.onPurchaseOk();
					ConfirmDialog.this.dismiss();
				}
			}

		});
	}

	public interface OnPurchaseOkListener {
		void onPurchaseOk();
	}

	public void setOnPurchaseOkListener(OnPurchaseOkListener listener) {
		pListener = listener;
	}
}