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
import android.widget.ImageView;
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

import java.util.HashMap;

public class ProfileUser extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    EditText firstNameEdit, lastNameEdit, dobEdit, addressEdit, licenseEdit, iceEdit;
    String firstName, lastName, dob, address, licenceNum, iceNum;
    Switch editable;
    ArrayAdapter<String> adapter;
    DrawerLayout drawerLayout;
    ImageView menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        dobEdit = findViewById(R.id.dobEdit);
        addressEdit = findViewById(R.id.addressEdit);
        licenseEdit = findViewById(R.id.licenseEdit);
        iceEdit = findViewById(R.id.iceContactEdit);
        editable = findViewById(R.id.editable);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = (ImageView) findViewById(R.id.menuButton);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");
        HashMap<String, Object> map = new HashMap<>();
        dbRef.child(currentUser.getUid()).child("User Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", "Logging data " + String.valueOf(task.getResult().getValue()));

                    for (DataSnapshot childSnapshot: task.getResult().getChildren()) {
                        map.put((String)childSnapshot.getKey(), (String)childSnapshot.getValue());
                        map.put((String) childSnapshot.getKey(), (String) childSnapshot.getValue());
                    }
                    firstNameEdit.setText(String.valueOf(map.get("First Name")));
                    lastNameEdit.setText(String.valueOf(map.get("Last Name")));
                    dobEdit.setText(String.valueOf(map.get("DOB")));
                    addressEdit.setText(String.valueOf(map.get("Address")));
                    licenseEdit.setText(String.valueOf(map.get("License Number")));
                    iceEdit.setText(String.valueOf(map.get("ICE Contact Number")));
                }
            }
        });
    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileVehicle.class);
        startActivity(intent);
    }

    public void edit_insurance(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileInsurance.class);
        startActivity(intent);

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
                if (editable.isChecked()) {
                    activate(firstNameEdit);
                    activate(lastNameEdit);
                    activate(dobEdit);
                    activate(addressEdit);
                    activate(licenseEdit);
                    activate(iceEdit);
                } else {
                    deactivate(firstNameEdit);
                    deactivate(lastNameEdit);
                    deactivate(dobEdit);
                    deactivate(addressEdit);
                    deactivate(licenseEdit);
                    deactivate(iceEdit);
                }
            }
        });
    }

    public void save(View view) {
        //push updated data over to firebase
        firstName = firstNameEdit.getText().toString();
        lastName = lastNameEdit.getText().toString();
        dob = dobEdit.getText().toString();
        address = addressEdit.getText().toString();
        licenceNum = licenseEdit.getText().toString();
        iceNum = iceEdit.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("ICE Contact Number", iceNum);
        data.put("License Number", licenceNum);
        data.put("Address", address);
        data.put("DOB", dob);
        data.put("Last Name", lastName);
        data.put("First Name", firstName);

        dbRef.child(currentUser.getUid()).child("User Info").updateChildren(data, completionListener);

        deactivate(firstNameEdit);
        deactivate(lastNameEdit);
        deactivate(dobEdit);
        deactivate(addressEdit);
        deactivate(licenseEdit);
        deactivate(iceEdit);

        editable.setChecked(false);
        Toast.makeText(ProfileUser.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(ProfileUser.this, message, Toast.LENGTH_SHORT).show();
    }

    public void openProfileDialog(View view){
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(ProfileUser.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(ProfileUser.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(ProfileUser.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(ProfileUser.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(ProfileUser.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, InsuranceProfile.class);
//                            startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (ProfileUser.this, Login.class);
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
        redirectActivity(this, Handbook.class);
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