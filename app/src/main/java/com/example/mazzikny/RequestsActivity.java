package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.concurrent.ExecutionException;

public class RequestsActivity extends AppCompatActivity {
ListView listview;
ArrayList<ProfileCard> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        listview = (ListView) findViewById(R.id.request_list);
        getPending();
    }

    public void getPending() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String checkAppIDURL = "http://10.0.2.2:3000";
        String s;
//        user.getIdToken(true).addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.e(TAG, "Could not get authentication token.");
//                Toast.makeText(profile.this, "Could not authenticate. Try again later.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Log.d("Connection", "Got token");
//            String idToken = task.getResult().getToken();
        StringRequest getUserDetails = new StringRequest(Request.Method.POST,
                checkAppIDURL + "/getPending",
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
        );
        queue.add(getUserDetails);
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
                if (msg.getString("uid1").equals(msg.getString("id")))
                {
                    list.get(i).setSentFlag(0);
                }
                else
                {
                    list.get(i).setSentFlag(1);
                }
            }
            RequestsAdapter adapter = new RequestsAdapter(this, list);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

}