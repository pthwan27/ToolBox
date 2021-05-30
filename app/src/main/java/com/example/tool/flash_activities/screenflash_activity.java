package com.example.tool.flash_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.toolbox.R;

public class screenflash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screenflash_activity);

        Intent intent = getIntent();
        int value = intent.getIntExtra("Brightness", 0);

        changeScreenBrightness(value);

    }

    private void changeScreenBrightness(int value){
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = value * 1.0f / 255;
        window.setAttributes(layoutParams);
    }
}