package com.example.toolbox.Tool_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.mariuszgromada.math.mxparser.*;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toolbox.BackPressHandler;
import com.example.toolbox.R;

public class tool_calculate extends AppCompatActivity {
    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0,
            btPlus, btMinus, btMultiply, btDivide, btEqual, btC, btBracket, btPercent, btDot, btBs;
    TextView tv1, tv2;
    boolean checking = true;
    String input;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_calculate);


        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        bt8 = findViewById(R.id.bt8);
        bt9 = findViewById(R.id.bt9);
        bt0 = findViewById(R.id.bt0);

        btPlus = findViewById(R.id.btPlus);
        btMinus = findViewById(R.id.btMinus);
        btMultiply = findViewById(R.id.btMultiply);
        btDivide = findViewById(R.id.btDivide);
        btEqual = findViewById(R.id.btEqual);


        btC = findViewById(R.id.btC);
        btBracket = findViewById(R.id.btBracket);
        btPercent = findViewById(R.id.btPercent);
        btDot = findViewById(R.id.btDot);
        btBs = findViewById(R.id.btBs);

        tv1 = findViewById(R.id.tvInput);
        tv2 = findViewById(R.id.tvOutput);

        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "0");
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "1");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "2");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "3");
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "4");
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "5");
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "6");
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "7");
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "8");
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "9");
            }
        });
        btC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText("");
                tv1.setText("");
            }
        });
        btBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checking) {
                    input = tv1.getText().toString();
                    tv1.setText(input + "(");
                    checking = false;
                } else {
                    input = tv1.getText().toString();
                    tv1.setText(input + ")");
                    checking = true;
                }
            }
        });
        btPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "%");
            }
        });
        btDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "÷");
            }
        });
        btMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "x");
            }
        });
        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "-");
            }
        });
        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "+");
            }
        });
        btDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + ".");
            }
        });
        btBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                    tv1.setText(input);
                } else {
                    input = "";
                    tv1.setText(input);
                }
            }
        });
        btEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();

                input = input.replaceAll("÷", "/");
                input = input.replaceAll("x", "*");
                input = input.replaceAll("%", "/100");

                Expression exp = new Expression(input);
                String result = String.valueOf(exp.calculate());
                tv2.setText(result);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);
    }
}