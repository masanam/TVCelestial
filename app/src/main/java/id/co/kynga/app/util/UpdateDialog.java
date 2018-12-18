package id.co.kynga.app.util;

import id.co.kynga.app.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class UpdateDialog extends Dialog{

	OnUpdateConfirmedListener onUpdateConfirmedListener;
	private Button bCancel,bOK;
	private TextView messageView, titleView;
	private String title;
	private String message;

	public UpdateDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

    public UpdateDialog(Context context, String title, String message) {
        super(context);
        this.title = title;
        this.message = message;
    }

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update_dialog);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        titleView = (TextView) findViewById(R.id.textView1);
        messageView = (TextView) findViewById(R.id.textView2);
        titleView.setText(title);
        messageView.setText(message);

		bCancel = (Button)findViewById(R.id.bUpdateCancel);
		bOK = (Button)findViewById(R.id.bUpdateOk);
		bCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateDialog.this.dismiss();
			}
		});
		bOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(onUpdateConfirmedListener!=null){
					onUpdateConfirmedListener.onConfirm();
				}
				UpdateDialog.this.dismiss();
			}
		});
		
	}
	
	public static abstract interface OnUpdateConfirmedListener{
		void onConfirm();
	}
	public void setOnUpdateConfirmed(OnUpdateConfirmedListener listener){
		onUpdateConfirmedListener = listener;
	}
}
