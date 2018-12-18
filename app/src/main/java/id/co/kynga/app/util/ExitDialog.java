package id.co.kynga.app.util;

import id.co.kynga.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ExitDialog extends Dialog {
	private Button b1, b2;
	OnExitListener onExitListener;

	public ExitDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.exit_dialog);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawableResource(R.color.transparan);
		b1 = (Button) findViewById(R.id.exitDialogCancel);
		b2 = (Button) findViewById(R.id.exitDialogOk);
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitDialog.this.dismiss();
			}

		});
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onExitListener != null) {
					onExitListener.exitApp();
					ExitDialog.this.dismiss();
				}
			}

		});
	}

	public interface OnExitListener {
		void exitApp();
	}

	public void setOnExitListener(OnExitListener listener) {
		onExitListener = listener;
	}
}