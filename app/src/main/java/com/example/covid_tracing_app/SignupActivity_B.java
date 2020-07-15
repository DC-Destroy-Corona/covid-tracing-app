package com.example.covid_tracing_app;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignupActivity_B extends AppCompatActivity {
    EditText editEmail;
    EditText editCode;
    Button btnGetCode;
    Button btnCheckCode;
    Button btnNext;
    TextView textNoCode;

    private String url = "http://203.250.32.29:80";
    //private String url = "http://1.251.103.64:8888";
    //private String url = "http://180.189.121.112:63000";

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
                    String email = editEmail.getText().toString().trim();

                    /* DB 대조 */
                    JSONObject values = new JSONObject();
                    values.put("email", email);

                    NetworkTask networkTask = new NetworkTask(url+"/user/sign-up/email", values, "POST");
                    networkTask.execute();//서버로 인증코드 요청 후 반환
                }catch (JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
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
                    String email = editEmail.getText().toString().trim();
                    String code = editCode.getText().toString().trim();

                    /* DB 대조 */
                    JSONObject values = new JSONObject();
                    values.put("email", email);
                    values.put("authKey", code);

                    NetworkTask networkTask = new NetworkTask(url+"/user/sign-up/check-email", values, "POST");
                    networkTask.execute();//서버로 인증코드 요청 후 반환
                    //서버와 입력한 인증코드 비교
                }catch (JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
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

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        JSONObject values;
        String method;

        NetworkTask(String url, JSONObject values, String method){
            this.url = url;
            this.values = values;
            this.method = method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress bar를 보여주는 등등의 행위
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values, method);
            return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
        }

        @Override
        protected void onPostExecute(String result) {
            // 통신이 완료되면 호출됩니다.
            // 결과에 따른 UI 수정 등은 여기서 합니다.
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}