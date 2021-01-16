package edu.aucegypt.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Profile_temp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_temp);

        TextView textView = findViewById(R.id.textView4);
        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("Name");
        textView.setText(name + "'s Profile");
    }
}