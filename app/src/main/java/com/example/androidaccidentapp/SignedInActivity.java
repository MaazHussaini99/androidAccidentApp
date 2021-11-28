package com.example.androidaccidentapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignedInActivity extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    String name = "Tino";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, Registering.class));
            finish();
            return;
        }
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");
       // dbRef.addValueEventListener(changeListener);


    }
    ValueEventListener changeListener = new ValueEventListener() {


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            String change = dataSnapshot.child(
                    currentUser.getUid()).child("message")
                    .getValue(String.class);

            userText.setText(change);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            notifyUser("Database error: " + databaseError.toException());
        }
    };

    public void saveData(View view) {
        dbRef.child(currentUser.getUid()).child("message")
                .setValue(name, completionListener);
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
        Toast.makeText(SignedInActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void goToMaps(View view) {
        startActivity(new Intent(this.getApplicationContext(), MapsActivity.class));
    }
}