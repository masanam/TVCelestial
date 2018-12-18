package id.co.kynga.app.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import id.co.kynga.app.model.BannerModel;
import id.co.kynga.app.ui.fragment.BannerFragment;

public class BannerListAdapter extends FragmentStatePagerAdapter {
	private ArrayList<BannerModel> banner_list;

	public BannerListAdapter(FragmentManager fm, final ArrayList<BannerModel> banner_list) {
		super(fm);
		this.banner_list = banner_list;
	}

	@Override
	public Fragment getItem(int position) {
		final BannerModel banner_model = banner_list.get(position);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(BannerModel.TAG, banner_model);
		final BannerFragment banner_fragment = new BannerFragment();
		banner_fragment.setArguments(bundle);
		return banner_fragment;
	}

	@Override
	public int getCount() {
		return banner_list.size();
	}
}