package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class studio extends AppCompatActivity {

    Location loc;
    double lat;
    double lon;
    String checkAppIDURL;
    studioAdapter myAdapter;
    ArrayList<Item> names = new ArrayList<Item>();
    ArrayList<Item> stat = new ArrayList<Item>();
    ArrayList<Item> rating = new ArrayList<Item>();
    ArrayList<JSONObject> studio_loc = new ArrayList<JSONObject>();
    ArrayList<String> place_id = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);
//        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.studioView);

        final RequestQueue queue = Volley.newRequestQueue(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
        }else {
                loc = locationManager.getLastKnownLocation(locationManager.getProviders(true).get(0));
            }

            assert loc != null;
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            checkAppIDURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ lat + "," + lon + "&radius=5000&keyword=music&key=AIzaSyCOrHHgxzOH0HEiNsXXEgxW9mJlx6mmilw";
            StringRequest getPlace = new StringRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                checkAppIDURL,
                new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Connected");
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray result = obj.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject detail = result.getJSONObject(i);
                                names.add(new Item(detail.getString("name"),R.id.studioName));
                                stat.add(new Item(detail.getString("business_status"),R.id.studioStat));
                                rating.add(new Item(detail.getString("rating"),R.id.studioStat));
                                studio_loc.add(detail.getJSONObject("geometry").getJSONObject("location"));
                                place_id.add(detail.getString("place_id"));
                            }
                            draw_studio(names, stat, rating, studio_loc, place_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},
                new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Couldn't Connect to Server");
                    }
                }){
        };
        queue.add(getPlace);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

    public void draw_studio(ArrayList<Item> names, ArrayList<Item> stat, ArrayList<Item> rating, ArrayList<JSONObject> studio_loc, final ArrayList<String> place_id) throws JSONException {
        double lat = 0;
        double lng = 0;
        for(int i = 0; i < studio_loc.size(); i++){
            lat = studio_loc.get(0).getDouble("lat");
            lng = studio_loc.get(0).getDouble("lng");
        }
        ListView listView = (ListView) findViewById(R.id.studioView);
        myAdapter = new studioAdapter(this, R.layout.studio_item, names, stat, rating);
        listView.setAdapter(myAdapter);
        final double finalLat = lat;
        final double finalLng = lng;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(studio.this, studio_info.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", finalLat);
                bundle.putDouble("lng", finalLng);
                bundle.putString("place_id", place_id.get(position));
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}