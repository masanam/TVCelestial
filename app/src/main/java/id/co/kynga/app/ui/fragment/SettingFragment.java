package id.co.kynga.app.ui.fragment;

import java.util.List;

import id.co.kynga.app.R;

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
//import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SettingFragment extends Fragment{
	private LinearLayout lSetting,lBrowser;
	private ViewGroup root;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);	
		View vi = inflater.inflate(R.layout.setting_fragment, container,false);
		root = container;
		int gridWidth = root.getWidth();
		int itemWidth = (int) ((gridWidth - 60) / 4) ;
		//int itemHeight = (int)(itemWidth * 0.74);
		ImageView img1 = (ImageView)vi.findViewById(R.id.imageView1);
		ImageView img2 = (ImageView)vi.findViewById(R.id.imageView2);
		LinearLayout.LayoutParams iParams = new LinearLayout.LayoutParams(itemWidth,itemWidth);
		img1.setLayoutParams(iParams);
		img2.setLayoutParams(iParams);
		lSetting = (LinearLayout)vi.findViewById(R.id.systemSetting);
		lBrowser = (LinearLayout)vi.findViewById(R.id.browserSetting);
		lSetting.setOnClickListener(onClickListener);
		lBrowser.setOnClickListener(onClickListener);
		return vi;
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			List<PackageInfo> gapps = getActivity().getPackageManager().getInstalledPackages(0);
			boolean isAppInstalled = false;
			Intent i = null;
			switch(v.getId()){
			case R.id.systemSetting:
				i = new Intent(Settings.ACTION_WIFI_SETTINGS);
				startActivity(i);
				break;
			case R.id.browserSetting:
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
