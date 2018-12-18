package id.co.kynga.app.ui.fragment;

import java.util.List;

import id.co.kynga.app.R;
//import id.co.kynga.app.util.MyBrowser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PrefFragment extends Fragment{
	private LinearLayout wifi,browser,file,sound,lang,display;
	//private PreferenceUtil pref;
	//private boolean loggedin;
	//private TextView tLogin;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle arg0){
		super.onCreateView(inflater, container, arg0);
		View vi = inflater.inflate(R.layout.pref_fragment,container,false);
		//pref = new PreferenceUtil(getActivity());
		//tLogin = (TextView)vi.findViewById(R.id.textLogin);
		//loggedin = pref.getPrefBoolean(DataVariable.ACTION_LOGIN);
		
		lang = (LinearLayout)vi.findViewById(R.id.langLayout);
		display = (LinearLayout)vi.findViewById(R.id.displayLayout);
		wifi = (LinearLayout)vi.findViewById(R.id.wLayout);
		sound = (LinearLayout)vi.findViewById(R.id.soundLayout);
		file = (LinearLayout)vi.findViewById(R.id.fLayout);
		browser = (LinearLayout)vi.findViewById(R.id.bLayout);
		//user = (LinearLayout)vi.findViewById(R.id.uLayout);
		
		lang.setOnClickListener(prefListener);
		display.setOnClickListener(prefListener);
		wifi.setOnClickListener(prefListener);	
		sound.setOnClickListener(prefListener);			
		file.setOnClickListener(prefListener);
		browser.setOnClickListener(prefListener);
		//user.setOnClickListener(prefListener);
		return vi;
	}
	
	private View.OnClickListener prefListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = null;
			Boolean isAppInstalled = false;
			List<PackageInfo> gapps = getActivity().getPackageManager().getInstalledPackages(0);
			switch (v.getId()){
			
			case R.id.langLayout:
				i=new Intent(Settings.ACTION_LOCALE_SETTINGS);
		        startActivity(i);
			break;
			case R.id.displayLayout:
				i=new Intent(Settings.ACTION_DISPLAY_SETTINGS);
		        startActivity(i);
		      break;
			case R.id.wLayout:
				i = new Intent(Settings.ACTION_WIFI_SETTINGS);
		        startActivity(i);
		      break;
			case R.id.soundLayout:
				i=new Intent(Settings.ACTION_SOUND_SETTINGS);
		        startActivity(i);
		        break;
		        
			case R.id.fLayout:
				for(int k=0;k<gapps.size();k++) {
					PackageInfo p = gapps.get(k);
					if(p.packageName.equals("com.softwinner.TvdFileManager")){
						isAppInstalled = true;
						
					}
				}
				
				if(isAppInstalled){
					i = getActivity().getPackageManager().getLaunchIntentForPackage("com.softwinner.TvdFileManager");
		             startActivity(i);
				}else{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					
					b.setTitle("No Application installed");
					
					b.setPositiveButton("OK", new Dialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dial, int arg1) {
							// TODO Auto-generated method stub
							dial.dismiss();
						}
						
					});
					b.create().show();
					
				}
			break;
			case R.id.bLayout:
				for(int j=0;j<gapps.size();j++) {
					PackageInfo p = gapps.get(j);
					if(p.packageName.equals("com.android.browser")){
						isAppInstalled = true;
						
					}
				}
				
				if(isAppInstalled){
					i = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.browser");
		             startActivity(i);
				}else{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					
					b.setTitle("No Application installed");
					
					b.setPositiveButton("OK", new Dialog.OnClickListener(){

						@Override
						public void onClick(DialogInterface dial, int arg1) {
							// TODO Auto-generated method stub
							dial.dismiss();
						}
						
					});
					b.create().show();
					
				}
				break;
			
			}
		}
		
	};

}
/*
 * Author
 * Sigit Budirahardjo
 * sigitbudirahardjo@gmail.com
 * 
 */

