package com.example.mobilefinalproject;

import java.util.ArrayList;

public class Users {

    public String fullName, email, age, password;

    public static class Database {
        public String title, description, date, time, stamp;

        public Database() {

        }

        public Database(String title, String description, String date, String time, String stamp) {
            this.title = title;
            this.description = description;
            this.date = date;
            this.time = time;
            this.stamp = stamp;
        }
    }

    //Empty Constructor method
    public Users() {

    }

    public Users(String fullName, String email, String age, String password) {
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.password = password;
    }
}
