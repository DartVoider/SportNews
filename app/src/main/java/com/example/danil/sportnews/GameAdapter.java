package com.example.danil.sportnews;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
//Адаптер ListView
public class GameAdapter extends ArrayAdapter<Game> {

    private LayoutInflater inflater;
    private int layout;
    private List<Game> games;

    public GameAdapter(Context context, int resource, List<Game> games) {
        super(context, resource, games);
        this.games = games;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        TextView titleView = (TextView) view.findViewById(R.id.title_view);
        TextView coefView = (TextView) view.findViewById(R.id.coef_view);
        TextView timeView = (TextView) view.findViewById(R.id.time_view);
        TextView placeView = (TextView) view.findViewById(R.id.place_view);
        TextView previewView = (TextView) view.findViewById(R.id.preview_view);
        Game game = games.get(position);
        titleView.setText(game.getTitle());
        //Меняем слово "Коэффициент" и его значение
        String buf1 = game.getCoefficient().substring(0,4);
        String buf2 = "Коэффициент: " + buf1;
        coefView.setText(buf2);
        timeView.setText(game.getTime());
        //Убираем строку Турнир: если она пустая
        if (game.getPlace().length()<9){
            placeView.setText("");
            placeView.setTextSize(0);
            placeView.setHeight(0);
        }else{
            placeView.setText(game.getPlace());
        }
        previewView.setText(game.getPreview());
        return view;
    }
}
