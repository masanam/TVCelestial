package id.co.kynga.app.util;

import id.co.kynga.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class NoDataDialog extends Dialog {

	public NoDataDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.no_data_dialog);
		this.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.getWindow().setBackgroundDrawableResource(R.color.transparan);
		Button retry = (Button) findViewById(R.id.no_data_dialog_ok);
		Button exit = (Button) findViewById(R.id.b_no_data_dialog_exit);
		retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onRetry();
				}
				NoDataDialog.this.dismiss();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onExit();
				}
				NoDataDialog.this.dismiss();
			}
		});
	}

	private NoDataListener listener;

	public interface NoDataListener {
		void onRetry();

		void onExit();
	}

	public void setListener(NoDataListener l) {
		listener = l;
	}
}