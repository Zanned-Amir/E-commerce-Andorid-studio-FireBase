package com.sph.sbh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sph.sbh.R;
import com.sph.sbh.databinding.ActivityProfileBinding;

public class Profile extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        displayUserData();



        binding.Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
        finish();
            }
        });
    }

    private void displayUserData() {
        String name = sharedPreferences.getString("name", "");
        String lastName = sharedPreferences.getString("lastName", "");
        String email = sharedPreferences.getString("email", "");
        String phone = sharedPreferences.getString("phoneNumber", "");
        String birthDate = sharedPreferences.getString("birthDate", "");
        String gender = sharedPreferences.getString("gender", "");
        String state = sharedPreferences.getString("state", "");
        binding.username.setText(name +" "+lastName);
        binding.nameContent.setText(name);
        binding.mailContent.setText(email);
        binding.phoneContent.setText(phone);
        binding.birthdateContent.setText(birthDate);
        binding.genderContent.setText(gender);
        binding.stateContent.setText(state);
        // You can similarly display other user data as needed
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();
        // Clear SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Navigate to LoginActivity
        Toast.makeText(this,"you logout ",Toast.LENGTH_SHORT);
        Intent intent = new Intent(Profile.this, LoginAccount.class);
        startActivity(intent);
        finish();
    }
}
