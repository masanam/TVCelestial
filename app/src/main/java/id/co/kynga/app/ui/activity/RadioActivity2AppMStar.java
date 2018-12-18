package id.co.kynga.app.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tyorikan.voicerecordingvisualizer.RecordingSampler;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import org.videolan.libvlc.LibVLC;

import java.util.ArrayList;

import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.GlobalController;
import id.co.kynga.app.general.controller.RadioController;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.service.RadioService;
import id.co.kynga.app.ui.adapter.RadioListAdapter;


public class RadioActivity2AppMStar extends FragmentActivity implements RecordingSampler.CalculateVolumeListener {
    public static RadioActivity2AppMStar instance;
    private ImageButton btn_back;
    private TextView lbl_title;
    private GridView gvw_main;
    public ImageButton btn_playstop;
    public TextView lbl_message;
    private Button btn_play_pause;
    private RadioListAdapter radio_list_adapter;
    private Visualizer visualizer;
    private ArrayList<RadioModel> radio_list;
    private static int position = 0;
    private RadioModel radio_model;
    private boolean radioBound = false;
    private AudioManager audioManager;
    private RadioController controller;
    private LibVLC libvlc;
    private Intent playIntent;
    private RadioService radioSrv;
    private RecordingSampler recordingSampler;

    //Visualizer
    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = 1;
    private static final int RECORDER_ENCODING_BIT = 16;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_DECIBELS = 120;

    private static final String TAG = RadioActivity2AppMStar.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_radio);
        setInitial();
    }

    protected void notif() {
        lbl_message.setVisibility(View.VISIBLE);
        lbl_message.setText(GlobalController.getString(R.string.message_player_preparing));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // Hide your View after 3 seconds
                lbl_message.setVisibility(View.INVISIBLE);
            }
        }, 3000);
    }
    @Override
    protected void onStart() {
        super.onStart();
        final RadioModel radio_model = radio_list.get(0);
        final String radioTitle = radio_model.Title;
        final String radioplay = radio_model.LinkURL;

        playIntent = new Intent(RadioActivity2AppMStar.this, RadioService.class);
        // playIntent.putExtra()
        //playIntent.putExtra("title",radioTitle);
        playIntent.putExtra("url", radioplay);
        startService(new Intent(this, RadioService.class));
        bindService(playIntent, mConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        radioSrv.notifOn();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        recordingSampler.release();
        super.onDestroy();
        radioSrv.releasePlayer();
        stopService(playIntent);
        unbindService(mConnection);
        radioSrv.stopSelf();
    }

    private void setInitial() {

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (getIntent().hasExtra(RadioModel.TAG)) {
            final RadioModel radio_model = getIntent().getParcelableExtra(RadioModel.TAG);
            radio_list = new ArrayList<>(radio_model.list);
        }
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_playstop = (ImageButton) findViewById(R.id.btn_play_stop);
        btn_playstop.setBackgroundResource(R.drawable.ic_stopcircle);
        lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(null, Typeface.BOLD);
        gvw_main = (GridView) findViewById(R.id.gvw_main);
        lbl_message = (TextView) findViewById(R.id.lbl_message);
        btn_play_pause = (Button) findViewById(R.id.btn_play_pause);
        //final RadioModel radio_model = radio_list.get(0); //get(0);
        final RadioModel radio_model = radio_list.get(0); //get(0);
        final String radioTitle = radio_model.Title;
        final String radioplay = radio_model.LinkURL;

        VisualizerView visualizerView = (VisualizerView) findViewById(R.id.visualizer);

        recordingSampler = new RecordingSampler();
        recordingSampler.setVolumeListener(this);  // for custom implements
        recordingSampler.setSamplingInterval(100); // voice sampling interval
        recordingSampler.link(visualizerView);     // link to visualizer
        recordingSampler.startRecording();

        playIntent = new Intent(this, RadioService.class);
        playIntent.putExtra("title",radioTitle);
        playIntent.putExtra("url", radioplay);
        bindService(playIntent, mConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);
        populateData();
        setRadioContoller();
    }

    private void playfirstSong(RadioService radioSrv){
        if(radio_list.size() > 0){
            radioSrv.createPlayer(radio_list.get(0).LinkURL);
            lbl_title.setText(radio_list.get(0).Title);
            notif();
            // GlobalController.radio_model.LinkURL, 20000);
        }
    }

    private void setRadioContoller() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioSrv.releasePlayer();
                stopService(playIntent);
                finish();
            }
        });

        btn_playstop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (radioSrv.isPlaying()) {
                    radioSrv.pause();
                    btn_playstop.setBackgroundResource(R.drawable.ic_playcircle);
                    recordingSampler.stopRecording();
                } else {
                    radioSrv.play();
                    btn_playstop.setBackgroundResource(R.drawable.ic_stopcircle);
                    recordingSampler.startRecording();
                }
            }
        });
    }

    private void populateData() {

        radio_list_adapter = new RadioListAdapter(radio_list, new RadioListAdapter.RadioListAdapterCallback() {
            @Override
            public void didRadioListAdapterActioned(RadioModel radio_model) {
                lbl_title.setText(radio_model.Title);
                //GlobalController.showToast(radio_model.LinkURL, 20000);
                radioSrv.createPlayer(radio_model.LinkURL);
                notif();
            }
        });
        gvw_main.setAdapter(radio_list_adapter);
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            RadioService.RadioBinder binder = (RadioService.RadioBinder) service;
            radioSrv = binder.getService();
            //radioSrv.setList(radio_list);
            radioBound = true;
            playfirstSong(radioSrv);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            radioBound = false;
        }

    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

                    return true;
                case KeyEvent.KEYCODE_BACK:
                    radioSrv.releasePlayer();
                    finish();
                    return true;
            }
        }

        return true;
    }

    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onCalculateVolume(int volume) {
        Log.d(TAG, String.valueOf(volume));
    }
}

