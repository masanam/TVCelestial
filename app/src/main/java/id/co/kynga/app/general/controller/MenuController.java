package id.co.kynga.app.general.controller;

import android.content.Intent;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.ui.activity.LogoutActivityAppMStar;
import id.co.kynga.app.ui.activity.MainActivityAppMStar;
import id.co.kynga.app.ui.activity.SubscriptionListActivity;
import id.co.kynga.app.ui.adapter.MenuListAdapter;

import static id.co.kynga.app.KyngaApp.context;
import static id.co.kynga.app.KyngaApp.getContext;
import static id.co.kynga.app.KyngaApp.getInstance;
import static id.co.kynga.app.ui.activity.MainActivityAppMStar.instance;

public class MenuController {
	public enum MenuType {
		HomeMenuType(0),
		ProfileMenuType(1),
		SettingsMenuType(2),
		/*WalletMenuType(3),
		SubscriptionMenuType(4),*/
		LogoutMenuType(3);
		private final int id;

		MenuType(final int id) {
			this.id = id;
		}

		public final int getValue() {
			return id;
		}
	}

	public enum SettingsMenuType {
		NoneSettingsMenuType(-1),
		LanguageSettingsMenuType(0),
		FAQSettingsMenuType(1),
		PolicySettingsMenuType(2),
		TOCSettingsMenuType(3);
		//ParentalSettingsMenuType(4);
		private final int id;

		SettingsMenuType(final int id) {
			this.id = id;
		}

		public final int getValue() {
			return id;
		}
	}

	public enum WalletMenuType {
		NoneWalletMenuType(0),
		BalanceWalletMenuType(1),
		TopupWalletMenuType(2),
		PaymentWalletMenuType(3),
		PaymentWalletMenuType2(4);
		private final int id;

		WalletMenuType(final int id) {
			this.id = id;
		}

		public final int getValue() {
			return id;
		}
	}

	public enum SubscriptionMenuType {
		NoneSubscriptionMenuType(0),
		BuySubscriptionMenuType(1),
		StatusSubscriptionMenuType(2);
		private final int id;

		SubscriptionMenuType(final int id) {
			this.id = id;
		}

		public final int getValue() {
			return id;
		}
	}

	public enum LanguageMenuType {
		IDLanguageMenuType(0),
		ENLanguageMenuType(1);
		private final int id;

		LanguageMenuType(final int id) {
			this.id = id;
		}

		public final int getValue() {
			return id;
		}
	}

	public static MenuType menu_type = MenuType.HomeMenuType;
	public static SettingsMenuType settings_menu_type = SettingsMenuType.NoneSettingsMenuType;
	public static WalletMenuType wallet_menu_type = WalletMenuType.NoneWalletMenuType;
	public static SubscriptionMenuType subscription_menu_type = SubscriptionMenuType.NoneSubscriptionMenuType;

	private ListView lst_menu;
	private TextView lbl_version;
	private MenuListAdapter menu_list_adapter;

	private String[] menu_list;

	public MenuController() {
		setInitial();
	}

	public static MenuType getMenuType() {
		return menu_type;
	}

	private void setInitial() {
		lst_menu = (ListView) instance.findViewById(R.id.lst_menu);
		lbl_version = (TextView) instance.findViewById(R.id.lbl_version);
		menu_list = context.getResources().getStringArray(R.array.menu);
		populateData();
		lbl_version.setText(GlobalController.getVersionNameAppMStar());
	}

	private void doSelect(final int index) {
		if (menu_type == MenuType.HomeMenuType) {
			if (index == MenuType.HomeMenuType.getValue()) {
				instance.closeDrawer();
			} else if (index == MenuType.ProfileMenuType.getValue()) {
				instance.closeDrawer();
				AppController.openProfileActivityAppMStar(instance);
			} else if (index == MenuType.SettingsMenuType.getValue()) {
				menu_type = MenuType.SettingsMenuType;
				menu_list = context.getResources().getStringArray(R.array.menu_settings);
				populateData();
			} /*else if (index == MenuType.WalletMenuType.getValue()) {
				menu_type = MenuType.WalletMenuType;
				menu_list = context.getResources().getStringArray(R.array.menu_wallet);
				populateData();
			} else if (index == MenuType.SubscriptionMenuType.getValue()) {
				menu_type = MenuType.SubscriptionMenuType;
				menu_list = context.getResources().getStringArray(R.array.menu_subscription);
				populateData();
			}*/
			else if (index == MenuType.LogoutMenuType.getValue()) {
				//MainActivity.instance.closeDrawer();
				//AppController.signout(MainActivity.instance);
/*
				android.app.AlertDialog.Builder  builder = new android.app.AlertDialog.Builder(instance);
				//builder.setTitle(String.valueOf(keyCode));
				builder.setMessage("DO YOU WANT TO LOGOUT?");
				builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						instance.closeDrawer();
						AppController.signout(instance);
					}
				});

				builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//No action
					}
				});
				android.app.AlertDialog alert11 = builder.create();
				alert11.show();
				*/


				GlobalController.showQuestion(instance, R.string.message_exit_confirm, new GlobalController.AlertCallback() {
					@Override
					public void didAlertButton1() {
						//AppController.signoutAppMStar(instance); kenapa instance == null?
						//instance.closeDrawer();
						final Intent intent = new Intent(instance, LogoutActivityAppMStar.class);
						instance.startActivity(intent);
					}

					@Override
					public void didAlertButton2() {
					}
				});

			}
		} else if (menu_type == MenuType.SettingsMenuType) {
			if (settings_menu_type == SettingsMenuType.LanguageSettingsMenuType) {
				if (index == LanguageMenuType.IDLanguageMenuType.getValue()) {
					GlobalController.changeLocale("id_ID");
					resetMenu();
				} else if (index == LanguageMenuType.ENLanguageMenuType.getValue()) {
					GlobalController.changeLocale("en_US");
					resetMenu();
				}
				instance.closeDrawer();
				return;
			}
			if (index == SettingsMenuType.LanguageSettingsMenuType.getValue()) {
				settings_menu_type = SettingsMenuType.LanguageSettingsMenuType;
				menu_list = context.getResources().getStringArray(R.array.menu_language);
				populateData();
			} else if (index == SettingsMenuType.FAQSettingsMenuType.getValue()) {
				resetMenu();
				AppController.openFAQActivity(instance);
				instance.closeDrawer();
			} else if (index == SettingsMenuType.PolicySettingsMenuType.getValue()) {
				resetMenu();
                AppController.openPolicyActivity(instance);
                instance.closeDrawer();
			} else if (index == SettingsMenuType.TOCSettingsMenuType.getValue()) {
				resetMenu();
				AppController.openTCActivity(instance);
				instance.closeDrawer();
			}
			/*
				else if (index == SettingsMenuType.ParentalSettingsMenuType.getValue()) {
				resetMenu();
				AppController.openTCActivity(instance);
				instance.closeDrawer();
			}
			*/
		} /*else if (menu_type == MenuType.WalletMenuType) {
*//*
			Toast toast = Toast.makeText(MStarApp.getAppContext(),String.valueOf(index), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			*//*
			if (index == WalletMenuType.BalanceWalletMenuType.getValue()) {
				resetMenu();
				AppController.openBalanceActivityAppMStar(instance);
				instance.closeDrawer();
			}

		*//* dicomment malam minggu
			else if (index == WalletMenuType.TopupWalletMenuType.getValue()) {
				resetMenu();
				AppController.openTopupActivity(instance);
				instance.closeDrawer();
			} else if (index == WalletMenuType.PaymentWalletMenuType.getValue()) {
				resetMenu();
				AppController.openPaymentActivity(instance);
				//AppController.openPaymentActivitySnap(instance);
				instance.closeDrawer();
			}else if (index == WalletMenuType.PaymentWalletMenuType2.getValue()) {
				resetMenu();
				AppController.openPaymentActivity2(instance);
				//AppController.openPaymentActivitySnap(instance);
				instance.closeDrawer();
			}

		}
		else if (menu_type == MenuType.SubscriptionMenuType) {
			if (index == SubscriptionMenuType.BuySubscriptionMenuType.getValue()) {
				resetMenu();
				AppController.openBuySubscriptionFromMenuActivity(instance);
				instance.closeDrawer();
			} else if (index == SubscriptionMenuType.StatusSubscriptionMenuType.getValue()) {
				resetMenu();
				AppController.openSubscriptionListActivity(instance);
				instance.closeDrawer();
			}
			*//*
		}*/

	}

	private void populateData() {
		instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				menu_list_adapter = new MenuListAdapter(menu_list, new MenuListAdapter.MenuListAdapterCallback() {
					@Override
					public void didMenuListAdapterActioned(int index) {
						//doSelect(index);
						//Walet and blockbuster subscription
						/*if (index == 3 || index == 4){
							//GlobalController.showComingSoon(MStarApp.getAppContext());
							//Toast.makeText(MStarApp.getAppContext(), "Coming soon...", Toast.LENGTH_LONG).show();
							Toast toast = Toast.makeText(context,"Coming soon", Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}else{
							doSelect(index);
						}*/
                        doSelect(index);

					}

					@Override
					public void didMenuListAdapterBack() {
						if (menu_type == MenuType.SettingsMenuType) {
							if (settings_menu_type == SettingsMenuType.NoneSettingsMenuType) {
								resetMenu();
							} else if (settings_menu_type == SettingsMenuType.LanguageSettingsMenuType) {
								settings_menu_type = SettingsMenuType.NoneSettingsMenuType;
								menu_list = context.getResources().getStringArray(R.array.menu_settings);
								populateData();
							}
						} /*else if (menu_type == MenuType.WalletMenuType) {
							resetMenu();
						}
						else if (menu_type == MenuType.SubscriptionMenuType) {
							resetMenu();
						}*/
					}
				});
				lst_menu.setAdapter(menu_list_adapter);
			}
		});
	}

	private void resetMenu() {
		menu_type = MenuType.HomeMenuType;
		settings_menu_type = SettingsMenuType.NoneSettingsMenuType;
		/*wallet_menu_type = WalletMenuType.NoneWalletMenuType;
		subscription_menu_type = SubscriptionMenuType.NoneSubscriptionMenuType;*/
		menu_list = context.getResources().getStringArray(R.array.menu);
		populateData();
	}
}