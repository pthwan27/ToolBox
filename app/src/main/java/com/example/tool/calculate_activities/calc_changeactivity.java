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
    Double resulttoDouble;

    TextView tvResult;
    TextView tvchangedResult;
    TextView tvchangedResult2;

    Spinner spinner;

    private String URL = "http://fx.kebhana.com/FER1101M.web";
    private String jsonstr;
    Object obj;
    JSONObject jsonObject;

    JSONArray jsonListarr;

    String[] name = new String[49];
    Double[] price = new Double[49];

    String[] listarr;

    double changedResult;

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
        tvchangedResult2 = findViewById(R.id.tvchangedResult2);

        spinner = findViewById(R.id.changeSpinner);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        listarr = getResources().getStringArray(R.array.listarray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listarr);
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
                    if (position == 0) {
                        Toast.makeText(getApplicationContext(),"나라를 선택하세요!", Toast.LENGTH_LONG).show();
                    } else {
                        tvchangedResult.setText("");
                        changedResult = price[position - 1] * resulttoDouble;
                        String temp = String.valueOf(changedResult);
                        tvchangedResult.setText(name[position-1] + ":" + temp);


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}