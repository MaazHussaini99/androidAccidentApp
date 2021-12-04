package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VehicleDetails extends AppCompatActivity {


    EditText make;
    EditText year;
    EditText plate;
    EditText state;
    EditText type;

    String vehicleMake, vehicleYear, vehiclePlate, vehicleState, vehicleType;
    String provider, policyNum, policyHolder;
    String firstName, lastName, dateOfBirth, addressDriver, licenceNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        make = findViewById(R.id.vehicleMake);
        year = findViewById(R.id.VehicleYear);
        plate = findViewById(R.id.plateNo);
        state = findViewById(R.id.state);
        type = findViewById(R.id.vehicleType);

        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");

        provider = getIntent().getStringExtra("Provider");
        policyNum = getIntent().getStringExtra("Policy Number");
        policyHolder = getIntent().getStringExtra("Holder");

        Button saveVehicle = findViewById(R.id.toMaps);

        saveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vehicleMake = make.getText().toString();
                vehicleYear = year.getText().toString();
                vehiclePlate = plate.getText().toString();
                vehicleState = state.getText().toString();
                vehicleType = type.getText().toString();

                changeToNextActivity(view);
            }
        });
    }

    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, finalExchangeActivity.class);

        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);

        intent.putExtra("Provider", provider);
        intent.putExtra("Policy Number", policyNum);
        intent.putExtra("Holder", policyHolder);

        intent.putExtra("VehicleMake", vehicleMake);
        intent.putExtra("VehicleYear", vehicleYear);
        intent.putExtra("VehiclePlate", vehiclePlate);
        intent.putExtra("VehicleState", vehicleState);
        intent.putExtra("VehicleType", vehicleType);

        this.startActivity(intent);
    }
}