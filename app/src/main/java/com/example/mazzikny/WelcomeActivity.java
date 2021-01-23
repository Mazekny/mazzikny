package com.example.mazzikny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity {

//    Button addButton, testNotificationButton;
    Gson gson;
    RequestQueue queue;
//    FirebaseUser user;


    // A Java class used for the request's JSON object.
    static class MyRequest {
        String fcmToken;
    }
    // Tag for Log.d/Log.e.
    private static final String TAG = "MAIN";

    // This is a random code used for onActivityResult to know which activity it was. It can be anything.
    private static final int RC_SIGN_IN = 123;
    TextView uProf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
      //  uProf=findViewById(R.id.prof);
       // String prof= getIntent().getStringExtra("gender");
    //    uProf.setText(prof);

        // This starts the FCM service, enabling you to receive notifications.
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);

        // Get user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "User authenticated with " + user.getEmail());

        // Set up messaging token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Failed to obtain FCM token.");
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "FCM token obtained: " + token);

                    sendRegistrationToServer(token);
                }
        );


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(WelcomeActivity.this, home_page.class);
                startActivity(i);

                finish();
            }
        }, 3000);
    }

    // You need to send the token to the server after you get it.
    // This is so your server can associate a device
    // with a specific account.
    private void sendRegistrationToServer(String token) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();

        if (user == null) {
            Log.d(TAG, "No user logged in.");
            return;
        }

        // Get the *account authentication* token: The API is going to require authentication.
        // Without authentication, how will we know which user is matched to this device?
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
                return;
            }
            Log.d(TAG, "Got authentication token.");

            String idToken = task.getResult().getToken();

            StringRequest example = new StringRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:3000/register_notification_token",
                    response -> {
                        // Status code 2XX: Seems good!
                        Log.d(TAG, "Server responded okay.");
                    }, error -> {
                // Failed to connect or status code 4XX/5XX: No good!
                Log.e(TAG, "Server responded with communication error.");
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + idToken);
                    params.put("Content-Type", "application/json");
                    return params;
                }

                @Override
                public byte[] getBody() {
                    MyRequest r = new MyRequest();
                    r.fcmToken = token;

                    String json = gson.toJson(r);
                    return json.getBytes();
                }
            };
            queue.add(example);
        });
    }
}