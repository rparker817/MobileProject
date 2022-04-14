package com.example.mobilefinalproject;

//Test comment
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting onClick Event on the Login Button
        Button btnLogin = (Button) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    //Method to go to the HomePage Class/Screen
    public void login() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

}