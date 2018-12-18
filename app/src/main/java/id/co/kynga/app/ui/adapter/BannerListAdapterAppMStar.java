package id.co.kynga.app.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;


import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.ui.fragment.BannerFragmentAppMStar;


public class BannerListAdapterAppMStar extends FragmentStatePagerAdapter {
	private ArrayList<BannerModelAppMStar> banner_list;

	public BannerListAdapterAppMStar(FragmentManager fm, final ArrayList<BannerModelAppMStar> banner_list) {
		super(fm);
		this.banner_list = banner_list;
	}

	@Override
	public Fragment getItem(int position) {
		final BannerModelAppMStar banner_model = banner_list.get(position);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(BannerModelAppMStar.TAG, banner_model);
		final BannerFragmentAppMStar banner_fragment = new BannerFragmentAppMStar();
		banner_fragment.setArguments(bundle);
		return banner_fragment;
	}

	@Override
	public int getCount() {
		return banner_list.size();
	}
}