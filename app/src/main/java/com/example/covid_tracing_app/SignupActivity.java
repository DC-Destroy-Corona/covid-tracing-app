package com.example.covid_tracing_app;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class SignupActivity extends AppCompatActivity {
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnNext = (Button)findViewById(R.id.buttonNext);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this,SignupActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });
    }
}