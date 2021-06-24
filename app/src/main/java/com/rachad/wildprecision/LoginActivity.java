package com.rachad.wildprecision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    ImageView signIn;
    EditText email, password;
    TextView signUp;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Wildprecisiontrack", 0);
        signIn = findViewById(R.id.imageViewSignin);
        signUp = findViewById(R.id.textViewSignup);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPasword);
        user = pref.getString("email", "null");
        pass = pref.getString("password", "null");
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            LoginActivity.super.finish();
        });
        if (!user.equals("null") && !pass.equals("null")) {
            //signin(user, pass);
            if (!pref.getString("realEmail", "null").equals("null"))
                email.setText(pref.getString("realEmail", "null"));
        } else if (!user.equals("null")) {
            if (!pref.getString("realEmail", "null").equals("null"))
                email.setText(pref.getString("realEmail", "null"));
            Toast.makeText(this, "Safe State is false please login to stop alarm", Toast.LENGTH_SHORT).show();
        }
        signIn.setOnClickListener(v -> {
            int i = 0;
            if (password.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "username or password can't be empty", Toast.LENGTH_LONG).show();
                i = 0;
            } else {
                if (email.getText().toString().contains("@gmail.com")) {
                    i++;
                } else {
                    Toast.makeText(getApplicationContext(), "enter a valid email", Toast.LENGTH_LONG).show();
                }
                if (password.getText().toString().length() != 8) {
                    Toast.makeText(this, "The password must be 8 digit", Toast.LENGTH_SHORT).show();
                } else {
                    i++;
                }
            }
            if (password.getText().toString().contains(" ") || email.getText().toString().contains(" ")) {
                Toast.makeText(getApplicationContext(), "space are not allowed in Email or Password", Toast.LENGTH_LONG).show();
                i = 0;
            } else {
                i++;
            }
            if (i == 3) {
                String email1, password1;
                email1 = email.getText().toString();
                password1 = password.getText().toString();
                changer(email1, password1);
            }
        });

    }

    public void changer(String email, String password1) {

        char[] ch1 = new char[email.length()];
        for (int ii = 0; ii < email.length(); ii++) {
            if (email.charAt(ii) != '.') {
                ch1[ii] = email.charAt(ii);
            } else {
                ch1[ii] = '_';
            }
        }
        email = "";
        for (char value : ch1) {
            email += value;
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
        for (char c : ch2) {
            password1 += c;
        }
        signin(email, password1);
    }

    public void signin(String email1, String password) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setMessage("Loading please wait...");
        alertDialog.setCancelable(false);
        alertDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("account/email_password/" + email1 + "_" + password + "/active");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null) {
                    if (value.equals("true")) {
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("Wildprecisiontrack", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("email", email1);
                        editor.putString("password", password);
                        editor.putString("realEmail",email.getText().toString());
                        editor.apply();
                        intent();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "username or password is incorrect", Toast.LENGTH_LONG).show();
                    alertDialog.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void intent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        LoginActivity.super.finish();
    }

}