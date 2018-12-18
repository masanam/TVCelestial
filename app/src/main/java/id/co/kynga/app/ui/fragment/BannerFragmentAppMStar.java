package id.co.kynga.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import id.co.kynga.app.R;
import id.co.kynga.app.control.RecyclingImageView;
import id.co.kynga.app.general.controller.ImageController;
import id.co.kynga.app.model.BannerModelAppMStar;
import id.co.kynga.app.ui.activity.MainActivityAppMStar;

public class BannerFragmentAppMStar extends Fragment {
	private RecyclingImageView img_banner;
	private Button btn_action;
	private BannerModelAppMStar banner_model;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		try {
			final View view = inflater.inflate(R.layout.fragment_banner_app_mstar, container, false);
			setInitial(view);
			return view;
		} catch (InflateException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void setInitial(final View view) {
		banner_model = getArguments().getParcelable(BannerModelAppMStar.TAG);
		img_banner = (RecyclingImageView) view.findViewById(R.id.img_banner);
		btn_action = (Button) view.findViewById(R.id.btn_action);
		setEventListener();
		populateData();
	}

	private void setEventListener() {
		btn_action.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doAction();
			}
		});
	}

	private void doAction() {
		if (MainActivityAppMStar.instance == null) {
			return;
		}
		MainActivityAppMStar.instance.onBannerClicked(banner_model);
	}

	private void populateData() {
		ImageLoader.getInstance().displayImage(banner_model.ImageURL, img_banner, ImageController.getOption(true));
	}
}