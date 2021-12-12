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

public class Step3 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);

        drawerLayout = findViewById(R.id.drawer_layout);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports", "Access Camera"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
    }

    public void next(View view) {
        Intent intent = new Intent(this, driverInfo.class);
        this.startActivity(intent);
    }

    public void previous(View view) {
        Intent intent = new Intent(this, Step22.class);
        this.startActivity(intent);
    }

    public void infoexchangegoto(View view) {
        Intent intent = new Intent(this, driverInfo.class);
        this.startActivity(intent);
    }

    public void clickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void openProfileDialog(View view) {
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(Step3.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity
        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(Step3.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Step3.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(Step3.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Toast.makeText(Step3.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, reports.class);
//                            startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(Step3.this, Camera.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        //Sign out button
        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Toast.makeText(Step3.this, "Clicked Sign Out", Toast.LENGTH_LONG).show();
        });


        profileDialog.create().show();
    }

    public void clickGuide(View view){
        recreate();
    }

    public void clickHome(View view){
        redirectActivity(this, Home.class);
    }

    public void clickMaps(View view){
        redirectActivity(this, MapsActivity.class);
    }

    public void clickHandBook(View view){
        redirectActivity(this, Handbook.class);
    }

    public void clickLogin(View view){
        redirectActivity(this, Login.class);
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