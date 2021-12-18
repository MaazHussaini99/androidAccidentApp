package com.example.androidaccidentapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.graphics.Bitmap.Config.ARGB_8888;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeneratePDF extends AppCompatActivity {
    HashMap<String, Object> imageList1 = new HashMap<>();

    // variables for our buttons.
    Button generatePDFbtn;

    DrawerLayout drawerLayout;
    ArrayAdapter<String> adapter;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    int mapSize;
    String paths[];
    EditText etEmail;
    EditText etSubject;
    EditText etMessage;
    String name;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    HashMap<String, Object> newMap = new HashMap<>();
    String reportName;
    Dialog emailDialog;
    Button Send;
    Button attachment;
    TextView tvAttachment;
    String email;
    String subject;
    String message;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    String UsersVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);

        Intent intent = getIntent();
        imageList1 = (HashMap<String, Object>)intent.getSerializableExtra("Images");
        UsersVehicle = getIntent().getStringExtra("userVehicle");

        // initializing our variables.
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_04);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        String[] options = {"View User Profile", "View Vehicle Profile", "View Insurance Policy"};
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, options);
        drawerLayout = findViewById(R.id.drawer_layout);

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

        reportName = getIntent().getStringExtra("Report Name");
        generatePDF();

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

    public void generatePDF(){
        // calling method to
        // generate our PDF file.
        readDataImages(new myCallBack() {
            @Override
            public void onCallBack(HashMap<String, String> mapsImages) {
                readDataUser(new myCallBack() {
                    @Override
                    public void onCallBack(HashMap<String, String> maps2) {
                        readData(new myCallBack() {
                            @Override
                            public void onCallBack(HashMap<String, String> maps) {


                                SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
                                Date todayDate = new Date();
                                String thisDate = currentDate.format(todayDate);

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
                                title.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

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

                                canvas.drawText("Your Accident Report", 270, 150, title);
                                canvas.drawText("Date Created: " + thisDate, 500, 200, title);
                                canvas.drawText("ASAP   Accident Safety and Prevention", 209, 80, title);
                                canvas.drawText("Other Driver's Info: ", 50, 300, title);
                                canvas.drawText("Page 1", 700, 1100, title);
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



                                canvas.drawText("Accident Location: " + location.toUpperCase(), 50, 250, title);

                                canvas.drawText("First Name: " + fName.toUpperCase(), 100, 350 + 15, title);
                                canvas.drawText("Last Name: " + lName.toUpperCase(), 100, 400 + 20, title);
                                canvas.drawText("Date of Birth: " + dob.toUpperCase(), 100, 450 + 25, title);
                                canvas.drawText("Address: " + address.toUpperCase(), 100, 500 + 30, title);
                                canvas.drawText("License Num: " + license.toUpperCase(), 100, 550 + 35, title);
                                canvas.drawText("Insurance Provider: " + prov.toUpperCase(), 100, 600 + 40, title);
                                canvas.drawText("Policy Number: " + polNum.toUpperCase(), 100, 650 + 45, title);
                                canvas.drawText("Policy Holder: " + holder.toUpperCase(), 100, 700 + 50, title);
                                canvas.drawText("Vehicle Make: " + make.toUpperCase(), 100, 750 + 55, title);
                                canvas.drawText("Vehicle Type: " + type.toUpperCase(), 100, 800 + 65, title);
                                canvas.drawText("Vehicle State: " + vehState.toUpperCase(), 100, 850 + 70, title);
                                canvas.drawText("Vehicle Year: " + vehYear.toUpperCase(), 100, 900 + 75, title);
                                canvas.drawText("Vehicle Plate: " + VehiclePlate.toUpperCase(), 100, 950 + 80, title);

                                String userLicense = String.valueOf(maps2.get("License Number"));
                                String userAddress = String.valueOf(maps2.get("Address"));
                                String userHolder = String.valueOf(maps2.get("Insurance Holder"));
                                String userDOB = String.valueOf(maps2.get("DOB"));
                                String userFname = String.valueOf(maps2.get("First Name"));
                                String userPol = String.valueOf(maps2.get("Policy Number"));
                                String userLname = String.valueOf(maps2.get("Last Name"));
                                String userInsProv = String.valueOf(maps2.get("Insurance Provider"));

                                pdfDocument.finishPage(myPage);

                                PdfDocument.PageInfo mypageInfo2 = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 2).create();
                                PdfDocument.Page myPage2 = pdfDocument.startPage(mypageInfo2);
                                Canvas canvas2 = myPage2.getCanvas();

                                title.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                                canvas2.drawBitmap(scaledbmp, 56, 40, paint);
                                canvas2.drawText("Your Accident Report", 270, 150, title);
                                canvas2.drawText("ASAP   Accident Safety and Prevention", 209, 80, title);
                                canvas2.drawText("Page 2", 700, 1100, title);
                                canvas2.drawText("Your Info: ", 50, 250, title);
                                title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                                canvas2.drawText("First Name: " + userFname.toUpperCase(), 100, 300 + 15, title);
                                canvas2.drawText("Last Name: " + userLname.toUpperCase(), 100,  350+ 20, title);
                                canvas2.drawText("Date of Birth: " + userDOB.toUpperCase(), 100,  400+ 25, title);
                                canvas2.drawText("Address: " + userAddress.toUpperCase(), 100,  450+ 30, title);
                                canvas2.drawText("License: " + userLicense.toUpperCase(), 100,  500+ 35, title);
                                canvas2.drawText("Insurance Provider: " + userInsProv.toUpperCase(), 100,  550+ 40, title);
                                canvas2.drawText("Policy Number: " + userPol.toUpperCase(), 100,  600+ 45, title);
                                canvas2.drawText("Policy Holder: " + userHolder.toUpperCase(), 100,  650+ 50, title);

                                String userMake = String.valueOf(mapsImages.get("Vehicle Make"));
                                String userPlate = String.valueOf(mapsImages.get("Vehicle Plate"));
                                String userState = String.valueOf(mapsImages.get("Vehicle State"));
                                String userType = String.valueOf(mapsImages.get("Vehicle Type"));
                                String userYear = String.valueOf(mapsImages.get("Vehicle Year"));

                                canvas2.drawText("Vehicle Make: " + userMake.toUpperCase(), 100, 700 + 55, title);
                                canvas2.drawText("Vehicle Plate: " + userPlate.toUpperCase(), 100, 750 + 60, title);
                                canvas2.drawText("Vehicle State: " + userState.toUpperCase(), 100, 800 + 65, title);
                                canvas2.drawText("Vehicle Type: " + userType.toUpperCase(), 100, 850 + 70, title);
                                canvas2.drawText("Vehicle Year: " + userYear.toUpperCase(), 100, 900 + 75, title);



                                pdfDocument.finishPage(myPage2);

//                                        Set<String> keys = mapsImages.keySet();
//                                        for(String key : keys){
//                                            try{
//                                                URI url = new URI(mapsImages.get(key));
//                                                Bitmap bitmap = Glide
//                                                        .with(GeneratePDF.this)
//                                                        .asBitmap()
//                                                        .load(url)
//                                                        .submit().get();
//                                                canvas.drawBitmap(bitmap, 100, 940 + 50, paint);
//                                            }
//                                            catch (URISyntaxException | ExecutionException e){
//                                                e.printStackTrace();
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                        }
                                // after adding all attributes to our
                                // PDF file we will be finishing our page.



                                Log.d("firebase", "ImageList1 " + imageList1);
                                int pageNum = 3;
                                Paint pageTitle = new Paint();
                                pageTitle.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
                                pageTitle.setTextSize(25);

                                Set<String> keys = imageList1.keySet();
                                for(String key : keys){
                                    PdfDocument.PageInfo mypageInfo3 = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, pageNum).create();
                                    PdfDocument.Page myPage3 = pdfDocument.startPage(mypageInfo3);
                                    Canvas canvas3 = myPage3.getCanvas();
                                    canvas3.drawText("Page " + pageNum, 700,  1100, pageTitle);
                                    Bitmap resizedBitmap = BitmapFactory.decodeFile(String.valueOf(imageList1.get(key)));
                                    Bitmap resized = Bitmap.createScaledBitmap(resizedBitmap, 700, 1000, true);
                                    canvas3.drawBitmap(resized, 56 , 40, paint);
                                    pdfDocument.finishPage(myPage3);
                                    pageNum++;
                                }


                                String fileName = "storage/emulated/0/Android/data/com.example.androidaccidentapp/files/Pictures/IMG_20211216_214434_2862665050733970153.jpg";







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

    private void readData (myCallBack myCallBack){
        dbRef.child(currentUser.getUid()).child("Accident Reports").child(reportName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

    private void readDataImages(myCallBack myCallBack) {
        dbRef.child(currentUser.getUid()).child("Vehicles").child(UsersVehicle).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> maps = new HashMap<>();
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Car Name " + UsersVehicle);
                    Log.d("firebase", "Car details " + task.getResult().getValue());
                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        maps.put(childSnapshot.getKey(), String.valueOf(childSnapshot.getValue()));
                    }
                    myCallBack.onCallBack(maps);
                }
            }
        });
    }
    //open email dialog to share PDF in email
    public void sendEmail(View view) {
        emailDialog = new Dialog(GeneratePDF.this);
        emailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        emailDialog.setCancelable(true);
        emailDialog.setContentView(R.layout.email_dialog);

        etEmail = emailDialog.findViewById(R.id.etTo);
        etSubject = emailDialog.findViewById(R.id.etSubject);
        etMessage = emailDialog.findViewById(R.id.etMessage);
        attachment = emailDialog.findViewById(R.id.btAttachment);
        tvAttachment = emailDialog.findViewById(R.id.tvAttachment);


        Send = emailDialog.findViewById(R.id.btSend);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
        //attachment button listener
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });

        emailDialog.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            URI = data.getData();
            tvAttachment.setText(URI.getLastPathSegment());
            tvAttachment.setVisibility(View.VISIBLE);
        }
    }
    public void sendEmail() {
        try {
            email = etEmail.getText().toString();
            subject = etSubject.getText().toString();
            message = etMessage.getText().toString();
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void openFolder() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
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
        AlertDialog.Builder profileDialog = new AlertDialog.Builder(GeneratePDF.this);
        //Set User Profile Dialog Title
        profileDialog.setTitle("User Account Options:");
        //List Options, when item selected, switch to that activity

        profileDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent = new Intent(GeneratePDF.this, ProfileUser.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(GeneratePDF.this, ProfileVehicle.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(GeneratePDF.this, ProfileInsurance.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        profileDialog.setNegativeButton("Sign Out", (v, a) -> {
            Intent intent = new Intent (GeneratePDF.this, Login.class);
            startActivity(intent);
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_LONG).show();
            finish();
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
