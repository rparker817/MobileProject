package com.example.mobilefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    //Creating variables for EditText fields
    private EditText editFullName, editEmail, editAge, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //Setting onClick Event on the Register Button
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        //Assigning the variables created above to the text fields using their ID
        editFullName = (EditText) findViewById(R.id.fullName);
        editEmail = (EditText) findViewById(R.id.email);
        editAge = (EditText) findViewById(R.id.age);
        editPassword = (EditText) findViewById(R.id.password);
    }


    //Method to register a user and storing it in the Firebase Database
    public void registerUser() {
        //Converting all the text fields to string values
        String age = editAge.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String fullName = editFullName.getText().toString().trim();
        Users.Database database = new Users.Database();
        database.id = "1";
        Users.Database.Event event = new Users.Database.Event();
        event.title = "Cinema";
        event.description = "Going to the Cinema";
        event.date = "2022-04-19";
        event.time = "18:00:00";
        event.stamp = "2022-04-19 18:00:00";


        //Giving the user an error if the name field is empty
        if(fullName.isEmpty()) {
            editFullName.setError("Full Name is Required");
            editFullName.requestFocus();
            return;
        }

        //Giving the user an error if the email field is empty
        if(email.isEmpty()) {
            editEmail.setError("Email is Required");
            editEmail.requestFocus();
            return;
        }

        //Giving the user an error if the email is invalid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please provide a valid Email");
            editEmail.requestFocus();
            return;
        }

        //Giving the user an error if the age field is empty
        if(age.isEmpty()) {
            editAge.setError("Age is Required");
            editAge.requestFocus();
            return;
        }

        //Giving the user an error if the password field is empty
        if(password.isEmpty()) {
            editPassword.setError("Password is Required");
            editPassword.requestFocus();
            return;
        }

        //Giving the user an error if the password is less than 6 characters
        if(password.length() < 6) {
            editPassword.setError("Password should be at least 6 characters long");
            editPassword.requestFocus();
            return;
        }

        //Creating a user with email and password (Inbuilt method of firebase)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If the task (creating a user) is successful
                        if(task.isSuccessful()) {
                            //Making an object of type users and storing the values in it
                            Users users = new Users(fullName, email, age, password, database, event);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        register(); //Calling this method to change the intent
                                        Toast.makeText(Register.this, "User has been Registered Successfully!", Toast.LENGTH_LONG).show();
                                    } else { //Displaying a message if the registration fails
                                        Toast.makeText(Register.this, "Failed to Register", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else { //Displaying a message if the registration fails
                            Toast.makeText(Register.this, "Failed to Register the User", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Method to go to the MainActivity(Login) Class/Screen
    public void register() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}