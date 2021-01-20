package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class sell_item extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    EditText itemName;
    EditText itemPrice;
    EditText itemDescription;
    String name;
    String price;
    String description;
    Button post;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        post = (Button) findViewById((R.id.signup));


        itemName = findViewById(R.id.name);
        itemPrice = findViewById(R.id.price);
        itemDescription = findViewById(R.id.desc);
        user = FirebaseAuth.getInstance().getCurrentUser();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = itemName.getText().toString().trim();
                price = itemPrice.getText().toString().trim();
                description = itemDescription.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    itemName.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(price)) {
                    itemPrice.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    itemDescription.setError("Email is required");
                    return;
                }
                try {
                    addItem();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(sell_item.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(sell_item.this, home_page.class));
                //parse data and send it to api that adds item to store after entering data and pressing post
            }
        });
    }

    public void addItem() throws JSONException {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("price", price);
        obj.put("desc", description);
        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("Connection", "Got token");
            String idToken = task.getResult().getToken();
            JsonObjectRequest getUserDetails = new JsonObjectRequest(Request.Method.POST,
                    checkAppIDURL + "/additem", obj,
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
            };
            queue.add(getUserDetails);
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}