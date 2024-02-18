package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegistrationActivity extends AppCompatActivity {
    EditText usernamereg, emailreg, passwordreg, confirmpass;
    TextView tv;
    Button btn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        usernamereg = findViewById(R.id.edittextregnname);
        emailreg = findViewById(R.id.edittextregnemail);
        passwordreg = findViewById(R.id.edittextregnpass);
        confirmpass = findViewById(R.id.edittextregnpass2);
        btn = findViewById(R.id.btnregsign_up);
        tv = findViewById(R.id.textview_signup);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateRegistration()) {
                    registerUser();
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    public boolean validateRegistration() {
        String username = usernamereg.getText().toString();
        String email = emailreg.getText().toString();
        String password = passwordreg.getText().toString();
        String confirmPassword = confirmpass.getText().toString();

        if (TextUtils.isEmpty(username)) {
            usernamereg.setError("Please enter username");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailreg.setError("Please enter email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordreg.setError("Please enter password");
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmpass.setError("Please enter confirm password");
            return false;
        }
        if (!password.matches(".*[@#&$%].*")) {
            Toast.makeText(RegistrationActivity.this, "Password must contain special characters like @, #, &, $, %", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(RegistrationActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmpass.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    public void registerUser() {
        String username = usernamereg.getText().toString();
        String email = emailreg.getText().toString();
        String password = passwordreg.getText().toString();
        String confirmpassword = confirmpass.getText().toString();

       // username = username.replaceAll("[^a-zA-Z0-9]", "");


        // Check if the username already exists in the database
        DatabaseReference usernameRef = FirebaseDatabase.getInstance().getReference("Users").child(username);
        usernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username already exists
                    Toast.makeText(RegistrationActivity.this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is available, proceed with registration
                    HelperClasss helperClasss = new HelperClasss(username, email, password,confirmpassword);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(username).setValue(helperClasss);
                    Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential database error
                Toast.makeText(RegistrationActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
