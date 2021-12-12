package com.example.androidaccidentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfileVehicle extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    EditText carMakeEdit, yearEdit, plateNumEdit, stateEdit, carTypeEdit;
    String vehicleMake, vehicleYear, vehiclePlateNum, vehicleState, vehicleType;
    Switch editable;

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    HashMap<String, Map<String, String>> map =
            new HashMap<String, Map<String, String>>();
    HashMap<String, String> vehicleData = new HashMap<>();

    Spinner spinner;
    ArrayList<String> cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vehicle);

        carMakeEdit = findViewById(R.id.makeEdit);
        yearEdit = findViewById(R.id.yearEdit);
        plateNumEdit = findViewById(R.id.plateNumEdit);
        stateEdit = findViewById(R.id.stateEdit);
        carTypeEdit = findViewById(R.id.typeEdit);

        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
        drawerLayout = findViewById(R.id.drawer_layout);

        editable = findViewById(R.id.editable);
        spinner = findViewById(R.id.spinner);
        cars = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        pullData();
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileVehicle.this,
//                android.R.layout.simple_spinner_item, cars);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setSelection(-1, true);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        String car = spinner.getSelectedItem().toString();
//                        Set<String> keys = map.get(car).keySet();
//                        for (String key : keys) {
//                            vehicleData.put(key, map.get(car).get(key));
//                            Log.d("Vehicle Data Select", "Vehicle" + vehicleData);
//                        }
//
//                        carMakeEdit.setText(String.valueOf(map.get("VehicleMake")));
//                        yearEdit.setText(String.valueOf(map.get("VehicleYear")));
//                        plateNumEdit.setText(String.valueOf(map.get("VehiclePlate")));
//                        stateEdit.setText(String.valueOf(map.get("VehicleState")));
//                        carTypeEdit.setText(String.valueOf(map.get("usersVehicle")));
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                String car = spinner.getSelectedItem().toString();
//                Set<String> keys = map.get(car).keySet();
//                for (String key : keys) {
//                    vehicleData.put(key, map.get(car).get(key));
//                    Log.d("Vehicle Data Nothing", "Vehicle" + vehicleData);
//                }
//            }
//        });

        Log.d("Vehicle Data Map", "Vehicle" + vehicleData);

    }

    public void pullData (){

        dbRef.child(currentUser.getUid()).child("Vehicles").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete (@NonNull Task < DataSnapshot > task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Logging data " + String.valueOf(task.getResult().getValue()));

                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        map.put(String.valueOf(childSnapshot.getKey()), (Map) childSnapshot.getValue());
                    }

                    String car = null;
                    if (map != null) {
                        Set<String> keys = map.keySet();
                        for (String key : keys) {
                            cars.add(key);
                            car = key;
                            Log.d("Cars List", "" + cars);
                        }
                    }

                    Set<String> keys = map.get(car).keySet();
                    for (String key : keys) {
                        vehicleData.put(key, map.get(car).get(key));
                        Log.d("Vehicle Data Select", "" + vehicleData);
                    }

                    carMakeEdit.setText(String.valueOf(vehicleData.get("Vehicle Make")));
                    yearEdit.setText(String.valueOf(vehicleData.get("Vehicle Year")));
                    plateNumEdit.setText(String.valueOf(vehicleData.get("Vehicle Plate")));
                    stateEdit.setText(String.valueOf(vehicleData.get("Vehicle State")));
                    carTypeEdit.setText(String.valueOf(vehicleData.get("Vehicle Type")));
                }
            }
        });
}

    public void activate (EditText et){
        et.setEnabled(true);
        et.setTextColor(Color.BLACK);
    }

    public void deactivate(EditText et){
        et.setEnabled(false);
        et.setTextColor(Color.GRAY);
    }

    public void updateText(View view) {
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(editable.isChecked()){
                    activate(carMakeEdit);
                    activate(yearEdit);
                    activate(plateNumEdit);
                    activate(stateEdit);
                    activate(carTypeEdit);
                } else {
                    deactivate(carMakeEdit);
                    deactivate(yearEdit);
                    deactivate(plateNumEdit);
                    deactivate(stateEdit);
                    deactivate(carTypeEdit);
                }
            }
        });

    }

    public void edit_profile(View view) {
        Intent intent = new Intent(ProfileVehicle.this, ProfileUser.class);
        startActivity(intent);
    }

    public void edit_Insurance(View view) {
        Intent intent = new Intent(ProfileVehicle.this, ProfileInsurance.class);
        startActivity(intent);
    }

    public void save(View view) {
        //Push updated data over to Firebase

        vehicleMake = carMakeEdit.getText().toString();
        vehicleYear= yearEdit.getText().toString();
        vehiclePlateNum= plateNumEdit.getText().toString();
        vehicleState= stateEdit.getText().toString();
        vehicleType= carTypeEdit.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("Vehicle Make", vehicleMake);
        data.put("Vehicle Year", vehicleYear);
        data.put("Vehicle Plate", vehiclePlateNum);
        data.put("Vehicle State", vehicleState);
        data.put("Vehicle Type", vehicleType);

        dbRef.child(currentUser.getUid()).child("Vehicles").updateChildren(data, completionListener);

        deactivate(carMakeEdit);
        deactivate(yearEdit);
        deactivate(plateNumEdit);
        deactivate(stateEdit);
        deactivate(carTypeEdit);
        editable.setChecked(false);

        Toast.makeText(ProfileVehicle.this, "Vehicle Data Updated", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(ProfileVehicle.this, message, Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(ProfileVehicle.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(ProfileVehicle.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(ProfileVehicle.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(ProfileVehicle.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(ProfileVehicle.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, InsuranceProfile.class);
//                            startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (ProfileVehicle.this, Login.class);
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

//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String car = spinner.getSelectedItem().toString();
//        Set<String> keys = map.get(car).keySet();
//        for (String key: keys){
//            vehicleData.put(key, map.get(car).get(key));
//            Log.d("Vehicle Data Select", "Vehicle" + vehicleData);
//        }
//
//        carMakeEdit.setText(String.valueOf(map.get("VehicleMake")));
//        yearEdit.setText(String.valueOf(map.get("VehicleYear")));
//        plateNumEdit.setText(String.valueOf(map.get("VehiclePlate")));
//        stateEdit.setText(String.valueOf(map.get("VehicleState")));
//        carTypeEdit.setText(String.valueOf(map.get("usersVehicle")));
//
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        String car = spinner.getSelectedItem().toString();
//        Set<String> keys = map.get(car).keySet();
//        for (String key: keys){
//            vehicleData.put(key, map.get(car).get(key));
//            Log.d("Vehicle Data Nothing", "Vehicle" + vehicleData);
//        }
//    }

}