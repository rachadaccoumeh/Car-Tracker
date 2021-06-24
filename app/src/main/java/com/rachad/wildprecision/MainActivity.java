package com.rachad.wildprecision;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView deviceId, safeState;
    SwitchCompat switchOne, switchTwo, switchThree, switchFour;
    TextView ioOne, ioTwo, ioThree, ioFour;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ImageView openMap;
    String m_Text;
    String email, password;
    boolean fromUser = false;
    String about, privacyPolicy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Wildprecisiontrack", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("a", false);
        editor.apply();

        email = pref.getString("email", "null");
        password = pref.getString("password", "null");
        myRef = database.getReference("account/email_password/" + email + "_" + password);
        deviceId = findViewById(R.id.deviceId);
        safeState = findViewById(R.id.safeState);
        switchOne = findViewById(R.id.switchOne);
        switchTwo = findViewById(R.id.switchTow);
        switchThree = findViewById(R.id.switchThree);
        switchFour = findViewById(R.id.switchFour);
        ioOne = findViewById(R.id.ioOne);
        ioTwo = findViewById(R.id.ioTwo);
        ioThree = findViewById(R.id.ioThree);
        ioFour = findViewById(R.id.ioFour);
        openMap = findViewById(R.id.openMap);

        loadData();
        switchCompat(switchOne, "IO1", ioOne);
        switchCompat(switchTwo, "IO2", ioTwo);
        switchCompat(switchThree, "IO3", ioThree);
        switchCompat(switchFour, "IO4", ioFour);
        editor.putBoolean("q", true);
        editor.apply();
        Intent intent1 = new Intent(MainActivity.this, MyService.class);
        intent1.putExtra("stop", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent1);
        } else {
            startService(intent1);
        }
        sendBroadcast(new Intent(MainActivity.this, MyReceiverAlarmManger.class));

    }


    public void switchCompat(SwitchCompat switchCompat, String string, TextView textView) {
        switchCompat.setOnTouchListener((v, event) -> {
            if (event.ACTION_DOWN == MotionEvent.ACTION_DOWN)
                fromUser = true;
            else
                fromUser = false;
            return false;
        });
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // if (isChecked) {
            if (fromUser) {
                fromUser = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter your 8-digit code");
                builder.setCancelable(false);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    m_Text = input.getText().toString();
                    if (m_Text.equals(password)) {
                        myRef.child(string).setValue(isChecked);
                        textView.setText(string + ": " + isChecked);
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(!isChecked);
                        myRef.child(string).setValue(!isChecked);
                        textView.setText(string + ": " + !isChecked);
                    }
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    switchCompat.setChecked(!isChecked);
                    myRef.child(string).setValue(!isChecked);
                    textView.setText(string + ": " + !isChecked);
                    dialog.cancel();
                });
                builder.show();
            }
            // } else {
               /* myRef.child(string).setValue(isChecked);
                textView.setText(string + ": " + isChecked);*/
            //}
        });
        openMap.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        });
    }


    void loadData() {
        myRef.child("IO1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(boolean.class);
                switchOne.setChecked(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("IO2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(boolean.class);
                switchTwo.setChecked(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("IO3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(boolean.class);
                switchThree.setChecked(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("IO4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.getValue(boolean.class);
                switchFour.setChecked(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("DeviceId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                deviceId.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        myRef.child("SafeState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.getValue(Boolean.class));
                safeState.setText(value);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference myRef1 = database.getReference();
        myRef1.child("privacyPolicy").get().addOnSuccessListener(dataSnapshot -> {
            privacyPolicy = Objects.requireNonNull(dataSnapshot.getValue()).toString();
        });
        myRef1.child("about").get().addOnSuccessListener(dataSnapshot -> {
            about = Objects.requireNonNull(dataSnapshot.getValue()).toString();
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        MainActivity.super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                SharedPreferences pref = getApplicationContext().getSharedPreferences("Wildprecisiontrack", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email", "null");
                editor.putString("password", "null");
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.super.finish();
                return true;
            case R.id.item2:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicy)));
                return true;
            case R.id.item3:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(about)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}