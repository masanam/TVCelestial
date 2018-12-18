package id.co.kynga.app;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import com.crashlytics.android.Crashlytics;

import id.co.kynga.app.general.controller.GlobalVariables;
import io.fabric.sdk.android.Fabric;

public class KyngaApp extends MultiDexApplication {
	public static Context context;

	public KyngaApp() {
		super();
	}

	public static final String TAG = KyngaApp.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private static KyngaApp mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		mInstance = this;
		context = getApplicationContext();
		AnalyticsTrackers.initialize(context);
		GlobalVariables.load();
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}

	public static synchronized KyngaApp getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static Context getContext() {
		return context;
	}
}