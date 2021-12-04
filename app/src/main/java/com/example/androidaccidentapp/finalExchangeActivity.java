package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class finalExchangeActivity extends AppCompatActivity {

    String vehicleMake, vehicleYear, vehiclePlate, vehicleState, vehicleType;
    String provider, policyNum, policyHolder;
    String firstName, lastName, dateOfBirth, addressDriver, licenceNum;

    TextView fName, lName, DOB, Address, license, insProvider, insPolicyNum, insPolicyHolder, Make,
            Year, Plate, State, Type;

    Button submit;
    EditText ReportName;

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_exchange);

        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");

        provider = getIntent().getStringExtra("Provider");
        policyNum = getIntent().getStringExtra("Policy Number");
        policyHolder = getIntent().getStringExtra("Holder");

        vehicleMake = getIntent().getStringExtra("VehicleMake");
        vehicleYear = getIntent().getStringExtra("VehicleYear");
        vehiclePlate = getIntent().getStringExtra("VehiclePlate");
        vehicleState = getIntent().getStringExtra("VehicleState");
        vehicleType = getIntent().getStringExtra("VehicleType");

        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        DOB = findViewById(R.id.DOB);
        Address = findViewById(R.id.Address);
        license = findViewById(R.id.license);
        insProvider = findViewById(R.id.insProvider);
        insPolicyNum = findViewById(R.id.insPolicyNum);
        insPolicyHolder = findViewById(R.id.insPolicyHolder);
        Make = findViewById(R.id.Make);
        Year = findViewById(R.id.Year);
        Plate = findViewById(R.id.Plate);
        State = findViewById(R.id.vehicleState);
        Type = findViewById(R.id.Type);

        fName.setText(firstName);
        lName.setText(lastName);
        DOB.setText(dateOfBirth);
        Address.setText(addressDriver);
        license.setText(licenceNum);
        insProvider.setText(provider);
        insPolicyNum.setText(policyNum);
        insPolicyHolder.setText(policyHolder);
        Make.setText(vehicleMake);
        Year.setText(vehicleYear);
        Plate.setText(vehiclePlate);
        State.setText(vehicleState);
        Type.setText(vehicleType);

        ReportName = findViewById(R.id.ReportName);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        submit = findViewById(R.id.submitInfoToFirebase);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(view);
            }
        });
    }
    public void saveData(View view) {

        String reportName = ReportName.getText().toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("Vehicle Type", vehicleType);
        map.put("Vehicle State", vehicleState);
        map.put("Vehicle Plate", vehiclePlate);
        map.put("Vehicle Year", vehicleYear);
        map.put("Vehicle Make", vehicleMake);
        map.put("Insurance Holder", policyHolder);
        map.put("Policy Number", policyNum);
        map.put("Insurance Provider", provider);
        map.put("License Number", licenceNum);
        map.put("Address", addressDriver);
        map.put("DOB", dateOfBirth);
        map.put("Last Name", lastName);
        map.put("First Name", firstName);

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
        Toast.makeText(finalExchangeActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

}