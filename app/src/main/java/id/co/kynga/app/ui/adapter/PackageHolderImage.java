package id.co.kynga.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import id.co.kynga.app.R;

/**
 * @author Aji Subastian
 */

public class PackageHolderImage extends RecyclerView.ViewHolder {

    public ImageView packageBuble;

    public PackageHolderImage(View itemView) {
        super(itemView);

        packageBuble = (ImageView) itemView.findViewById(R.id.image_package_buble);
    }

}
