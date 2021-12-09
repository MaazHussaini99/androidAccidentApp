package com.example.androidaccidentapp;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneratePDF extends AppCompatActivity {

    // variables for our buttons.
    Button generatePDFbtn;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    int mapSize;
    String paths[];
    String name;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    HashMap<String, Object> newMap = new HashMap<>();

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);

        // initializing our variables.
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_04);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                readDataUser(new myCallBack() {
                    @Override
                    public void onCallBack(HashMap<String, String> maps2) {
                        readData(new myCallBack() {
                            @Override
                            public void onCallBack(HashMap<String, String> maps) {

                                // creating an object variable
                                // for our PDF document.
                                PdfDocument pdfDocument = new PdfDocument();

                                // two variables for paint "paint" is used
                                // for drawing shapes and we will use "title"
                                // for adding text in our PDF file.
                                Paint paint = new Paint();
                                Paint title = new Paint();

                                // we are adding page info to our PDF file
                                // in which we will be passing our pageWidth,
                                // pageHeight and number of pages and after that
                                // we are calling it to create our PDF.
                                PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

                                // below line is used for setting
                                // start page for our PDF file.
                                PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

                                // creating a variable for canvas
                                // from our page of PDF.
                                Canvas canvas = myPage.getCanvas();

                                // below line is used to draw our image on our PDF file.
                                // the first parameter of our drawbitmap method is
                                // our bitmap
                                // second parameter is position from left
                                // third parameter is position from top and last
                                // one is our variable for paint.
                                canvas.drawBitmap(scaledbmp, 56, 40, paint);

                                // below line is used for adding typeface for
                                // our text which we will be adding in our PDF file.
                                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

                                // below line is used for setting text size
                                // which we will be displaying in our PDF file.
                                title.setTextSize(25);

                                // below line is sued for setting color
                                // of our text inside our PDF file.
                                title.setColor(ContextCompat.getColor(GeneratePDF.this, R.color.background));


                                // below line is used to draw text in our PDF file.
                                // the first parameter is our text, second parameter
                                // is position from start, third parameter is position from top
                                // and then we are passing our variable of paint which is title.
                                //String name = paths[0];

                                canvas.drawText("Your Accident Report", 209, 150, title);
                                canvas.drawText("ASAP   Accident Safety and Prevention", 209, 80, title);

                                // similarly we are creating another text and in this
                                // we are aligning this text to center of our PDF file.
                                title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                                title.setColor(ContextCompat.getColor(GeneratePDF.this, R.color.background));
                                title.setTextSize(25);

                                // below line is used for setting
                                // our text to center of PDF.
                                String location = String.valueOf(maps.get("Accident Location"));
                                String VehiclePlate = String.valueOf(maps.get("Vehicle Plate"));
                                String license = String.valueOf(maps.get("License Number"));
                                String address = String.valueOf(maps.get("Address"));
                                String holder = String.valueOf(maps.get("Insurance Holder"));
                                String make = String.valueOf(maps.get("Vehicle Make"));
                                String type = String.valueOf(maps.get("Vehicle Type"));
                                String fName = String.valueOf(maps.get("First Name"));
                                String polNum = String.valueOf(maps.get("Policy Number"));
                                String prov = String.valueOf(maps.get("Insurance Provider"));
                                String vehState = String.valueOf(maps.get("Vehicle State"));
                                String dob = String.valueOf(maps.get("DOB"));
                                String vehYear = String.valueOf(maps.get("Vehicle Year"));
                                String lName = String.valueOf(maps.get("Last Name"));


                                canvas.drawText("Other Driver's Info: ", 50, 300, title);
                                canvas.drawText("First Name: " +  fName.toUpperCase(), 100, 320 + 15, title);
                                canvas.drawText("Last Name: " +  lName.toUpperCase(), 100, 340 + 20, title);
                                canvas.drawText("Date of Birth: " +  dob.toUpperCase(), 100, 360 + 25, title);
                                canvas.drawText("Address: " +  address.toUpperCase(), 100, 380 + 30, title);
                                canvas.drawText("License Num: " +  license.toUpperCase(), 100, 400 + 35, title);
                                canvas.drawText("Insurance Provider: " +  prov.toUpperCase(), 100, 420 + 40, title);
                                canvas.drawText("Policy Number: " +  polNum.toUpperCase(), 100, 440 + 45, title);
                                canvas.drawText("Policy Holder: " +  holder.toUpperCase(), 100, 480 + 50, title);
                                canvas.drawText("Vehicle Make: " +  make.toUpperCase(), 100, 500 + 55, title);
                                canvas.drawText("Vehicle Type: " +  type.toUpperCase(), 100, 520 + 65, title);
                                canvas.drawText("Vehicle State: " +  vehState.toUpperCase(), 100, 540 + 70, title);
                                canvas.drawText("Vehicle Year: " +  vehYear.toUpperCase(), 100, 560 + 75, title);
                                canvas.drawText("Vehicle Plate: " +  VehiclePlate.toUpperCase(), 100, 580 + 80, title);

                                String userLicense = String.valueOf(maps2.get("License Number"));
                                String userAddress = String.valueOf(maps2.get("Address"));
                                String userHolder = String.valueOf(maps2.get("Insurance Holder"));
                                String userDOB = String.valueOf(maps2.get("DOB"));
                                String userFname = String.valueOf(maps2.get("First Name"));
                                String userPol = String.valueOf(maps2.get("Policy Number"));
                                String userLname = String.valueOf(maps2.get("Last Name"));
                                String userInsProv = String.valueOf(maps2.get("Insurance Provider"));



                                canvas.drawText("Your Info: ", 50, 760, title);
                                canvas.drawText("First Name: " +  userFname.toUpperCase(), 100, 780 + 15, title);
                                canvas.drawText("Last Name: " +  userLname.toUpperCase(), 100, 800 + 20, title);
                                canvas.drawText("Date of Birth: " +  userDOB.toUpperCase(), 100, 820 + 25, title);
                                canvas.drawText("Address: " +  userAddress.toUpperCase(), 100, 840 + 30, title);
                                canvas.drawText("License: " +  userLicense.toUpperCase(), 100, 860 + 35, title);
                                canvas.drawText("Insurance Provider: " +  userInsProv.toUpperCase(), 100, 880 + 40, title);
                                canvas.drawText("Policy Number: " +  userPol.toUpperCase(), 100, 900 + 45, title);
                                canvas.drawText("Policy Holder: " +  userHolder.toUpperCase(), 100, 920 + 50, title);




                                // after adding all attributes to our
                                // PDF file we will be finishing our page.
                                pdfDocument.finishPage(myPage);

                                // below line is used to set the name of
                                // our PDF file and its path.
                                File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

                                try {
                                    // after creating a file name we will
                                    // write our PDF file to that location.
                                    pdfDocument.writeTo(new FileOutputStream(file));

                                    // below line is to print toast message
                                    // on completion of PDF generation.
                                    Toast.makeText(GeneratePDF.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    // below line is used
                                    // to handle error
                                    e.printStackTrace();
                                }
                                // after storing our pdf to that
                                // location we are closing our PDF file.
                                pdfDocument.close();
                            }
                        });
                    }
                });

            }
        });
    }
    private void readDataUser (myCallBack myCallBack){
        dbRef.child(currentUser.getUid()).child("User Info").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> maps = new HashMap<>();
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Maaz logging data " + task.getResult().getValue());
                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        maps.put(childSnapshot.getKey(), String.valueOf(childSnapshot.getValue()));
                    }
                    myCallBack.onCallBack(maps);
                }
            }
        });
    }

    private void readData (myCallBack myCallBack){
        dbRef.child(currentUser.getUid()).child("Accident Reports").child("Maaz 1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> maps = new HashMap<>();
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Maaz logging data " + task.getResult().getValue());
                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        maps.put(childSnapshot.getKey(), String.valueOf(childSnapshot.getValue()));
                    }
                    myCallBack.onCallBack(maps);
                }
            }
        });
    }

    private interface myCallBack{
        void onCallBack(HashMap<String, String> maps);

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
