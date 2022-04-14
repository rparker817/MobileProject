package com.example.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

}