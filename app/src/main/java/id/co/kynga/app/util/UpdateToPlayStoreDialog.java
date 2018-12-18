package id.co.kynga.app.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import id.co.kynga.app.R;


public class UpdateToPlayStoreDialog extends Dialog {

	public UpdateToPlayStoreDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dialog_update_playstore);
		this.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.getWindow().setBackgroundDrawableResource(R.color.transparan);
		Button retry = (Button) findViewById(R.id.no_data_dialog_ok);
		Button exit = (Button) findViewById(R.id.b_no_data_dialog_exit);
		retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onRestart();
				}
				UpdateToPlayStoreDialog.this.dismiss();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onResume();
				}
				UpdateToPlayStoreDialog.this.dismiss();
			}
		});
	}

	private ResumeListener listener;

	public interface ResumeListener {
		void onRestart();

		void onResume();
	}

	public void setListener(ResumeListener l) {
		listener = l;
	}
}