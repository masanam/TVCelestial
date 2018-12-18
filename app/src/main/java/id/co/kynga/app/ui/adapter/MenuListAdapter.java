package id.co.kynga.app.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.MenuController;
import id.co.kynga.app.general.controller.session.SessionControllerAppMStar;
import id.co.kynga.app.model.UserModel;
import id.co.kynga.app.ui.activity.MainActivityAppMStar;


public class MenuListAdapter extends BaseAdapter {
	public interface MenuListAdapterCallback {
		void didMenuListAdapterActioned(final int index);

		void didMenuListAdapterBack();
	}

	private class ViewHolder {
		TextView lbl_menu;
	}

	private LayoutInflater inflater;
	private String[] menu_list;
	private MenuListAdapterCallback menu_list_adapter_callback;

	public MenuListAdapter(
			final String[] menu_list,
			final MenuListAdapterCallback menu_list_adapter_callback) {
		inflater = (LayoutInflater) MainActivityAppMStar.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.menu_list = menu_list;
		this.menu_list_adapter_callback = menu_list_adapter_callback;
	}

	@Override
	public int getCount() {
		if (MenuController.getMenuType() == MenuController.MenuType.HomeMenuType) {
			return menu_list.length;
		} else {
			return menu_list.length + 1;
		}
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.list_menu, null);
			holder.lbl_menu = (TextView) view.findViewById(R.id.lbl_menu);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (MenuController.getMenuType() == MenuController.MenuType.HomeMenuType) {
			holder.lbl_menu.setText(menu_list[i]);
			holder.lbl_menu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (menu_list_adapter_callback != null) {
						menu_list_adapter_callback.didMenuListAdapterActioned(i);
					}
				}
			});
		} else {
			/*if (MenuController.getMenuType() == MenuController.MenuType.WalletMenuType) {
				if (i == 0) {
					final UserModel user_model = SessionControllerAppMStar.getUser();
					//holder.lbl_menu.setText(NumberController.formatCurrency(user_model.Credit));
					holder.lbl_menu.setBackgroundColor(Color.TRANSPARENT);
					return view;
				}
			}*/
			if (i >= menu_list.length) {
				holder.lbl_menu.setText(GlobalController.getString(R.string.menu_back));
				holder.lbl_menu.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (menu_list_adapter_callback != null) {
							menu_list_adapter_callback.didMenuListAdapterBack();
						}
					}
				});
			} else {
				holder.lbl_menu.setText(menu_list[i]);
				holder.lbl_menu.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (menu_list_adapter_callback != null) {
							menu_list_adapter_callback.didMenuListAdapterActioned(i);
						}
					}
				});
			}
		}

		return view;
	}
}