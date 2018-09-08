package com.example.danil.sportnews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListNews extends AppCompatActivity {

    private List<Game> games = new ArrayList();
    ListView newsList;

    public static String LOG_TAG = "my_log";
    private String kindofsport = "football";//переменная для передачи выбранной категории
    private String link = "";//переменная для передачи выбранной ссылки

    //вывод списка
    public void updateCat(){
        newsList = (ListView) findViewById(R.id.newsList);
        // создаем адаптер
        GameAdapter gameAdapter = new GameAdapter(this, R.layout.list_item, games);
        // устанавливаем адаптер
        newsList.setAdapter(gameAdapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //проверяем подключение
                if (isNetworkAvailable()){
                    // получаем выбранный пункт
                    Game selectedGame = (Game)parent.getItemAtPosition(position);
                    link = selectedGame.getArticle();
                    //получаем статью и отображаем
                    new ParseArt().execute();
                }else {
                    //выводим сообщение на экран
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Проблема с подключением к серверу. Пожалуйста, проверьте интернет соединение", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };
        newsList.setOnItemClickListener(itemListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        //показываем категорию
        TextView cattv = (TextView)findViewById(R.id.category);
        //если есть сохраненные данные
        if (savedInstanceState != null) {
            kindofsport = savedInstanceState.getString("kindofsport");
            switch (kindofsport){
                case "football":
                    cattv.setText("ФУТБОЛ");
                    break;
                case "hockey":
                    cattv.setText("ХОККЕЙ");
                    break;
                case "volleyball":
                    cattv.setText("ВОЛЕЙБОЛ");
                    break;
                case "tennis":
                    cattv.setText("ТЕННИС");
                    break;
                case "basketball":
                    cattv.setText("БАСКЕТБОЛ");
                    break;
                case "cybersport":
                    cattv.setText("КИБЕРСПОРТ");
                    break;
                    default:
                        cattv.setText("ФУТБОЛ");
                        break;
            }
            updateCat();
        }
        //проверяем подключение
        if (isNetworkAvailable()){
            new ParseCat().execute();
        } else {
            //выводим сообщение на экран
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Проблема с подключением к серверу. Пожалуйста, проверьте интернет соединение", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    //сохраняем если что то изменилось
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("kindofsport", kindofsport);
    }

    //функция для проверки подключения
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //получение списка статей по категориям
    private class ParseCat extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://mikonatoruri.win/list.php?category="+kindofsport);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                //выводим сообщение об ошибке
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Проблема с подключением к серверу. Пожалуйста, проверьте интернет соединение", Toast.LENGTH_SHORT);
                toast.show();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONObject dataJsonObj = null;
            games.clear();
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray events = dataJsonObj.getJSONArray("events");
                // парсим json
                for (int i = 0; i < events.length(); i++) {
                    JSONObject event = events.getJSONObject(i);
                    String title = event.getString("title");
                    String coefficient = event.getString("coefficient");
                    String time = event.getString("time");
                    String place = event.getString("place");
                    String preview = event.getString("preview");
                    String article = event.getString("article");
                    Game game = new Game(title, coefficient, time, place, preview, article);
                    games.add(game);
                }
            } catch (JSONException e) {
                Log.d(LOG_TAG, "WARNING: " + e.toString());
            //    e.printStackTrace();
            }
            //выводим список
           updateCat();
        }
    }

    //получение статьи по ссылке
    private class ParseArt extends AsyncTask<Void, Void, String>{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL("http://mikonatoruri.win/post.php?article="+link);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
            } catch (Exception e) {
                //вывод сообщения об ошибке
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Проблема с подключением к серверу. Пожалуйста, проверьте интернет соединение", Toast.LENGTH_SHORT);
                toast.show();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONObject dataJsonObj = null;
            Intent intent = new Intent(ListNews.this, ArticleActivity.class);//интент для второго активити
            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray articles = dataJsonObj.getJSONArray("article");
                //парсим json
                String team1 = dataJsonObj.getString("team1");
                String team2 = dataJsonObj.getString("team2");
                String time = dataJsonObj.getString("time");
                String tournament = dataJsonObj.getString("tournament");
                String prediction = dataJsonObj.getString("prediction");
                //помещаем данные в интент
                intent.putExtra("team1",team1);
                intent.putExtra("team2",team2);
                intent.putExtra("time",time);
                intent.putExtra("tournament",tournament);
                intent.putExtra("prediction",prediction);
                Log.d(LOG_TAG, "team1: " + team1);
                Log.d(LOG_TAG, "team2: " + team2);
                Log.d(LOG_TAG, "time: " + time);
                Log.d(LOG_TAG, "tournament: " + tournament);
                Log.d(LOG_TAG, "prediction: " + prediction);
                // парсим json
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String header = article.getString("header");
                    String text = article.getString("text");
                    //помещаем данные в интент
                    intent.putExtra("header"+String.valueOf(i+1),header);
                    intent.putExtra("text"+String.valueOf(i+1),text);
                }
                //запускаем активити
                startActivity(intent);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "WARNING: " + e.toString());
               // e.printStackTrace();
            }
        }
    }

    // меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
    int order = item.getOrder();
    //проверяем подключение
    if (isNetworkAvailable()){
        TextView category = (TextView)findViewById(R.id.category);
         switch (order){
             case 1:
                 kindofsport = "football";
                 category.setText("ФУТБОЛ");
                 break;
             case 2:
                 kindofsport = "hockey";
                 category.setText("ХОККЕЙ");
                 break;
             case 3:
                 kindofsport = "volleyball";
                 category.setText("ВОЛЕЙБОЛ");
                 break;
             case 4:
                 kindofsport = "tennis";
                 category.setText("ТЕННИС");
                 break;
             case 5:
                 kindofsport = "basketball";
                 category.setText("БАСКЕТБОЛ");
                 break;
             case 6:
                 kindofsport = "cybersport";
                 category.setText("КИБЕРСПОРТ");
                 break;
             default:
                 kindofsport = "football";
                 category.setText("ФУТБОЛ");
                 break;
         }
         //обновляем список
             new ParseCat().execute();
     } else {
        //выводим сообщение об ошибке
         Toast toast = Toast.makeText(getApplicationContext(),
                 "Проблема с подключением к серверу. Пожалуйста, проверьте интернет соединение", Toast.LENGTH_LONG);
         toast.show();
     }
        return super.onOptionsItemSelected(item);
    }
}
