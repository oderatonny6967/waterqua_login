package com.example.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PublicKey;
import java.util.Objects;
public class LoginActivity extends AppCompatActivity {
    EditText loginusername, loginpassword;
    TextView tv1,tv2;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginusername = findViewById(R.id.edittextLOGINname);
        loginpassword = findViewById(R.id.edittextLOGINpass);
        tv1 = findViewById(R.id.textview_LOGIN);
        tv2=findViewById(R.id.textview_LOGINforgot);
        btn = findViewById(R.id.btnLOGIN);


        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotpasswordActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword()) {
                    checkUser();
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    public Boolean validateUsername() {
        String val = loginusername.getText().toString();
        if (val.isEmpty()) {
            loginusername.setError("Username cannot be empty");
            return false;
        } else {
            loginusername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginpassword.getText().toString();
        if (val.isEmpty()) {
            loginpassword.setError("Password cannot be empty");
            return false;
        } else {
            loginpassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String Userusername = loginusername.getText().toString();
        String Userpassword = loginpassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(Userusername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        if (passwordFromDB != null && passwordFromDB.equals(Userpassword)) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            return;
                        }
                    }
                    loginpassword.setError("Invalid credentials");
                    loginpassword.requestFocus();
                } else {
                    loginusername.setError("User does not exist");
                    loginusername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential database error
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
