package id.co.kynga.app.general.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Locale;
import java.util.regex.Pattern;

import id.co.kynga.app.BuildConfig;
import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;

import static id.co.kynga.app.KyngaApp.context;


public class GlobalController {

	public interface AlertCallback {
		void didAlertButton1();

		void didAlertButton2();
	}

	private static MaterialDialog loading;
	private static MaterialDialog alert_dialog;
	private static Toast toast;
	public static String getString(final int resource_id) {
		return KyngaApp.getContext().getString(resource_id);
	}

	public static boolean isLandscape() {
		return context.getResources().getBoolean(R.bool.landscape);
	}

	public static boolean isNotNull(final String value) {
		if (value == null) {
			return false;
		}
		if (value.trim().equals(Config.text_blank)) {
			return false;
		} else {
			return true;
		}
	}

	public static void showLoading(final Activity activity) {
		if (activity == null) {
			return;
		}
		if (loading != null) {
			return;
		}
		loading = new MaterialDialog.Builder(
				new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
				.title(GlobalController.getString(R.string.app_name))
				.content(GlobalController.getString(R.string.message_loading))
				.progress(true, 0)
				.cancelable(false)
				.show();
	}

	public static void closeLoading() {
		if (loading == null) {
			return;
		}
		try {
			loading.cancel();
			loading.dismiss();
			loading = null;
		} catch (IllegalArgumentException ex) {
		}
	}

	@SuppressLint("ShowToast")
	public static void showToast(final String value, final int delay) {
		if (toast != null) {
			if (toast.getView().isShown()) {
				return;
			}
		}
		toast = Toast.makeText(context, value, delay);
		toast.show();
	}

	public static void showRequestFailedWarning(final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.title_warning))
						.content(GlobalController.getString(R.string.error_request_failed))
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showRequestFailedWarning(final Activity activity, final AlertCallback alert_callback) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.title_warning))
						.content(GlobalController.getString(R.string.error_request_failed))
						.positiveText(GlobalController.getString(R.string.button_close))
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton1();
								}
							}
						})
						.show();
			}
		});
	}

	public static String getVersionNameAppMStar() {
		String version = Config.text_blank;
		try {
			final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = info.versionName;
		} catch (PackageManager.NameNotFoundException ex) {
			ex.printStackTrace();
		}
		if (!GlobalController.isNotNull(version)) {
			version = BuildConfig.VERSION_NAME;
		}
		version = "v." + version;
		return version;
	}

	public static void closeAlert() {
		if (alert_dialog == null) {
			return;
		}
		alert_dialog.dismiss();
		alert_dialog.cancel();
		alert_dialog = null;
	}

	public static void showWarning(
			final Activity activity,
			final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.title_warning))
						.content(message)
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showWarning(
			final Activity activity,
			final int resource_id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.title_warning))
						.content(GlobalController.getString(resource_id))
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showWarning(
			final Activity activity,
			final String message,
			final AlertCallback alert_callback) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.title_warning))
						.content(message)
						.positiveText(GlobalController.getString(R.string.button_close))
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton1();
								}
							}
						})
						.show();
			}
		});
	}

	public static void showQuestion(
			final Activity activity,
			final String message,
			final AlertCallback alert_callback) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(message)
						.positiveText(GlobalController.getString(R.string.button_yes))
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton1();
								}
							}
						})
						.negativeText(GlobalController.getString(R.string.button_no))
						.onNegative(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton2();
								}
							}
						})
						.show();
			}
		});
	}

	public static void showQuestion(
			final Activity activity,
			final int resource_id,
			final AlertCallback alert_callback) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(GlobalController.getString(resource_id))
						.positiveText(GlobalController.getString(R.string.button_yes))
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton1();
								}
							}
						})
						.negativeText(GlobalController.getString(R.string.button_no))
						.onNegative(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton2();
								}
							}
						})
						.show();
			}
		});
	}

	public static boolean isEmail(final String text) {
		final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
				"[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
						"\\@" +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
						"(" +
						"\\." +
						"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
						")+");
		return EMAIL_ADDRESS_PATTERN.matcher(text).matches();
	}

	public static void showComingSoon(
			final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(GlobalController.getString(R.string.message_coming_soon))
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showSuccessMessage(
			final Activity activity) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(GlobalController.getString(R.string.message_success))
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showMessage(
			final Activity activity,
			final String message,
			final AlertCallback alert_callback) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(message)
						.positiveText(GlobalController.getString(R.string.button_close))
						.onPositive(new MaterialDialog.SingleButtonCallback() {
							@Override
							public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
								if (alert_callback != null) {
									alert_callback.didAlertButton1();
								}
							}
						})
						.show();
			}
		});
	}

	public static void showMessage(
			final Activity activity,
			final String message) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(message)
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void showMessage(
			final Activity activity,
			final int resource_id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				closeAlert();
				alert_dialog = new MaterialDialog.Builder(new ContextThemeWrapper(activity, R.style.MaterialDrawerTheme_mstar_Light_DarkToolbar_TranslucentStatus))
						.title(GlobalController.getString(R.string.app_name))
						.content(GlobalController.getString(resource_id))
						.positiveText(GlobalController.getString(R.string.button_close))
						.show();
			}
		});
	}

	public static void changeLocale(final String language) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		android.content.res.Configuration conf = context.getResources().getConfiguration();
		final Locale locale = new Locale(language);
		Locale.setDefault(locale);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			context.getResources().getConfiguration().setLocale(locale);
		} else {
			conf.locale = locale;
		}
		context.getResources().updateConfiguration(conf, dm);
	}


}