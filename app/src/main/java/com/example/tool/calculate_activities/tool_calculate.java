package com.example.tool.calculate_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tool.BackPressHandler;
import com.example.toolbox.R;

import org.mariuszgromada.math.mxparser.*;

public class tool_calculate extends AppCompatActivity {
    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0,
            btPlus, btMinus, btMultiply, btDivide, btEqual, btC, btBracket, btPercent, btDot, btBs;
    TextView tvInput, tvOutput;

    boolean checking = true;
    String input;
    String output;

    Button btChange;


    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_calculate);

        ImageView imageBack = findViewById(R.id.imageBack);
        //뒤로가는 버튼
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        tvInput = findViewById(R.id.tvInput);
        tvOutput = findViewById(R.id.tvOutput);

        btChange = findViewById(R.id.btChange);

        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "0");
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "1");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "2");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "3");
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "4");
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "5");
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "6");
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "7");
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "8");
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "9");
            }
        });
        btC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOutput.setText("");
                tvInput.setText("");
                output = "";
            }
        });
        btBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checking) {
                    input = tvInput.getText().toString();
                    tvInput.setText(input + "(");
                    checking = false;
                } else {
                    input = tvInput.getText().toString();
                    tvInput.setText(input + ")");
                    checking = true;
                }
            }
        });
        btPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "%");
            }
        });
        btDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "÷");
            }
        });
        btMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "x");
            }
        });
        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "-");
            }
        });
        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + "+");
            }
        });
        btDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                tvInput.setText(input + ".");
            }
        });
        btBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();
                if (input.length() > 0) {
                    input = input.substring(0, input.length() - 1);
                    tvInput.setText(input);
                } else {
                    input = "";
                    tvInput.setText(input);
                }
            }
        });
        btEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tvInput.getText().toString();

                input = input.replaceAll("÷", "/");
                input = input.replaceAll("x", "*");
                input = input.replaceAll("%", "/100");

                Expression exp = new Expression(input);
                output = String.valueOf(exp.calculate());
                tvOutput.setText(output);
            }
        });

        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (output != "NaN" && output.length() > 0) {
                    Intent intent = new Intent(getApplicationContext(), calc_changeactivity.class);
                    intent.putExtra("Result", output);
                    startActivity(intent);
                } else;
            }
        });


    }
}