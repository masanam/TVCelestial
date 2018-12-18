package id.co.kynga.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import id.co.kynga.app.R;

public class StartArActivity extends Activity {

    android.widget.ProgressBar progress;
    int downloadSize = 0;

    String serverUrl = "";

    TextView tv, tv1;

    static String EXTRA_MESSAGE_OBJNAME = "com.test.arObj";
    static String EXTRA_MESSAGE_TEXNAME = "com.test.arTex";
    static String EXTRA_MESSAGE_MARKERNAME = "com.test.arMarker";
    static String EXTRA_MESSAGE_SOUNDNAME = "com.test.arSound";
    String data ="";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startaractivity);


        //progress = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.textView);
        tv1 = (TextView) findViewById(R.id.textView2);
        //showProgress();
        serverUrl = this.getString(R.string.server_url);

        Intent i = getIntent();
        data = i.getStringExtra(HomeARActivity.EXTRA_MESSAGE_DATA);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDatabaseFirst(serverUrl+data, getFilesDir().getPath()+data);
                //downloadFile("http://192.168.1.202/test/jet.g3db", getFilesDir().getPath()+"/model.g3db");
            }
        }).start();

    }


    private void downloadFile(String url, String savePath)
    {

        downloadSize = 0;
        File file = new File(savePath);

        if(!file.exists())
        {
            try
            {
                URL u = new URL(url);
                URLConnection connection = u.openConnection();
                final int lengthContent = connection.getContentLength();

               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progress.setTitle(""+ Gdx.files.getLocalStoragePath());
                        //progress.setMax(lengthContent);

                    }
                });*/

                FileOutputStream fileOutputStream = new FileOutputStream(file);

                InputStream input = u.openStream();
                byte[] buffer = new byte[lengthContent];
                int bufferLength = 0;
                //input.readFully(buffer);
                while((bufferLength = input.read(buffer)) > 0)
                {
                    downloadSize += bufferLength;
                    fileOutputStream.write(buffer, 0, bufferLength);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progress.setProgress(downloadSize);
                            tv1.setText(""+downloadSize+"/"+lengthContent);
                        }
                    });
                }
                fileOutputStream.close();
                input.close();



                /*DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
                out.write(buffer);
                out.flush();
                out.close();*/

                //hideProgressIndicator();
               // goToArActivity();

            }
            catch (final FileNotFoundException ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //textView.setText(ex.toString());
                    }
                });

            }
            catch (final IOException ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //textView.setText(ex.toString());
                    }
                });
            }


       }
        else //check size file, if corrupt or not completed, download file
        {
            downloadSize = 0;
            try
            {

                URL u = new URL(url);
                URLConnection connection = u.openConnection();
                final int lengthContent = connection.getContentLength();
                int size = (int) file.length();
                if(size != lengthContent)
                {
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progress.setTitle(""+ Gdx.files.getLocalStoragePath());
                            progress.setMax(lengthContent);
                        }
                    });*/

                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    InputStream input = u.openStream();
                    byte[] buffer = new byte[lengthContent];
                    int bufferLength = 0;
                    //input.readFully(buffer);
                    while((bufferLength = input.read(buffer)) > 0)
                    {
                        downloadSize += bufferLength;
                        fileOutputStream.write(buffer, 0, bufferLength);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //progress.setProgress(downloadSize);
                                tv1.setText(""+downloadSize+"/"+lengthContent);
                            }
                        });
                    }
                    fileOutputStream.close();
                    input.close();
                }

            }
            catch(IOException ex)
            {
                Gdx.app.log("Error ", ex.getMessage());
            }
        }
    }

    private void hideProgressIndicator()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progress.dismiss();
            }
        });
    }


    private void showProgress()
    {
        //progress.setMessage("Download... ");
        progress.setIndeterminate(true);
        progress.setProgress(0);
        //progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progress.setCancelable(true);
        //progress.show();
    }

    public void goToArActivity(String objName, String texName, String markerName, String soundName)
    {
        Intent i = new Intent(StartArActivity.this, AndroidLauncher.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(EXTRA_MESSAGE_OBJNAME, objName);
        i.putExtra(EXTRA_MESSAGE_TEXNAME, texName);
        i.putExtra(EXTRA_MESSAGE_MARKERNAME, markerName);
        i.putExtra(EXTRA_MESSAGE_SOUNDNAME, soundName);
        startActivity(i);
        this.finish();
    }




    public void getDatabaseFirst(String url, String savePath)
    {
        //downloadFile(serverUrl+"data.txt", getFilesDir().getPath()+"/data.txt");

        try {
            // Create a URL for the desired page
            URL u = new URL(url);

            // Read all the text returned by the server
            final StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
            String str;

            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
                sb.append(str);
                sb.append('\n');
            }

            in.close();


            final String files[] = sb.toString().split(""+'\n');

            for(int i = 0; i < files.length; i++)
            {
                final int a = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tv.setText("Process Download Files "+files[a]);
                    }
                });
                downloadFile(serverUrl+files[a],  getFilesDir().getPath()+"/"+files[a]);
            }

            File fi = new File(getFilesDir().getPath()+"/"+files[1]);
            String newFileName = fi.getName().replace('_', ' ');
            fi.renameTo(new File(getFilesDir().getPath()+"/"+newFileName));
            if(files.length > 4)
            {
                goToArActivity(files[0], files[1], files[2], files[files.length - 1]);
            }
            else
            {
                goToArActivity(files[0], files[1], files[2], "");
            }



        } catch (MalformedURLException e)
        {
            Log.d("Exception", e.getMessage());
        }
        catch (IOException e)
        {
            Log.d("Exception", e.getMessage());
        }

    }
}
