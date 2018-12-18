package id.co.kynga.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import id.co.kynga.app.R;

/**
 * @author Aji Subastian
 */

public class PackageHolderTitle extends RecyclerView.ViewHolder {

    public TextView packageTitle;

    public PackageHolderTitle(View itemView) {
        super(itemView);

        packageTitle = (TextView) itemView.findViewById(R.id.text_package_title);
    }

}
