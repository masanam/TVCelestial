package id.co.kynga.app.ui.fragment;

import id.co.kynga.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TopupFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View vi = inflater.inflate(R.layout.topup_fragment, container, false);
		return vi;
	}
}