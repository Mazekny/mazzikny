package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class interested_users extends AppCompatActivity {

    private interested_adapter adapter;
    private ArrayList<ProfileCard> list = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseUser user;
    RecyclerView.LayoutManager layoutManager;

    private final String apiURL = "http://10.0.2.2:3000/getAllUsers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_users);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.connections_list);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = "http://10.0.2.2:3000/check_interested?seller="+user.getUid();
        JsonArrayRequest getinterestedusers = new JsonArrayRequest(Request.Method.GET, URL, null,
        response -> {
                        Log.e("Rest Response", response.toString());
                        for (int i=0; i < response.length(); i++)
                        {
                            Log.e("oops", "oh yeah");
                            try {
                                JSONObject oneObject = response.getJSONObject(i);
                                String buyer=oneObject.getString("buyer");
                                String item=oneObject.getString("item_name");

                                RequestQueue queue1 = Volley.newRequestQueue(this);

                                String url = "http://10.0.2.2:3000/getUser?id="+buyer;

                                JsonArrayRequest interesteduser=new JsonArrayRequest(Request.Method.GET,url,null,
                                        response1 -> {
                                            Log.e("Rest Response", response1.toString());
                                            for (int j=0; j < response1.length(); j++)
                                            {
                                                Log.e("oops", "oh yeah");
                                                try{
                                                    JSONObject oneObject1 = response1.getJSONObject(j);
                                                    list.add(new ProfileCard(oneObject1.getString("id"), R.drawable.dp1, oneObject1.getString("name"), oneObject1.getString("instrument"), oneObject1.getString("prof"), oneObject1.getString("facebook"), oneObject1.getString("twitter"), oneObject1.getString("email"), oneObject1.getString("phone_number"), oneObject1.getString("address"), oneObject1.getString("exp"), oneObject1.getString("likes"), oneObject1.getString("dislikes")));
                                                    adapter = new interested_adapter(list, item);
                                                    recyclerView.setLayoutManager(layoutManager);
                                                    recyclerView.setAdapter(adapter);

                                                    adapter.setOnItemClickListener(new interested_adapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(int position) {
                                                            Intent intent = new Intent(interested_users.this, userProfile.class);
                                                            intent.putExtra("user", list.get(position));
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }catch (JSONException e) {
                                                    Log.e("oops", e.toString());
                                                }
                                            }
                                        },
                                        error -> {
                                            Log.e("Rest Response", error.toString());
                                        });
                                queue1.add(interesteduser);
                            } catch (JSONException e) {
                                Log.e("oops", e.toString());
                            }
                        }
                    },
        error -> {
                    Log.e("Rest Response", error.toString());
                 });

            queue.add(getinterestedusers);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }



}