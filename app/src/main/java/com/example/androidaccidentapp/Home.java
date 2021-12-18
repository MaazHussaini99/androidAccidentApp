package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Home extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
        drawerLayout = findViewById(R.id.drawer_layout);

        adView = findViewById(R.id.adViewHome);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    public void openProfileDialog(View view){
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(Home.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(Home.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(Home.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(Home.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (Home.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            finish();
        });

        profileDialog.create().show();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.step_by_step:{
                Intent myIntent = new Intent(Home.this, Step1.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.handbook:{
                Intent myIntent = new Intent(Home.this, Handbook.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.info_exchange:{
                Intent myIntent = new Intent(Home.this, driverInfo.class);
                Home.this.startActivity(myIntent);
                break;
            }
        }
    }

    public void clickMenu(View view){
        openDrawer(drawerLayout);
    }
    public void clickLogin(View view){
        redirectActivity(this, Login.class);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void clickHome(View view){
        recreate();
    }

    public void clickHandBook(View view){
        redirectActivity(this, Registering.class);
    }

    public void clickMaps(View view){
        redirectActivity(this, MapsActivity.class);
    }

    public void clickGuide(View view){
        redirectActivity(this, Step1.class);
    }

    public void clickProfile(View view){
        redirectActivity(this, ProfileUser.class);
    }

    static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}