package id.co.kynga.app.ui.activity;

import android.net.TrafficStats;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.model.UserModel;


public class ProfileActivity extends CommonActivity {
	private ImageButton btn_back;
	private TextView lbl_fullname;
	private TextView lbl_phone_number;
	private TextView lbl_email;
	private TextView lbl_total_data_usage;
	private TextView lbl_mstar_data_usage;
	private long tx=0;
	private long rx=0;
	private long tx_Mstar=0;
	private long rx_Mstar=0;
	private long totalUsage=0;
	private long totalMstarUsage=0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setInitial();
	}

	private void setInitial() {
		tx= TrafficStats.getTotalTxBytes();
		rx=TrafficStats.getTotalRxBytes();
		totalUsage=tx+rx;
		tx_Mstar= TrafficStats.getUidTxBytes(Binder.getCallingUid());
		rx_Mstar= TrafficStats.getUidRxBytes(Binder.getCallingUid());
		totalMstarUsage=tx_Mstar+rx_Mstar;

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		lbl_fullname = (TextView) findViewById(R.id.lbl_fullname);
		lbl_phone_number = (TextView) findViewById(R.id.lbl_phone_number);
		lbl_email = (TextView) findViewById(R.id.lbl_email);
		lbl_total_data_usage = (TextView) findViewById(R.id.lbl_total_data_usage);
		lbl_mstar_data_usage = (TextView) findViewById(R.id.lbl_mstar_data_usage);
		setEventListener();
		populateData();
	}

	private void setEventListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private void populateData() {
		if (SessionControllerAppMStar.isSession()) {
			final UserModel user_model = SessionController.getUser();
			lbl_fullname.setText(user_model.Fullname);
			lbl_phone_number.setText(user_model.PhoneNumber);
			lbl_email.setText(user_model.Email);
			lbl_total_data_usage.setText(readableFileSize(totalUsage));
			lbl_mstar_data_usage.setText(readableFileSize(totalMstarUsage));
		}
	}

	public static String readableFileSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}