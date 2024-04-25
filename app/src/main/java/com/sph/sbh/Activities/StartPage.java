package com.sph.sbh.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.sph.sbh.R;

public class StartPage extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(StartPage.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.start_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
    public void signup(View view){
        startActivity(new Intent(StartPage.this, RegisterAccount.class));


    }
    public void login(View view){
        startActivity(new Intent(StartPage.this, LoginAccount.class));
    }

}
