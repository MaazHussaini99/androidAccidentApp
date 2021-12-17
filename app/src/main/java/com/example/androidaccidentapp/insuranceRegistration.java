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

public class insuranceRegistration extends AppCompatActivity {



    EditText prov;
    EditText policyNo;
    EditText holder;
    Button btn1;
    String provider;
    String policyNum;
    String policyHolder;

    String firstName;
    String lastName;
    String dateOfBirth;
    String addressDriver;
    String licenceNum;
    String iceNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_registration);

        prov = findViewById(R.id.provider);
        policyNo = findViewById(R.id.policyNo);
        holder = findViewById(R.id.policyHolder);
        btn1 = findViewById(R.id.saveInsuranceInfo);



        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");
        iceNum = getIntent().getStringExtra("ICE Contact");


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                provider = prov.getText().toString();
                policyNum = policyNo.getText().toString();
                policyHolder = holder.getText().toString();

                changeToNextActivity(view);
            }
        });
    }




    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, vehicleRegistration.class);

        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        intent.putExtra("ICE Contact", iceNum);

        intent.putExtra("Provider", provider);
        intent.putExtra("Policy Number", policyNum);
        intent.putExtra("Holder", policyHolder);

        this.startActivity(intent);
    }

}