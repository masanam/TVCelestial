package id.co.kynga.app.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ar.libgdx.Engine;
import ar.vuforia.AppSession;
import ar.vuforia.SessionControl;
import ar.vuforia.VuforiaException;
import ar.vuforia.VuforiaRenderer;
import com.vuforia.CameraDevice;
import com.vuforia.DataSet;
import com.vuforia.HINT;
import com.vuforia.ObjectTracker;
import com.vuforia.STORAGE_TYPE;
import com.vuforia.State;
import com.vuforia.Tracker;
import com.vuforia.TrackerManager;
import com.vuforia.Vuforia;

import id.co.kynga.app.R;

public class AndroidLauncher extends AndroidApplication implements SessionControl {

	private static final String LOGTAG = "MAIN ABC";

	private AppSession session;

	private DataSet posterDataSet;
	private Engine mEngine;

	VuforiaRenderer mRenderer;

	String objName, texName, markerName, soundName;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ar);
		Log.d(LOGTAG, "onCreate");

		session = new AppSession(this);
		session.initAR(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Intent i = getIntent();
		objName = i.getStringExtra(StartArActivity.EXTRA_MESSAGE_OBJNAME);
		texName = i.getStringExtra(StartArActivity.EXTRA_MESSAGE_TEXNAME);
		markerName = i.getStringExtra(StartArActivity.EXTRA_MESSAGE_MARKERNAME);
		soundName = i.getStringExtra(StartArActivity.EXTRA_MESSAGE_SOUNDNAME);

		Log.d("from gdx : ", objName);
		Log.d("from gdx : ", texName);

		mRenderer = new VuforiaRenderer(session);

		FrameLayout container = (FrameLayout) findViewById(R.id.ar_container);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		//config.useGL20 = true;

		mEngine = new Engine(mRenderer, objName, texName, soundName);
		View glView = initializeForView(mEngine, config);

		container.addView(glView);

		//AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new MyGdxGame(), config);

	}



	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOGTAG, "onResume");

		//we do not resume AR here if splash screen is visible
		try {
			session.resumeAR();
		} catch (VuforiaException e) {
			Toast.makeText(this, "Unable to start augmented reality.", Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, e.getString());
		}
	}

	@Override
	protected void onPause() {
		Log.d(LOGTAG, "onPause");
		super.onPause();

		try {
			session.pauseAR();
		} catch (VuforiaException e) {
			Toast.makeText(this, "Unable to stop augmented reality.", Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, e.getString());
		}
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		Log.d(LOGTAG, "onConfigurationChanged");
		super.onConfigurationChanged(config);
		session.onConfigurationChanged();
	}


	@Override
	public void onInitARDone(VuforiaException exception) {
		if (exception == null) {
			mRenderer.mIsActive = true;

			try {
				session.startAR(CameraDevice.CAMERA_DIRECTION.CAMERA_DIRECTION_DEFAULT);
			} catch (VuforiaException e) {
				Log.e(LOGTAG, e.getString());
			}

			boolean result = CameraDevice.getInstance().setFocusMode(CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);

			if (!result) Log.e(LOGTAG, "Unable to enable continuous autofocus");

			try {
				mEngine.resume();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			Toast.makeText(this, "Unable to start augmented reality.", Toast.LENGTH_LONG).show();
			Log.e(LOGTAG, exception.getString());
			finish();
		}

	}


	// The final call you receive before your activity is destroyed.
	@Override
	protected void onDestroy() {
		Log.d(LOGTAG, "onDestroy");
		super.onDestroy();

		try {
			session.stopAR();
		} catch (VuforiaException e) {
			Log.e(LOGTAG, e.getString());
		}

		System.gc();
	}


	@Override
	public boolean doInitTrackers() {
		// Indicate if the trackers were initialized correctly
		boolean result = true;

		// Initialize the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		Tracker tracker = trackerManager.initTracker(ObjectTracker.getClassType());

		if (tracker == null) {
			Log.d(LOGTAG, "Failed to initialize ImageTracker.");
			result = false;
		}

		return result;
	}


	@Override
	public boolean doLoadTrackersData() {
		// Get the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		ObjectTracker imageTracker = (ObjectTracker) trackerManager.getTracker(ObjectTracker.getClassType());

		if (imageTracker == null) {
			Log.d(LOGTAG, "Failed to load tracking data set because the ImageTracker has not been initialized.");
			return false;
		}

		// Create the data sets:
		posterDataSet = imageTracker.createDataSet();
		if (posterDataSet == null) {
			Log.d(LOGTAG, "Failed to create a new tracking data.");
			return false;
		}

		// Load the data sets:

		if (!posterDataSet.load(getFiles().getLocalStoragePath()+"/"+markerName, STORAGE_TYPE.STORAGE_ABSOLUTE)) {
			Log.d(LOGTAG, "Failed to load data set.");
			return false;
		}
		else
		{
			Log.d(LOGTAG, "Get Marker Name "+getFiles().getLocalStoragePath()+"/"+markerName);
		}

/*		if (!posterDataSet.load("StonesAndChips.xml", STORAGE_TYPE.STORAGE_APPRESOURCE)) {
			Log.d(LOGTAG, "Failed to load data set.");
			return false;
		}*/

		// Activate the data set:
		if (!imageTracker.activateDataSet(posterDataSet)) {
			Log.d(LOGTAG, "Failed to activate data set.");
			return false;
		}

		Log.d(LOGTAG, "Successfully loaded and activated data set.");
		return true;
	}


	@Override
	public boolean doStartTrackers() {
		// Indicate if the trackers were started correctly
		boolean result = true;

		Tracker imageTracker = TrackerManager.getInstance().getTracker(
				ObjectTracker.getClassType());
		if (imageTracker != null) {
			imageTracker.start();
			Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);
		} else
			result = false;

		return result;
	}

	public void onBackPressed() {
		super.onBackPressed();

		//Intent bubbleActivity = new Intent(this, FirstActivity_10bubbles.class);
		//startActivity(bubbleActivity);
		this.finish();
	}

	@Override
	public boolean doStopTrackers() {
		// Indicate if the trackers were stopped correctly
		boolean result = true;

		Tracker imageTracker = TrackerManager.getInstance().getTracker(
				ObjectTracker.getClassType());
		if (imageTracker != null)
			imageTracker.stop();
		else
			result = false;

		return result;
	}


	@Override
	public boolean doUnloadTrackersData() {
		// Indicate if the trackers were unloaded correctly
		boolean result = true;

		// Get the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		ObjectTracker imageTracker = (ObjectTracker) trackerManager
				.getTracker(ObjectTracker.getClassType());
		if (imageTracker == null) {
			Log.d(LOGTAG, "Failed to destroy the tracking data set because the ImageTracker has not been initialized.");
			return false;
		}
		else
		{

		}

		for(int i = 0; i < imageTracker.getActiveDataSetCount(); i++)
		{
			if (posterDataSet != null) {
				if (imageTracker.getActiveDataSet(i) == posterDataSet && !imageTracker.deactivateDataSet(posterDataSet)) {
					Log.d(LOGTAG, "Failed to destroy the tracking data set StonesAndChips because the data set could not be deactivated.");
					result = false;
				} else if (!imageTracker.destroyDataSet(posterDataSet)) {
					Log.d(LOGTAG, "Failed to destroy the tracking data set StonesAndChips.");
					result = false;
				}
			}

			posterDataSet = null;
		}

		return result;
	}

	@Override
	public boolean doDeinitTrackers() {

		// Deinit the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		trackerManager.deinitTracker(ObjectTracker.getClassType());

		return true;
	}

	@Override
	public void onQCARUpdate(State state) {
	}
}
