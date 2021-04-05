package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;

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
}
