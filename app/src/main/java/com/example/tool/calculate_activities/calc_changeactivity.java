package com.example.tool.calculate_activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class calc_changeactivity extends AppCompatActivity {

    String result;
    TextView tvResult;
    TextView tvTemp;

    private String URL = "http://fx.kebhana.com/FER1101M.web";
    private String jsonstr;

    Object obj;
    JSONObject jsonObject;

    JSONArray jsonlistarr;
    JSONObject Jsonlistobj;

    Float usd, jpy;

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

        tvTemp = findViewById(R.id.tvtemp2);
        tvTemp.setMovementMethod(new ScrollingMovementMethod());

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
            return null;
        }

        //Json object객체{}, array배열[]
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jsonstr = jsonstr.replaceAll(" ", "");
            jsonstr = jsonstr.substring(jsonstr.indexOf("=") + 1, jsonstr.indexOf(",]}") + 3);

            try {
                JSONParser parser = new JSONParser();
                obj = parser.parse(jsonstr);
                jsonObject = (JSONObject) obj;
                jsonstr = (String) jsonObject.get("날짜");
                jsonlistarr = (JSONArray) jsonObject.get("리스트");




            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTemp.setText(jsonstr);
        }
    }
}