package com.example.shahrzad;

import android.content.Context;
import android.util.Log;



import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shahrzad on 01/01/2016.
 */
public class RemoteWeather {

    public static final String OPEN_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static JSONObject getJSON(Context mycontext,String city) {
        try {
            String url_con = OPEN_WEATHER_URL + city;
            URL url = new URL(String.format(url_con ,"&appid="));

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            Log.d("URL", url.toString());

            connection.addRequestProperty("x-api-key", mycontext.getString(R.string.open_weather_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp = "";
            while((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            Log.d("RWCurrentData","IS : " + data.toString());

            if (data.getInt("cod")!=200){
                return null;
            }

            return data;

        } catch (Exception e) {
            return null;
        }
    }

    //Forecast
    public static final String OPEN_WEATHER_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q=";

    public static JSONObject getForecastJSON(Context mycontext,String city) {
        try {
            String url_con = OPEN_WEATHER_FORECAST_URL + city;
            URL url = new URL(String.format(url_con ,"&appid="));

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            Log.d("URL", url.toString());

            connection.addRequestProperty("x-api-key", mycontext.getString(R.string.open_weather_app_id));

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String tmp = "";
            while((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            Log.d("RWCurrentData","IS : " + data.toString());

            if (data.getInt("cod")!=200){
                return null;
            }

            return data;

        } catch (Exception e) {
            return null;
        }
    }


}
