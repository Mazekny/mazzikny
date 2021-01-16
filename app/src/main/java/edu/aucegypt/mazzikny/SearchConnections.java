package edu.aucegypt.mazzikny;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchConnections extends AppCompatActivity {
    private ProfileListAdapter adapter;
    private ArrayList<ProfileCard> exampleList;
    private RecyclerView recyclerView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_connections);

        textView = findViewById(R.id.textView3);

        exampleList = new ArrayList<>();
        exampleList.add(new ProfileCard(R.drawable.dp1, "Ahmed", "Guitarist", "Professional"));
        exampleList.add(new ProfileCard(R.drawable.dp2, "Mohamed", "Pianist", "Beginner"));
        exampleList.add(new ProfileCard(R.drawable.dp3, "Omar", "Drummer", "Expert"));
        exampleList.add(new ProfileCard(R.drawable.dp3, "Omar2", "Drummer", "Beginner"));

        recyclerView = findViewById(R.id.connections_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ProfileListAdapter(exampleList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchConnections.this, Profile_temp.class);
                intent.putExtra("Name", exampleList.get(position).getmText1());
                startActivity(intent);
            }
        });

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

}