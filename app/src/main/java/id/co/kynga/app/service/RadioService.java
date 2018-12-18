package id.co.kynga.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.session.SessionNotification;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.ui.activity.RadioActivity2AppMStar;
import id.co.kynga.app.ui.activity.SplashActivityAppMStar;
import id.co.kynga.app.util.PlaybackStatus;


/**
 * Created by macbookpro on 26/1/17.
 */

public class RadioService extends Service {
    public final static String TAG = "LibVLCAndroidSample/RadioService";
    private LibVLC libvlc;
    private MediaPlayer mMediaPlayer = null;
    private int mVideoWidth;
    private int mVideoHeight;
    private final static int VideoSizeChanged = -1;

    private int rposition;
    private NotificationManager nManager;
    public static RemoteViews notificationView;
    private NotificationCompat.Builder nBuilder;
    private RadioModel radio_model;
    private String radioTitle;
    private String radioURL;
    int NOTIFICATION_ID = 1111;

    private final IBinder mBinder = new RadioBinder();
    // public Builder lbl_message;

    public void onCreate(){
        //create the service
        super.onCreate();
        rposition = 0;//initialize position
        setInitial();
    }

    public void setInitial(){
        radioURL = "";
        //createPlayer(radioURL);
    }

    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<RadioService> mOwner;

        public MyPlayerListener(RadioService owner) {
            mOwner = new WeakReference<RadioService>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            RadioService player = mOwner.get();
            switch(event.type) {
                case MediaPlayer.Event.EndReached:
                    Log.d(TAG, "MediaPlayerEndReached");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.EncounteredError:
                    Log.d(TAG, "Media Player Error, re-try");
                    //player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }

    public void createPlayer(String media) {
        releasePlayer();
        try {
            if (media.length() > 0) {
                Toast toast = Toast.makeText(this, media, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0);
                //toast.show();
            }

            // Create LibVLC
            // TODO: make this more robust, and sync with audio demo
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");
            options.add("--aout=opensles");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            options.add("--http-reconnect");
            options.add("--network-caching="+6*1000);
            libvlc = new LibVLC(options);
            //libvlc.setOnHardwareAccelerationError(this);


            // Create media player
            mMediaPlayer = new MediaPlayer(libvlc);
            mMediaPlayer.setEventListener(mPlayerListener);

            Media m = new Media(libvlc, Uri.parse(media));
            mMediaPlayer.setMedia(m);
            mMediaPlayer.play();
        } catch (Exception e) {
            //Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
        }
    }

    // TODO: handle this cleaner
    public void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        libvlc.release();
        libvlc = null;
    }

    public boolean isPlaying() {
        //RadioActivity2.instance.lbl_message.setText(" ");
        return mMediaPlayer.isPlaying();
    }

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.play();
        }
    }
    public void notifOn() {
        setNotification(PlaybackStatus.START);
    }
    public void play() {
        mMediaPlayer.play();
    }
    public void pause() {
        mMediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        radio_model = intent.getParcelableExtra(RadioModel.TAG);
        radioTitle = intent.getStringExtra("title");
        radioURL = intent.getStringExtra("url");
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int StartId){
        //       createPlayer(radioURL);
//        setNotification(PlaybackStatus.PLAYING);
/*        if (intent.getAction().equals(SessionNotification.STARTFOREGROUND_ACTION)) {
            setNotification(PlaybackStatus.PLAYING);
        }
        else if(intent.getAction().equals((SessionNotification.STOPFOREGROUND_ACTION))){
            notificationCancel();
        } */
        handleIncomingActions(intent);
        return Service.START_NOT_STICKY;
    }

    private void setNotification(PlaybackStatus playbackStatus) {
        /**
         * Notification actions -> playbackAction()
         *  0 -> Play
         *  1 -> Pause
         *  2 -> Next track
         *  3 -> Previous track
         */

        notificationView = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.view_radio_notification);
        //notificationView.setImageViewResource(R.id.btnStop, R.drawable.mstar_ic);


        if (playbackStatus == PlaybackStatus.PLAYING) {
            Log.i("pause button active!","pause button active!");
            PendingIntent play_pauseAction = null;
            notificationView.setImageViewResource(R.id.btnStop, android.R.drawable.ic_media_pause);
            RadioActivity2AppMStar.instance.btn_playstop.setBackgroundResource(R.drawable.ic_stopcircle);
            //create the pause action
            play_pauseAction = playbackAction(0);
            notificationView.setOnClickPendingIntent(R.id.btnStop, play_pauseAction);


        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            Log.i("play button deactive!","play button deactive!");
            PendingIntent play_pauseAction = null;
            notificationView.setImageViewResource(R.id.btnStop, android.R.drawable.ic_media_play);
            RadioActivity2AppMStar.instance.btn_playstop.setBackgroundResource(R.drawable.ic_playcircle);
            //create the play action
            play_pauseAction = playbackAction(1);
            notificationView.setOnClickPendingIntent(R.id.btnStop, play_pauseAction); //finish here after pause

        } else if (playbackStatus == PlaybackStatus.START) {
            Log.i("Start button deactive!","Start button deactive!");
        }
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mstar_ic);
        nBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setLargeIcon(bmp)
                .setSmallIcon(R.drawable.mstar_ic)
                .setContentTitle("Mstar Radio is playing")
                .setOngoing(true);

        notificationView.setTextViewText(R.id.textRadio,radioTitle);
        nBuilder.setContent(notificationView);

        Intent resultIntent = new Intent(getApplicationContext(), SplashActivityAppMStar.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(SplashActivityAppMStar.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(resultPendingIntent);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);

        startForeground(SessionNotification.NOTIFICATION_ID.FOREGROUND_SERVICE, nBuilder.build());
        /*
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(SplashActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(resultPendingIntent);
        notificationView.setTextViewText(R.id.textRadio,radioTitle);
        nBuilder.setContent(notificationView);
        */
    }

    public void notificationCancel() {
        nManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, RadioService.class);
        switch (actionNumber) {
            case 0:
                mMediaPlayer.stop();
                playbackAction.setAction(SessionNotification.ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);

            case 1:
                mMediaPlayer.play();
                playbackAction.setAction(SessionNotification.ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);

            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(SessionNotification.ACTION_PLAY)) {
            setNotification(PlaybackStatus.PLAYING);
        } else if (actionString.equalsIgnoreCase(SessionNotification.ACTION_PAUSE)) {
            setNotification(PlaybackStatus.PAUSED);
        }
    }


    public class RadioBinder extends Binder {
        public final RadioService getService() {
            return RadioService.this;
        }
    }



}