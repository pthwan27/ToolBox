package com.example.tool.calculate_activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toolbox.R;


import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class calc_changeactivity extends AppCompatActivity {

    /*환율 부분*/
    String result;
    Double resulttoDouble; // intent로 받아온 걸 double형으로

    TextView tvResult;
    TextView tvchangedResult; // 환율 계산 결과표시

    Spinner spinner;

    private String URL = "http://fx.kebhana.com/FER1101M.web";
    private String jsonstr;
    Object obj;
    JSONObject jsonObject;
    JSONArray jsonListarr;

    String[] name = new String[49]; //ex) 미국 USD, 일본 JPY
    Double[] price = new Double[49]; // 환율 가격 담은 배열

    String[] moneylistarr; // Spinner에 표현하기위한 것

    int selectedposition;

    /*단위 부분*/
    EditText etinputNum;
    Double etinputDouble; // etinputNum -> Double
    String[] lengthlistarr;

    Spinner spinner2;
    Spinner spinner3;

    Button changeBt;
    TextView tvchangedResult2;

    int getsp2position;
    int getsp3position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_changeactivity);


        ImageView imageBack = findViewById(R.id.imageBack);
        //뒤로가는 버튼
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*환율 부분*/
        tvResult = findViewById(R.id.tvResult);
        result = getIntent().getStringExtra("Result");
        tvResult.setText(result);

        resulttoDouble = Double.parseDouble(result);

        tvchangedResult = findViewById(R.id.tvchangedResult);

        spinner = findViewById(R.id.changeSpinner);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        moneylistarr = getResources().getStringArray(R.array.listarray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moneylistarr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setPrompt("선택하세요");

        /*단위 부분*/
        etinputNum = findViewById(R.id.editTextNumber);
        etinputNum.setText(result);

        spinner2 = findViewById(R.id.changeSpinner2);
        spinner3 = findViewById(R.id.changeSpinner3);

        changeBt = findViewById(R.id.changebt);
        tvchangedResult2 = findViewById(R.id.tvchangedResult2);

        lengthlistarr = getResources().getStringArray(R.array.listarray2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lengthlistarr);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter1);
        spinner3.setAdapter(adapter1);

        spinner2.setSelection(0);
        spinner3.setSelection(0);


        changeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getsp2position == 0 || getsp3position == 0) {
                    Toast.makeText(getApplicationContext(), "두개 모두 선택해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    etinputDouble = Double.parseDouble(etinputNum.getText().toString());
                    tvchangedResult2.setText("결과");

                    changelength(etinputDouble, lengthlistarr[getsp2position], lengthlistarr[getsp3position]);
                }
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(URL).get();
                Elements elements = doc.select("body");

                for (Element element : elements) {
                    jsonstr += element.toString() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            jsonstr = jsonstr.replaceAll(" ", "");
            jsonstr = jsonstr.substring(jsonstr.indexOf("=") + 1, jsonstr.indexOf(",]}") + 3);

            try {
                JSONParser parser = new JSONParser();
                obj = parser.parse(jsonstr);
                jsonObject = (JSONObject) obj;
                jsonListarr = (JSONArray) jsonObject.get("리스트");


                for (int i = 0; i < jsonListarr.size(); i++) {
                    JSONObject objinList = (JSONObject) jsonListarr.get(i);
                    String temp = objinList.get("통화명").toString();
                    String temp2 = objinList.get("매매기준율").toString();
                    Double temp3 = Double.parseDouble(temp2);

                    name[i] = temp;
                    price[i] = temp3;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    double changedResult;
                    BigDecimal bigDecimal;
                    String temp;
                    DecimalFormat form = new DecimalFormat("#.###");

                    if (position == 0) {
                        spinner.setSelection(selectedposition, false);
                        Toast.makeText(getApplicationContext(), "나라를 선택하세요!", Toast.LENGTH_LONG).show();
                    } else {
                        selectedposition = position;
                        tvchangedResult.setText("");
                        if (name[position - 1].contains("100"))
                            changedResult = resulttoDouble / price[position - 1] * 100;
                        else changedResult = resulttoDouble / price[position - 1];

                        bigDecimal = new BigDecimal(changedResult);
                        changedResult = Double.parseDouble(String.valueOf(bigDecimal));
                        temp = form.format(changedResult);
                        tvchangedResult.setText(String.valueOf(temp) + " " + name[position - 1].substring(2, 5));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getsp2position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getsp3position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private void changelength(double x, String A, String B) {
        x = resulttoDouble;
        String result;
        x = otherTocm(x, A);
        tvchangedResult2.setText(cmToother(x, B));
    }
    private double otherTocm(double x, String A){
        if(A != "cm"){
            switch (A){
                case "km" :
                    x *= 100000;
                    break;
                case "m" :
                    x *= 100;
                    break;
                case "cm":
                    break;
                case "mm" :
                    x /= 10;
                    break;
                case "ųm" :
                    x /= 10000;
                    break;
                case "nm":
                    x /= 1e+7;
                    break;
                case "mi":
                    x *= 160934;
                    break;
                case"yd":
                    x *= 91.44;
                    break;
                case"ft":
                    x *= 30.48;
                    break;
                default:
                    x *= 2.54;
                    break;
            }
        }
        return x;
    }
    private String cmToother(double x, String B){
        String temp;
        switch (B){
            case "km" :
                temp = "km";
                x /= 100000;
                break;
            case "m" :
                temp="m";
                x /= 100;
                break;
            case "cm":
                temp="cm";
                break;
            case "mm" :
                temp="mm";
                x *= 10;
                break;
            case "ųm" :
                temp="ųm";
                x *= 10000;
                break;
            case "nm":
                temp="nm";
                x *= 1e+7;
                break;
            case "mi":
                temp="mi";
                x /= 160934;
                break;
            case"yd":
                temp="yd";
                x /= 91.44;
                break;
            case"ft":
                temp="ft";
                x /= 30.48;
                break;
            default:
                temp="in";
                x /= 2.54;
                break;
        }
        BigDecimal bigDecimal = new BigDecimal(x);
        x = Double.parseDouble(String.valueOf(bigDecimal));
        DecimalFormat form = new DecimalFormat("#.########");

        return form.format(x)+temp;
    }
}