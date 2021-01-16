package edu.aucegypt.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.lang.String;


public class studio_info extends AppCompatActivity {

    double lat;
    double lng;
    String placeId;
    private PlacesClient placesClient;
    private String [] open_hours = new String[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio_info);
        FloatingActionButton fab = findViewById(R.id.fab2);
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCOrHHgxzOH0HEiNsXXEgxW9mJlx6mmilw");
        }
        placesClient = Places.createClient(this);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");
        placeId = bundle.getString("place_id");

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.PHONE_NUMBER, Place.Field.OPENING_HOURS);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            TextView textView_name = (TextView) findViewById(R.id.studioName);
            TextView textView_number = (TextView) findViewById(R.id.number);
            TextView textView_address = (TextView) findViewById(R.id.address);
            textView_name.setText(place.getName());
            textView_number.setText(place.getPhoneNumber());
            textView_address.setText(place.getAddress());
            open_hours = Objects.requireNonNull(place.getOpeningHours()).getWeekdayText().toArray(new String[0]);
            computeHours(open_hours);

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                final int statusCode = apiException.getStatusCode();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(studio_info.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", lat);
                bundle.putDouble("lng", lng);
                i.putExtras(bundle);
                startActivity(i);

            }
        });
    }

    private void computeHours(String [] open_hours){
        String[] days = new String[open_hours.length];
        String[] time = new String[open_hours.length];
        ArrayList<String> temp;
        hoursAdapter myAdapter;
        for(int i = 0; i < open_hours.length; i++) {
            String[] parts;
            parts = open_hours[i].split("\\:");
            days[i] = parts[0];
            time[i] = parts[1];
        }
        ListView listView = (ListView)findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.hours_view, open_hours);
        listView.setAdapter(arrayAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }


}