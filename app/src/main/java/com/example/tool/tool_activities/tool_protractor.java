package com.example.tool.tool_activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tool.BackPressHandler;
import com.example.toolbox.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;


public class tool_protractor extends Activity implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;

    Button takepicture;

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tool_protractor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //LayoutInflater는 안드로이드에서 View를 만드는 가장 기본적인 방법
        //surfaceView위에 control 뷰를 붙히기?.
        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);

        //사진저장
        takepicture = (Button) findViewById(R.id.btCapture);
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
            }
        });

        //포커스 맞추기
        ConstraintLayout layoutbackground = (ConstraintLayout) findViewById(R.id.backlayout);
        layoutbackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takepicture.setEnabled(false);
                camera.autoFocus(myAutoFocusCallback);
            }
        });
    }

    //SurfaceHolder method
    //surfaceview 보여줌.
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }


    //사진저장
    ShutterCallback myShutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    PictureCallback myPictureCallback_RAW = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
        }
    };

    PictureCallback myPictureCallback_JPG = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
             Uri uriTarget = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

            OutputStream imageFileOS;
            try {
                imageFileOS = getContentResolver().openOutputStream(uriTarget);
                imageFileOS.write(arg0);
                imageFileOS.flush();
                imageFileOS.close();
                Toast.makeText(com.example.tool.tool_activities.tool_protractor.this,
                        "Image saved: " + uriTarget.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    };
    //포커스
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            takepicture.setEnabled(true);
        }
    };

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 1000);
    }
}
