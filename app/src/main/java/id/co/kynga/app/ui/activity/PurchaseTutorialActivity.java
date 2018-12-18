package id.co.kynga.app.ui.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import id.co.kynga.app.R;
import id.co.kynga.app.util.DataVariable;

/**
 * @author Aji Subastian
 */
public class PurchaseTutorialActivity extends AppCompatActivity {

    public static final String DATA_URL = "id.co.kynga.DATA_URL";

    private MediaPlayer mPlayer;
    private ProgressBar pb;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_tutorial);

        url = getIntent().getStringExtra(DATA_URL);

        initializeViews();
    }

    private void initializeViews() {

        pb = (ProgressBar) findViewById(R.id.tutorial_vid_progress);
        showProgressBar();

        SurfaceView sv = (SurfaceView) findViewById(R.id.tutorial_vid_sview);
        SurfaceHolder surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //playVideo();
                mPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //do nothing
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //do nothing
            }
        });

        preparePlayer();
    }

    private void preparePlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                hideProgressBar();
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releasePlayer(mp);
                finish();
            }
        });
    }

    private void playVideo() {
        if (!TextUtils.isEmpty(url)) {
            //preparePlayer();
            try {
                mPlayer.setDataSource(url);
                mPlayer.prepareAsync();
            }
            catch (IllegalArgumentException | IllegalStateException | IOException e) {
                Log.e(DataVariable.TAG, e.getMessage());
            }
        }
    }

    private void releasePlayer(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    private void showProgressBar() {
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        pb.setIndeterminate(false);
        pb.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        playVideo();
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer(mPlayer);
    }

}
