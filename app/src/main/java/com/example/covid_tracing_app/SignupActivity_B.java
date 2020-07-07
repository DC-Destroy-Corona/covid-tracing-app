package com.example.covid_tracing_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class SignupActivity_B extends AppCompatActivity {
    EditText editEmail;
    EditText editCode;
    Button btnGetCode;
    Button btnCheckCode;
    Button btnNext;
    TextView textNoCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_b);

        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editCode = (EditText)findViewById(R.id.editTextCode);
        btnGetCode = (Button)findViewById(R.id.buttonGetCode);
        btnCheckCode = (Button)findViewById(R.id.buttonCheckCode);
        btnNext = (Button)findViewById(R.id.buttonNext);
        textNoCode = (TextView)findViewById(R.id.textViewNoCode);
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>=10){
                    btnGetCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogin));
                    btnGetCode.setEnabled(true);
                }
                else{
                    btnGetCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogindisable));
                    btnGetCode.setEnabled(false);
                }
            }
        });

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //서버로 인증코드 요청 후 반환
                }catch (Exception e){
                    e.printStackTrace();
                    throw e;
                    //실패 시
                }finally {
                    btnGetCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogindisable));
                    btnGetCode.setEnabled(false);
                    btnCheckCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogin));
                    btnCheckCode.setEnabled(true);
                    textNoCode.setTextColor(ContextCompat.getColor(SignupActivity_B.this,R.color.colorTextMain));
                    textNoCode.setEnabled(true);
                }
            }
        });

        btnCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //서버와 입력한 인증코드 비교
                }catch (Exception e){
                    e.printStackTrace();
                    throw e;
                    //실패 시
                }finally {
                    editEmail.setBackground(ContextCompat.getDrawable(SignupActivity_B.this,R.drawable.editdisable));
                    editEmail.setTextColor(ContextCompat.getColor(SignupActivity_B.this,R.color.colorTextHidden));
                    editEmail.setEnabled(false);
                    editCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this,R.drawable.editdisable));
                    editCode.setTextColor(ContextCompat.getColor(SignupActivity_B.this,R.color.colorTextHidden));
                    editCode.setEnabled(false);
                    btnGetCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogindisable));
                    btnGetCode.setEnabled(false);
                    btnCheckCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogindisable));
                    btnCheckCode.setEnabled(false);
                    textNoCode.setTextColor(ContextCompat.getColor(SignupActivity_B.this,R.color.colorTextHidden));
                    textNoCode.setEnabled(false);
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
            }
        });

        textNoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGetCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogin));
                btnGetCode.setEnabled(true);
                btnCheckCode.setBackground(ContextCompat.getDrawable(SignupActivity_B.this, R.drawable.btnlogindisable));
                btnCheckCode.setEnabled(false);
                textNoCode.setTextColor(ContextCompat.getColor(SignupActivity_B.this,R.color.colorTextHidden));
                textNoCode.setEnabled(false);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity_B.this, SignupActivity_C.class);
                startActivity(intent);

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        });
    }
}