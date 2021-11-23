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
    EditText entry1, entry4;
    String string, string2;
    private EditText email;
    private EditText password;
    private Button register, signIn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent recString = getIntent();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);

        auth = FirebaseAuth.getInstance();

        //  entry1 = (EditText) findViewById(R.id.entry1); //make them findable by id
        // entry4 = (EditText) findViewById(R.id.entry2);
        username = (TextView) findViewById(R.id.username);
        Register = (Button) findViewById(R.id.Skip);
        Login = (Button) findViewById(R.id.Login);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        //  mGoogleSignInClient = GoogleSignInActivity.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(Login.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Login.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signIn(txt_email, txt_password);
                }
            }
        });

    }

    public void LoginToApp(View v) { //button to login, checks if username and password matches registration
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String string = sharedPref.getString("username", "");
        String string2 = sharedPref.getString("password", "");
        if (username.getText().toString().equals(string) && (password.getText().toString().equals(string2))) {
            Toast.makeText(Login.this, "Successful login", //displays on success
                    Toast.LENGTH_LONG).show();
            Intent n = new Intent(this, SignedInActivity.class);
            startActivity(n);
        } else {
            Toast.makeText(Login.this, "No account name/password found with those details, try again",
                    Toast.LENGTH_LONG).show(); //displays if failed and doesn't match registration
        }
    }

    public void Register(View v) { //switch to registration activity
        Intent n = new Intent(this, Registering.class);
        startActivity(n);
    }


    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(Login.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                            Intent n = new Intent(Login.this, SignedInActivity.class);
                            startActivity(n);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }
}