package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
    private List<String> open_hours;
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
            open_hours = place.getOpeningHours().getWeekdayText();
            sched_adapter sched = new sched_adapter(this,open_hours);
            GridView gridView = (GridView) findViewById(R.id.list);
            gridView.setAdapter(sched);
//            computeHours(open_hours);

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

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }


public class sched_adapter extends BaseAdapter {

    private List<String> sched;
    private Context context;

    public sched_adapter(Context c,List<String> sched){
        this.context=c;
        this.sched=sched;
    }

    @Override
    public int getCount() {
        return sched.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view=getLayoutInflater().inflate(R.layout.hours_view, null);

        TextView textView=view.findViewById(R.id.textView2);

        textView.setText(sched.get(position));

        return view;
    }
}
}