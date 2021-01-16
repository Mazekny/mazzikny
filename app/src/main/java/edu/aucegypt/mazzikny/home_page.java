package edu.aucegypt.mazzikny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        homeAdapter myAdapter;
        final ArrayList<Item> homeList=new ArrayList<Item>();

        GridView grid = (GridView) findViewById(R.id.homeGrid);
        homeList.add(new Item("Profile",R.drawable.profile2));
        homeList.add(new Item("Search",R.drawable.search2));
        homeList.add(new Item("Store",R.drawable.selectinstrument));
        homeList.add(new Item("Studio",R.drawable.studio2));
        homeList.add(new Item("E-Learning",R.drawable.learn2));
        homeList.add(new Item("Store",R.drawable.store));
        homeList.add(new Item("Settings",R.drawable.settings2));
        homeList.add(new Item("About",R.drawable.about));

        myAdapter = new homeAdapter(this, R.layout.home_view_items, homeList);
        grid.setAdapter(myAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = homeList.get(position);
                if(item.getbuttonName().equals("Profile")) {
                    Intent intent = new Intent(getApplicationContext(), profile.class);
                    startActivity(intent);
                }
                else if(item.getbuttonName().equals("Studio")) {
                    Intent intent = new Intent(getApplicationContext(), studio.class);
                    startActivity(intent);
                }

            }
        });
    }
}