package com.vsoft.restaurant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetServer {
    private final String URL="http://192.168.1.2:3000/api/v1.0";
    public String stringQuery(String vurl, String method){
        HttpURLConnection con=null;
        try {
            URL url = new URL(URL+vurl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(3000);
            con.setRequestMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK) {
                return reader.readLine();
            }
            else {
                return "No string.";
            }
        }
        catch(Exception e) {
            return "Error en el servidor";
        }
    }
}