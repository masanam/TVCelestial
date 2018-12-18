package id.co.kynga.app.exoplayer;

import com.google.android.exoplayer.hls.HlsMasterPlaylist;

public class ExoPlayerInterface {
	public interface OnTitleListener {
		void onTitle(String title);
	}

	public interface OnBitrateAvailableListener {
		void onBitrateAvailable(HlsMasterPlaylist playlist);
	}
}