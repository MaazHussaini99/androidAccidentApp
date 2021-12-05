package com.example.androidaccidentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.androidaccidentapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private ActivityMapsBinding binding;
    private List<String> reports;
    private int reportsCounter;
    TextView viewAddress;
    Dialog dialog;
    Address address;
    FusedLocationProviderClient client;
    SupportMapFragment mapFragment;
    Hashtable<String, String> coordinates = new Hashtable<String, String>();
    Button btn;
    String name = "Bryan";
    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy", "View Reports"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);

        drawerLayout = findViewById(R.id.drawer_layout);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);


        //check permissions
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission is granted
            getCurrentLocation();

        } else {
            //when permission is denied
            //request permission
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        geocoder = new Geocoder(this);

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //success
                if (location != null){
                    //sync map with current location
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {


                            //initialize new latlng
                            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

                            //set marker for location
                            MarkerOptions options = new MarkerOptions().position(latlng).title("You are here");

                            //zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

                            //add marker to map
                            googleMap.addMarker(options);
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permission is granted call getLocation method
                getCurrentLocation();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission is granted
            // getCurrentLocation();
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location arg0) {
                    //when maps finds current location, add coordinates to hashmap to later add location coordinates to database
                    coordinates.put("Latitude", ""+arg0.getLatitude());
                    coordinates.put("Longitude", ""+arg0.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("You are here"));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            //when location marker is clicked
                            Log.d("Your Location: ", "" + coordinates.get("Latitude") + ", " + coordinates.get("Longitude"));
                            //use reverse geocoding to get formatted address from coordinates
                            try {
                                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(coordinates.get("Latitude")), Double.parseDouble(coordinates.get("Longitude")), 1);
                                address = addresses.get(0);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("Your address: ", "" + address.getAddressLine(0));
                            confirmAddress();
                            //leaveMaps();
                            return true;
                        }
                    });
                }
            });
        }
        else {
            //when permission is denied
            //request permission
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    //ideally this goes back to report accident page?
    public void leaveMaps(){
        startActivity(new Intent(this.getApplicationContext(), Login.class));

    }

    //method to use dialog to confirm address user wants to save to report
    public void confirmAddress(){
        dialog = new Dialog(MapsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        viewAddress = dialog.findViewById(R.id.addressTextView);
        viewAddress.setText("" + address.getAddressLine(0));

        dialog.show();

    }


    //method to save address to firebase for accident report
    public void saveAddress(View view) {
        reportsCounter = 1;
        String reportName = "Report " + reportsCounter;
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Accident Location", address.getAddressLine(0));

        //if list of reports is empty start at report 1
        //if(reports.isEmpty());
        //check whether child already exists before adding
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Accident Reports").child(reportName);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //testing to make sure it recognizes
                    Toast.makeText(MapsActivity.this, "Report 1 already exists", Toast.LENGTH_SHORT).show();

                } else {
                    // Don't exist! Do something.
                    FirebaseDatabase.getInstance().getReference().child("Accident Reports").child(reportName).updateChildren(map);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });
    }
    public String getNextReport(){
        String result = "Report + " + reports.size() + 1;
        return result;
    }

    public void deleteData(View view) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Accident Reports").child("Report 1");
        DatabaseReference reportReference = FirebaseDatabase.getInstance().getReference().child("Report 1");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //delete if exists
                    FirebaseDatabase.getInstance().getReference().child("Accident Reports").child("Report 1").removeValue();
                    Toast.makeText(MapsActivity.this, "Report deleted", Toast.LENGTH_SHORT).show();

                } else {
                    // Don't exist! Do something.

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

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
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(MapsActivity.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(MapsActivity.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(MapsActivity.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(MapsActivity.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Toast.makeText(MapsActivity.this, "Access User Reports", Toast.LENGTH_LONG).show();
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

    public void clickRegister(View view){
        redirectActivity(this, Registering.class);
    }

    public void clickMaps(View view){
        recreate();
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
        closeDrawer(drawerLayout);
    }
}