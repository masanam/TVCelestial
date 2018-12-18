package id.co.kynga.app.general.controller.session;

import android.content.Context;
import android.content.SharedPreferences;

import id.co.kynga.app.general.controller.Config;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.model.LoginModel;
import id.co.kynga.app.model.LoginModelSimple;
import id.co.kynga.app.model.RegisterModel;
import id.co.kynga.app.model.RegisterModel2;
import id.co.kynga.app.model.UserModel;

import static id.co.kynga.app.KyngaApp.context;

public class SessionControllerAppMStar {
	//used when user not registered yet
	public static void open(final RegisterModel2 register_model2, final String phoneNumber) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.TOKEN_APP_MSTAR, register_model2.Token);
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR, phoneNumber);
		editor_app_mstar.apply();
	}
	//used when user not registered yet
	public static void open(final LoginModelSimple login_model_simple) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR, login_model_simple.PhoneNumber);
		editor_app_mstar.apply();
	}
	public static void open(final LoginModel login_model) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.TOKEN_APP_MSTAR, login_model.Token);
		editor_app_mstar.putString(SessionConfigAppMStar.FULLNAME_APP_MSTAR, login_model.Fullname);
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR, login_model.PhoneNumber);
		editor_app_mstar.putString(SessionConfigAppMStar.EMAIL_APP_MSTAR, login_model.Email);
		//editor.putString(SessionConfig.VANUMBER, login_model.VANumber);
		editor_app_mstar.putLong(SessionConfigAppMStar.CREDIT_APP_MSTAR, login_model.Credit);
		editor_app_mstar.apply();
	}

	public static void open(final RegisterModel register_model) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.TOKEN_APP_MSTAR, register_model.Token);
		editor_app_mstar.putString(SessionConfigAppMStar.FULLNAME_APP_MSTAR, register_model.Fullname);
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR, register_model.PhoneNumber);
		editor_app_mstar.putString(SessionConfigAppMStar.EMAIL_APP_MSTAR, register_model.Email);
		editor_app_mstar.apply();
	}

	public static void update(final UserModel user_model) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.FULLNAME_APP_MSTAR, user_model.Fullname);
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR, user_model.PhoneNumber);
		editor_app_mstar.putString(SessionConfigAppMStar.EMAIL_APP_MSTAR, user_model.Email);
		editor_app_mstar.putString(SessionConfigAppMStar.VANUMBER_APP_MSTAR, user_model.VANumber);
		editor_app_mstar.putLong(SessionConfigAppMStar.CREDIT_APP_MSTAR, user_model.Credit);
		editor_app_mstar.apply();
	}

	public static void setPhoneNumberVer(final String phone_number) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.putString(SessionConfigAppMStar.PHONENUMBERVER_APP_MSTAR, phone_number);
		editor_app_mstar.apply();
	}

	public static boolean isPhoneNumberVer() {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		return (GlobalController.isNotNull(preferences_app_mstar.getString(SessionConfigAppMStar.PHONENUMBERVER_APP_MSTAR, Config.text_blank)));
	}

	public static String getPhoneNumberVer() {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		return preferences_app_mstar.getString(SessionConfigAppMStar.PHONENUMBERVER_APP_MSTAR, Config.text_blank);
	}

	public static void close() {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor_app_mstar = preferences_app_mstar.edit();
		editor_app_mstar.clear();
		editor_app_mstar.apply();
	}

	public static boolean isSession() {
		return GlobalController.isNotNull(getStringFromSession(SessionConfigAppMStar.TOKEN_APP_MSTAR));
	}

	public static UserModel getUser() {
		final UserModel user_model_app_mstar = new UserModel();
		user_model_app_mstar.PhoneNumber = getStringFromSession(SessionConfigAppMStar.PHONENUMBER_APP_MSTAR);
		user_model_app_mstar.Fullname = getStringFromSession(SessionConfigAppMStar.FULLNAME_APP_MSTAR);
		user_model_app_mstar.Email = getStringFromSession(SessionConfigAppMStar.EMAIL_APP_MSTAR);
		user_model_app_mstar.VANumber = getStringFromSession(SessionConfigAppMStar.VANUMBER_APP_MSTAR);
		//user_model.Credit = getLongFromSession(SessionConfig.CREDIT);
		return user_model_app_mstar;
	}

	public static String getToken() {
		return getStringFromSession(SessionConfigAppMStar.TOKEN_APP_MSTAR);
	}

	private static final String getStringFromSession(final String id) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		if (GlobalController.isNotNull(preferences_app_mstar.getString(id, Config.text_blank))) {
			return preferences_app_mstar.getString(id, Config.text_blank);
		} else {
			return Config.text_blank;
		}
	}

	private static final long getLongFromSession(final String id) {
		SharedPreferences preferences_app_mstar = context.getSharedPreferences(SessionConfigAppMStar.SESSION_NAME_APP_MSTAR, Context.MODE_PRIVATE);
		return preferences_app_mstar.getLong(id, 0L);
	}

}