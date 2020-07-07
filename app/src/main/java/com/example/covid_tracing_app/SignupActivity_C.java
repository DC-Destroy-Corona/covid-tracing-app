package com.example.covid_tracing_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

public class SignupActivity_C extends AppCompatActivity {
    EditText editPassword;
    EditText editConfirm;
    EditText editName;
    EditText editBirth;
    EditText editPhone;
    Button btnNext;

    private boolean isPasswordEnable = false;
    private boolean isConfirmEnable = false;
    private boolean isNameEnable = false;
    private boolean isBirthEnable = false;
    private boolean isPhoneEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_c);

        editPassword = (EditText)findViewById(R.id.editTextPassword);
        editConfirm = (EditText)findViewById(R.id.editTextConfirm);
        editName = (EditText)findViewById(R.id.editTextName);
        editBirth = (EditText)findViewById(R.id.editTextBirth);
        editPhone = (EditText)findViewById(R.id.editTextPhone);
        btnNext = (Button)findViewById(R.id.buttonNext);
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                if(isPasswordEnable&&isConfirmEnable&&isNameEnable&&isBirthEnable&&isPhoneEnable){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else{
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        editConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=8){
                    isConfirmEnable = true;
                }
                else{
                    isConfirmEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isPasswordEnable&&isConfirmEnable&&isNameEnable&&isBirthEnable&&isPhoneEnable){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else{
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=2){
                    isNameEnable = true;
                }
                else{
                    isNameEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isPasswordEnable&&isConfirmEnable&&isNameEnable&&isBirthEnable&&isPhoneEnable){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else{
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        editBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=8){
                    isBirthEnable = true;
                }
                else{
                    isBirthEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isPasswordEnable&&isConfirmEnable&&isNameEnable&&isBirthEnable&&isPhoneEnable){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else{
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=10){
                    isPhoneEnable = true;
                }
                else{
                    isPhoneEnable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isPasswordEnable&&isConfirmEnable&&isNameEnable&&isBirthEnable&&isPhoneEnable){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else{
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_C.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity_C.this, SignupActivity_D.class);
                startActivity(intent);

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        });
    }
}