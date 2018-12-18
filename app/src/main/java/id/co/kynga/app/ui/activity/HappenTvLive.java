package id.co.kynga.app.ui.activity;

/**
 * Created by yongki on 8/3/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nodemedia.LivePublisher;
import cn.nodemedia.LivePublisherDelegate;
import id.co.kynga.app.KyngaApp;
import id.co.kynga.app.R;
import id.co.kynga.app.general.controller.session.SessionController;
import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.util.Utility;

public class HappenTvLive extends AppCompatActivity implements View.OnClickListener, LivePublisherDelegate {
    private GLSurfaceView glsv;
    private Button swtBtn, videoBtn, flashBtn;
    private boolean isStarting = false;
    private boolean isMicOn = true;
    private boolean isCamOn = true;
    private boolean isFlsOn = true;
    public String token,status,events,nilaievents,pubUrl;
    EditText password_home;
    Toast m_currentToast;
    private int widthLayout;
    private int heightLayout;
    private ArrayList<Toast> msjsToast = new ArrayList<Toast>();
    private Button campic,HappenClose_home;
    private LinearLayout main;
    private FrameLayout pic;
    private static final int CAMERA_REQUEST = 1888;
    ImageView mimageView;

    private Button buttonUpload;
    private EditText editText;
    private int PICK_IMAGE_REQUEST = 1;

    private static final int STORAGE_PERMISSION_CODE = 123;

    private Bitmap bitmap;

    private Uri filePath;
    private String UPLOAD_URL, IMAGES_URL;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private String userChoosenTask;
    private ProgressBar progressBarHome;
    private FileOutputStream fo;
    private File f3;
    private Button micBtn;
    private Button camBtn;
    private TextView golive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_happen_tv_live);
        isStarting = false;
        main = (LinearLayout) findViewById(R.id.main_layout);
        pic = (FrameLayout) findViewById(R.id.pic_layout);
        main.setVisibility(View.VISIBLE);
        micBtn = (Button) findViewById(R.id.button_mic_official);
        camBtn = (Button) findViewById(R.id.button_cam_official);
        glsv = (GLSurfaceView) findViewById(R.id.camera_preview_home);
        swtBtn = (Button) findViewById(R.id.button_sw_home);
        progressBarHome = (ProgressBar) findViewById(R.id.progressBar);
        videoBtn = (Button) findViewById(R.id.button_video_home);
        HappenClose_home = (Button) findViewById(R.id.HappenClose_home);
        flashBtn = (Button) findViewById(R.id.button_flash_home);
        password_home = (EditText) findViewById(R.id.password_home);
        campic = (Button) findViewById(R.id.CamPicLogin_home);
        golive = (TextView) findViewById(R.id.Golive);
        widthLayout = getResources().getDisplayMetrics().widthPixels;
        heightLayout = getResources().getDisplayMetrics().heightPixels;
        swtBtn.setOnClickListener(this);
        HappenClose_home.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        flashBtn.setOnClickListener(this);
        campic.setOnClickListener(this);

        password_home.setOnEditorActionListener(new DoneOnEditorActionListener());
        glsv.setZOrderOnTop(true);
        glsv.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glsv.getHolder().setFormat(PixelFormat.RGBA_8888);

        mimageView = (ImageView) this.findViewById(R.id.image_from_camera);
        Button button = (Button) this.findViewById(R.id.take_image_from_camera);

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        editText = (EditText) findViewById(R.id.editTextName);

        UPLOAD_URL = "http://192.168.94.1/AndroidImageUpload/upload.php";
        f3=new File(Environment.getExternalStorageDirectory()+"/HappenLivePic/");

        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.VISIBLE);
                glsv.setVisibility(View.VISIBLE);
                pic.setVisibility(View.GONE);
            }
        });

        LivePublisher.init(this);
        LivePublisher.setDelegate(this);
        LivePublisher.setAudioParam(32 * 1000, LivePublisher.AAC_PROFILE_HE);

        LivePublisher.setVideoParam(640, 640, 15, 500 * 1000, LivePublisher.AVC_PROFILE_MAIN);

        LivePublisher.setDenoiseEnable(true);

        LivePublisher.setDynamicRateEnable(true);

        LivePublisher.setSmoothSkinLevel(0);

        LivePublisher.startPreview(glsv, LivePublisher.CAMERA_FRONT, true);

    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(HappenTvLive.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(HappenTvLive.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 85, bytes);

        if(!f3.exists()) {
            f3.mkdirs();}
        else {
            f3.delete();
            f3.mkdirs();
        }
        File destination = new File(Environment.getExternalStorageDirectory()+ "/HappenLivePic/",
                System.currentTimeMillis() + ".png");

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mimageView.setMinimumHeight(350);
        mimageView.setMaxHeight(350);
        mimageView.setMinimumWidth(350);
        mimageView.setMaxWidth(350);
        mimageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mimageView.setImageBitmap(bm);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LivePublisher.startPreview(glsv, LivePublisher.CAMERA_FRONT, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LivePublisher.stopPublish();
        LivePublisher.stopPreview();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button_sw_home:
                LivePublisher.switchCamera();
                LivePublisher.setFlashEnable(false);
                isFlsOn = false;
                flashBtn.setBackgroundResource(R.drawable.ic_flash_off);
                break;
            case R.id.button_video_home:
                if (isStarting) {
                    token = "0";
                    String pubUrl = "rtmp://haptv.kynga.co.id/mobile/" + token;
                    status = "0";
                    LivePublisher.stopPublish();
                    events = "No Events";
                    sendtoken();
                    progressBarHome.setVisibility(View.GONE);
                } else {
                    token = SessionController.getToken();
                    pubUrl = "rtmp://haptv.kynga.co.id/mobile/" + token;
                    status = "1";
                    showToast("Live Video Start");
                    Toast.makeText(HappenTvLive.this, "Live Video Start", Toast.LENGTH_LONG).show();
                    LivePublisher.startPublish(pubUrl);
                    Toast.makeText(HappenTvLive.this, "Live Video Start", Toast.LENGTH_LONG).show();
                    showToast("Live Video Start");
                    Log.i("Publish", " : " + pubUrl);
                    nilaievents = password_home.getText().toString();
                    if (!nilaievents.isEmpty()) {
                        events = nilaievents;
                    }
                    else {
                        events = "No Events";
                    }
                    sendtoken();
                    progressBarHome.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button_flash_home:
                int ret = -1;
                if (isFlsOn) {
                    ret = LivePublisher.setFlashEnable(false);
                } else {
                    ret = LivePublisher.setFlashEnable(true);
                }
                if (ret == -1) {
                } else if (ret == 0) {
                    flashBtn.setBackgroundResource(R.drawable.ic_flash_off);
                    isFlsOn = false;
                } else {
                    flashBtn.setBackgroundResource(R.drawable.ic_flash_on);
                    isFlsOn = true;
                }
                break;
            case R.id.CamPicLogin_home:
                if (isStarting) {
                    token = "0";
                    String pubUrl = "rtmp://haptv.kynga.co.id/mobile/" + token;
                    status = "0";
                    LivePublisher.stopPublish();
                    events = "No Events";
                    sendtoken();
                    pic();
                } else {
                    pic();
                }
                break;
            case R.id.HappenClose_home:
                if (isStarting) {
                    onBackPressed();
                } else {
                    onBackPressed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventCallback(int event, String msg) {
        handler.sendEmptyMessage(event);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2001:
                    showToast("Live Video Start");
                    Toast.makeText(HappenTvLive.this, "Live Video Start", Toast.LENGTH_LONG).show();
                    videoBtn.setBackgroundResource(R.drawable.ic_video_start);
                    micBtn.setVisibility(View.VISIBLE);
                    camBtn.setVisibility(View.VISIBLE);
                    golive.setText("Stop Live");
                    isStarting = true;
                    break;
                case 2004:
                    videoBtn.setBackgroundResource(R.drawable.ic_video_stop);
                    micBtn.setVisibility(View.GONE);
                    camBtn.setVisibility(View.GONE);
                    golive.setText("Go Live");
                    isStarting = false;
                    break;
                default:
                    break;
            }
        }
    };

    private void sendtoken() {
        String url = URLController.getURL(R.string.url_happenlive);
        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.d("Validation", arg0);
                        Log.i("Success", "RTMP URL: " + pubUrl);
                        Log.i("Success", "RTMP status: " + status);
                        Log.i("events", " : " + events);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        Log.d("Validation", "Error :" + arg0.getMessage());
                        Log.i("Failed", "RTMP URL: " + pubUrl);
                        Log.i("Filede", "RTMP status: " + status);
                        Log.i("events", " : " + events);
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("status", status);
                params.put("events", events);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return URLController.getHeaders();
            }
        };
        KyngaApp.getInstance().addToRequestQueue(request);
    }

    private void showToast(String text)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        m_currentToast.show();

    }

    private void pic() {
        main.setVisibility(View.GONE);
        glsv.setVisibility(View.GONE);
        pic.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (main.getVisibility() == View.VISIBLE) {
            token = "0";
            status = "0";
            LivePublisher.stopPublish();
            LivePublisher.stopPreview();
            sendtoken();
            finish();
            this.overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            if(!f3.exists()) {
                f3.delete();}
            else {
                f3.delete();
                super.onBackPressed();
            }
        }
        else {
            pic.setVisibility(View.GONE);
            glsv.setVisibility(View.VISIBLE);
            main.setVisibility(View.VISIBLE);
        }
    }

    private class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                glsv.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        }
    }
}

