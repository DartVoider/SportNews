package com.example.danil.sportnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    //функция для программного изменения отступа в случае, если строка нулевая
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
           // view.requestLayout();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //получение данных из интента
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String tournament = intent.getStringExtra("tournament");
        String header1 = intent.getStringExtra("header1");
        String header2 = intent.getStringExtra("header2");
        String text1 = intent.getStringExtra("text1");
        String text2 = intent.getStringExtra("text2");
        String prediction = intent.getStringExtra("prediction");
        Log.d("my1",String.valueOf(tournament.length()));
        //вывод информации
        TextView timeView = (TextView)findViewById(R.id.time2_view);
        timeView.setText(time);
        TextView tournamentView = (TextView)findViewById(R.id.tournament_view);
        if(header1 == null){
            tournamentView.setTextSize(0);
            setMargins(tournamentView,0,0,0, 0);
        } else{
            tournamentView.setText(tournament);
        }
        TextView header1View = (TextView)findViewById(R.id.header1_view);
        if(header1 == null){
            header1View.setTextSize(0);
            setMargins(header1View,0,0,0, 0);
        } else{
            header1View.setText(header1);
        }
        TextView header2View = (TextView)findViewById(R.id.header2_view);
        if(header2 == null){
            setMargins(header2View,0,0,0, 0);
            header2View.setTextSize(0);
        } else{
            header2View.setText(header2);
        }
        TextView text1View = (TextView)findViewById(R.id.text1_view);
        if(text1 == null){
            text1View.setTextSize(0);
            setMargins(text1View,0,0,0, 0);
        } else{
            text1View.setText(text1);
        }
        TextView text2View = (TextView)findViewById(R.id.text2_view);
        if(text2 == null){
            text2View.setTextSize(0);
            setMargins(text2View,0,0,0, 0);
        } else{
            text2View.setText(text2);
        }
        TextView predictionView = (TextView)findViewById(R.id.prediction_view);
        predictionView.setText(prediction);
    }
}
