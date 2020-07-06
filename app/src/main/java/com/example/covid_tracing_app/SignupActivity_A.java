package com.example.covid_tracing_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SignupActivity_A extends AppCompatActivity {
    CheckBox checkAgree;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_a);

        checkAgree = (CheckBox)findViewById(R.id.checkBoxAgree);
        btnNext = (Button)findViewById(R.id.buttonNext);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_A.this, R.drawable.btnsignup));
                    btnNext.setEnabled(true);
                }
                else {
                    btnNext.setBackground(ContextCompat.getDrawable(SignupActivity_A.this, R.drawable.btnnext));
                    btnNext.setEnabled(false);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity_A.this, SignupActivity_B.class);
                startActivity(intent);

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                finish();
            }
        });
    }
}