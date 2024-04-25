package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sph.sbh.R;

public class LoginAccount extends AppCompatActivity {
    private FirebaseAuth auth;
    public EditText editTextEmail ,passwordEditText;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        editTextEmail =findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        auth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
    }

    public void signup(View view){
        startActivity(new Intent(LoginAccount.this, RegisterAccount.class));
    }

    public void login(View view) {
        String password = passwordEditText.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginAccount.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(LoginAccount.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Retrieve user data from Firebase
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // User data found, retrieve it
                                        String email = snapshot.child("email").getValue(String.class);
                                        String name = snapshot.child("name").getValue(String.class);
                                        String lastName = snapshot.child("lastName").getValue(String.class);
                                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                                        String selectedState = snapshot.child("state").getValue(String.class);
                                        String birthDate = snapshot.child("birthDate").getValue(String.class);
                                        String gender = snapshot.child("gender").getValue(String.class);


                                        // Store user data in SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email);
                                        editor.putString("name", name);
                                        editor.putString("lastName", lastName);
                                        editor.putString("phoneNumber", phoneNumber);
                                        editor.putString("state", selectedState);
                                        editor.putString("birthDate", birthDate);
                                        editor.putString("gender", gender);
                                        editor.apply();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle database error
                                    Toast.makeText(LoginAccount.this, "Error retrieving user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Navigate to MainActivity
                            startActivity(new Intent(LoginAccount.this, MainActivity.class));
                        } else {
                            // Login failed
                            Toast.makeText(LoginAccount.this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}


