package com.vsoft.restaurant;

//import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
//import android.widget.ImageView;

public class ListRestaurant extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Restaurants rest[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_restaurant);
        //setContentView(R.layout.grid);
        GridView gridView = (GridView)findViewById(R.id.grid);
        //Categorias []cat=(Categorias[])getIntent().getExtras().getSerializable("cat");

        MyGrid ad = new MyGrid(this, getRestaurants());
        gridView.setAdapter(ad);
        gridView.setOnItemClickListener(this);
    }

    private Restaurants []getRestaurants()
    {
        /*rest = new Restaurants[8];
        rest[0]= new Restaurants(1,"Charito1","");
        rest[1]= new Restaurants(2,"Charito2","");
        rest[2]= new Restaurants(3,"Charito3","");
        rest[3]= new Restaurants(4,"Charito4","");
        rest[4]= new Restaurants(1,"Charito5","");
        rest[5]= new Restaurants(2,"Charito6","");
        rest[6]= new Restaurants(3,"Charito7","");
        rest[7]= new Restaurants(4,"Charito8","");
        return rest;*/

        GetServer ob = new GetServer();
        String cad=ob.stringQuery("/list","GET");
        try
        {
            JSONObject json=new JSONObject(cad);
            JSONArray array=json.optJSONArray("restaurants");
            rest=new Restaurants[array.length()];
            for(int i=0;i<array.length();i++)
            {
                JSONObject hijo = array.getJSONObject(i);
                rest[i]=new Restaurants(hijo.optInt("_id"),hijo.optString("name"), hijo.optString("nit"), hijo.optString("property"));
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return rest;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),"ID: "+rest[position].property,Toast.LENGTH_LONG).show();
    }
}
