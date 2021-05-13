package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.toolbox.Tool_activities.tool_calculate;
import com.example.toolbox.Tool_activities.tool_flash;
import com.example.toolbox.Tool_activities.tool_protractor;
import com.example.toolbox.note_activities.tool_memomain;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    final private static String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void tool_flash(View view)
    {
        intent = new Intent(this, tool_flash.class);
        startActivity(intent);
    }
    public void tool_calculate(View view){
        intent = new Intent(this, tool_calculate.class);
        startActivity(intent);
    }
    public void tool_protractor(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        intent = new Intent(this, tool_protractor.class);
        startActivity(intent);
    }
    public void tool_memo(View view){
        intent = new Intent(this, tool_memomain.class);
        startActivity(intent);
    }
}
