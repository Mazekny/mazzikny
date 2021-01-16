package edu.aucegypt.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
public static final String EXTRA_TEXT="com.example.milestone2.EXTRA_TEXT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton= (Button) findViewById(R.id.login);
        Button signupButton= (Button)findViewById(R.id.signup);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWelcomeActivity();
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();

            }
        });
    }

    public void openSignUpActivity() {
        Intent intent= new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    public void openWelcomeActivity() {

        EditText text = (EditText)findViewById(R.id.username);
        String username= text.getText().toString();
        Intent intent= new Intent(LoginActivity.this, WelcomeActivity.class);
        intent.putExtra(EXTRA_TEXT,username);
        startActivity(intent);
    }


}