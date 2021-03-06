package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    //Variables to pick the time
    String datePicked = "";
    int hour, minute;

    //Making the variables for the input that the user would enter
    EditText eventTitle, eventDescription;
    Button eventDate, eventTime;

    //Database variable
    SQLiteDatabase offlineDb;

    //Variables for firebase
    private FirebaseAuth mAuth;
    private String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        //Getting the firebase instance
        mAuth = FirebaseAuth.getInstance();
        //Getting the current user's ID
        current_user = mAuth.getCurrentUser().getUid();

        //add an up button to the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //On click event for the add event button
        Button btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formCheck();
            }
        });

        //Assigning the variables created above to the text fields using their ID
        eventTitle = (EditText) findViewById(R.id.newEventTitle);
        eventDescription = (EditText) findViewById(R.id.newEventText);
        eventDate = (Button) findViewById(R.id.btnDate);
        eventTime = (Button) findViewById(R.id.btnTime);
        if(savedInstanceState!=null)
        {
            eventDate.setText(savedInstanceState.getString("savedDate"));
            eventTime.setText(savedInstanceState.getString("savedTime"));
        }

        //Setting the time
        Button btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating a new TimePickerDialog
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

    //Method to show the date
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

    //Method to check if the fields entered in the input boxes is valid or not
    public void formCheck()
    {
        String eTitle = eventTitle.getText().toString();

        String eDate = eventDate.getText().toString();
        String eTime = eventTime.getText().toString();
        String eStamp = eDate + " " + eTime;

        if(eTitle.equals("") || eTitle.equals("Event Title"))
        {
            Toast.makeText(NewEvent.this, "Please add a title", Toast.LENGTH_LONG).show();
        }
        else if(eDate.equals("") || eDate.equals("Pick Date"))
        {
            Toast.makeText(NewEvent.this, "Please pick a date", Toast.LENGTH_LONG).show();
        }
        else if(eTime.equals("") || eTime.equals("Pick Time"))
        {
            Toast.makeText(NewEvent.this, "Please pick a Time", Toast.LENGTH_LONG).show();
        }
        else
        {
            createNewEvent(current_user);
        }
    }

    //Method to create a new event
    public void createNewEvent(String id) {
        //Getting the text input and storing it as a String
        String eTitle = eventTitle.getText().toString();
        String eDescription = eventDescription.getText().toString();
        String eDate = eventDate.getText().toString();
        String eTime = eventTime.getText().toString();
        String eStamp = eDate + " " + eTime;

        //Creating a new database or opening the one with if already created
        offlineDb = this.openOrCreateDatabase("eventsDatabase", MODE_PRIVATE, null);

        //Creating the table
        offlineDb.execSQL("CREATE TABLE IF NOT EXISTS events (id INTEGER PRIMARY KEY, userID String, title VARCHAR, description VARCHAR, date DATE, time TIME,stamp DATETIME)");
        //Inserting the valeus in the table
        offlineDb.execSQL("INSERT INTO events (userID, title, description , date,time,stamp  ) " +
                "VALUES ('"+id+"', '"+eTitle+"','"+eDescription+"','"+eDate+"','"+eTime+"','"+eStamp+"')");

        //Displaying a message if the event is created and then moving to the HomePage
        Toast.makeText(NewEvent.this, "Event added successfully!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);


        Log.i("Title ", eTitle);
        Log.i("Description ", eDescription);
        Log.i("Date ", eDate);
        Log.i("Time ", eTime);
        Log.i("Stamp ", eStamp);
    }

    //Method to Logout
    public void logout(View view) {
        //Checking if a user is currently signed in
        if(current_user != null) {
            //Signing out the user, displaying the message and going back to the login page
            mAuth.signOut();
            Toast.makeText(NewEvent.this, "User Signed Out successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(NewEvent.this, "No User is Signed in", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String eTitle = eventTitle.getText().toString();
        String eDescription = eventDescription.getText().toString();
        String eDate = eventDate.getText().toString();
        String eTime = eventTime.getText().toString();
        //outState.putString("savedTitle", eTitle);
        outState.putString("savedTime", eTime);
        outState.putString("savedDate", eDate);

    }
}