package com.example.tool.calculate_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toolbox.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class calc_changeactivity extends AppCompatActivity {

    String result;
    TextView tvResult;
    TextView tvTemp;

    private String URL = "http://fx.kebhana.com/FER1101M.web";
    private String tempstr;

    float usd, jpy;

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
                    tempstr += element.toString() + "\n";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tempstr = tempstr.replaceAll(" ", "");
            tempstr = tempstr.substring(tempstr.indexOf("[{"), tempstr.indexOf("},]}"));
            tvTemp.setText(tempstr);
        }
    }
}