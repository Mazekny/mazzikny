package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONObject;
//import org.json.JSONObject;
import com.google.gson.JsonObject;


import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseUser user;
    Button signUp ;
    Button clickHere ;
    //Spinner countrySpinner;
    EditText location ;
    EditText name  ;
    EditText email ;
    EditText password ;
    EditText number ;
    EditText url ;
    EditText facebook ;
    EditText twitter ;
    EditText instrument ;
    EditText experienceYears;
    String uName ;
    String uEmail ;
    String uPassword ;
    String uFacebook;
    String uTwitter;
    String uInstrument;
    String uNumber ;
    String uUrl ;
    String uLocation ;
    String uExperienceYears;
    String uProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUp = (Button) findViewById(R.id.signup);
        clickHere = (Button) findViewById(R.id.clickhere);
        //Spinner countrySpinner= findViewById(R.id.countrySpinner);
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        number = findViewById(R.id.PhoneNumber);
        url = findViewById(R.id.videoURL);
        experienceYears = findViewById(R.id.ExperienceYears);
        facebook=findViewById(R.id.facebook);
        twitter=findViewById(R.id.twitter);
        instrument=findViewById(R.id.Instrument);


        radioGroup = findViewById(R.id.level);


        // countrySpinner.setOnItemSelectedListener(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uFacebook=facebook.getText().toString().trim(); // uFacebook has the link of Facebook account
                uTwitter=twitter.getText().toString().trim(); // uTwitter has the link of Twitter account
                uInstrument=instrument.getText().toString().trim(); // uInstrument has the name of the Instrument the user plays
                uName = name.getText().toString();  //uName has the name
                uEmail = email.getText().toString().trim(); // uEmail has the email
                uPassword = password.getText().toString().trim(); // uPassword has the Password
                uNumber = number.getText().toString().trim(); // uNumber has the Number
                uUrl = url.getText().toString().trim(); //uUrl has the url
                uLocation = location.getText().toString().trim();

                user = FirebaseAuth.getInstance().getCurrentUser();
                radioButton = findViewById(R.id.beginner);
                uExperienceYears = experienceYears.getText().toString().trim(); // uExperienceYears has the experience years
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                uProf = radioButton.getText().toString(); // uProf has the professionality level

                if (TextUtils.isEmpty(uName)) {
                    name.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(uLocation)) {
                    location.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(uEmail)) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(uPassword)) {
                    password.setError("Password is required");
                    return;
                }
                if (uPassword.length() < 6) {
                    password.setError("Password must be atleast 6 characters");
                    return;
                }
                if (TextUtils.isEmpty(uNumber)) {
                    number.setError("Phone number is required");
                    return;
                }
                if (TextUtils.isEmpty(uExperienceYears)) {
                    experienceYears.setError("Number of experience years is required");
                    return;
                }
                if (TextUtils.isEmpty(uUrl)) {
                    url.setError("Video URL is required");
                    return;
                }
                if(TextUtils.isEmpty(uInstrument)){
                    instrument.setError("Instrument is required");
                    return;
                }
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                // intent.putExtra("gender",uProf);
                try {
                    getUserDetails();
                    getUserInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }

        });


        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();

            }
        });
    }
    public void getUserInfo() throws JSONException {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        JSONObject obj = new JSONObject();
        obj.put("email", uEmail);
        obj.put("facebook", uFacebook);
        obj.put("twitter", uTwitter);
        obj.put("phone_number", uNumber);
        obj.put("instrument", uInstrument);
        obj.put("experience", uExperienceYears);
        obj.put("address", uLocation);
        obj.put("name", uName);
        obj.put("prof", uProf);
        obj.put("rating", "0");
        obj.put("token", "");
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
            JsonObjectRequest getUserDetails = new JsonObjectRequest(Request.Method.POST,
                    checkAppIDURL + "/setUserDetails", obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
//                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token " + idToken);
                    params.put("Content-Type", "application/json");
                    return params;
                }


//                @Override
//                public byte[] getBody()
//                    // To set the POST request body, we need to override the "getBody" function. String.getBytes() usually works here.
//                    // You should use it in conjunction with the Google Gson library to easily convert classes to JSON.
//                    /*
//                     * {
//                     *   "email": "ahmed@asjdb.com",
//                     *   "password": "123123"
//                     * }
//                     * */
//                {
////                    JsonObject obj = new JsonObject();
////                    JSONObject obj = new JSONObject();
////                    JSONObject object= new JSONObject();
//
////                        obj.put("email", uEmail);
////                        obj.put("password",uPassword);
//
////                    Gson gson = new Gson();
////                    String json = gson.toJson(obj);
////                    return json.getBytes();
//                }
            };
            queue.add(getUserDetails);
        });
    }
    public void getUserDetails() throws JSONException {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        JSONObject obj = new JSONObject();
//                    JSONObject obj = new JSONObject();
//                    JSONObject object= new JSONObject();
        obj.put("email", uEmail);
        obj.put("password", uPassword);
//                        obj.put("email", uEmail);
//                        obj.put("password",uPassword);

        Gson gson = new Gson();
        String json = gson.toJson(obj);
//        return json.getBytes();
//        user.getIdToken(true).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e(TAG, "Could not get authentication token.");
////                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Log.d("Connection", "Got token");
//            String idToken = task.getResult().getToken();
        //                        try {
        //                            getUserDetailsJSON(response);
        //                        } catch (JSONException e) {
        //                            e.printStackTrace();
        //                        }
        JsonObjectRequest getUserDetails = new JsonObjectRequest(Request.Method.POST,
                checkAppIDURL + "/sign_up", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
//                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }


//                @Override
//                public byte[] getBody()
//                // To set the POST request body, we need to override the "getBody" function. String.getBytes() usually works here.
//                // You should use it in conjunction with the Google Gson library to easily convert classes to JSON.
//                        /*
//                        * {
//                        *   "email": "ahmed@asjdb.com",
//                        *   "password": "123123"
//                        * }
//                        * */
//                {
//
//                }
        };
        queue.add(getUserDetails);
//        });
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    public void openMainActivity() {
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(intent);
    }


}
/*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}*/