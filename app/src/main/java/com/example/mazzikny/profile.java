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
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import java.util.Base64;

public class profile extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";

    public static final int GET_FROM_GALLERY = 3;
    Bitmap bitmap = null;
    TextView facebook;
    TextView twitter;
    FirebaseUser user;
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
        setContentView(R.layout.activity_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FloatingActionButton fab1 = findViewById(R.id.addPic);
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

        getUserDetails();


        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        facebook.setMovementMethod(LinkMovementMethod.getInstance());

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
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profile.this, video_player.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");
                i.putExtras(bundle);
                startActivity(i);
            }
        });


    }

    public void getUserDetails () {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
            StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                    checkAppIDURL + "/getMyDetails",
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
                    return params;
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
            facebook.setText(userDetail.getString("facebook"));
            twitter.setText(userDetail.getString("twitter"));
            email.setText(userDetail.getString("Email"));
            phone_number.setText(userDetail.getString("phone_number"));
            instrument.setText(userDetail.getString("instrument"));
            exp.setText(userDetail.getString("exp"));
            address.setText(userDetail.getString("address"));
            name.setText(userDetail.getString("name"));
            userProf.setText(userDetail.getString("prof"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setImageUser () {
//        final RequestQueue queue = Volley.newRequestQueue(this);
//        final String checkAppIDURL = "http://10.0.2.2:3000";
//        StringRequest setUserImage = new StringRequest(Request.Method.POST, //Make sure to use .GET if it's  a get request.
//                checkAppIDURL + "/uploadphoto",
//                new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                    }
//
//                },
//                new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
//                    }
//                }
//        ) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
////                params.put("name", NAME.getText().toString().trim());
////                params.put("image", imageToString(bitmap));
//                params.put("enctype", "multipart/form-data");
//                params.put("type", "file");
//                params.put("name", "picture");
//                params.put("accept", "image/*");
//                params.put("type", "submit");
//                params.put("path", imageToString(bitmap));
//                return params;
//            }
//            // POST REQUEST ONLY
////            @Override
////            public byte[] getBody()
////            {
////                String body = imageToString(bitmap);
////                return body.getBytes();
////            }
//        };
//        queue.add(setUserImage);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
            StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                    checkAppIDURL + "/getMyDetails",
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
                    params.put("pic", imageToString(bitmap));
                    return params;
                }
            };

            queue.add(getUserDetails);
        });
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
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
            ImageView imageView = (ImageView) findViewById(R.id.userImg);
            imageView.setImageBitmap(bitmap);
            setImageUser();
        }
    }

}