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
        dbRef = database.getReference();

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling method to
                // generate our PDF file.
                readData(new myCallBack() {
                    @Override
                    public void onCallBack(HashMap<String, String> maps) {

                        Log.d("firebase", "Maaz logging data " + maps);
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
                        title.setTextSize(15);

                        // below line is sued for setting color
                        // of our text inside our PDF file.
                        title.setColor(ContextCompat.getColor(GeneratePDF.this, R.color.purple_200));


                        // below line is used to draw text in our PDF file.
                        // the first parameter is our text, second parameter
                        // is position from start, third parameter is position from top
                        // and then we are passing our variable of paint which is title.
                        //String name = paths[0];

                        String wow = String.valueOf(maps.get("Accident Location"));
                        canvas.drawText(wow, 209, 100, title);
                        canvas.drawText("Geeks for Geeks", 209, 80, title);

                        // similarly we are creating another text and in this
                        // we are aligning this text to center of our PDF file.
                        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        title.setColor(ContextCompat.getColor(GeneratePDF.this, R.color.purple_200));
                        title.setTextSize(15);

                        // below line is used for setting
                        // our text to center of PDF.
                        title.setTextAlign(Paint.Align.CENTER);
                        canvas.drawText("This is sample document which we have created.", 396, 560, title);

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

    private void readData (myCallBack myCallBack){
        dbRef.child("Accident Reports").child("Report 1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> maps = new HashMap<>();
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {


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
