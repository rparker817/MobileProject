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

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ViewPlanner extends AppCompatActivity {
    Context context;
    LinearLayout.LayoutParams layoutparams;
    LinearLayout linearLayout;
    ScrollView scrollView;
    ArrayList<data> eventArray=new ArrayList<>();
    ArrayList<data> filterdArray = new ArrayList<>();
    String datePicked ="";

    SQLiteDatabase offlineDb;
    private FirebaseAuth mAuth;
    private String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_planner);

        //get instance of firebaseauth then get the current user as a string
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();


        //add an up button to the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        scrollView = (ScrollView)findViewById(R.id.calendarView);

        if (savedInstanceState !=null)
        {

            getDBData(current_user);
           // Button btn = findViewById(R.id.dateSelect);
            datePicked = savedInstanceState.getString("saved_date");
            //btn.setText(datePicked);
            populate_filtered_events_list();
        }
        else
        {
            getDBData(current_user);
            showAllEvents();
        }


    }

    private void showAllEvents() {
        clear_events();
        Date d = new Date();
        String curDate  = DateFormat.format("yyyy-MM-dd", d.getTime()).toString();
        for(int i = 0;i<eventArray.size();i++)
        {
            if(eventArray.get(i).date.compareTo(curDate) >=0)
            {
                createEventEntry(eventArray.get(i).title,eventArray.get(i).description,eventArray.get(i).date,eventArray.get(i).time);
            }

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


    protected void getDBData(String id) {

        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);

        Cursor c = offlineDb.rawQuery("SELECT * FROM events WHERE userID ='"+id+"'", null);


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

    public void showAllEvents(View view) {
        clear_events();
        Date d = new Date();
        String curDate  = DateFormat.format("yyyy-MM-dd", d.getTime()).toString();
        for(int i = 0;i<eventArray.size();i++)
        {
            if(eventArray.get(i).date.compareTo(curDate)>=0)
            {
                createEventEntry(eventArray.get(i).title,eventArray.get(i).description,eventArray.get(i).date,eventArray.get(i).time);
            }

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

    //saves all variables which can be reloaded in the on create function if the screen is rotated.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putString(getString(R.string.saved_date), datePicked);


            //outState.putString(getString(R.string.saved_date), "");



    }

    public void logout(View view) {
        if(current_user != null) {
            mAuth.signOut();
            Toast.makeText(ViewPlanner.this, "User Signed Out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(ViewPlanner.this, "No User is Signed in", Toast.LENGTH_LONG).show();
        }
    }
}