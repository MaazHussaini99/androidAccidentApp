package com.example.androidaccidentapp;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class driverInfo extends AppCompatActivity {

    EditText fName;
    EditText lName;
    EditText dob;
    EditText address;
    EditText licence;
    Button nxt;

    String firstName;
    String lastName;
    String dateOfBirth;
    String addressDriver;
    String licenceNum;

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        licence = findViewById(R.id.driveLicence);
        nxt = findViewById(R.id.next);

        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
        drawerLayout = findViewById(R.id.drawer_layout);



        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = fName.getText().toString();
                lastName = lName.getText().toString();
                dateOfBirth = dob.getText().toString();
                addressDriver = address.getText().toString();
                licenceNum = licence.getText().toString();

                if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(dateOfBirth) || isEmpty(addressDriver) || isEmpty(licenceNum)) {
                    Toast.makeText(driverInfo.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (licenceNum.length() < 9) {
                    Toast.makeText(driverInfo.this, "License Number should be at least 9 characters", Toast.LENGTH_SHORT).show();
                } else if(firstName.length() < 3 || firstName.length() > 30 || lastName.length() < 3 || lastName.length() > 30){
                    Toast.makeText(driverInfo.this, "First name and last name must be between 3 and 30 characters", Toast.LENGTH_SHORT).show();
                } else if(!dateOfBirth.matches("^(02/29/(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26]))))$"
                        + "|^(02/(0[1-9]|1[0-9]|2[0-8])/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$"
                        + "|^((0[13578]|10|12)/(0[1-9]|[12][0-9]|3[01])/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$"
                        + "|^((0[469]|11)/(0[1-9]|[12][0-9]|30)/((19[0-9]{2})|(20[0][0-9]|(20[1][0-5]))))$")){
                    Toast.makeText(driverInfo.this, "Invalid Date or Year or format mm/dd/yyyy", Toast.LENGTH_SHORT).show();
                } else{
                    changeToNextActivity(view);
                }


            }
        });

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
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(driverInfo.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(driverInfo.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(driverInfo.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(driverInfo.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(driverInfo.this, "Access User Reports", Toast.LENGTH_LONG).show();
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


    public void changeToNextActivity(View v){
        Intent intent = new Intent(this, insuranceExchange.class);
        intent.putExtra("FirstName", firstName);
        intent.putExtra("LastName", lastName);
        intent.putExtra("DOB", dateOfBirth);
        intent.putExtra("Address", addressDriver);
        intent.putExtra("DriverLicence", licenceNum);
        this.startActivity(intent);
    }
}