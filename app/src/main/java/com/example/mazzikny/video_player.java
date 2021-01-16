package com.example.mazzikny;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class video_player extends AppCompatActivity {

    private VideoView videoView;
    String TAG = "VideoPlayer";
    String url;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        configureVideoView();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void configureVideoView() {
        videoView = findViewById(R.id.videoView1);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String title = bundle.getString("title");
        videoView.setVideoPath(title);


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                Log.i(TAG, "Duration = " + videoView.getDuration());
            }
        });
        videoView.start();
    }
}