package edu.aucegypt.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent intent=getIntent();
        String username=intent.getStringExtra(LoginActivity.EXTRA_TEXT);
        TextView textview= (TextView)findViewById(R.id.usernametext);
        textview.setText(username);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(WelcomeActivity.this, HomePage.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }
}