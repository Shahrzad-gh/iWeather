package com.example.shahrzad;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Shahrzad on 01/01/2016.
 */
public class CityPreference {

    SharedPreferences pref ;

    public CityPreference(Activity activity) {

        pref = activity.getPreferences(Activity.MODE_PRIVATE);
    }
    public String getCity(){

        return pref.getString("city" , "Karaj, IR");
    }

    public void setCity(String city){

        pref.edit().putString("city",city).commit();
    }
}

