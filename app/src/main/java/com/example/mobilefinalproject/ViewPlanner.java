package com.example.mobilefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPlanner extends AppCompatActivity {
    Button button;
    Context context;
    CardView cardview;
    LinearLayout.LayoutParams layoutparams;
    TextView textview;
    LinearLayout linearLayout;
    ScrollView scrollView;
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
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();
        createEventEntry();

    }

    public void createEventEntry(){


        CardView card = new CardView(context);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0,0,0,20);
        card.setLayoutParams(params);


        card.setRadius(50);

        card.setContentPadding(15, 15, 15, 15);


        card.setCardBackgroundColor(Color.parseColor("#F08F00"));


        card.setMaxCardElevation(15);

        card.setCardElevation(9);

        TextView tv = new TextView(context);
        tv.setLayoutParams(params);
        tv.setText("Time   Date \nkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk\nkkkkkkkkkkkkkkkkkkkkkkkkkkkkksdlfksdlfsdfsdlfkhjs\ndlkfhjlksdhjflksdlkfjlksdjflsdjlfjsldkfjl ");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        tv.setTextColor(Color.BLACK);

        card.addView(tv);

        linearLayout.addView(card);


    }

    public void showDate(View view)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"DatePicker");
    }
    public void processDatePickerResult(int year, int month,int day)
    {
        String msg = Integer.toString(month)+"/"+Integer.toString(day)+"/"+Integer.toString(year);
        Toast.makeText(this,"Date: " +msg,Toast.LENGTH_SHORT).show();
        Button btn = findViewById(R.id.dateSelect);
        btn.setText(msg.toString());

    }


    public void openHome(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}