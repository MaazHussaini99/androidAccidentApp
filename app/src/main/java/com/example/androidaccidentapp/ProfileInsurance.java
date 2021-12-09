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

public class ProfileInsurance extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    EditText providerEdit, policyNumEdit, accHolderEdit;
    String provider, policyNum, policyHolder;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_insurance);

        providerEdit = findViewById(R.id.providerEdit);
        policyNumEdit = findViewById(R.id.policyNoEdit);
        accHolderEdit = findViewById(R.id.accHolderEdit);

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
                    providerEdit.setText(String.valueOf(map.get("Insurance Provider")));
                    policyNumEdit.setText(String.valueOf(map.get("Policy Number")));
                    accHolderEdit.setText(String.valueOf(map.get("Insurance Holder")));
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
                    activate(providerEdit);
                    activate(policyNumEdit);
                    activate(accHolderEdit);
                } else {
                    deactivate(providerEdit);
                    deactivate(policyNumEdit);
                    deactivate(accHolderEdit);
                }
            }
        });

    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(ProfileInsurance.this, ProfileVehicle.class);
        startActivity(intent);
    }

    public void edit_profile(View view) {
        Intent intent = new Intent(ProfileInsurance.this, ProfileUser.class);
        startActivity(intent);
    }

    public void save(View view) {
        //Push updated data over to Firebase

        provider = providerEdit.getText().toString();
        policyNum = policyNumEdit.getText().toString();
        policyHolder = accHolderEdit.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("Insurance Provider", provider);
        data.put("Policy Number", policyNum);
        data.put("Insurance Holder", policyHolder);

        dbRef.child(currentUser.getUid()).child("User Info").updateChildren(data, completionListener);

        deactivate(providerEdit);
        deactivate(policyNumEdit);
        deactivate(accHolderEdit);
        editable.setChecked(false);

        Toast.makeText(ProfileInsurance.this, "Insurance Data Updated", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(ProfileInsurance.this, message, Toast.LENGTH_SHORT).show();
    }
}