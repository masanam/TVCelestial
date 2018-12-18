package id.co.kynga.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.co.kynga.app.R;

/**
 * @author Aji Subastian
 */

public class PackageListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE = 0;
    private static final int IMAGE = 1;

    private List<Object> prefPackageList;

    public PackageListAdapter(List<Object> prefPackageList) {
        setPrefPackageItemList(prefPackageList);
    }

    public void setPrefPackageItemList(List<Object> prefPackageList) {
        this.prefPackageList = prefPackageList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TITLE:
                View viewTitle = layoutInflater.inflate(R.layout.item_package_title, parent, false);
                viewHolder = new PackageHolderTitle(viewTitle);
                break;
            case IMAGE:
                View viewImage = layoutInflater.inflate(R.layout.item_package_image, parent, false);
                viewHolder = new PackageHolderImage(viewImage);
                break;
            default:
                viewHolder = null;
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Get the object item
        Object object = prefPackageList.get(position);

        //Set object based on item view type
        switch (holder.getItemViewType()) {
            case TITLE:
                PackageHolderTitle holderTitle = (PackageHolderTitle) holder;
                String title = (String) object; //cast object to String
                //make sure that string is not empty to set into textview
                if (!TextUtils.isEmpty(title)) {
                    holderTitle.packageTitle.setText(title);
                }
                break;
            case IMAGE:
                PackageHolderImage holderImage = (PackageHolderImage) holder;
                int imageResource = (int) object; //cast object to Integer
                //make sure imageResource id is greater than zero
                if (imageResource > 0) {
                    holderImage.packageBuble.setImageResource(imageResource);
                }
                break;
            default:
                //if none specified, then skip the binding
                break;
        }
    }

    @Override
    public int getItemCount() {
        return prefPackageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (prefPackageList.get(position) instanceof String) {
            return TITLE;
        }
        else if(prefPackageList.get(position) instanceof Integer) {
            return IMAGE;
        }

        return -1;
    }
}
