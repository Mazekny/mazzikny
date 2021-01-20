package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class userProfile extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";

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
    TextView prof;
    Button connectButton;
    FirebaseUser user;
    private String userID;
    String status = " ";

    private final String apiURL = "http://10.0.2.2:3000/setConnection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent i = getIntent();
        FloatingActionButton fab2 = findViewById(R.id.fab2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        imageView = (ImageView) findViewById(R.id.userImg);
        facebook = findViewById(R.id.userFacebook);
        twitter = findViewById(R.id.userTwitter);
        email = (TextView) findViewById(R.id.userEmail);
        phone_number = (TextView) findViewById(R.id.userPhone);
        instrument = (TextView) findViewById(R.id.userInstrument);
        exp = (TextView) findViewById(R.id.userExperince);
        address = (TextView) findViewById(R.id.userLoc);
        name = (TextView) findViewById(R.id.userName);
        prof = (TextView) findViewById(R.id.userExpert);
        connectButton = (Button) findViewById(R.id.connect_button);

        if (i.getExtras() != null) {
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
            prof.setText(profileCard.getUserProf());
            userID = profileCard.getId();
        }

        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        facebook.setMovementMethod(LinkMovementMethod.getInstance());
        isRequested(userID);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

////        Log.e("666666666", status);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(userProfile.this, video_player.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendRequest(String userID) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
            StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                    checkAppIDURL + "/setConnection",
                    response -> {
                        System.out.println(response);
//                        try {
//                            getUserDetailsJSON(response);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }, error -> {
                System.out.println(error);
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + idToken);
//                    params.put("pic", imageToString(bitmap));
                    return params;
                }
                @Override
                public byte[] getBody()
                // To set the POST request body, we need to override the "getBody" function. String.getBytes() usually works here.
                // You should use it in conjunction with the Google Gson library to easily convert classes to JSON.
                {
                    String body = "uid=" + userID;
                    return body.getBytes();
                }
            };
            queue.add(getUserDetails);
        });
    }

    public void isRequested(String userID) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        String s;
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
        StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                checkAppIDURL + "/isRequested",
                response -> {
                    System.out.println(response);
                        try {
                            getUserDetailsJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }, error -> {
            System.out.println(error);
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + idToken);
//                    params.put("pic", imageToString(bitmap));
                return params;
            }
            @Override
            public byte[] getBody()
            // To set the POST request body, we need to override the "getBody" function. String.getBytes() usually works here.
            // You should use it in conjunction with the Google Gson library to easily convert classes to JSON.
            {
                String body = "uid2=" + userID;
                return body.getBytes();
            }
        };
        queue.add(getUserDetails);
        });
    }

    public void getUserDetailsJSON(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);
        try {
            JSONArray userArray = obj.getJSONArray("result");
            JSONObject userDetail = userArray.getJSONObject(0);
            status = userDetail.getString("status");
            setStatus(status);
            Log.e("statussss", status);

        } catch (JSONException e) {
            setStatus(status);
            e.printStackTrace();
        }
    }

    public void setStatus(String s)
    {
        if (s.equals("0")) {
            connectButton.setText("Pending");
            connectButton.setClickable(false);
        }
        else {
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectButton.setText("Pending");
                    connectButton.setClickable(false);
                    sendRequest(userID);
                }
            });
        }
    }
}