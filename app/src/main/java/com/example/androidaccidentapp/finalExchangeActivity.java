package com.example.androidaccidentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class finalExchangeActivity extends AppCompatActivity {

    String vehicleMake, vehicleYear, vehiclePlate, vehicleState, vehicleType;
    String provider, policyNum, policyHolder;
    String firstName, lastName, dateOfBirth, addressDriver, licenceNum;
    String UsersVehicle;
    String accidentLocation;
    String reportName;
    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    ArrayList<String> imageFileNames = new ArrayList<>();
    HashMap<String, Object> imageList = new HashMap<>();

    TextView fName, lName, DOB, Address, license, insProvider, insPolicyNum, insPolicyHolder, Make,
            Year, Plate, State, Type, Location;

    Button submit;
    EditText ReportName;
    ImageView image;

    File file;

    HashMap<String,String> maps= new HashMap();

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_exchange);

//        intent.putExtra("accidentLocation", accidentLocation);
//
//        intent.putExtra("Image Names", imageFileNames);
//        intent.putExtra("Images", imageList);

        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        dateOfBirth = getIntent().getStringExtra("DOB");
        addressDriver = getIntent().getStringExtra("Address");
        licenceNum = getIntent().getStringExtra("DriverLicence");

        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
        drawerLayout = findViewById(R.id.drawer_layout);

        provider = getIntent().getStringExtra("Provider");
        policyNum = getIntent().getStringExtra("Policy Number");
        policyHolder = getIntent().getStringExtra("Holder");

        vehicleMake = getIntent().getStringExtra("VehicleMake");
        vehicleYear = getIntent().getStringExtra("VehicleYear");
        vehiclePlate = getIntent().getStringExtra("VehiclePlate");
        vehicleState = getIntent().getStringExtra("VehicleState");
        vehicleType = getIntent().getStringExtra("VehicleType");
        UsersVehicle = getIntent().getStringExtra("usersVehicle");

        accidentLocation = getIntent().getStringExtra("accidentLocation");

        imageFileNames = getIntent().getStringArrayListExtra("Image Names");
        Intent intent = getIntent();
        imageList = (HashMap<String, Object>)intent.getSerializableExtra("Images");
        Log.d("firebase", "Maaz logging data Images" + imageList);

        String fileName = "storage/emulated/0/Android/data/com.example.androidaccidentapp/files/Pictures/IMG_20211216_214434_2862665050733970153.jpg";
        file = new File(fileName);
        image = findViewById(R.id.Image);
        image.setImageURI(Uri.fromFile(file));
//        String name;
//        String path;
//        Set<String> keys = imageList.keySet();
//        for(String key : keys){
//            name = key
//        }

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
        Location = findViewById(R.id.locationTv);

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
        Location.setText(accidentLocation);

        ReportName = findViewById(R.id.ReportName);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        submit = findViewById(R.id.submitInfoToFirebase);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(view);
                changeToNextActivity(view);
            }
        });
    }

    public void saveData(View view) {

        reportName = ReportName.getText().toString();
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
        map.put("Users Vehicle", UsersVehicle);
        map.put("Accident Location", accidentLocation);

        dbRef.child(currentUser.getUid()).child("Accident Reports").child(reportName).updateChildren(map, completionListener);

        Set<String> Keys = imageList.keySet();
        for (String key : Keys){
            int i = 0;
            String imageName = ("Image" + i);
            dbRef.child(currentUser.getUid()).child("Accident Reports").child(reportName).child("images").child(imageName).setValue(String.valueOf(imageList.get(key)), completionListener);
            i++;
        }




//        dbRef.child(currentUser.getUid()).child("Accident Reports").child("Maaz 1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", "Maaz logging data " + String.valueOf(task.getResult().getValue()));
//
//                    for (DataSnapshot childSnapshot: task.getResult().getChildren()) {
//                        maps.put((String)childSnapshot.getKey(), (String)childSnapshot.getValue());
//                    }
//                    Address.setText(maps.get("Address"));
//                }
//            }
//        });
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

    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, GeneratePDF.class);
        intent.putExtra("Report Name", reportName);
        intent.putExtra("Images", imageList);
        intent.putExtra("userVehicle", UsersVehicle);
        this.startActivity(intent);
    }

    public void clickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void openProfileDialog(View view){
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(finalExchangeActivity.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(finalExchangeActivity.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(finalExchangeActivity.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(finalExchangeActivity.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(finalExchangeActivity.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, InsuranceProfile.class);
//                            startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (finalExchangeActivity.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            finish();
        });

        profileDialog.create().show();
    }

    public void clickHome(View view){
        redirectActivity(this, Home.class);
    }

    public void clickHandBook(View view){
        redirectActivity(this, Handbook.class);
    }

    public void clickMaps(View view){
        recreate();
    }

    public void clickGuide(View view){
        redirectActivity(this, Step1.class);
    }

    public void clickLogin(View view){
        redirectActivity(this, Login.class);
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