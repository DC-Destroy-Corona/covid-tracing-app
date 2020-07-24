package com.example.covid_tracing_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity_D extends AppCompatActivity {
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_d);

        btnFinish = (Button)findViewById(R.id.buttonFinish);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignupActivity_D.this, LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }
}