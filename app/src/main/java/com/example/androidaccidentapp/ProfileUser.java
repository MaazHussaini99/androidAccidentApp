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

    EditText firstNameEdit, lastNameEdit, dobEdit, addressEdit, licenseEdit, iceEdit;
    String firstName, lastName, dob, address, licenceNum, iceNum;
    Switch editable;

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
        //Push updated data over to Firebase
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
        Toast.makeText(ProfileUser.this, message,
                Toast.LENGTH_SHORT).show();
    }
}