package com.example.mobilefinalproject;

//Test comment
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Creating variables for EditText fields
    private EditText textEmail, textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Setting onClick Event on the Login Button
        Button btnLogin = (Button) findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //Setting onClick Event on the Register Button
        Button btnRegister = (Button) findViewById(R.id.registerButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        //Assigning the variables created above to the text fields using their ID
        textEmail = (EditText) findViewById(R.id.emailLogin);
        textPassword = (EditText) findViewById(R.id.passwordLogin);

    }

    //Method to Login
    public void login() {
        //Converting all the text fields to string values
        String password = textPassword.getText().toString().trim();
        String email = textEmail.getText().toString().trim();

        //Giving the user an error if the email field is empty
        if(email.isEmpty()) {
            textEmail.setError("Email is Required");
            textEmail.requestFocus();
            return;
        }

        //Giving the user an error if the password field is empty
        if(password.isEmpty()) {
            textPassword.setError("Password is Required");
            textPassword.requestFocus();
            return;
        }

        //Signing in a user with email and password (Inbuilt method of firebase)
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If the login is successful
                        if(task.isSuccessful()) {
                            loginUser(); // Calling this method to change the intent
                            Toast.makeText(MainActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                        } else { //Displaying a message if the login fails
                            Toast.makeText(MainActivity.this, "Login Failed! Please enter valid credentials.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Method to go to the HomePage Class/Screen
    public void loginUser() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    //Method to go to the Register Class/Screen
    public void register() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}