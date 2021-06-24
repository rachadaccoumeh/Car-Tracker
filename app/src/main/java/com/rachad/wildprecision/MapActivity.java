package com.rachad.wildprecision;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    LatLng latLng;
    Marker marker;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        latLng = new LatLng(20.0504188, 64.4139099);
        marker = map.addMarker(new MarkerOptions().position(latLng));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences pref = getSharedPreferences("Wildprecisiontrack", 0);
        String email, password;
        email = pref.getString("email", "null");
        password = pref.getString("password", "null");
        DatabaseReference myRef = database.getReference("account/email_password/" + email + "_" + password);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat = (double) dataSnapshot.child("gpsLat").getValue();
                lon = (double) dataSnapshot.child("gpsLon").getValue();
                marker.remove();
                latLng = new LatLng(lat, lon);
                marker = map.addMarker(new MarkerOptions().position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MapActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

}