package id.co.kynga.app.general.controller;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import id.co.kynga.app.AnalyticsTrackers;

public class GoogleAnalyticsController {
	public static void hit(final String screen_name) {
		final Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
		tracker.setScreenName(screen_name);
		tracker.send(new HitBuilders.ScreenViewBuilder().build());
	}

	public static void hitEvent(
			final String screen_name,
			final String category,
			final String movie,
			final String username) {
		final Tracker tracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
		tracker.setScreenName(screen_name);
		tracker.send(new HitBuilders.EventBuilder().setCategory(category)
				.setAction(movie).setLabel(username).build());
	}
}
