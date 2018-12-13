package com.vsoft.restaurant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

//import android.widget.ImageView;


public class MyGrid extends BaseAdapter {
    private LayoutInflater inflater;
    private int []colors={Color.RED, Color.MAGENTA, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.CYAN};
    private Restaurants rest[];
    public MyGrid(Context context, Restaurants rest[]) {
        this.rest = rest;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return rest.length;
    }

    @Override
    public Object getItem(int i) {
        return rest[i];
    }

    @Override
    public long getItemId(int i) {
        return rest[i]._id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.grid_item,viewGroup,false);
        }
        ImageView picture=(ImageView) view.findViewById(R.id.imagen);
        TextView name=(TextView) view.findViewById(R.id.texto);
        Random r=new Random();
        picture.setBackgroundColor(colors[r.nextInt(6)]);
        //picture.setImageBitmap(getImagen(rest[i].imagen));
        //picture.setImageBitmap(R.drawable.test);
        name.setText(rest[i].name);
        return view;
    }

    private Bitmap getImagen(String file)
    {
        String url="http://10.0.2.2/ComayPunto/"+file;
        URL imageUrl = null;
        try {
            imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
            return bmp;
        }catch (Exception ex){}
        return null;
    }
}
