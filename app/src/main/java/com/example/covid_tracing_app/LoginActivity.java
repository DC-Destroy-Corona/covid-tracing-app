package com.example.covid_tracing_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.covid_tracing_app.BeaconService.serviceIntent;

public class LoginActivity extends AppCompatActivity {
    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    Button btnSignup;

    Intent foregroundServiceIntent;

    private boolean isEmailEnable = false;
    private boolean isPasswordEnable = false;

    //private String url = "http://203.250.32.29:80";
    //private String url = "http://1.251.103.64:8888";
    private String url = "http://180.189.121.112:63000";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editPassword = (EditText)findViewById(R.id.editTextPassword);
        btnLogin = (Button)findViewById(R.id.buttonLogin);
        btnSignup = (Button)findViewById(R.id.buttonSignup);


        if (BeaconService.serviceIntent==null) {
            foregroundServiceIntent = new Intent(this, BeaconService.class);
            startService(foregroundServiceIntent);
        } else {
            foregroundServiceIntent = BeaconService.serviceIntent;//getInstance().getApplication();
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }


        foregroundServiceIntent = new Intent(LoginActivity.this,BeaconService.class);

        String pass = "";
        pass = PreferenceManager.getString(this,"uid");
        String token = "";
        token = PreferenceManager.getString(this,"token");
        if(!(pass.equals("")||token.equals(""))){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
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
                String id = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString();

                try {
                    /* DB 대조 */
                    JSONObject values = new JSONObject();
                    values.put("email", id);
                    values.put("password", password);

                    NetworkTask networkTask = new NetworkTask(url+"/user/login", values, "POST");
                    networkTask.execute();//서버로 인증코드 요청 후 반환
                } catch (JSONException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {

                }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (foregroundServiceIntent!=null) {
            stopService(foregroundServiceIntent);
            foregroundServiceIntent = null;
        }

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
            if(result.contains("201")){
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }
        }
    }

}