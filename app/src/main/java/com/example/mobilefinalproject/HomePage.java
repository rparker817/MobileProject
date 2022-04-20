package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class HomePage extends AppCompatActivity {
    String url ="https://api.openweathermap.org/data/2.5/weather?q=calgary,canada&appid=4e4161f8965e59803b6da6aa1cfd96bd";
    String appId ="4e4161f8965e59803b6da6aa1cfd96bd";
    DecimalFormat df = new DecimalFormat("#.#");
    ImageView imgView;
    Bitmap image;
    ArrayList<data> eventArray=new ArrayList<>();
    ConstraintLayout container;
    SQLiteDatabase offlineDb;

    private FirebaseAuth mAuth;
    private String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();
        Log.d("user", current_user.toString());


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
        checkDB();
        getDBData(current_user);

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


    protected void checkDB() {

        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);
        //offlineDb.execSQL("DROP TABLE events ");
        offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID VARCHAR, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");

        Cursor c = offlineDb.rawQuery("SELECT * FROM events", null);
        Log.i("database: ", "calling function"+String.valueOf(c.getCount()));
        Date d = new Date();
        String curDate = DateFormat.format("yyyy-MM-dd", d.getTime()).toString();

        if (c.getCount() != 0) {

        } else {
            Log.i("database: ", "Is empty");
            //offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID VARCHAR, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('qy6qeyYU8LadUrn4Lcq43dvhbqr1','Football','at the sports centre','2022-04-18','16:00','2022-04-18 16:00')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('qy6qeyYU8LadUrn4Lcq43dvhbqr1','Assignment 2','Complete a mobile app','2022-06-5','17:30','2022-06-5 17:30')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('qy6qeyYU8LadUrn4Lcq43dvhbqr1','Get Bread','Safeway has a sale','2022-05-14','12:00','2022-05-14 12:00')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('qy6qeyYU8LadUrn4Lcq43dvhbqr1','Work Event','Do not forget the presentation!','2022-04-28','19:30','2022-04-28 19:30')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('qy6qeyYU8LadUrn4Lcq43dvhbqr1','Suit Pick Up','I need to get a suit for the event tonight','2022-04-28','10:30','2022-04-28 10:30')");

            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('rUjmDYeX4yMk4DcDHORaPwhRPOL2','Tennis','playing tennis tournament','2022-05-17','13:00','2022-05-17 13:00')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('rUjmDYeX4yMk4DcDHORaPwhRPOL2','Friends Birthday','Get birthday present for friend','2022-05-05','14:30','2022-05-05 14:30')");
            offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES ('rUjmDYeX4yMk4DcDHORaPwhRPOL2','Movie Night','Do not forget popcorn','2022-05-18','19:00','2022-05-18 19:00')");


        }
        offlineDb.close();
    }



    protected void getDBData(String id) {

        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);
        //offlineDb.execSQL("DROP TABLE events ");
        offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID VARCHAR, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");

        Cursor c = offlineDb.rawQuery("SELECT * FROM events WHERE userID ='" + id + "'", null);
        Log.i("database: ", "calling function"+String.valueOf(c.getCount()));
        Date d = new Date();
        String curDate = DateFormat.format("yyyy-MM-dd", d.getTime()).toString();

        Log.i("database: ", "Is Not empty");
        if (c.moveToFirst()) {
            eventArray.clear();

            do {
                data event = new data();
                event.id = c.getString(0);
                event.title = c.getString(2);
                event.description = c.getString(3);
                event.date = c.getString(4);
                event.time = c.getString(5);
                event.stamp = c.getString(6);
                eventArray.add(event);

            } while (c.moveToNext());


            Collections.sort(eventArray);

            for (int i = 0; i < eventArray.size(); i++) {
                if (eventArray.get(i).date.compareTo(curDate) >= 0) {
                    createEventEntry(eventArray.get(i).title, eventArray.get(i).description, eventArray.get(i).date, eventArray.get(i).time);
                    break;
                }
            }

        }
        offlineDb.close();


    }
    public void createEventEntry(String title,String description,String date, String time){
        Log.i("creating event: ", "entry");
        container = findViewById(R.id.nextEventDetails);
        View view = getLayoutInflater().inflate(R.layout.event_card,null);
        TextView dateTitle = view.findViewById(R.id.textView13);
        dateTitle.setText(time+" "+date);
        TextView eventTitle = view.findViewById(R.id.textView14);
        eventTitle.setText(title);
        TextView eventDescription = view.findViewById(R.id.textView15);
        eventDescription.setText(description);
        container.addView(view);



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

    public void logout(View view) {
        if(current_user != null) {
            mAuth.signOut();
            Toast.makeText(HomePage.this, "User Signed Out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(HomePage.this, "No User is Signed in", Toast.LENGTH_LONG).show();
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

