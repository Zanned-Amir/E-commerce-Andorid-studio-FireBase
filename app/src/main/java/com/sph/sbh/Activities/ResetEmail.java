package com.sph.sbh.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sph.sbh.databinding.ActivityResetEmailBinding;

public class ResetEmail extends AppCompatActivity {
    ActivityResetEmailBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetEmail.this, LoginAccount.class);
                startActivity(intent);
            }
        });


    }

    public void resetPassword(View view) {
        String email = binding.editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            binding.editTextEmail.setError("Email is required");
            binding.editTextEmail.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetEmail.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetEmail.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
