package id.co.kynga.app.ui.fragment;

import id.co.kynga.app.R;
import id.co.kynga.app.model.User;
import id.co.kynga.app.util.Utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	private TextView a,b,c,d,e,f,g;
	private Button change;
	//private PreferenceUtil pref;
	LayoutInflater infl;
	//private LinearLayout l1;
	@SuppressLint("DefaultLocale")
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,
			Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		//pref = new PreferenceUtil(getActivity());
		View vi = inflater.inflate(R.layout.profile, container,false);
		a = (TextView)vi.findViewById(R.id.tName);
		b = (TextView)vi.findViewById(R.id.tEmail);
		c = (TextView)vi.findViewById(R.id.tMacId);
		d = (TextView)vi.findViewById(R.id.tGender);
		e = (TextView)vi.findViewById(R.id.tBirthdate);
		f = (TextView)vi.findViewById(R.id.tPhoneNumber);
		g = (TextView)vi.findViewById(R.id.tAddress);
		User user = User.getInstance();
		a.setText(user.getName().toLowerCase());
		b.setText(user.getEmail().toLowerCase());
		c.setText(Utils.getMACAddress("wlan0"));
		d.setText(user.getGender().toLowerCase());
		e.setText(user.getBirthdate().toLowerCase());
		f.setText(user.getPhoneNumber().toLowerCase());
		g.setText(user.getAddress().toLowerCase());
		change = (Button)vi.findViewById(R.id.bEditProfile);
		change.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ChangeProfile cp = new ChangeProfile();
				cp.show(getChildFragmentManager(), "Change Profile");
			}
			
		});
		return vi;
		
	}
	
}
/*
 * Author
 * Sigit Budirahardjo
 * sigitbudirahardjo@gmail.com
 * 
 */

