package com.example.task61foodrescueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.task61foodrescueapp.data.DatabaseHelper;
import com.example.task61foodrescueapp.model.User;

public class SignUpActivity extends AppCompatActivity {
    EditText username, email, phoneNumber, address, password, confirmPassword;
    Button saveButton;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.fullNameTextView);
        email = findViewById(R.id.emailTextView);
        phoneNumber = findViewById(R.id.phoneNumberTextView);
        address = findViewById(R.id.addressTextView);
        password = findViewById(R.id.passwordTextVIew);
        confirmPassword = findViewById(R.id.confirmPasswordTextView);
        saveButton = findViewById(R.id.saveButton);
        db = new DatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString() != null && email.getText().toString() != null && phoneNumber.getText().toString() != null && address.getText().toString() != null && password.getText().toString() != null && confirmPassword.getText().toString() != null ){
                    if(password.getText().toString().equals(confirmPassword.getText().toString())){
                        long result = db.insertUser(new User(username.getText().toString(), email.getText().toString(), phoneNumber.getText().toString(), address.getText().toString(), password.getText().toString()));
                        if (result == -1)
                        {
                            Toast.makeText(SignUpActivity.this, "Error Adding the user", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "User Successfully Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Passwords do not match " + confirmPassword.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Fields cannot be left Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}