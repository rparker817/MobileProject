package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ViewPlanner extends AppCompatActivity {
    Button button;
    Context context;
    CardView cardview;
    LinearLayout.LayoutParams layoutparams;
    TextView textview;
    LinearLayout linearLayout;
    ScrollView scrollView;
    ArrayList<data> eventArray=new ArrayList<>();
    ArrayList<data> filterdArray = new ArrayList<>();
    String datePicked;

    SQLiteDatabase offlineDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_planner);
        //add an up button to the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        scrollView = (ScrollView)findViewById(R.id.calendarView);
        getDBData(1);
        for(int i = 0;i<eventArray.size();i++)
        {
            createEventEntry(eventArray.get(i).title,eventArray.get(i).description,eventArray.get(i).date,eventArray.get(i).time);
        }


    }

    public void createEventEntry(String title,String description,String date, String time){

        View view = getLayoutInflater().inflate(R.layout.event_card,null);
        TextView dateTitle = view.findViewById(R.id.textView13);
        dateTitle.setText(time+" "+date);
        TextView eventTitle = view.findViewById(R.id.textView14);
        eventTitle.setText(title);
        TextView eventDescription = view.findViewById(R.id.textView15);
        eventDescription.setText(description);
        linearLayout.addView(view);



    }

    public void showDate(View view)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"DatePicker");
    }
    public void processDatePickerResult(int year, int month,int day)
    {
        String formatted_month ="";
        String formatted_day ="";
        if(month <10)
        {
            formatted_month = "0" + Integer.toString(month);
        }
        else
        {
            formatted_month = Integer.toString(month);
        }
        if(day <10)
        {
                formatted_day = "0" + Integer.toString(day);
        }
        else
        {
            formatted_day = Integer.toString(day);
        }
        String msg = Integer.toString(year)+"-"+formatted_month+"-"+formatted_day;
        //Toast.makeText(this,"Date: " +msg,Toast.LENGTH_SHORT).show();
        Button btn = findViewById(R.id.dateSelect);
        btn.setText(msg);
        datePicked = msg;
        Log.i("date picker: ",datePicked);
        clear_events();
        populate_filtered_events_list();

    }


    public void openHome(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


    protected void getDBData(int id) {

        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);
        //offlineDb.execSQL("DROP TABLE events ");
        //offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID INTEGER, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");
       // offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp ) VALUES (1,'Dinner','a nice time','2022-04-18','20:30','2022-04-18 20:30')");
        //offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (1,'Football','at the sports centre','2022-05-11','16:00','2022-05-11 16:00')");
        //offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (1,'Assignment 2','Complete a mobile app','2022-06-5','17:30','2022-06-5 17:30')");
        //offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (1,'Get Bread','Safeway has a sale','2022-05-14','12:00','2022-05-14 12:00')");
       // offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (1,'Work Event','Do not forget the presentation!','2022-04-28','19:30','2022-04-28 19:30')");
        //offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (1,'Suit Pick Up','I need to get a suit for the event tonight','2022-04-28','10:30','2022-04-28 10:30')");
        //SharedPreferences sharedPreferences = this.getSharedPreferences("com.mmurshed.top10_newsapi_news", Context.MODE_PRIVATE);
        //lastUpdated = sharedPreferences.getString("lastUpdated", "");

       // Date d = new Date();
        //String curDate  = DateFormat.format("yyyy/MM/dd", d.getTime()).toString();
        //Log.i("Last Updated ","Today is: "+curDate+", Last Updated: "+lastUpdated+".");

        //if(!curDate.equals(lastUpdated)) {
            //offlineDb.execSQL("DELETE FROM newsArticles");
            //DownloadTask collectTitleIDTask = new DownloadTask();

            //register at https://gnews.io and create your api key
            //download web-content from the address in the parameter of the .execute() method
            //Toast.makeText(this, "Please do not forget to add your API Key", Toast.LENGTH_LONG).show();
            //Toast.makeText(this, "Note! Note! Note!\nPlease create your user account in the GNEWS.IO (API) website and replace your api key in the following statement.\nThank you for your understanding.", Toast.LENGTH_LONG).show();
            //ToDo just add your api key here
            //collectTitleIDTask.execute("https://gnews.io/api/v4/top-headlines?topic=breaking-news&country=ca&token=745fe4874cc0226345aee917d670cee7&lang=en");
            //top-headlines page from news api is downloaded therefore, store the day of update.
            //sharedPreferences.edit().putString("lastUpdated", curDate).apply();
            Log.i("Loading from: ","Web");
        //}else{
        Cursor c = offlineDb.rawQuery("SELECT * FROM events WHERE userID ="+id, null);


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
            for(int i =0;i<eventArray.size();i++)
            {
                Log.i("data element: ",eventArray.get(0).date);
            }
            offlineDb.close();
            Collections.sort(eventArray);
        }


    }


    public class data implements Comparable<data>
    {
        String id;
        String title;
        String description;
        String date;
        String time;
        String stamp;

        @Override
        public int compareTo(data data) {

            return stamp.compareTo(data.stamp);
        }
    }

    public void clear_events()
        {

            while (linearLayout.getChildCount() >0)
            {
                linearLayout.removeView(linearLayout.getChildAt(0));
            }
        }

    public void populate_filtered_events_list()
    {
        filterdArray.clear();
        for(int i = 0;i <eventArray.size();i++)
        {
            if(eventArray.get(i).date.compareTo(datePicked) ==0)
            {
                filterdArray.add(eventArray.get(i));
            }
        }
        Collections.sort(filterdArray);
        if(filterdArray.size()>0)
        {
            for(int i =0;i<filterdArray.size();i++)
            {
                createEventEntry(filterdArray.get(i).title,filterdArray.get(i).description,filterdArray.get(i).date,filterdArray.get(i).time);
            }

        }
       else
       {
           createEventEntry("","","","No Events On This Date");
       }
    }

}