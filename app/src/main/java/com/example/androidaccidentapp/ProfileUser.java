package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class ProfileUser extends AppCompatActivity {

    EditText firstName, lastName, dob, address, license, iceContact;
    Switch editable;
    ArrayAdapter<String> adapter;
    DrawerLayout drawerLayout;
    ImageView menuButton;

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

        drawerLayout = findViewById(R.id.drawer_layout);
        menuButton = (ImageView) findViewById(R.id.menuButton);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);



        editable = findViewById(R.id.editable);
    }

    public void edit_vehicle(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileVehicle.class);
        startActivity(intent);
    }

    public void edit_insurance(View view) {
        Intent intent = new Intent(ProfileUser.this, ProfileInsurance.class);
        startActivity(intent);

    }

    public void updateText(View view) {
        editable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (editable.isChecked()) {
                    firstName.setEnabled(true);
                    firstName.setTextColor(Color.WHITE);
                    lastName.setEnabled(true);
                    lastName.setTextColor(Color.WHITE);
                    dob.setEnabled(true);
                    dob.setTextColor(Color.WHITE);
                    address.setEnabled(true);
                    address.setTextColor(Color.WHITE);
                    license.setEnabled(true);
                    license.setTextColor(Color.WHITE);
                    iceContact.setEnabled(true);
                    iceContact.setTextColor(Color.WHITE);
                } else {
                    firstName.setEnabled(false);
                    firstName.setTextColor(Color.GRAY);
                    lastName.setEnabled(false);
                    lastName.setTextColor(Color.GRAY);
                    dob.setEnabled(false);
                    dob.setTextColor(Color.GRAY);
                    address.setEnabled(false);
                    address.setTextColor(Color.GRAY);
                    license.setEnabled(false);
                    license.setTextColor(Color.GRAY);
                    iceContact.setEnabled(false);
                    iceContact.setTextColor(Color.GRAY);
                }
            }
        });
    }

    public void save(View view) {
        //push updated data over to firebase
        firstName.setEnabled(false);
        firstName.setTextColor(Color.GRAY);
        lastName.setEnabled(false);
        lastName.setTextColor(Color.GRAY);
        dob.setEnabled(false);
        dob.setTextColor(Color.GRAY);
        address.setEnabled(false);
        address.setTextColor(Color.GRAY);
        license.setEnabled(false);
        license.setTextColor(Color.GRAY);
        iceContact.setEnabled(false);
        iceContact.setTextColor(Color.GRAY);
        editable.setChecked(false);
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
                        Intent intent = new Intent(ProfileUser.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(ProfileUser.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(ProfileUser.this, Home.class);
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