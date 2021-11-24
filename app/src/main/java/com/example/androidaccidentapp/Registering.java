package com.example.androidaccidentapp;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registering extends AppCompatActivity {

    private EditText email, email2;
    private EditText password, password2;
    private Button register, signIn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registering);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email2 = findViewById(R.id.email2);
        password2 = findViewById(R.id.password2);
        register = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Registering.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Registering.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password);
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email2 = email2.getText().toString();
                String txt_password2 = password2.getText().toString();

                if (isEmpty(txt_email2) || TextUtils.isEmpty(txt_password2)) {
                    Toast.makeText(Registering.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (txt_password2.length() < 6) {
                    Toast.makeText(Registering.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(txt_email2, txt_password2);
                }
            }
        });

    }


    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registering.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    Toast.makeText(Registering.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Registering.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void signIn(String email2, String password2) {
        auth.signInWithEmailAndPassword(email2, password2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(Registering.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                            Intent n = new Intent(Registering.this, SignedInActivity.class);
                            startActivity(n);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Registering.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }

    public void Complete(View view) {
        Intent n = new Intent(this, Login.class);
        startActivity(n);
    }
}