package com.example.tool.calculate_activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    String[] namelistarr; // Spinner에 표현하기위한 것

    int selectedposition;
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

        tvResult = findViewById(R.id.tvResult);
        result = getIntent().getStringExtra("Result");
        tvResult.setText(result);

        resulttoDouble = Double.parseDouble(result);

        tvchangedResult = findViewById(R.id.tvchangedResult);

        spinner = findViewById(R.id.changeSpinner);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        namelistarr = getResources().getStringArray(R.array.listarray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namelistarr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setPrompt("선택하세요");
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
                        tvchangedResult.setText(String.valueOf(temp) +" "+ name[position-1].substring(2, 5));
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}