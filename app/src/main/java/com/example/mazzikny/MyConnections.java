package com.example.mazzikny;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyConnections extends AppCompatActivity {
    private ProfileListAdapter adapter;
    private static final String TAG = "AuthenticatedAddition";
//    private ArrayList<ProfileCard> exampleList;
    private ArrayList<ProfileCard> list = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseUser user;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connections);
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.connections_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        Button search_button = findViewById(R.id.search_button1);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyConnections.this, SearchConnections.class);
                startActivity(intent);
            }
        });

        getPending();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    public void getPending() {
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
                    checkAppIDURL + "/getAllConnection",
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
            JSONObject msg;// = userArray.getJSONObject(0);
            int sentFlag;
            for (int i = 0; i < userArray.length(); i++)
            {
                msg = userArray.getJSONObject(i);
                list.add(new ProfileCard(msg.getString("id"), R.drawable.dp1, msg.getString("name"), msg.getString("instrument"), msg.getString("prof"), msg.getString("facebook"), msg.getString("twitter"), msg.getString("email"), msg.getString("phone_number"), msg.getString("address"), msg.getString("exp"), msg.getString("rating")));
            }
            adapter = new ProfileListAdapter(list);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MyConnections.this, userProfile.class);
                    intent.putExtra("user", list.get(position));
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}