package id.co.kynga.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserEntity {
	private SQLiteDatabase database;
	private DbHelper dbHelper;

	public UserEntity(Context ctx) {
		dbHelper = new DbHelper(ctx);
		DatabaseManager.initializeInstance(dbHelper);
	}

	public void open() throws SQLException {
		database = DatabaseManager.getInstance().openDatabase();
	}

	public void close() {
		DatabaseManager.getInstance().closeDatabase();
	}

	public boolean insert(User user) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.NAME, user.getName());
		values.put(DbHelper.PHONE, user.getPhoneNumber());
		values.put(DbHelper.EMAIL, user.getEmail());
		values.put(DbHelper.PASSWORD, user.getPassword());
		values.put(DbHelper.ADDRESS, user.getAddress());
		values.put(DbHelper.GENDER, user.getGender());
		values.put(DbHelper.BIRTHDATE, user.getBirthdate());
		values.put(DbHelper.BALANCE, user.getBalance());
		long insertId = database.insert(DbHelper.TABLE_USER, null, values);
		if (insertId != -1) {
			Log.d("Db", "data inserted");
			return true;

		}
		return false;
	}
}