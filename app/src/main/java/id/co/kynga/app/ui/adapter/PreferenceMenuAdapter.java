package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.kynga.app.R;
import id.co.kynga.app.model.pojo.PrefMenuPojo;

/**
 * @author Aji Subastian
 */

public class PreferenceMenuAdapter extends BaseAdapter {

    private List<PrefMenuPojo> menuItemList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public PreferenceMenuAdapter(Context context, List<PrefMenuPojo> menuItemList) {
        layoutInflater = LayoutInflater.from(context);
        setMenuItemList(menuItemList);
    }

    public void setMenuItemList(List<PrefMenuPojo> prefMenuEntityList) {
        this.menuItemList = prefMenuEntityList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_pref_menu, parent, false);

            viewHolder.imagePrefMenu = (ImageView) convertView.findViewById(R.id.img_pref_menu);
            viewHolder.textPrefMenu = (TextView) convertView.findViewById(R.id.txt_pref_menu);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PrefMenuPojo menuItem = menuItemList.get(position);
        viewHolder.imagePrefMenu.setImageResource(menuItem.getImageRes());
        viewHolder.textPrefMenu.setText(menuItem.getMenuTitle());

        return convertView;
    }

    private class ViewHolder {

        private ImageView imagePrefMenu;
        private TextView textPrefMenu;

    }

}
