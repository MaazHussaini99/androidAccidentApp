package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class insuranceExchange extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    int reportsCounter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_exchange);

        prov = findViewById(R.id.provider);
        policyNo = findViewById(R.id.policyNo);
        holder = findViewById(R.id.policyHolder);
        btn1 = findViewById(R.id.saveInsuranceInfo);

        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");


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

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infoexchange);


        prov = findViewById(R.id.provider);
        policyNo = findViewById(R.id.policyNo);
        holder = findViewById(R.id.policyHolder);
        btn1 = findViewById(R.id.saveInsuranceInfo);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, Registering.class));
            finish();
            return;
        }
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider = prov.getText().toString();
                policyNum = policyNo.getText().toString();
                policyHolder = holder.getText().toString();

                saveData(view);
                reportsCounter++;
            }
        });
    }

    public void saveData(View view) {

        reportsCounter = 2;
        String reportName = "Report " + reportsCounter;
        HashMap<String, Object> map = new HashMap<>();
        map.put("Provider", provider);
        map.put("Policy Number", policyNum);
        map.put("Policy Holder", policyHolder);


        dbRef.child(currentUser.getUid()).child("Accident Reports").child(reportName).updateChildren(map, completionListener);

    }

    DatabaseReference.CompletionListener completionListener =
            new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError,
                                       DatabaseReference databaseReference) {

                    if (databaseError != null) {
                        notifyUser(databaseError.getMessage());
                    }
                }
            };
    private void notifyUser(String message) {
        Toast.makeText(insuranceExchange.this, message,
                Toast.LENGTH_SHORT).show();
    }*/


    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, VehicleDetails.class);

        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);

        intent.putExtra("Provider", provider);
        intent.putExtra("Policy Number", policyNum);
        intent.putExtra("Holder", policyHolder);

        this.startActivity(intent);
    }
}