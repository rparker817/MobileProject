package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewEvent extends AppCompatActivity {
    String datePicked = "";
    int hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        //add an up button to the action bar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

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
                                btnTime.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false
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
}