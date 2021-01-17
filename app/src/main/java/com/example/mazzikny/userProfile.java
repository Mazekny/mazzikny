package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class userProfile extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    Bitmap bitmap = null;
    TextView facebook;
    TextView twitter;
    ProfileCard profileCard;
    ImageView imageView;
    TextView email;
    TextView phone_number;
    TextView instrument;
    TextView exp;
    TextView address;
    TextView name;
    TextView userProf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent i = getIntent();
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        imageView = (ImageView) findViewById(R.id.userImg);
        facebook = findViewById(R.id.userFacebook);
        twitter = findViewById(R.id.userTwitter);
        email = (TextView) findViewById(R.id.userEmail);
        phone_number = (TextView) findViewById(R.id.userPhone);
        instrument = (TextView) findViewById(R.id.userInstrument);
        exp = (TextView) findViewById(R.id.userExperince);
        address = (TextView) findViewById(R.id.userLoc);
        name = (TextView) findViewById(R.id.userName);
        userProf=findViewById(R.id.userExpert);


        if(i.getExtras() != null) {
            profileCard = (ProfileCard) i.getSerializableExtra("user");
            facebook.setText(profileCard.getFbLink());
            twitter.setText(profileCard.getTwLink());
            imageView.setImageResource(profileCard.getUserImage());
            email.setText(profileCard.getEmail());
            phone_number.setText(profileCard.getPhoneNumber());
            instrument.setText(profileCard.getUserInstrument());
            exp.setText(profileCard.getExp());
            address.setText(profileCard.getAddress());
            name.setText(profileCard.getUserName());
            userProf.setText(profileCard.getUserProf());
        }

        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        facebook.setMovementMethod(LinkMovementMethod.getInstance());


        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(userProfile.this, video_player.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
                i.putExtras(bundle);
                startActivity(i);
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}