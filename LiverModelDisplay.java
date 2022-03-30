package com.example.cody.liverhub;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ar.myapplication.ConData;
import com.ar.myapplication.MainActivity;
import com.body_detection.body_detection;
import com.example.cody.liverhub.Objects.LiverModel;
import com.example.cody.liverhub.Renderer.LiverModelRenderer;
import com.example.cody.liverhub.util.NoSSLv3SocketFactory;
import com.example.cody.liverhub.util.SSL;
import com.example.cody.liverhub.util.TLSSocketFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okio.Timeout;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;

public class LiverModelDisplay extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean redererSet = false;
    private boolean dataReceived = false;
    private List<LiverModel> raw_liverList = new ArrayList<>();
    private String url;
    private String url_tumor;
    private LiverModelRenderer liverModelRenderer = null;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        url_tumor = intent.getStringExtra("URL_TUMOR");
        if(url == null || !url.contains("http")){
            url = "http://47.106.34.252/processed33.json";
        }
        if(url_tumor == null || !url_tumor.contains("http")){
            url_tumor = "http://47.106.34.252/tumor.json";
        }
        setContentView(R.layout.activity_liver_model_display);

        Thread thread = sendRequestWithOkHttp(url);
        Thread thread_tumor = sendRequestWithOkHttp(url_tumor);
        try{
            thread.start();
            thread_tumor.start();
        }catch(Exception e){
            e.printStackTrace();
        }

        glSurfaceView = new GLSurfaceView(this);
//        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
//        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        glSurfaceView.setZOrderOnTop(true);
//        glSurfaceView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Log.d("LiverModeDisplay", "startRender");
        setRanderer();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.addContentView(linearLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        Button b = new Button(this);
        b.setText( "Change Liver Model Display Mode");

        linearLayout.addView(b, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liverModelRenderer.setdisplayLiverModel();
            }
        });

//        Button bt = new Button(this);
//        bt.setText( "Change Tumor Model Display Mode");
//        linearLayout.addView(bt, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                liverModelRenderer.setdisplayTumuerModel();
//            }
//        });

        Button ar = new Button(this);
        ar.setText( "AR mode");
        linearLayout.addView(ar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT ,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiverModelDisplay.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }

    private void setRanderer(){
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        liverModelRenderer = new LiverModelRenderer(this, raw_liverList);
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                && (Build.FINGERPRINT.startsWith("generic")
                ||Build.FINGERPRINT.startsWith("unknown")
                ||Build.MODEL.contains("google_sdk")
                ||Build.MODEL.contains("Emulator")
                ||Build.MODEL.contains("Android SDK built for x86")));
        if(supportsEs2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(liverModelRenderer);
            redererSet = true;
        } else {
            Toast.makeText(this,"This device does not support Opengl ES 2.0",
                    Toast.LENGTH_LONG).show();
            return;
        }
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                                             @Override
                                             public boolean onTouch(View v, MotionEvent event) {
                                                 if(event!=null){
                                                     final float normalizedX = (event.getX() / (float) v.getWidth()) * 2 -1;
                                                     final float normalizedY = -((event.getY() / (float) v.getHeight()) * 2 - 1);
                                                     if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                         glSurfaceView.queueEvent(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 liverModelRenderer.handleTouchPress(
                                                                         normalizedX, normalizedY);
                                                             }
                                                         });
                                                     } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                                                         glSurfaceView.queueEvent(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 liverModelRenderer.handleTouchDrag(
                                                                         normalizedX, normalizedY);
                                                             }
                                                         });
                                                     }
                                                     return true;
                                                 } else {
                                                     return false;
                                                 }
                                             }
                                         });
                setContentView(glSurfaceView);

    }

    private Thread sendRequestWithOkHttp(final String ref_url){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true)
//                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build();

                    Request request = new Request.Builder()
                            .url(ref_url)
//                            .addHeader("Connection","close")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("LiverModelDisplay", "connected");
                    parseJSONWithGSON(responseData);
                    dataReceived = true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return thread;
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        List<LiverModel> list = gson.fromJson(jsonData, new TypeToken<List<LiverModel>>(){}.getType());
        for (LiverModel liverModel : list){
            raw_liverList.add(liverModel);
            Constant.dynamic_model.add(liverModel);
            Log.d("LiverModelDisplay", "name is " + liverModel.getName());
            Log.d("LiverModelDisplay", "data is " + liverModel.getVertices());
            Log.d("LiverModelDisplay", "ref_point is " + liverModel.getRef_point());
        }
    }

    @Override
    protected  void onPause (){
        super.onPause();
        if(redererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
        if(redererSet) {
            glSurfaceView.onResume();
        }
    }


}
