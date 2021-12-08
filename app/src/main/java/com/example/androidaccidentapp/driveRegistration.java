package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class driveRegistration extends AppCompatActivity {

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
        setContentView(R.layout.activity_drive_registration);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        licence = findViewById(R.id.driveLicence);
        nxt = findViewById(R.id.saveInsuranceInfo);

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                dateOfBirth = dob.getText().toString();
                addressDriver = address.getText().toString();
                licenceNum = licence.getText().toString();

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
        this.startActivity(intent);
    }
}