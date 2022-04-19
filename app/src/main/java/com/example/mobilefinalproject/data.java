package com.example.mobilefinalproject;

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
