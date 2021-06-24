package com.rachad.wildprecision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class SignupActivity extends AppCompatActivity {
    ImageView signUp;
    static EditText email, password, username;
    TextView signIn;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signIn = findViewById(R.id.textViewSignin);
        signUp = findViewById(R.id.imageViewSignup);
        email = findViewById(R.id.editTextEmail);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPasword);
        signIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            SignupActivity.super.finish();
        });
        signUp.setOnClickListener(v -> {
            int i = 0;
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "email or password can't be empty", Toast.LENGTH_LONG).show();
                i = 0;
            } else {
                if (email.getText().toString().contains("@gmail.com")) {
                    i++;
                } else {
                    Toast.makeText(getApplicationContext(), "enter a valid email", Toast.LENGTH_LONG).show();
                }
                if (password.getText().toString().length() != 8) {
                    Toast.makeText(this, "The password must be 8 digit", Toast.LENGTH_SHORT).show();
                } else
                    i++;
            }
            if (email.getText().toString().contains(" ") || password.getText().toString().contains(" ") || username.getText().toString().contains(" ")) {
                Toast.makeText(getApplicationContext(), "space are not allowed in Email or Password", Toast.LENGTH_LONG).show();
                i = 0;
            } else {
                i++;
            }
            if (i == 3) {
                String email1, password1, username1;
                email1 = email.getText().toString();
                password1 = password.getText().toString();
                username1 = username.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("account/email_password/");
                alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
                alertDialog.setMessage("Loading please wait...");
                alertDialog.setCancelable(false);
                alertDialog.show();
                myRef.get().addOnSuccessListener((dataSnapshot) -> {
                    changer(email1, password1, username1, dataSnapshot.toString());
                });
            }
        });
    }

    public void changer(String email, String password1, String email1, String dataSnapshot) {
//check if password contain . because you cant store . inside path
        char[] ch1 = new char[email.length()];
        for (int ii = 0; ii < email.length(); ii++) {
            if (email.charAt(ii) != '.') {
                ch1[ii] = email.charAt(ii);
            } else {
                ch1[ii] = '_';
            }
        }
        email = "";
        for (int iii = 0; iii < ch1.length; iii++) {
            email += ch1[iii];
        }
        ///////////////////////////////////
        char[] ch2 = new char[password1.length()];
        for (int ii2 = 0; ii2 < password1.length(); ii2++) {
            if (password1.charAt(ii2) != '.') {
                ch2[ii2] = password1.charAt(ii2);
            } else {
                ch2[ii2] = '_';
            }
        }
        password1 = "";
        for (int iii2 = 0; iii2 < ch2.length; iii2++) {
            password1 += ch2[iii2];
        }
        ////////////////////////////////////////

        char[] ch3 = new char[email1.length()];
        for (int ii3 = 0; ii3 < email1.length(); ii3++) {
            if (email1.charAt(ii3) != '.') {
                ch3[ii3] = email1.charAt(ii3);
            } else {
                ch3[ii3] = '_';
            }
        }
        email1 = "";
        for (int iii3 = 0; iii3 < ch3.length; iii3++) {
            email1 += ch3[iii3];
        }
        if (dataSnapshot.contains(email1)) {
            Toast.makeText(this, "This email is already registered", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        } else {
            signup(email, password1, email1);
        }

    }

    public void signup(String email, String password, String username) {
        //put default value  to firebase when sign up
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Wildprecisiontrack", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("realEmail", SignupActivity.email.getText().toString());
        editor.apply();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/email_password/" + email + "_" + password + "/active");
        myRef.setValue("true");
        DatabaseReference myRef1 = database.getReference("account/email_password/" + email + "_" + password + "/username");
        myRef1.setValue(username);
        DatabaseReference myRef5 = database.getReference("account/email_password/" + email + "_" + password + "/IO1");
        myRef5.setValue(false);
        DatabaseReference myRef7 = database.getReference("account/email_password/" + email + "_" + password + "/IO2");
        myRef7.setValue(false);
        DatabaseReference myRef8 = database.getReference("account/email_password/" + email + "_" + password + "/IO3");
        myRef8.setValue(false);
        DatabaseReference myRef9 = database.getReference("account/email_password/" + email + "_" + password + "/IO4");
        myRef9.setValue(false);
        DatabaseReference myRef10 = database.getReference("account/email_password/" + email + "_" + password + "/SafeState");
        myRef10.setValue(true);
        DatabaseReference myRef11 = database.getReference("account/email_password/" + email + "_" + password + "/gpsLat");
        myRef11.setValue(20.0504188);
        DatabaseReference myRef12 = database.getReference("account/email_password/" + email + "_" + password + "/gpsLon");
        myRef12.setValue(64.4139099);
        DatabaseReference myRef13 = database.getReference("account/email_password/" + email + "_" + password + "/DeviceId");
        myRef13.setValue(String.valueOf(new Random().nextInt((99999999 - 10000000) + 1) + 10000000));

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "account created", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SignupActivity.super.finish();
    }
}