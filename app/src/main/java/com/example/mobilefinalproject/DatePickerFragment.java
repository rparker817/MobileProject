package com.example.mobilefinalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String page = "view_planner";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        if(page.equals("view_planner"))
        {
            ViewPlanner activity = (ViewPlanner) getActivity();
            activity.processDatePickerResult(year, month+1, day);
        }
        else
        {
            NewEvent activity1 = (NewEvent) getActivity();
            activity1.processDatePickerResult(year, month+1, day);
        }


    }
}
