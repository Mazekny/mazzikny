package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SearchConnections extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    private ProfileListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textView;
    FirebaseUser user;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<ProfileCard> list = new ArrayList<>();

    private final String apiURL = "http://10.0.2.2:3000/getAllUsers";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_connections);
        user = FirebaseAuth.getInstance().getCurrentUser();

        textView = findViewById(R.id.textView3);

        recyclerView = findViewById(R.id.connections_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        getPending();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.INVISIBLE);
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);

                return true;
            }
        });

        return true;
    }


    public void getPending() {
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
                    checkAppIDURL + "/getAllUsers",
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
                list.add(new ProfileCard(msg.getString("id"), R.drawable.dp1, msg.getString("name"), msg.getString("instrument"), msg.getString("prof"), msg.getString("facebook"), msg.getString("twitter"), msg.getString("email"), msg.getString("phone_number"), msg.getString("address"), msg.getString("exp"), msg.getString("likes"), msg.getString("dislikes")));
            }
            adapter = new ProfileListAdapter(list);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(SearchConnections.this, userProfile.class);
                    intent.putExtra("user", list.get(position));
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}