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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class vehicleRegistration extends AppCompatActivity {


    EditText make;
    EditText year;
    EditText plate;
    EditText state;
    EditText type;

    String vehicleMake, vehicleYear, vehiclePlate, vehicleState, vehicleType;
    String provider, policyNum, policyHolder;
    String firstName, lastName, dateOfBirth, addressDriver, licenceNum, iceNum;

    ArrayAdapter<String> adapter;
    DrawerLayout drawerLayout;
    ImageView menuButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        make = findViewById(R.id.vehicleMake);
        year = findViewById(R.id.VehicleYear);
        plate = findViewById(R.id.plateNo);
        state = findViewById(R.id.state);
        type = findViewById(R.id.vehicleType);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = (ImageView) findViewById(R.id.menuButton);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");
        iceNum = getIntent().getStringExtra("ICE Contact");

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

                if (isEmpty(vehicleMake) || TextUtils.isEmpty(vehicleYear) || (TextUtils.isEmpty(vehiclePlate)
                        || (TextUtils.isEmpty(vehicleState) || TextUtils.isEmpty(vehicleType)))) {
                    Toast.makeText(vehicleRegistration.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (vehicleYear.length() != 4){
                    Toast.makeText(vehicleRegistration.this, "Vehicle year must be 4 digits (YYYY)", Toast.LENGTH_SHORT).show();
                }
                else if (vehiclePlate.length() < 2 || vehiclePlate.length() > 8 ) {
                    Toast.makeText(vehicleRegistration.this, "Vehicle plate should be between 2 and 8 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    changeToNextActivity(view);

                }
            }
        });
    }

    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, finalCheck.class);

        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        intent.putExtra("ICE Contact", iceNum);

        intent.putExtra("Provider", provider);
        intent.putExtra("Policy Number", policyNum);
        intent.putExtra("Holder", policyHolder);

        intent.putExtra("VehicleMake", vehicleMake);
        intent.putExtra("VehicleYear", vehicleYear);
        intent.putExtra("VehiclePlate", vehiclePlate);
        intent.putExtra("VehicleState", vehicleState);
        intent.putExtra("VehicleType", vehicleType);
        Log.d("iceNum", ""+ iceNum);
        this.startActivity(intent);
    }

    public void openProfileDialog(View view){
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(vehicleRegistration.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(vehicleRegistration.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(vehicleRegistration.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(vehicleRegistration.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (vehicleRegistration.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            finish();
        });

        profileDialog.create().show();
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
        redirectActivity(this, Home.class);
    }

    public void clickRegister(View view){
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
        //closeDrawer(drawerLayout);
    }
}