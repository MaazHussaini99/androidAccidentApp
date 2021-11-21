package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Step1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step1);

        final Button call = findViewById(R.id.dial911_btn); //button for 911 which is call
        final Button emergency = findViewById(R.id.emergencyContact_btn); //button for contact which is emergency

        call.setOnClickListener(new View.OnClickListener() { //call button is 911, dials it for you
            @Override
            public void onClick(View view) {
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

  private class PhoneCallListener extends PhoneStateListener { //this looks for what state the phone is in

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
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                            getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    phoneCall = false;
                }
            }
        }
    }
}

