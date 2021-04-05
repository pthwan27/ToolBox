package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import org.mariuszgromada.math.mxparser.*;
import android.widget.LinearLayout;
import android.widget.TextView;

public class tool_calculate extends AppCompatActivity {
    LinearLayout btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnPlus, btnMinus, btnMultiply, btnDivide, btnEqual, btnC, btnBracket, btnPercent, btnDot;
    TextView tv1, tv2;
    boolean checking  = false;
    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_calculate);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);

        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnEqual = findViewById(R.id.btnEqual);

        btnC = findViewById(R.id.btnC);
        btnBracket = findViewById(R.id.btnBracket);
        btnPercent = findViewById(R.id.btnPercent);
        btnDot = findViewById(R.id.btnDot);

        tv1 = findViewById(R.id.tvInput);
        tv2 = findViewById(R.id.tvOutput);

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "0");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + "9");
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText("");
                tv1.setText("");
            }
        });
        btnBracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checking){
                    input = tv1.getText().toString();
                    tv1.setText(input + ")");
                    checking = false;
                }else{
                    input = tv1.getText().toString();
                    tv1.setText(input + "(");
                    checking = true;
                }
            }
        });
        btnPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + " % ");
            }
        });
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + " ÷ ");
            }
        });
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + " x ");
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + " - ");
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + " + ");
            }
        });
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();
                tv1.setText(input + ".");
            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = tv1.getText().toString();

                input = input.replaceAll("÷", "/");
                input = input.replaceAll("x", "*");
                input = input.replaceAll("%", "/100");

                Expression exp = new Expression(input);
                String ans = String.valueOf(exp.calculate());
                tv2.setText(ans);
            }
        });
    }
}