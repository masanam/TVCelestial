package id.co.kynga.app.general.controller;

import android.database.sqlite.SQLiteDatabase;

import id.co.kynga.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static id.co.kynga.app.KyngaApp.context;

public class GlobalVariables {
	public static SQLiteDatabase sql_connection;
	public static void load() {

		ImageController.initLoader(context);
		initFont();
	}

	private static void initFont() {
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Avenir-Book.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
	}
}