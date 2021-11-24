package com.example.androidaccidentapp;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button Register, Login; //set up to use later
    TextView username;
    private EditText email;
    private EditText password;
    private Button register, signIn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);

        auth = FirebaseAuth.getInstance();

        username = (TextView) findViewById(R.id.username);
        Register = (Button) findViewById(R.id.Skip);
        Login = (Button) findViewById(R.id.Login);


    }

    public void Register(View v) { //switch to registration activity
        Intent n = new Intent(this, Registering.class);
        startActivity(n);
    }

    private void updateUI(FirebaseUser user) {

    }
}