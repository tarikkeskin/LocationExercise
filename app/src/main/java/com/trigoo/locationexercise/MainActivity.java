package com.trigoo.locationexercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.trigoo.locationexercise.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding t;
    private int izinKontrol = 0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Task<Location> locationTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(t.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        t.buttonKonumAl.setOnClickListener(view -> {
            izinKontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(izinKontrol != PackageManager.PERMISSION_GRANTED){//izin onaylanmamışsa
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
            }else{//izin onaylanmış ise
                locationTask = fusedLocationProviderClient.getLastLocation();
                konumBilgisiAl();
            }

        });
    }

    public void konumBilgisiAl(){
        locationTask.addOnSuccessListener(location -> {
            if(location != null){
                t.textViewEnlem.setText("Enlem : "+location.getLatitude());
                t.textViewBoylam.setText("Boylam : "+location.getLongitude());
            }else{
                t.textViewBoylam.setText("Boylam bilgisi alınamadı...");
                t.textViewEnlem.setText("Enlem bilgisi alınamadı...");
            }
        });
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100){
            izinKontrol = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "izin kabul edildi", Toast.LENGTH_SHORT).show();
                locationTask = fusedLocationProviderClient.getLastLocation();
                konumBilgisiAl();
            }else{
                Toast.makeText(getApplicationContext(), "izin reddedildi", Toast.LENGTH_SHORT).show();
            }

        }
    }
}