package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    String datePicked = "";
    int hour, minute;

    EditText eventTitle, eventDescription;
    Button eventDate, eventTime;

    SQLiteDatabase offlineDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        //add an up button to the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        eventTitle = (EditText) findViewById(R.id.newEventTitle);
        eventDescription = (EditText) findViewById(R.id.newEventText);
        eventDate = (Button) findViewById(R.id.btnDate);
        eventTime = (Button) findViewById(R.id.btnTime);


        createNewEvent(2);

        //Setting the time
        Button btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewEvent.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int currentMinute) {
                                hour = hourOfDay;
                                minute = currentMinute;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, hour, minute);
                                btnTime.setText(DateFormat.format("HH:mm", calendar));
                            }
                        }, 24, 0, true
                );
                timePickerDialog.updateTime(hour, minute);
                timePickerDialog.show();
            }
        });
    }

    public void showDate(View view)
    {
        DialogFragment newFragment = new DatePickerFragment();
        ((DatePickerFragment) newFragment).page = "new_event";
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
        Button btn = findViewById(R.id.btnDate);
        btn.setText(msg);
        datePicked = msg;
        Log.i("date picker: ",datePicked);

    }

    public void createNewEvent(int id) {
        String eTitle = eventTitle.getText().toString();
        String eDescription = eventDescription.getText().toString();
        String eDate = eventDate.getText().toString();
        String eTime = eventTime.getText().toString();
        String eStamp = eDate + " " + eTime;

        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);

        offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID INTEGER, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");
        offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) VALUES (2, '"+eTitle+"','"+eDescription+"','"+eDate+"','"+eTime+"','"+eStamp+"')");

        Log.i("Title ", eTitle);
        Log.i("Title ", eDescription);
        Log.i("Title ", eDate);
        Log.i("Title ", eTime);
        Log.i("Title ", eStamp);
    }
}