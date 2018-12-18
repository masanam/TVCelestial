package id.co.kynga.app.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	private final static int DB_VERSION = 1;
	private static final String DATABASE_NAME = "nmedia";
	public static final String TABLE_VIEWER = "viewer";
	public static final String TABLE_PREFERENCE = "preference";
	public static final String ID = "id";
	public static final String USER_ID = "user_id";
	//public static final String USER_NAME= "username";
	public static final String MOVIE_ID = "movie_id";
	public static final String LAST_POSITION = "position";
	public static final String TABLE_SERIES = "series";
	public static final String SERIES_ID = "series_id";
	public static final String EPISODE_ID = "episode_id";
	
	/*
	 * USER
	 */
	public static final String TABLE_USER = "user";
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String GENDER = "gender";
	public static final String BIRTHDATE = "birthdate";
	public static final String ADDRESS = "address";
	public static final String BALANCE = "balance";
	public static final String MACID = "macid";
	public static final String VALUE = "value";
	
	String CREATE_TABLE_VIEWER = "CREATE TABLE " + TABLE_VIEWER + "(" + ID +
			" integer primary key autoincrement, " + USER_ID + " TEXT," +
			MOVIE_ID + " TEXT," + LAST_POSITION + " INTEGER DEFAULT 0 )"; 
	String CREATE_TABLE_SERIES_VIEWER = "CREATE TABLE " + TABLE_SERIES + "(" +
			ID + " integer primary key autoincrement," + SERIES_ID + " TEXT,"+
			EPISODE_ID + " TEXT," + LAST_POSITION + " INTEGER DEFAULT 0 )";
	String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" + ID  +
			" integer primary key autoincrement, " + PHONE + " TEXT," +
			NAME + " TEXT," + EMAIL + " TEXT," + PASSWORD + " TEXT, " + 
			GENDER + " TEXT," + ADDRESS + " TEXT," + BIRTHDATE + " TEXT," +
			BALANCE + " TEXT," + MACID + " TEXT )";
	String CREATE_TABLE_PREFERENCE = "CREATE TABLE " + TABLE_PREFERENCE + "(" + ID +
			" integer primary key autoincrement, " + NAME + " TEXT," +
			VALUE + " TEXT )";
	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_VIEWER);
		db.execSQL(CREATE_TABLE_USER);
		db.execSQL(CREATE_TABLE_PREFERENCE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEWER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREFERENCE);
		onCreate(db);
	}

}
