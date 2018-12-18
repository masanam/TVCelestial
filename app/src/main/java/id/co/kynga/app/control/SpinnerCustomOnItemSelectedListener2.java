package id.co.kynga.app.control;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import id.co.kynga.app.ui.activity.RegisterActivity_new2_box;

public class SpinnerCustomOnItemSelectedListener2 implements OnItemSelectedListener {

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
							   long id) {
/*
		Toast.makeText(parent.getContext(),
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
*/
		//RegisterActivity_new.checkSpinner(parent.getItemAtPosition(pos).toString());
		RegisterActivity_new2_box.checkSpinner(parent.getItemAtPosition(pos).toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}