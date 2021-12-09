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
import java.util.Map;

public class ProfileVehicle extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    EditText carMake, yearEdit, plateNumEdit, stateEdit, carTypeEdit;
    String vehicleMake, vehicleYear, vehiclePlateNum, vehicleState, vehicleType;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vehicle);

        carMake = findViewById(R.id.makeEdit);
        yearEdit = findViewById(R.id.yearEdit);
        plateNumEdit = findViewById(R.id.plateNumEdit);
        stateEdit = findViewById(R.id.stateEdit);
        carTypeEdit = findViewById(R.id.typeEdit);

        editable = findViewById(R.id.editable);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        HashMap<String, Object> map = new HashMap<>();

        dbRef.child(currentUser.getUid()).child("Vehicles").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                    for (Map.Entry<String, Object> entry : map.entrySet()) {

                        carMake.setText(map.get("VehicleMake").toString());
                        yearEdit.setText(map.get("VehicleYear").toString());
                        plateNumEdit.setText(map.get("VehiclePlate").toString());
                        stateEdit.setText(map.get("VehicleState").toString());
                        carTypeEdit.setText(map.get("usersVehicle").toString());
                    }
                }
            }
        });
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
        //Response is slow -- verify correct implementation being used
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(editable.isChecked()){
                    activate(carMake);
                    activate(carMake);
                    activate(plateNumEdit);
                    activate(stateEdit);
                    activate(carTypeEdit);
                } else {
                    deactivate(carMake);
                    deactivate(carMake);
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

        vehicleMake = carMake.getText().toString();
        vehicleYear= yearEdit.getText().toString();
        vehiclePlateNum= plateNumEdit.getText().toString();
        vehicleState= stateEdit.getText().toString();
        vehicleType= carTypeEdit.getText().toString();

        HashMap<String, Object> data = new HashMap<>();
        data.put("VehicleMake", vehicleMake);
        data.put("VehicleYear", vehicleYear);
        data.put("VehiclePlate", vehiclePlateNum);
        data.put("VehicleState", vehicleState);
        data.put("VehicleType", vehicleType);

        dbRef.child(currentUser.getUid()).child("Vehicles").updateChildren(data, completionListener);

        deactivate(carMake);
        deactivate(carMake);
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
}