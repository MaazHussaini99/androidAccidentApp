package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class finalCheck extends AppCompatActivity {

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
    ArrayAdapter<String> adapter;
    DrawerLayout drawerLayout;
    ImageView menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_check);

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

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = (ImageView) findViewById(R.id.menuButton);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

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
                //go to home page once confirmed
                Intent intent = new Intent(finalCheck.this, Home.class);
                startActivity(intent);
            }
        });
    }
    public void saveData(View view) {


        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("Vehicle Type", vehicleType);
        map2.put("Vehicle State", vehicleState);
        map2.put("Vehicle Plate", vehiclePlate);
        map2.put("Vehicle Year", vehicleYear);
        map2.put("Vehicle Make", vehicleMake);
        map.put("Insurance Holder", policyHolder);
        map.put("Policy Number", policyNum);
        map.put("Insurance Provider", provider);
        map.put("License Number", licenceNum);
        map.put("Address", addressDriver);
        map.put("DOB", dateOfBirth);
        map.put("Last Name", lastName);
        map.put("First Name", firstName);

        dbRef.child(currentUser.getUid()).child("User Info").updateChildren(map, completionListener);
        dbRef.child(currentUser.getUid()).child("Vehicles").child(vehicleMake).updateChildren(map2, completionListener);

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
        Toast.makeText(finalCheck.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void openProfileDialog(View view){
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(finalCheck.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(finalCheck.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(finalCheck.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(finalCheck.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(finalCheck.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, InsuranceProfile.class);
//                            startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Toast.makeText(this, "Clicked Sign Out", Toast.LENGTH_LONG).show();
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
        closeDrawer(drawerLayout);
    }

}