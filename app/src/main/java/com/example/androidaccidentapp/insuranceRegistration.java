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
                if (isEmpty(provider) || TextUtils.isEmpty(policyNum) || (TextUtils.isEmpty(policyHolder))) {
                    Toast.makeText(insuranceRegistration.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (firstName.length() < 3 || firstName.length() > 30 ) {
                    Toast.makeText(insuranceRegistration.this, "Policy holder name should be between 3 and 30 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    changeToNextActivity(view);

                }

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
        Log.d("iceNum", ""+ iceNum);

        this.startActivity(intent);
    }

}