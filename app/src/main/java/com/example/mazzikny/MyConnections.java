package com.example.mazzikny;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyConnections extends AppCompatActivity {
    private ProfileListAdapter adapter;
    private ArrayList<ProfileCard> exampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connections);

        Button search_button = findViewById(R.id.search_button1);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyConnections.this, SearchConnections.class);
                startActivity(intent);
            }
        });

        exampleList = new ArrayList<>();
        exampleList.add(new ProfileCard(R.drawable.dp1, "Ahmed", "Guitarist", "Professional", "www.facebook.com", "www.twitter.com", "mohamed_elnaggar@aucegypt.edu", "01123481729", "5th District, Cairo", "5 Years"));
        exampleList.add(new ProfileCard(R.drawable.dp2, "Mohamed", "Pianist", "Beginner", "www.facebook.com", "www.twitter.com", "mohamed_elnaggar@aucegypt.edu", "01123481729", "5th District, Cairo", "5 Years"));
        exampleList.add(new ProfileCard(R.drawable.dp3, "Omar", "Drummer", "Expert", "www.facebook.com", "www.twitter.com", "mohamed_elnaggar@aucegypt.edu", "01123481729", "5th District, Cairo", "5 Years"));

        RecyclerView recyclerView = findViewById(R.id.connections_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProfileListAdapter(exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MyConnections.this, userProfile.class);
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
}

