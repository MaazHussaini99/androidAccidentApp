package com.example.androidaccidentapp;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VehicleDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText make;
    EditText year;
    EditText plate;
    EditText state;
    EditText type;

    String vehicleMake, vehicleYear, vehiclePlate, vehicleState, vehicleType;
    String provider, policyNum, policyHolder;
    String firstName, lastName, dateOfBirth, addressDriver, licenceNum;
    String usersVehicle;


    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    int mapSize;

    private Spinner spinner;
    private static final String[] paths = {"", "", "", "", ""};

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

        checkData();
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleDetails.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        saveVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vehicleMake = make.getText().toString();
                vehicleYear = year.getText().toString();
                vehiclePlate = plate.getText().toString();
                vehicleState = state.getText().toString();
                vehicleType = type.getText().toString();


                if (isEmpty(vehicleMake) || isEmpty(vehicleYear) || isEmpty(vehiclePlate) || isEmpty(vehicleState) || isEmpty(vehicleType)) {
                    Toast.makeText(VehicleDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    changeToNextActivity(view);
                }
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
        intent.putExtra("usersVehicle", usersVehicle);

        this.startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                usersVehicle = paths[0];
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                usersVehicle = paths[1];
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                usersVehicle = paths[2];
                break;
            case 3:
                // Whatever you want to happen when the thrid item gets selected
                usersVehicle = paths[3];
                break;
            case 4:
                // Whatever you want to happen when the thrid item gets selected
                usersVehicle = paths[4];
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void checkData(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");
        HashMap<String, String> maps = new HashMap();
        dbRef.child(currentUser.getUid()).child("Vehicles").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Maaz logging data " + String.valueOf(task.getResult().getValue()));

                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        maps.put((String) childSnapshot.getKey(), String.valueOf(childSnapshot.getValue()));
                    }
                }
                mapSize = maps.size();
                int i = 0;
                if (maps != null) {
                        Set<String> keys = maps.keySet();
                        for(String key:keys) {
                            paths[i] = key;
                            i++;
                        }
                }
            }
        });
    }
}