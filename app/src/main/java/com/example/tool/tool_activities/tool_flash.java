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

import com.example.tool.BackPressHandler;
import com.example.toolbox.R;


public class tool_flash extends AppCompatActivity {

    private Button btFlashOnOff;
    private boolean checkFlash;
    private CameraManager mCameraManager;
    private String mCameraId;

    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_flash);
        ImageView imageBack = findViewById(R.id.imageBack);
        //뒤로가는 이미지 버튼
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlightonoff(false);
                finish();
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(getApplicationContext(), "지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 500);
            return;
        }

        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        btFlashOnOff = findViewById(R.id.idFlashOnoff);
        flashlight();

        /*수정 할 것
         * on, off 했을 때 boolean 값도 함께 변경되어야함, 추가로 종료시에도.*/
        btFlashOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlightonoff(checkFlash);
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
        try {
            mCameraManager.setTorchMode(mCameraId, false);
        } catch (
                CameraAccessException e) {
            e.printStackTrace();
        }
        btFlashOnOff.setText("LIGHT");
        checkFlash = true;
    }

    public void flashlightonoff(boolean check) {
        if (check) {
            try {
                mCameraManager.setTorchMode(mCameraId, check);
            } catch (
                    CameraAccessException e) {
                e.printStackTrace();
            }
            btFlashOnOff.setText("OFF");
            checkFlash = !check;
        } else {
            try {
                mCameraManager.setTorchMode(mCameraId, check);
            } catch (
                    CameraAccessException e) {
                e.printStackTrace();
            }
            btFlashOnOff.setText("ON");
            checkFlash = !check;
        }

    }


    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 1000);
        flashlightonoff(false);
    }
}
