package id.co.kynga.app.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.co.kynga.app.KyngaApp;

public class PreferenceUtil {
	public void setPreference(String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public void clearAllPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.apply();
	}

	public void setPreferenceBoolean(String key, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public String getPreference(String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		return prefs.getString(key, "");
	}

	public boolean getPrefBoolean(String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		return prefs.getBoolean(key, false);
	}

	/**
	 * Added by Aji Subastian
	 *
	 * @param key String
	 * @param value String
	 */
	public void setPreferenceString(String key, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(KyngaApp.getContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.apply();
	}

}