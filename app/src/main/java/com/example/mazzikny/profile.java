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

public class profile extends AppCompatActivity {

    public static final int GET_FROM_GALLERY = 3;
    Bitmap bitmap = null;
    TextView facebook;
    TextView twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        ImageButton imageButton = findViewById(R.id.imageButton);
        FloatingActionButton fab1 = findViewById(R.id.addPic);
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        final ImageView imageView = (ImageView) findViewById(R.id.userImg);
        facebook = findViewById(R.id.userFacebook);
        facebook.setMovementMethod(LinkMovementMethod.getInstance());
        twitter = findViewById(R.id.userTwitter);
        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(
                        new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
                        ),
                        GET_FROM_GALLERY
                );
                imageView.setImageBitmap(bitmap);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profile.this, video_player.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
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