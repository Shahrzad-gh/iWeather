package com.example.shahrzad;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class WeatherFragment extends Fragment {

    Typeface weatherFont;

    public ImageView weather_icon;
    public TextView min_max_temp;
    public TextView current_weather_disc;
    public TextView current_weather;
    public TextView city_country;

    Handler handler;
    private ProgressBar spinner;
    private String photo_url_str;
    private TextView NameOFWeek;
    private TextView DayOne;
    private TextView DayTwo;
    private TextView DayThree;
    private TextView DayFour;
    private TextView tempDayOne;
    private TextView tempDayTwo;
    private TextView tempDayThree;
    private TextView tempDayFour;
    private ImageView conditionPicNoOne;
    private ImageView conditionPicNoTwo;
    private ImageView conditionPicNoThree;
    private ImageView conditionPicNoFour;


    public WeatherFragment() {
        // Required empty public constructor
        handler = new Handler();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fontello.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        city_country = (TextView) view.findViewById(R.id.city_country);
        weather_icon = (ImageView) view.findViewById(R.id.weather_ico);
        current_weather = (TextView) view.findViewById(R.id.current_temp);
        current_weather_disc = (TextView) view.findViewById(R.id.current_weather_disc);
        //min_max_temp = (TextView) view.findViewById(R.id.current_temp);
        // humidity = (TextView) view.findViewById(R.id.humidity);
        // wind_speed = (TextView) view.findViewById(R.id.wind_speed);
        NameOFWeek = (TextView) view.findViewById(R.id.dayOfWeek);
        DayOne = (TextView)view.findViewById(R.id.NameDayOne);
        DayTwo = (TextView)view.findViewById(R.id.NameDayTwo);
        DayThree = (TextView)view.findViewById(R.id.NameDayThree);
        DayFour = (TextView)view.findViewById(R.id.NameDayFour);

        tempDayOne = (TextView)view.findViewById(R.id.TempDayOne);
        tempDayTwo = (TextView)view.findViewById(R.id.TempDayTwo);
        tempDayThree = (TextView)view.findViewById(R.id.TempDayThree);
        tempDayFour = (TextView)view.findViewById(R.id.TempDayFour);

        conditionPicNoOne = (ImageView)view.findViewById(R.id.weather_icon_condition_DayOne);
        conditionPicNoTwo = (ImageView)view.findViewById(R.id.weather_icon_condition_DayTwo);
        conditionPicNoThree = (ImageView)view.findViewById(R.id.weather_icon_condition_DayThree);
        conditionPicNoFour = (ImageView)view.findViewById(R.id.weather_icon_condition_DayFour);

        //weather_icon.setTypeface(weatherFont);

        return view;
    }

    public void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject json = RemoteWeather.getJSON(getActivity(), city);
                final JSONObject forecast = RemoteWeather.getForecastJSON(getActivity(), city);

                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.palce_not_found), Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json, forecast);
                        }
                    });
                }
            }

        }.start();
    }

    public void renderWeather(JSONObject currentJson,JSONObject forecastJson) {
        try {

            Log.d("WFRW","forecastJSON IS: " + forecastJson.toString());
            Log.d("WFRW","currentJSON IS: " + currentJson.toString());

            city_country.setText(currentJson.getString("name").toUpperCase(Locale.US) + ", "  +
                    currentJson.getJSONObject("sys").getString("country"));

            JSONObject currentdetails = currentJson.getJSONArray("weather").getJSONObject(0);
            JSONObject main = currentJson.getJSONObject("main");

            current_weather_disc.setText(currentdetails.getString("description").toLowerCase(Locale.US) + "\n" + "Humidity : " +
                    main.get("humidity") + "%" + "\n" + "pressure : " + main.getString("pressure") + "  hpa");


            current_weather.setText(String.format("%.0f", main.getDouble("temp") - 273.15) + " °C");
            Log.d("WFRWdetails","IS " + currentdetails.toString());

            setWeatherIcon(currentdetails.getInt("id"), 0, null);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            NameOFWeek.setText(dayOfTheWeek);

            Log.d("WFRWid","Is: "+ currentdetails.getInt("id"));

            switch(dayOfTheWeek){
                case "Mon":
                    DayOne.setText("TUE");
                    DayTwo.setText("WED");
                    DayThree.setText("THU");
                    DayFour.setText("FRI");
                    break;
                case "Tue":
                    DayOne.setText("WED");
                    DayTwo.setText("THU");
                    DayThree.setText("FRI");
                    DayFour.setText("SAT");
                    break;
                case "Wed":
                    DayOne.setText("THU");
                    DayTwo.setText("FRI");
                    DayThree.setText("SAT");
                    DayFour.setText("SUN");
                    break;
                case "Thu":
                    DayOne.setText("FRI");
                    DayTwo.setText("SAT");
                    DayThree.setText("SUN");
                    DayFour.setText("MON");
                    break;
                case "Fri":
                    DayOne.setText("SAT");
                    DayTwo.setText("SUN");
                    DayThree.setText("MON");
                    DayFour.setText("TUE");
                    break;
                case "Sat":
                    DayOne.setText("SUN");
                    DayTwo.setText("MON");
                    DayThree.setText("TUE");
                    DayFour.setText("WED");
                    break;
                case "Sun":
                    DayOne.setText("MON");
                    DayTwo.setText("TUE");
                    DayThree.setText("WED");
                    DayFour.setText("THU");
                    break;
            }

            //////////////////////////////////////Forecast Details//////////////////////////////////////////////////////
            JSONArray forecastdetails = forecastJson.getJSONArray("list");

            JSONObject forecastDayNo1 = forecastdetails.getJSONObject(4);
            JSONObject forecastMain = forecastDayNo1.getJSONObject("main");
            String pod = forecastDayNo1.getJSONObject("sys").getString("pod");

            //////////SET ICON
            int idOne = forecastDayNo1.getJSONArray("weather").getJSONObject(0).getInt("id");
            setWeatherIcon(idOne,1,pod);
            Log.d("id","forcastDetalis List Is: "+ idOne);
            /////////////////
            tempDayOne.setText(String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
//            Log.d("WFRW2","forcastDetalis List Is: " +String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
//                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));

            Log.d("id","forecastDayNo1 List Is: "+ idOne);

            ///////////////////////////////////////////////////////////
            forecastMain =null;

            JSONObject forecastDayNo2 = forecastdetails.getJSONObject(12);
            forecastMain = forecastDayNo2.getJSONObject("main");
            pod = forecastDayNo2.getJSONObject("sys").getString("pod");

            tempDayTwo.setText(String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
//            Log.d("WFRW2","forcastDetalis List Is: " +String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
//                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
            Log.d("Main",forecastMain.toString());
            //////////SET ICON
            int idTwo = forecastDayNo2.getJSONArray("weather").getJSONObject(0).getInt("id");
            setWeatherIcon(idTwo,2,pod);
            Log.d("id","forecastDayNo2 List Is: "+ idTwo);

            /////////////////////////////////////////////////////////////
            forecastMain =null;

            JSONObject forecastDayNo3 = forecastdetails.getJSONObject(20);
            forecastMain = forecastDayNo3.getJSONObject("main");
            pod = forecastDayNo3.getJSONObject("sys").getString("pod");

            tempDayThree.setText(String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
//            Log.d("WFRW3","forcastDetalis List Is: " +String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
//                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
            Log.d("Main",forecastMain.toString());
            int idThree = forecastDayNo3.getJSONArray("weather").getJSONObject(0).getInt("id");
            setWeatherIcon(idThree,3,pod.toString());
            Log.d("id","forecastDayNo3 List Is: "+ idThree);
            ///////////////////////////////////////////////////////////////////
            forecastMain =null;

            JSONObject forecastDayNo4 = forecastdetails.getJSONObject(28);
            forecastMain = forecastDayNo4.getJSONObject("main");
            pod = forecastDayNo4.getJSONObject("sys").getString("pod");

            tempDayFour.setText(String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
//            Log.d("WFRW4","forcastDetalis List Is: " +String.format("%.0f", forecastMain.getDouble("temp_max") - 273.15) + " °C\n" +
//                    (String.format("%.0f",forecastMain.getDouble("temp_min") - 273.15) + " °C"));
            Log.d("Main",forecastMain.toString());
            int idFour = forecastDayNo4.getJSONArray("weather").getJSONObject(0).getInt("id");
            setWeatherIcon(idFour,4,pod);
            Log.d("id","forecastDayNo4 List Is: "+ idFour);

//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updateOn = df.format(new Date(json.getLong("dt") * 1000));
//            UpdateFiled.setText("Last Update On :" + updateOn);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setWeatherIcon(int id, int numberOfDay, String pod) {
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        Log.d("timeZone",tz.toString());
        switch (id) {
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.cloudswith2lighting);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.cloudswith2lighting);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.cloudswith2lighting);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.cloudswith2lighting);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.cloudswith2lighting);
                }
                break;
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
            case 520:
            case 521:
            case 522:
            case 531:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.cloudswithrain);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.cloudswithrain);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.cloudswithrain);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.cloudswithrain);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.cloudswithrain);
                }
                break;
            case 500:
            case 501:
            case 502:
            case 503:
            case 504:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.sunrainsnow01);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.sunrainsnow01);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.sunrainsnow01);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.sunrainsnow01);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.sunrainsnow01);
                }
                break;
            case 511:
            case 600:
            case 601:
            case 602:
            case 611:
            case 612:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.cloudswithsnow);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.cloudswithsnow);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.cloudswithsnow);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.cloudswithsnow);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.cloudswithsnow);
                }
                break;
            case 700:
            case 701:
            case 711:
            case 721:
            case 731:
            case 741:
            case 751:
            case 761:
            case 762:
            case 771:
            case 781:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.moonhaze01);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.moonhaze01);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.moonhaze01);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.moonhaze01);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.moonhaze01);
                }
                break;
            case 800:
                switch(numberOfDay) {
                    case 1:
                        if(pod == "d")
                            conditionPicNoOne.setImageResource(R.drawable.sunhaze01);
                        else
                            conditionPicNoOne.setImageResource(R.drawable.moonhaze01);

                        break;
                    case 2:
                        if(pod == "d")
                            conditionPicNoTwo.setImageResource(R.drawable.sunhaze01);
                        else
                            conditionPicNoTwo.setImageResource(R.drawable.moonhaze01);

                        break;
                    case 3:
                        if(pod =="d")
                            conditionPicNoThree.setImageResource(R.drawable.sunhaze01);
                        else
                            conditionPicNoThree.setImageResource(R.drawable.moonhaze01);

                        break;
                    case 4:
                        if(pod == "d")
                            conditionPicNoFour.setImageResource(R.drawable.sunhaze01);
                        else
                            conditionPicNoFour.setImageResource(R.drawable.moonhaze01);
                        break;
                    default:
                        if(pod == "d")
                            weather_icon.setImageResource(R.drawable.sunhaze01);
                        else
                            weather_icon.setImageResource(R.drawable.moonhaze01);
                }
                break;
            case 801:
                switch(numberOfDay) {
                    case 1:
                        if(pod =="d")
                            conditionPicNoOne.setImageResource(R.drawable.sunwith3clouds);
                        else
                            conditionPicNoOne.setImageResource(R.drawable.moonwithclouds);

                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.moonwithclouds);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.moonwithclouds);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.moonwithclouds);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.moonwithclouds);
                }
                break;
            case 802:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.cloudswithlighting);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.cloudswithlighting);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.cloudswithlighting);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.cloudswithlighting);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.cloudswithlighting);
                }
                break;
            case 803:
            case 804:
                switch(numberOfDay) {
                    case 1:
                        conditionPicNoOne.setImageResource(R.drawable.clouds);
                        break;
                    case 2:
                        conditionPicNoTwo.setImageResource(R.drawable.clouds);
                        break;
                    case 3:
                        conditionPicNoThree.setImageResource(R.drawable.clouds);
                        break;
                    case 4:
                        conditionPicNoFour.setImageResource(R.drawable.clouds);
                        break;
                    default:
                        weather_icon.setImageResource(R.drawable.clouds);
                }
                break;
        }

    }

    public void changeCity(String city) {

        updateWeatherData(city);
    }



}
