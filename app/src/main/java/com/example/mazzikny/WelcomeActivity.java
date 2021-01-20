package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
TextView uProf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      //  uProf=findViewById(R.id.prof);
       // String prof= getIntent().getStringExtra("gender");
    //    uProf.setText(prof);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(WelcomeActivity.this, home_page.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }
}