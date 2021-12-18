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
    String firstName, lastName, dob, address, licenseNum, iceNum;
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
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

        //Pulling user data down from Firebase and populating fields
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
                    iceEdit.setText(String.valueOf(map.get("Emergency Contact")));
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
        if (editable.isChecked()) {
            //Open all fields to allow user to update data
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

    public void save(View view) {
        //Push updated data over to Firebase
        //Get info from the edit fields
        firstName = firstNameEdit.getText().toString();
        lastName = lastNameEdit.getText().toString();
        dob = dobEdit.getText().toString();
        address = addressEdit.getText().toString();
        licenseNum = licenseEdit.getText().toString();
        iceNum = iceEdit.getText().toString();

        //Validation
            // Date of Birth RegEx modeled off of Gregorian Date (section 3.7 - https://www.baeldung.com/java-date-regular-expressions)
        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(dob) || isEmpty(address) || isEmpty(licenseNum)) {
            Toast.makeText(ProfileUser.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (licenseNum.length() < 9) {
            Toast.makeText(ProfileUser.this, "License Number should be at least 9 characters", Toast.LENGTH_SHORT).show();
        } else if(firstName.length() < 3 || firstName.length() > 30 || lastName.length() < 3 || lastName.length() > 30){
            Toast.makeText(ProfileUser.this, "First name and last name must be between 3 and 30 characters", Toast.LENGTH_SHORT).show();
        } else if(!dob.matches("^(02/29/(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26]))))$"
                + "|^(02/(0[1-9]|1[0-9]|2[0-8])/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$"
                + "|^((0[13578]|10|12)/(0[1-9]|[12][0-9]|3[01])/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$"
                + "|^((0[469]|11)/(0[1-9]|[12][0-9]|30)/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$")){
            Toast.makeText(ProfileUser.this, "Invalid Date of Birth.\nPlease follow format MM/DD/YYYY", Toast.LENGTH_LONG).show();
        } else{
            //Format the data inputs
            String fName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            String lName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
            String license = licenseNum.trim().toUpperCase();
            String number = iceNum.replaceAll("\\D", "");
            String contactNum = number.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");

            //Add to data map to push to Firebase
            HashMap<String, Object> data = new HashMap<>();
            data.put("Emergency Contact", contactNum);
            data.put("License Number", license);
            data.put("Address", address);
            data.put("DOB", dob);
            data.put("Last Name", lName);
            data.put("First Name", fName);

            dbRef.child(currentUser.getUid()).child("User Info").updateChildren(data, completionListener);

            //Return fields to false edit state
            deactivate(firstNameEdit);
            deactivate(lastNameEdit);
            deactivate(dobEdit);
            deactivate(addressEdit);
            deactivate(licenseEdit);
            deactivate(iceEdit);

            editable.setChecked(false);
            Toast.makeText(ProfileUser.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
        }
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

    public void clickProfile(View view){
        recreate();
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