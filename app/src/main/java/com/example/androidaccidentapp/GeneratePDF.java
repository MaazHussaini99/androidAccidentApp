package com.example.androidaccidentapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.androidaccidentapp.Report;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GeneratePDF extends AppCompatActivity {

    // variables for our buttons.
    Button generatePDFbtn;
    private EditText myEditText;

    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    String myString;
    HashMap<String, String> dataMap;
    String key;
    String value;
    Report report;
    private final String[] keys = new String[2];
    FirebaseUser currentUser;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    int mapSize;


    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo2);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 200, 50, false);

        ActivityCompat.requestPermissions(GeneratePDF.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);



    }

    public void generatePDF(View view){

        int pageHeight = 600;
        int pageWidth = 300;

        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Paint myPaint = new Paint();
        HashMap<String,String> map = new HashMap<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Accident Reports");
        rootRef.child("Report 1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", "Maaz logging data " + String.valueOf(task.getResult().getValue()));

                    for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                        map.put((String) childSnapshot.getKey(), (String) childSnapshot.getValue());
                    }
                }
                mapSize = map.size();
                int i = 0;
                if (map != null) {
                    Set<String> setKeys = map.keySet();
                    for(String key:setKeys) {
                        keys[i] = map.get(key);
                        i++;
                    }
                }
            }
            });




        //myString = "jsfs";
        int x = 25;
        Log.d("Is this null?", ""+keys);
        myPage.getCanvas().drawBitmap(scaledbmp, 80, 25, myPaint);
        myPage.getCanvas().drawText(myString, x, 300, myPaint);
        myPdfDocument.finishPage(myPage);

        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile3.pdf";
        File myFile = new File(myFilePath);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myFile));
            Toast.makeText(GeneratePDF.this, "Successful", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        myPdfDocument.close();
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}