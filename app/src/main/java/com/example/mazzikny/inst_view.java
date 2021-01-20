package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class inst_view extends AppCompatActivity {

    ItemsModel item;
    ImageView imageView;
    TextView name;
    TextView price;
    TextView desc;
    Button button;
    Button button1;
    Button button2;
    TextView likes;
    TextView dislikes;
    FirebaseUser user;

    String like;
    String dislike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_view);

        imageView=findViewById(R.id.photo);
        name=findViewById(R.id.product_name);
        price=findViewById(R.id.price);
        desc=findViewById(R.id.item_details);
        button=findViewById(R.id.button);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        likes=findViewById(R.id.rating_likes);
        dislikes=findViewById(R.id.rating_dislikes);
        like=null;
        user = FirebaseAuth.getInstance().getCurrentUser();
        dislike=null;

        Intent intent=getIntent();


            item= (ItemsModel) intent.getSerializableExtra("item");
            String seller=item.getSeller();


        RequestQueue queue = Volley.newRequestQueue(this);

        String url="http://10.0.2.2:3000/getUser?id="+seller;

        JsonArrayRequest example = new JsonArrayRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                url,null,
                new Response.Listener<JSONArray>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        Log.e("Rest Response", response.toString());
                        for (int i=0; i < response.length(); i++)
                        {
                            try {
                                JSONObject oneObject = response.getJSONObject(i);
                                like=oneObject.getString("likes");
                                dislike=oneObject.getString("dislikes");
                            } catch (JSONException e) {
                                Log.e("oops", e.toString());
                            }
                        }

                        imageView.setImageResource(item.getImage());
                        name.setText(item.getName());
                        price.setText("Price: "+item.getPrice()+" EGP");
                        desc.setText("Description: "+item.getDesc());
                        likes.setText("Seller Likes: "+like);
                        dislikes.setText("Seller Dislikes: "+dislike);
                    }

                },
                new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("Rest Response", error.toString());
                    }
                }
        );

        queue.add(example);


        button.setOnClickListener(view->{


            //send notification to seller
        });

        button1.setOnClickListener(view->{

            RequestQueue queue1 = Volley.newRequestQueue(this);


            String URL="http://10.0.2.2:3000/check_vote?buyer="+user.getUid()+"&seller="+seller;

            JsonArrayRequest example1 = new JsonArrayRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                    URL,null,
                    new Response.Listener<JSONArray>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            Log.e("Rest Response", response.toString());
                            if(response.length()!=0)
                            {
                                Toast.makeText(inst_view.this, "You already rated this seller", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                RequestQueue queue2 = Volley.newRequestQueue(inst_view.this);
                                String URL1="http://10.0.2.2:3000/vote?buyer="+user.getUid()+"&seller="+seller;

                                StringRequest example2 = new StringRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                                        URL1,
                                        new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                                        {
                                            @Override
                                            public void onResponse(String response)
                                            {
                                                Log.e("Rest Response", response);
                                                RequestQueue queue3 = Volley.newRequestQueue(inst_view.this);
                                                String URL2="http://10.0.2.2:3000/dislike?dislikes="+Integer.toString(Integer.parseInt(dislike)+1)+"&id="+seller;
                                                StringRequest example3 = new StringRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                                                        URL2,
                                                        new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                                                        {
                                                            @Override
                                                            public void onResponse(String response)
                                                            {
                                                                Log.e("Rest Response", response);

                                                            }

                                                        },
                                                        new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                                                        {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error)
                                                            {
                                                                Log.e("Rest Response", error.toString());
                                                            }
                                                        }
                                                );

                                                queue3.add(example3);
                                            }

                                        },
                                        new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error)
                                            {
                                                Log.e("Rest Response", error.toString());
                                            }
                                        }
                                );

                                queue2.add(example2);

                            }
                        }

                    },
                    new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.e("Rest Response", error.toString());
                        }
                    }
            );

            queue1.add(example1);

            //rating functionality (get ratings from database and calculate average and option to add rate; if didn't rate this seller before then rate if did then edit previous rate and the average gets calculated)
        });

        button2.setOnClickListener(view->{


            RequestQueue queue1 = Volley.newRequestQueue(this);


            String URL="http://10.0.2.2:3000/check_vote?buyer="+user.getUid()+"&seller="+seller;

            JsonArrayRequest example1 = new JsonArrayRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                    URL,null,
                    new Response.Listener<JSONArray>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                    {
                        @Override
                        public void onResponse(JSONArray response)
                        {
                            Log.e("Rest Response", response.toString());
                            if(response.length()!=0)
                            {
                                Toast.makeText(inst_view.this, "You already rated this seller", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                RequestQueue queue2 = Volley.newRequestQueue(inst_view.this);
                                String URL1="http://10.0.2.2:3000/vote?buyer="+user.getUid()+"&seller="+seller;

                                StringRequest example2 = new StringRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                                        URL1,
                                        new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                                        {
                                            @Override
                                            public void onResponse(String response)
                                            {
                                                Log.e("Rest Response", response);
                                                RequestQueue queue3 = Volley.newRequestQueue(inst_view.this);
                                                String URL2="http://10.0.2.2:3000/like?likes="+Integer.toString(Integer.parseInt(like)+1)+"&id="+seller;
                                                StringRequest example3 = new StringRequest(Request.Method.GET, //Make sure to use .GET if it's  a get request.
                                                        URL2,
                                                        new Response.Listener<String>() //This is an in-lined function that waits for a response from the server running asynchronously- you do not need to set up the async yourself.
                                                        {
                                                            @Override
                                                            public void onResponse(String response)
                                                            {
                                                                Log.e("Rest Response", response);

                                                            }

                                                        },
                                                        new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                                                        {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error)
                                                            {
                                                                Log.e("Rest Response", error.toString());
                                                            }
                                                        }
                                                );

                                                queue3.add(example3);
                                            }

                                        },
                                        new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                                        {
                                            @Override
                                            public void onErrorResponse(VolleyError error)
                                            {
                                                Log.e("Rest Response", error.toString());
                                            }
                                        }
                                );

                                queue2.add(example2);

                            }
                        }

                    },
                    new Response.ErrorListener() //This is an in-lined function that handles CONNECTION errors. Errors from the SERVER (i.e. wrong username/password, et cetera) should still be handled by Response.Listener.
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.e("Rest Response", error.toString());
                        }
                    }
            );

            queue1.add(example1);

            //rating functionality (get ratings from database and calculate average and option to add rate; if didn't rate this seller before then rate if did then edit previous rate and the average gets calculated)
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