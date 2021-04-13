package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class tool_flash extends AppCompatActivity {

    private Button mbtFlashOnOff;
    private boolean mFlashOn;
    private CameraManager mCameraManager;
    private String mCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_flash);
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

    public void onDestroy() {
        flashLightOff();
        super.onDestroy();
    }
}
