package com.example.mobilefinalproject;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class Users {

    public String fullName, email, age, password;
    public Database database;
    public Database.Event event;

    public static class Database {

        public static class Event{

            public String title, description, date, time, stamp;


            public Event() {

            }

            public Event(String title, String description, String date, String time, String stamp) {
                this.title = title;
                this.description = description;
                this.date = date;
                this.time = time;
                this.stamp = stamp;
            }
        }

        public String id;

        public Database() {

        }

        public Database(String id) {
            this.id = id;
        }
    }

    //Empty Constructor method
    public Users() {

    }

    public Users(String fullName, String email, String age, String password, Database database, Database.Event event) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.password = password;
        this.database = database;
        this.event = event;

    }
}
