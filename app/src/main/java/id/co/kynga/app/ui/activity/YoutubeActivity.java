package id.co.kynga.app.ui.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.model.YoutubeContent;
import id.co.kynga.app.model.pojo.SubCategory;
import id.co.kynga.app.model.pojo.YoutubeApi;
import id.co.kynga.app.model.pojo.YoutubePojo;
import id.co.kynga.app.ui.adapter.YoutubeListAdapter;
import id.co.kynga.app.ui.fragment.PopupFragment;
import id.co.kynga.app.util.DataVariable;
import id.co.kynga.app.util.NoConnectionDialog;
import id.co.kynga.app.util.NoDataDialog;
import id.co.kynga.app.util.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import id.co.kynga.app.R;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.gson.Gson;

/**
 * Modified by Aji Subastian
 */

@SuppressLint("ClickableViewAccessibility")
public class YoutubeActivity extends FragmentActivity implements View.OnClickListener,
        YoutubeListAdapter.OnYoutubeContentClickListener {

    public static final String TAG = YoutubeActivity.class.getSimpleName();
    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int COLUMN = 5;

    private YoutubeListAdapter youtubeListAdapter;
    //private int menuId = 1;

    private List<YoutubePojo> channelList = new ArrayList<>();
    private List<YoutubeContent> contentList = new ArrayList<>();
    private LinearLayout youtubeChannelLayout;
    private Button youtubePrevBtn;
    private Button youtubeNextBtn;
    private Button youtubePlayallBtn;
    private EditText youtubeSearch;
    private LinearLayout youtubeLoading;

    private int globalWidth;
    private String playList = null;
    private String nextToken = null;
    private String prevToken = null;
    private boolean nextPage = false;
    private boolean prevPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        List<SubCategory.Content> subCategoryList = getIntent().getParcelableArrayListExtra(TAG);
        channelList = subCategoryList.get(0).Youtube;

        if (channelList != null && channelList.size() > 0) {
            initialize();
            setupChannel();
        }
        else {
            showComingSoon();
        }
    }

    private void showComingSoon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setMessage("Coming Soon");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initialize() {

        //LinearLayout youtubeTopLayout = (LinearLayout) findViewById(R.id.youtube_top_layout);
        youtubeChannelLayout = (LinearLayout) findViewById(R.id.youtube_channel_layout);

        youtubeListAdapter = new YoutubeListAdapter(contentList);
        youtubeListAdapter.setOnYoutubeContentClickListener(this);

        RecyclerView.LayoutManager layoutManagerContent = new GridLayoutManager(getApplicationContext(), COLUMN);
        RecyclerView youtubeContentLayout = (RecyclerView) findViewById(R.id.youtube_content_layout);
        youtubeContentLayout.setLayoutManager(layoutManagerContent);
        youtubeContentLayout.setAdapter(youtubeListAdapter);

        youtubeLoading = (LinearLayout) findViewById(R.id.youtube_loading);

        youtubePrevBtn = (Button) findViewById(R.id.youtube_prev_btn);
        youtubeNextBtn = (Button) findViewById(R.id.youtube_next_btn);
        youtubePlayallBtn = (Button) findViewById(R.id.youtube_playall_btn);
        youtubePlayallBtn.setOnClickListener(this);
        Button youtubeSearchBtn = (Button) findViewById(R.id.youtube_search_btn);
        youtubeSearchBtn.setOnClickListener(this);

        youtubeSearch = (EditText) findViewById(R.id.youtube_search);

        Display display = getWindowManager().getDefaultDisplay();
        //noinspection deprecation
        globalWidth = display.getWidth();

    }

    private void setupChannel() {

        if (!Utils.isNetworkAvailable()) {
            NoConnectionDialog noConnectionDialog = new NoConnectionDialog(this);
            noConnectionDialog.show();
        }
        else {

            int channelCount = channelList.size();
            if (channelCount > 0) {
                youtubeChannelLayout.removeAllViews();

                for (final YoutubePojo pojo : channelList) {

                    LayoutInflater layoutInflatter = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout child = new LinearLayout(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(globalWidth / 5, LinearLayout.LayoutParams.WRAP_CONTENT);
                    child.setLayoutParams(params);

                    @SuppressLint("InflateParams") View view = layoutInflatter.inflate(R.layout.youtube_playlist_button, null);
                    Button btnPlaylist = (Button) view.findViewById(R.id.buttonPlaylist);
                    btnPlaylist.setText(pojo.Title);

                    btnPlaylist.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setupContent(pojo.PlaylistID);
                        }
                    });

                    youtubeChannelLayout.addView(view);
                }
            } else {
                NoDataDialog noDataDialog = new NoDataDialog(this);
                noDataDialog.show();
            }

            YoutubePojo youtubeChannel = channelList.get(0);
            setupContent(youtubeChannel.PlaylistID);

        }
    }

    private void setupContent(String playList) {
        this.playList = playList;
        String url = URLController.getURL(R.string.url_youtube_playlist, playList);
        loadContent(url, Request.Method.POST);
    }

    private void loadContent(String url, int methodRequest) {

        youtubePlayallBtn.setVisibility(playList != null ? View.VISIBLE : View.GONE);
        youtubeLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(methodRequest, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Log.d(DataVariable.TAG, s);
                Gson gson = new Gson();
                try {
                    YoutubeApi api = gson.fromJson(s, YoutubeApi.class);
                    if (api != null && api.Status == 1 && api.Result != null && api.Result.List.size() != 0) {

                        contentList = api.Result.List;
                        nextToken = api.Result.nextToken;
                        prevToken = api.Result.prevToken;

                        nextPage = nextToken != null && !nextToken.isEmpty();
                        prevPage = prevToken != null && !prevToken.isEmpty();
                        updateNextpage();

                        youtubeListAdapter.setContentList(contentList);
                        youtubeLoading.setVisibility(View.GONE);


                    } else if (api != null && api.Status == 0) {
                        final Bundle bundle = new Bundle();
                        bundle.putString("message", api.Message);
                        PopupFragment rf = new PopupFragment();
                        rf.setArguments(bundle);
                        rf.show(getSupportFragmentManager(), "Failed");
                    }
                } catch (Exception e) {
                    Log.e(DataVariable.TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    String err = new String(volleyError.networkResponse.data, "utf-8");
                    Log.e(DataVariable.TAG, err);
                } catch (UnsupportedEncodingException e) {
                    Log.e(DataVariable.TAG, e.getMessage());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLController.getHeaders();
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        KyngaApp.getInstance().addToRequestQueue(request);
    }

    public void updateNextpage() {
        if (nextPage) {
            if (prevPage) {
                youtubePrevBtn.setEnabled(true);
                youtubePrevBtn.setTextColor(Color.WHITE);
            }
            if (nextPage) {
                youtubeNextBtn.setEnabled(true);
                youtubeNextBtn.setTextColor(Color.WHITE);
            }
        } else {
            youtubePrevBtn.setEnabled(false);
            youtubeNextBtn.setEnabled(false);
            youtubeNextBtn.setTextColor(Color.GRAY);
            youtubePrevBtn.setTextColor(Color.GRAY);
        }
    }

    @Override
    public void onYoutubeContentClick(View v, int position) {

        int size = youtubeListAdapter.getItemCount();
        String[] arrayVideoId = new String[size];

        int j = position;
        for (int number = 0; number < (arrayVideoId.length - position); number++) {
            YoutubeContent youtubeContent = contentList.get(j);
            arrayVideoId[number] = youtubeContent.YoutubeID;
            j++;
        }
        int i = 0;
        for (int number = (arrayVideoId.length - position); number < arrayVideoId.length; number++) {
            YoutubeContent youtubeContent = contentList.get(i);
            arrayVideoId[number] = youtubeContent.YoutubeID;
            i++;
        }
        YoutubeContent youtubeContent = contentList.get(position);

        String videoId = youtubeContent.YoutubeID;
        Intent intent = new Intent(YoutubeActivity.this, YoutubePlayer.class);
        intent.putExtra("arrayVideoId", arrayVideoId);
        intent.putExtra("position", String.valueOf(position));
        intent.putExtra("videoId", videoId);
        intent.putExtra("video_type", YoutubePlayer.TYPE_VIDEO);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.youtube_prev_btn:
                break;
            case R.id.youtube_next_btn:
                break;
            case R.id.youtube_playall_btn:
                Intent intent = new Intent(YoutubeActivity.this, YoutubePlayer.class);
                intent.putExtra("position", "0");
                intent.putExtra("playlist", playList);
                intent.putExtra("video_type", YoutubePlayer.TYPE_PLAYLIST);
                startActivity(intent);
                break;
            case R.id.youtube_search_btn:
                String search = youtubeSearch.getText().toString();
                String query = search + "/" + nextToken;
                String url = URLController.getURL(R.string.url_youtube_search, query);
                loadContent(url, Request.Method.GET);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(this, 0).show();
            } else {
                String errorMessage = "Error";
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = getIntent();
        in.putExtra("PAGEID", "music");
        setResult(RESULT_OK, in);
        finish();
    }

    @Override
    public void onStop() {
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

}