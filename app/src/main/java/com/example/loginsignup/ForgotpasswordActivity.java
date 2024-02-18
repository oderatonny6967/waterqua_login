package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotpasswordActivity extends AppCompatActivity {
    EditText usernameEditText, newPasswordEditText;
    Button resetPasswordButton;

    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        usernameEditText = findViewById(R.id.usernameEditText); // Changed to usernameEditText
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim(); // Changed to username
                String newPassword = newPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Please enter username");
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    newPasswordEditText.setError("Please enter new password");
                    return;
                }

                // Check if the user exists in the database
                usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User found, update password
                            usersRef.child(username).child("password").setValue(newPassword);
                            Toast.makeText(ForgotpasswordActivity.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // User not found
                            Toast.makeText(ForgotpasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(ForgotpasswordActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(ForgotpasswordActivity.this, LoginActivity.class));
            }
        });
    }
}
