package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class driveRegistration extends AppCompatActivity {

    EditText fName;
    EditText lName;
    EditText dob;
    EditText address;
    EditText licence;
    EditText iceContact;
    Button nxt;


    String firstName;
    String lastName;
    String dateOfBirth;
    String addressDriver;
    String licenceNum;
    String iceNum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_registration);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        licence = findViewById(R.id.driveLicence);
        iceContact = findViewById(R.id.icePhone);
        nxt = findViewById(R.id.saveInsuranceInfo);


        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                dateOfBirth = dob.getText().toString();
                addressDriver = address.getText().toString();
                licenceNum = licence.getText().toString();
                iceNum = iceContact.getText().toString();

                changeToNextActivity(view);
            }
        });

    }



    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, insuranceRegistration.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        intent.putExtra("ICE Contact", iceNum);
        Log.d("iceNum", ""+ iceNum);
        this.startActivity(intent);
    }


}