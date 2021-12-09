package com.example.androidaccidentapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class driverInfo extends AppCompatActivity {

    EditText fName;
    EditText lName;
    EditText dob;
    EditText address;
    EditText licence;
    Button nxt;

    String firstName;
    String lastName;
    String dateOfBirth;
    String addressDriver;
    String licenceNum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        licence = findViewById(R.id.driveLicence);
        nxt = findViewById(R.id.next);



        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                dateOfBirth = dob.getText().toString();
                addressDriver = address.getText().toString();
                licenceNum = licence.getText().toString();

                if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(dateOfBirth) || isEmpty(addressDriver) || isEmpty(licenceNum)) {
                    Toast.makeText(driverInfo.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (licenceNum.length() < 9) {
                    Toast.makeText(driverInfo.this, "License Number should be at least 9 characters", Toast.LENGTH_SHORT).show();
                } else {
                    changeToNextActivity(view);
                }


            }
        });

    }


    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, insuranceExchange.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        this.startActivity(intent);
    }
}