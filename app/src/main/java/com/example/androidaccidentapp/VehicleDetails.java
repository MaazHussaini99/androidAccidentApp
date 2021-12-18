package com.example.androidaccidentapp;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
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

    ArrayAdapter<String> adapter;
    DrawerLayout drawerLayout;
    ImageView menuButton;


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

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = (ImageView) findViewById(R.id.menuButton);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

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

                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR);
                String yearInString = String.valueOf(year);


                if (isEmpty(vehicleMake) || isEmpty(vehicleYear) || isEmpty(vehiclePlate) || isEmpty(vehicleState) || isEmpty(vehicleType)) {
                    Toast.makeText(VehicleDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (vehicleYear.length() != 4){
                    Toast.makeText(VehicleDetails.this, "Vehicle year must be 4 digits (YYYY)", Toast.LENGTH_SHORT).show();
                }else if(vehiclePlate.length() < 2 || vehiclePlate.length() > 8 ) {
                    Toast.makeText(VehicleDetails.this, "Vehicle plate should be between 2 and 8 characters", Toast.LENGTH_SHORT).show();
                } else if(((Integer.parseInt(vehicleYear)) > (Integer.parseInt(yearInString))) || ((Integer.parseInt(vehicleYear)) < 1900)){
                    Toast.makeText(VehicleDetails.this, "Invalid Year", Toast.LENGTH_SHORT).show();
                } else {
                    changeToNextActivity(view);
                }
            }
        });
    }

    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, MapsActivity.class);

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

    public void openProfileDialog(View view) {
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(VehicleDetails.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity
        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(VehicleDetails.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(VehicleDetails.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(VehicleDetails.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Toast.makeText(VehicleDetails.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, reports.class);
//                            startActivity(intent);
                        break;
                    }
                }
            }
        });
        //Sign out button
        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (VehicleDetails.this, Login.class);
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

    public void clickHandBook(View view){
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
        closeDrawer(drawerLayout);
    }

}