package com.example.androidaccidentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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

    EditText firstName, lastName, dob, address, license, iceContact;
    String fName, lName, dateOfBirth, addressDriver, licenceNum, iceNum;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        firstName = findViewById(R.id.firstNameEdit);
        lastName = findViewById(R.id.lastNameEdit);
        dob = findViewById(R.id.dobEdit);
        address = findViewById(R.id.addressEdit);
        license = findViewById(R.id.licenseEdit);
        iceContact = findViewById(R.id.iceContactEdit);

        editable = findViewById(R.id.editable);

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
                    }
                    firstName.setText(map.get("First Name").toString());
                    lastName.setText(map.get("Last Name").toString());
                    dob.setText(map.get("DOB").toString());
                    address.setText(map.get("Address").toString());
                    license.setText(map.get("License Number").toString());
                    iceContact.setText(map.get("ICE Contact Number").toString());
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
        et.setTextColor(Color.WHITE);
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
                    activate(firstName);
                    activate(lastName);
                    activate(dob);
                    activate(address);
                    activate(license);
                    activate(iceContact);
                } else {
                    deactivate(firstName);
                    deactivate(lastName);
                    deactivate(dob);
                    deactivate(address);
                    deactivate(license);
                    deactivate(iceContact);
                }
            }
        });
    }

    public void save(View view) {
        //Push updated data over to Firebase
        fName = firstName.getText().toString();
        lName = lastName.getText().toString();
        dateOfBirth = dob.getText().toString();
        addressDriver = address.getText().toString();
        licenceNum = license.getText().toString();
        iceNum = iceContact.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("ICE Contact Number", iceNum);
        data.put("License Number", licenceNum);
        data.put("Address", addressDriver);
        data.put("DOB", dateOfBirth);
        data.put("Last Name", lName);
        data.put("First Name", fName);

        dbRef.child(currentUser.getUid()).child("User Info").updateChildren(data, completionListener);

        deactivate(firstName);
        deactivate(lastName);
        deactivate(dob);
        deactivate(address);
        deactivate(license);
        deactivate(iceContact);

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
        Toast.makeText(ProfileUser.this, message,
                Toast.LENGTH_SHORT).show();
    }
}