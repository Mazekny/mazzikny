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
import java.util.concurrent.ExecutionException;

public class SearchConnections extends AppCompatActivity {
    private static final String TAG = "AuthenticatedAddition";
    private ProfileListAdapter adapter;
    private ArrayList<ProfileCard> exampleList;
    private RecyclerView recyclerView;
    private TextView textView;
    FirebaseUser user;

    private final String apiURL = "http://10.0.2.2:3000/getAllUsers";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_connections);
        user = FirebaseAuth.getInstance().getCurrentUser();

        textView = findViewById(R.id.textView3);

//        new getAllUsers().execute(apiURL);
        try {
            exampleList = new getAllUsers().execute(apiURL).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.connections_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProfileListAdapter(exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchConnections.this, userProfile.class);
                intent.putExtra("user", exampleList.get(position));
                startActivity(intent);
            }
        });
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



    private class getAllUsers extends AsyncTask<String, Void, ArrayList<ProfileCard>>
    {
        HttpURLConnection http;
        JSONArray apiIn;
        JSONObject msg;
        StringBuffer consoleOut = new StringBuffer();
        URL url;
        ArrayList<ProfileCard> list = new ArrayList<>();

        @Override
        protected ArrayList<ProfileCard> doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.connect();

                if (http.getResponseCode() == 200)
                {
                    InputStream stream = new BufferedInputStream(http.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder apiStream = new StringBuilder();
                    String inputString;

                    while ((inputString = bufferedReader.readLine()) != null)
                    {
                        apiStream.append(inputString);
                    }
                    apiIn = new JSONArray(apiStream.toString());
                    if(apiIn.length() > 0)
                    {
                        for (int i = 0; i < apiIn.length(); i++)
                        {
                            msg = apiIn.getJSONObject(i);
                            list.add(new ProfileCard(msg.getString("id"), R.drawable.dp1, msg.getString("name"), msg.getString("instrument"), msg.getString("prof"), msg.getString("facebook"), msg.getString("twitter"), msg.getString("email"), msg.getString("phone_number"), msg.getString("address"), msg.getString("exp"), msg.getString("rating")));
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}