package id.co.kynga.app.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import id.co.kynga.app.model.pojo.BannerPojo;
import id.co.kynga.app.ui.fragment.BannerFragment;

/**
 * BannerAdapter
 *
 * @author Aji Subastian
 */

public class BannerAdapter extends FragmentStatePagerAdapter {

    private List<BannerPojo.Content> bannerList = new ArrayList<>();

    public BannerAdapter(FragmentManager fm, List<BannerPojo.Content> bannerList) {
        super(fm);
        setBannerList(bannerList);
    }

    public void setBannerList(List<BannerPojo.Content> bannerList) {
        this.bannerList = bannerList;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return BannerFragment.newInstance(bannerList.get(position));
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }
}
