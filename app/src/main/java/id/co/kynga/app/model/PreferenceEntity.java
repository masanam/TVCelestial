package id.co.kynga.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PreferenceEntity {

	private SQLiteDatabase database;
	private DbHelper dbHelper;

	public PreferenceEntity(Context ctx) {
		dbHelper = new DbHelper(ctx);
		DatabaseManager.initializeInstance(dbHelper);
	}

	public void open() throws SQLException {
		database = DatabaseManager.getInstance().openDatabase();
	}

	public void close() {
		DatabaseManager.getInstance().closeDatabase();
	}

	public boolean insert(Preference preference) {

		ContentValues values = new ContentValues();
		values.put(DbHelper.NAME, preference.getName());
		values.put(DbHelper.VALUE, preference.getValue());
		long insertId = database.insert(DbHelper.TABLE_PREFERENCE, null, values);
		if (insertId != -1) {
			return true;

		}
		return false;

	}

	public String getPreference(String name) {
		String result = "";
		Cursor cursor = database.query(DbHelper.TABLE_PREFERENCE, new String[]{DbHelper.NAME, DbHelper.VALUE}, DbHelper.NAME + "=?", new String[]{name}, null, null, null);
		if (cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				result = cursor.getString(1);

			}
			cursor.close();
		}
		return result;

	}

	public boolean update(Preference preference) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.NAME, preference.getName());
		values.put(DbHelper.VALUE, preference.getValue());
		int update = database.update(DbHelper.TABLE_PREFERENCE, values, DbHelper.NAME + "=?", new String[]{preference.getName()});
		if (update != -1) {
			return true;
		}
		return false;
	}

	public boolean isDataSored(String name) {
		Cursor cursor = database.query(DbHelper.TABLE_PREFERENCE, new String[]{DbHelper.NAME, DbHelper.VALUE}, DbHelper.NAME + "=?", new String[]{name}, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.close();
			return true;
		}
		return false;
	}
}