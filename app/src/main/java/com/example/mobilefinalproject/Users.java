package com.example.mobilefinalproject;

import android.provider.ContactsContract;

import java.util.ArrayList;

public class Users {

    public String fullName, email, age, password;


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
