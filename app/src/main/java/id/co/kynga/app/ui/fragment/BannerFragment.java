package id.co.kynga.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import id.co.kynga.app.R;
import id.co.kynga.app.model.pojo.BannerPojo;

/**
 * BannerFragment
 *
 * @author Aji Subastian
 */

public class BannerFragment extends Fragment {

    private static final String ARG_PARAM = BannerFragment.class.getSimpleName() + ".DATA";

    private BannerPojo.Content obj;

    public BannerFragment() {
        // Required empty public constructor
    }

    public static BannerFragment newInstance(BannerPojo.Content content) {
        BannerFragment fragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            obj = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.image_banner);
        Picasso.with(getContext()).load(obj.ImageURL).placeholder(R.drawable.bg_default_banner3).into(iv);

        return view;
    }

}
