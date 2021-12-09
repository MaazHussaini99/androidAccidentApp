package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageView userAcct;
    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);


        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports", "Access Camera"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);




    }
    public void openProfileDialog(View view) {
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(Home.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity
        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(Home.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Home.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(Home.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Toast.makeText(Home.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, reports.class);
//                            startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(Home.this, Camera.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        //Sign out button
        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Toast.makeText(Home.this, "Clicked Sign Out", Toast.LENGTH_LONG).show();
        });


        profileDialog.create().show();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.step_by_step:{
                Toast.makeText(Home.this, "Step-By-Step", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(Home.this, Step1.class);
                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.handbook:{
                Toast.makeText(Home.this, "Handbook", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, Handbook.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.info_exchange:{
                Toast.makeText(Home.this, "Info Exchange", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, InfoExchange.class);
//                Home.this.startActivity(myIntent);
                break;
            }
            case R.id.reports:{
                Toast.makeText(Home.this, "Reports", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(Home.this, Reports.class);
//                Home.this.startActivity(myIntent);
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