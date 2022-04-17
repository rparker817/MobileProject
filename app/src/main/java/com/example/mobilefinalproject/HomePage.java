package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class HomePage extends AppCompatActivity {
    String url ="https://api.openweathermap.org/data/2.5/weather?q=calgary,canada&appid=4e4161f8965e59803b6da6aa1cfd96bd";
    String appId ="4e4161f8965e59803b6da6aa1cfd96bd";
    DecimalFormat df = new DecimalFormat("#.#");
    ImageView imgView;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Log.d("response", "£££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££££");
        //Setting onClick Event on the Calendar button
        Button btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendar();
            }
        });

        //Setting onClick Event on the New Event button
        Button btnNewEvent = (Button) findViewById(R.id.btnNewEvent);
        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEvent();
            }
        });
        imgView = findViewById(R.id.Weather);
        getWeatherDetails();
    }
    public void getWeatherDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("response", response);
                String output = "";
                try {
                    JSONObject jsonresponse = new JSONObject(response);
                    JSONArray jsonArray = jsonresponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonresponse.getJSONObject("main");
                    Double temp = jsonObjectMain.getDouble("temp") -273.15;
                    TextView temperature = findViewById(R.id.temperature);
                    temperature.setText(df.format(temp)+" C");
                    HttpURLConnection urlConnection;
                    InputStream is;
                    String icon = jsonObjectWeather.getString("icon");
                    Log.d("response", icon);
                    String iconUrl = "https://openweathermap.org/img/w/"+icon+".png";
                    loadImage(imgView, iconUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }




    //Method to go to the ViewPlanner Class/Screen
    public void openCalendar() {
        Intent intent = new Intent(this, ViewPlanner.class);
        startActivity(intent);
    }

    //Method to go to the New Event Class/Screen
    public void newEvent() {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }

    public void loadImage(View view,String iconurl)
    {
        HomePage.DownloadImage task=new HomePage.DownloadImage(this);
        Bitmap downloadedImage;
        try {
            downloadedImage=task.execute(iconurl).get();
            imgView.setImageBitmap(downloadedImage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        WeakReference<HomePage> homePageWeakReference;
        public DownloadImage(HomePage activity)
        {
            homePageWeakReference =  new WeakReference<>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HomePage activity = homePageWeakReference.get();
            if(activity == null || activity.isFinishing())
                return;

            //activity.image_load_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            HomePage activity = homePageWeakReference.get();
            if(activity == null || activity.isFinishing())
                return null;
            URL url;
            HttpURLConnection urlConnection;
            InputStream is;

            try {
                url=new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                is=urlConnection.getInputStream();
                activity.image= BitmapFactory.decodeStream(is);
                return activity.image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            HomePage activity = homePageWeakReference.get();
            if(activity == null || activity.isFinishing())
                return;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            HomePage activity = homePageWeakReference.get();
            if(activity == null || activity.isFinishing())
                return;

        }
    }


}