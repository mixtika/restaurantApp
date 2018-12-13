package com.vsoft.restaurant;

import android.content.Intent;
import android.graphics.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Restaurants rest[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView)findViewById(R.id.grid);
        Restaurants []temp = getRestaurants();
        if(temp!=null)
        {
            MyGrid ad = new MyGrid(this, getRestaurants());
            gridView.setAdapter(ad);
            gridView.setOnItemClickListener(this);
        }
    }

    private Restaurants []getRestaurants()
    {
        try
        {
            GetServer ob = new GetServer();
            String cad=ob.stringQuery("/list","GET");
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
    @Override
    public void onClick(View v) {
        //View contenedor = v.getRootView();
        /*Intent ob;
        switch (v.getId()) {
            case R.id.button1:
                ob = new Intent(this, NewRestaurant.class);
                startActivity(ob);
                break;
            case R.id.button2:
                ob = new Intent(this,ListRestaurant.class);
                startActivity(ob);
                break;
            default:
                //color = Color.WHITE; // Blano
        }*/
        //contenedor.setBackgroundColor(color);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.opcion1) {
            //GetServer ob = new GetServer();
            //String cad=ob.stringQuery("http://10.0.2.2:3000/api/v1.0/list","GET");
            //String cad=ob.stringQuery("http://192.168.1.2:3000/api/v1.0/save_restaurant?name=Restaurant el Futuro&nit=6601200015&property=Jhon Daniel&street=Boqueron&phone=67935935&lat=-19.75&log=-75.75","POST");
            //Toast.makeText(getApplicationContext(),"en proceso...",Toast.LENGTH_LONG).show();
            Intent ob = new Intent(this, Login.class);
            startActivity(ob);
            return true;
        }
        if(id == R.id.opcion2)
        {
            Intent ob = new Intent(this, NewRestaurant.class);
            startActivity(ob);
        }
        return super.onOptionsItemSelected(item);
    }
}


