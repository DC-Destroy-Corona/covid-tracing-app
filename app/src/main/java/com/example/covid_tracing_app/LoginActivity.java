package com.example.covid_tracing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    Button btnSignup;
    private boolean isEmailEnable = false;
    private boolean isPasswordEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editPassword = (EditText)findViewById(R.id.editTextPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnSignup = (Button)findViewById(R.id.buttonSignup);
    }

    @Override
    protected void onStart() {
        super.onStart();

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=10){
                    isEmailEnable = true;
                }
                else{
                    isEmailEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmailEnable&&isPasswordEnable){
                    btnLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.btnlogin));
                    btnLogin.setEnabled(true);
                }
                else{
                    btnLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.btnlogindisable));
                    btnLogin.setEnabled(false);
                }
            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=8){
                    isPasswordEnable = true;
                }
                else{
                    isPasswordEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmailEnable&&isPasswordEnable){
                    btnLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.btnlogin));
                    btnLogin.setEnabled(true);
                }
                else{
                    btnLogin.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.btnlogindisable));
                    btnLogin.setEnabled(false);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.getText();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity_A.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}