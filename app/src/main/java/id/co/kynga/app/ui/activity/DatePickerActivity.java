package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import id.co.kynga.app.R;

public class DatePickerActivity extends Activity {
	DatePicker picker;
	Button displayDate;
	TextView textview1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_picker);
		
		textview1=(TextView)findViewById(R.id.textView1);
		picker=(DatePicker)findViewById(R.id.datePicker1);
		displayDate=(Button)findViewById(R.id.button1);
		
		textview1.setText(getCurrentDate());
		
		displayDate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				textview1.setText(getCurrentDate());
			}
			
		});
	}
    public String getCurrentDate(){
    	StringBuilder builder=new StringBuilder();
		builder.append("Current Date: ");
		builder.append((picker.getMonth() + 1)+"/");//month is 0 based
		builder.append(picker.getDayOfMonth()+"/");
		builder.append(picker.getYear());
		return builder.toString();
    }
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_date, menu);
		return true;
	}
*/
}
