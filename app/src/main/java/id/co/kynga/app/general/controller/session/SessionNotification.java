package id.co.kynga.app.general.controller.session;

import android.os.Handler;

/**
 * Created by macbookpro on 17/1/17.
 */

public class SessionNotification {


    //song number which is playing right now from SONGS_LIST
    public static int SONG_NUMBER = 0;
    //song is playing or paused
    public static boolean SONG_PAUSED = true;
    //song changed (next, previous)
    public static boolean SONG_CHANGED = false;
    //handler for song changed(next, previous) defined in service(SongService)
    public static Handler SONG_CHANGE_HANDLER;
    //handler for song play/pause defined in service(SongService)
    public static Handler PLAY_PAUSE_HANDLER;
    //handler for showing song progress defined in Activities(MainActivity, AudioPlayerActivity)
    public static Handler PROGRESSBAR_HANDLER;
    //notification
    public static String MAIN_ACTION = "id.co.mstar.app.general.session.main";
    public static String STARTFOREGROUND_ACTION = "id.co.mstar.app.general.session.startforeground";
    public static String STOPFOREGROUND_ACTION = "id.co.mstar.app.general.session.stopforeground";
    public static final String Broadcast_PLAY_NEW_AUDIO = "id.co.mstar.app.PlayNewAudio";
    public static final String NOTIFY_STOP = "id.co.mstar.app.stop";
    public static final String NOTIFY_CLOSE = "id.co.mstar.app.close";
    public static final String NOTIFY_PLAY = "id.co.mstar.app.play";
    public static final String ACTION_PLAY = "android.intent.action.play";
    public static final String ACTION_PAUSE = "android.intent.action.pause";

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
