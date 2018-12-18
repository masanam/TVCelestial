package id.co.kynga.app.util;

import id.co.kynga.app.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FourKDialog extends Dialog {
	private Button bDismiss;

	public FourKDialog(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.four_k_dialog);
		getWindow().setWindowAnimations(R.style.MyAnimation1);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		this.getWindow().setLayout(width, height);
		bDismiss = (Button) findViewById(R.id.bFourKDismiss);
		bDismiss.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FourKDialog.this.dismiss();
			}

		});
	}
}