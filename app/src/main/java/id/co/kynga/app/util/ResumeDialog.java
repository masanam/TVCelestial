package id.co.kynga.app.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import id.co.kynga.app.R;


public class ResumeDialog extends Dialog {

	ImageButton retry;
	ImageButton exit;

	public ResumeDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.resume_dialog);
		this.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.getWindow().setBackgroundDrawableResource(R.color.transparan);
		retry = (ImageButton) findViewById(R.id.no_data_dialog_ok);
		exit = (ImageButton) findViewById(R.id.b_no_data_dialog_exit);

		retry.setBackgroundResource(0);
		exit.setBackgroundResource(R.drawable.border_menu_focus);

		retry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean gainFocus) {
				retry.setBackgroundResource(R.drawable.border_menu_focus);
				exit.setBackgroundResource(0);
			}
		});

		exit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean gainFocus) {
				exit.setBackgroundResource(R.drawable.border_menu_focus);
				retry.setBackgroundResource(0);
			}
		});


		retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onRestart();
				}
				ResumeDialog.this.dismiss();
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onResume();
				}
				ResumeDialog.this.dismiss();
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