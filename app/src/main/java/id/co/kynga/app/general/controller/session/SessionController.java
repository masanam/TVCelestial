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

public class SessionController {
	//used when user not registered yet
	public static void open(final RegisterModel2 register_model2, final String phoneNumber) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.TOKEN, register_model2.Token);
		editor.putString(SessionConfig.PHONENUMBER, phoneNumber);
		editor.apply();
	}
	//used when user not registered yet
	public static void open(final LoginModelSimple login_model_simple) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.PHONENUMBER, login_model_simple.PhoneNumber);
		editor.apply();
	}
	public static void open(final LoginModel login_model) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.TOKEN, login_model.Token);
		editor.putString(SessionConfig.FULLNAME, login_model.Fullname);
		editor.putString(SessionConfig.PHONENUMBER, login_model.PhoneNumber);
		editor.putString(SessionConfig.EMAIL, login_model.Email);
		//editor.putString(SessionConfig.VANUMBER, login_model.VANumber);
		editor.putLong(SessionConfig.CREDIT, login_model.Credit);
		editor.apply();
	}

	public static void open(final RegisterModel register_model) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.TOKEN, register_model.Token);
		editor.putString(SessionConfig.FULLNAME, register_model.Fullname);
		editor.putString(SessionConfig.PHONENUMBER, register_model.PhoneNumber);
		editor.putString(SessionConfig.EMAIL, register_model.Email);
		editor.apply();
	}

	public static void update(final UserModel user_model) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.FULLNAME, user_model.Fullname);
		editor.putString(SessionConfig.PHONENUMBER, user_model.PhoneNumber);
		editor.putString(SessionConfig.EMAIL, user_model.Email);
		editor.putString(SessionConfig.VANUMBER, user_model.VANumber);
		editor.putLong(SessionConfig.CREDIT, user_model.Credit);
		editor.apply();
	}

	public static void setPhoneNumberVer(final String phone_number) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(SessionConfig.PHONENUMBERVER, phone_number);
		editor.apply();
	}

	public static boolean isPhoneNumberVer() {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		return (GlobalController.isNotNull(preferences.getString(SessionConfig.PHONENUMBERVER, Config.text_blank)));
	}

	public static String getPhoneNumberVer() {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		return preferences.getString(SessionConfig.PHONENUMBERVER, Config.text_blank);
	}

	public static void close() {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.apply();
	}

	public static boolean isSession() {
		return GlobalController.isNotNull(getStringFromSession(SessionConfig.TOKEN));
	}

	public static boolean isSessionAppMStar() {
		return GlobalController.isNotNull(getStringFromSession(SessionConfig.TOKEN));
	}


	public static UserModel getUser() {
		final UserModel user_model = new UserModel();
		user_model.PhoneNumber = getStringFromSession(SessionConfig.PHONENUMBER);
		user_model.Fullname = getStringFromSession(SessionConfig.FULLNAME);
		user_model.Email = getStringFromSession(SessionConfig.EMAIL);
		user_model.VANumber = getStringFromSession(SessionConfig.VANUMBER);
		//user_model.Credit = getLongFromSession(SessionConfig.CREDIT);
		return user_model;
	}

	public static String getToken() {
		return getStringFromSession(SessionConfig.TOKEN);
	}

	private static final String getStringFromSession(final String id) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		if (GlobalController.isNotNull(preferences.getString(id, Config.text_blank))) {
			return preferences.getString(id, Config.text_blank);
		} else {
			return Config.text_blank;
		}
	}

	private static final long getLongFromSession(final String id) {
		SharedPreferences preferences = context.getSharedPreferences(SessionConfig.SESSION_NAME, Context.MODE_PRIVATE);
		return preferences.getLong(id, 0L);
	}

}