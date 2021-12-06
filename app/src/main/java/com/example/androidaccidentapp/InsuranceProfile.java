package com.example.androidaccidentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class InsuranceProfile extends AppCompatActivity {

    EditText providerEdit, policyNoEdit, accHolderEdit;
    Switch editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_profile);

        providerEdit = findViewById(R.id.providerEdit);
        policyNoEdit = findViewById(R.id.policyNoEdit);
        accHolderEdit = findViewById(R.id.accHolderEdit);

        editable = findViewById(R.id.editable);
    }

    public void updateText(View view) {
        //Response is slow -- verify correct implementation being used
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(editable.isChecked()){
                    providerEdit.setEnabled(true);
                    providerEdit.setTextColor(Color.WHITE);
                    policyNoEdit.setEnabled(true);
                    policyNoEdit.setTextColor(Color.WHITE);
                    accHolderEdit.setEnabled(true);
                    accHolderEdit.setTextColor(Color.WHITE);
                }else{
                    providerEdit.setEnabled(false);
                    providerEdit.setTextColor(Color.GRAY);
                    policyNoEdit.setEnabled(false);
                    policyNoEdit.setTextColor(Color.GRAY);
                    accHolderEdit.setEnabled(false);
                    accHolderEdit.setTextColor(Color.GRAY);
                }
            }
        });

    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(InsuranceProfile.this, VehicleProfile.class);
        startActivity(intent);
    }

    public void edit_profile(View view) {
        Intent intent = new Intent(InsuranceProfile.this, UserProfile.class);
        startActivity(intent);
    }

    public void save(View view) {
        //push updated data over to firebase
        providerEdit.setEnabled(false);
        providerEdit.setTextColor(Color.GRAY);
        policyNoEdit.setEnabled(false);
        policyNoEdit.setTextColor(Color.GRAY);
        accHolderEdit.setEnabled(false);
        accHolderEdit.setTextColor(Color.GRAY);
        editable.setChecked(false);
    }
}