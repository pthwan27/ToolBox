package com.example.tool.tool_activities;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toolbox.R;


public class tool_flash extends AppCompatActivity {

    private Button mbtFlashOnOff;
    private boolean mFlashOn;
    private CameraManager mCameraManager;
    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_flash);
        ImageView imageBack = findViewById(R.id.imageBack);
        //뒤로가는 버튼
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(getApplicationContext(), "device is not support", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
            return;
        }

        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        mbtFlashOnOff = findViewById(R.id.idFlashOnoff);


        /*수정 할 것
        * on, off 했을 때 boolean 값도 함께 변경되어야함, 추가로 종료시에도.*/
        mbtFlashOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlight();
                if (mFlashOn == false) {
                    flashLightOff();
                    mbtFlashOnOff.setText("ON");
                } else {
                    flashLightOn();
                    mbtFlashOnOff.setText("OFF");
                }
            }
        });
    }

    void flashlight() {
        if (mCameraId == null) {
            try {
                for (String id : mCameraManager.getCameraIdList()) {
                    CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                    Boolean flahsAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    Integer lensFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flahsAvailable != null && flahsAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        mCameraId = id;
                        break;
                    }
                }
            } catch (CameraAccessException e) {
                mCameraId = null;
                e.printStackTrace();
                return;
            }
        }
        mFlashOn = !mFlashOn;
        try {
            mCameraManager.setTorchMode(mCameraId, mFlashOn);
        } catch (
                CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void flashLightOn() {
        try {
            mCameraManager.setTorchMode(mCameraId, true);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mbtFlashOnOff.setText("ON");
    }

    public void flashLightOff() {
        //stopFlicker(); //stopSOS();
        try {
            mCameraManager.setTorchMode(mCameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mbtFlashOnOff.setText("OFF");
    }

}
