package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class Step1 extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step1);
        drawerLayout = findViewById(R.id.drawer_layout);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports", "Access Camera"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

        final Button call = findViewById(R.id.dial911_btn); //button for 911 which is call
        final Button emergency = findViewById(R.id.emergencyContact_btn); //button for contact which is emergency

        call.setOnClickListener(new View.OnClickListener() { //call button is 911, dials it for you
            @Override
            public void onClick(View view) {

                PhoneCallListener phoneListener = new PhoneCallListener(); //PhoneCallListener looks if a call is ongoing
                TelephonyManager telephonyManager = (TelephonyManager) //tells you about telephony services and phone state
                        getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener,
                        PhoneStateListener.LISTEN_CALL_STATE);
                Uri callService = Uri.parse("tel://911"); //dials 911
                Intent callIntent = new Intent(Intent.ACTION_DIAL,callService); //notably has to be a dial, cannot do call with emergency service like 911 since call privileged is exclusive to system apps
                startActivity(callIntent);
            }
        });
   /* public void previous(View view) {
        Intent intentPrev = new Intent(Step1.this, Home.class);
        startActivity(intentPrev);
    }

    public void next(View view) {
        Intent intentNxt = new Intent(Step1.this, UserProfile.class);
        startActivity(intentNxt);
    } */
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M //api level > 23
                        && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                    //if permission hasn't been granted, ask for permission to access calling phone
                }
                else {
                    // String phoneNumber = phone.getText().toString();
                    // above code could be format for using with database

                    PhoneCallListener phoneListener = new PhoneCallListener(); //PhoneCallListener looks if a call is ongoing
                    TelephonyManager telephonyManager = (TelephonyManager) //tells you about telephony services and phone state
                            getSystemService(Context.TELEPHONY_SERVICE);
                    telephonyManager.listen(phoneListener,
                            PhoneStateListener.LISTEN_CALL_STATE);
                    String phoneNumber = "5551235428"; //using a random number for now
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+ phoneNumber)); //calls the number in the string
                    startActivity(intent);


                }
            }
        });
    }

    public void next(View view) {
        startActivity(new Intent(this.getApplicationContext(), Step2.class));

    }

    public class PhoneCallListener extends PhoneStateListener { //this looks for what state the phone is in

        private boolean phoneCall = false; //start as false, depending on state, does certain actions


        @Override
        public void onCallStateChanged(int status, String num) {

            if (TelephonyManager.CALL_STATE_OFFHOOK == status) {
                // the status is active

                phoneCall = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == status) {
                // if offhook over, will switch to this afterwards

                if (phoneCall) {
                    // this restarts the application back
                    Intent i = new Intent(Step1.this, Step1.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    phoneCall = false;
                }
            }
        }
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
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(Step1.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity
        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(Step1.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Step1.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(Step1.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Toast.makeText(Step1.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, reports.class);
//                            startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(Step1.this, Camera.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        //Sign out button
        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Toast.makeText(Step1.this, "Clicked Sign Out", Toast.LENGTH_LONG).show();
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
        redirectActivity(this, Registering.class);
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

