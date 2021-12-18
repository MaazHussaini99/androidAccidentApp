package com.example.androidaccidentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Step2 extends AppCompatActivity {

    TextView firstTextView;
    TextView secondTextView;
    TextView thirdTextView;
    TextView fourthTextView;

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2);

        drawerLayout = findViewById(R.id.drawer_layout);
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

        //initialize text views to allow to set visibilities
        firstTextView = (TextView)findViewById(R.id.step2firstTv);
        secondTextView = (TextView)findViewById(R.id.step2secondTv);
        thirdTextView = (TextView)findViewById(R.id.step2thirdTv);
        fourthTextView = (TextView)findViewById(R.id.step2fourthTv);

        //firstTextView will be visible when activity first created
        firstTextView.setVisibility(View.VISIBLE);

    }

    public void goBack(View view) {
        if(firstTextView.getVisibility() == View.VISIBLE){
            changeToPreviousActivity(view);
        }

        //to do finish other segments
        else if(secondTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.VISIBLE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
        }
        else if(thirdTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.VISIBLE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
        }
        else if(fourthTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.VISIBLE);
            fourthTextView.setVisibility(View.GONE);
        }
        else{
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.VISIBLE);
        }
    }

    //right arrow click
    public void goForward(View view) {
        if(firstTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.VISIBLE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.GONE);
        }
        else if(secondTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.VISIBLE);
            fourthTextView.setVisibility(View.GONE);
        }
        else if(thirdTextView.getVisibility() == View.VISIBLE){
            firstTextView.setVisibility(View.GONE);
            secondTextView.setVisibility(View.GONE);
            thirdTextView.setVisibility(View.GONE);
            fourthTextView.setVisibility(View.VISIBLE);
        }
        else{
            changeToNextActivity(view);
        }
    }

    public void changeToPreviousActivity(View view){
        Intent intent = new Intent(this, Step1.class);
        this.startActivity(intent);
    }

    public void changeToNextActivity(View view){
        Intent intent = new Intent(this, Step3.class);
        this.startActivity(intent);
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

    public void openProfileDialog(View view) {
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(Step2.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity
        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        Intent intent = new Intent(Step2.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Step2.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(Step2.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Toast.makeText(Step2.this, "Access User Reports", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Home.this, reports.class);
//                            startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(Step2.this, Camera.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        //Sign out button
        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (Step2.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            finish();
        });


        profileDialog.create().show();
    }

    public void clickGuide(View view){
        redirectActivity(this, Step1.class);
    }

    public void clickHome(View view){
        redirectActivity(this, Home.class);
    }

    public void clickMaps(View view){
        redirectActivity(this, MapsActivity.class);
    }

    public void clickHandBook(View view){
        redirectActivity(this, Registering.class);
    }

    public void clickLogin(View view){
        redirectActivity(this, Login.class);
    }

    public void clickProfile(View view){
        redirectActivity(this, ProfileUser.class);
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


}