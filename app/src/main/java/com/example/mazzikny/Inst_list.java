package com.example.mazzikny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Inst_list extends AppCompatActivity {

    GridView gridView;

//    int images[]={R.drawable.clarinet};//,R.drawable.drum,R.drawable.flute,R.drawable.guitar,R.drawable.piano,R.drawable.trumpet};

    List<ItemsModel>itemslist=new ArrayList<>();

    MainAdapter adapter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_list);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Button button = findViewById(R.id.button);

        button.setOnClickListener(view->{

            startActivity(new Intent(Inst_list.this, sell_item.class));

        });

        Button button1=findViewById(R.id.button1);

        button1.setOnClickListener(view->{

            startActivity(new Intent(Inst_list.this, user_items.class));

        });

        RequestQueue queue = Volley.newRequestQueue(Inst_list.this);

        String url="http://10.0.2.2:3000/store";

        //String[] items;//={"Clarinet", "Drums", "Flute", "Guitar", "Piano", "Trumpet"};
        //String prices[];//={"4999 EGP", "9999 EGP", "1499 EGP","3499 EGP","19999 EGP","6999 EGP"};
       // String desc[];//={"This is a \"Clarinet\"", "This is a \"Drums\"", "This is a \"Flute\"", "This is a \"Guitar\"", "This is a \"Piano\"", "This is a \"Trumpet\""};
       // String sellers[];//={"Ahmed", "Mohamed", "Omar", "Abdo", "shenawy", "Marwan"};
       // String ids[];//={"0", "2", "3", "4", "5", "6"};

        List<String>items=new ArrayList<>();
        List<String>prices=new ArrayList<>();
        List<String>desc=new ArrayList<>();
        List<String>sellers=new ArrayList<>();
        List<String>ids=new ArrayList<>();

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
                            Log.e("oops", "oh yeah");
                            try {
                                JSONObject oneObject = response.getJSONObject(i);
                                // Pulling items from the array
                                if(!user.getUid().equals(oneObject.getString("Seller"))) {
                                    items.add(oneObject.getString("item_name"));
                                    prices.add(oneObject.getString("price"));
                                    desc.add(oneObject.getString("Description"));
                                    sellers.add(oneObject.getString("Seller"));
                                    ids.add(Integer.toString(oneObject.getInt("id")));
                                }

//                                Log.e("Rest Response", items.get(i));
                            } catch (JSONException e) {
                                Log.e("oops", e.toString());
                            }
                        }
                adapter = new MainAdapter(Inst_list.this, itemslist);
                gridView = findViewById(R.id.grid);

                for(int j=0; j<items.size();j++)
                {
                    ItemsModel itemsModel=new ItemsModel(items.get(j), prices.get(j), desc.get(j), sellers.get(j), ids.get(j), R.drawable.instrument);
                    itemslist.add(itemsModel);
                }


                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        startActivity(new Intent(Inst_list.this, inst_view.class).putExtra("item",itemslist.get(position)));
                    }
                });
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
    }


    public class MainAdapter extends BaseAdapter implements Filterable {

        private List<ItemsModel>itemsModelList;
        private List<ItemsModel> itemsModelListfiltered;
        private Context context;

        public MainAdapter(Context c,List<ItemsModel> itemsModelList){
            this.context=c;
            this.itemsModelList=itemsModelList;
            this.itemsModelListfiltered=itemsModelList;
        }

        @Override
        public int getCount() {
            return itemsModelListfiltered.size();
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

            View view=getLayoutInflater().inflate(R.layout.grid_item, null);

            ImageView imageView=view.findViewById(R.id.image);
            TextView textView=view.findViewById(R.id.item);
            TextView textView1=view.findViewById(R.id.price);

            imageView.setImageResource(itemsModelListfiltered.get(position).getImage());
            textView.setText(itemsModelListfiltered.get(position).getName());
            textView1.setText(itemsModelListfiltered.get(position).getPrice());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Inst_list.this, inst_view.class).putExtra("item",itemsModelListfiltered.get(position)));
                }
            });

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults=new FilterResults();

                    if(constraint==null||constraint.length()==0){
                        filterResults.count=itemsModelList.size();
                        filterResults.values=itemsModelList;
                    }else{
                        String searchstr=constraint.toString().toLowerCase();
                        List<ItemsModel>resultData=new ArrayList<>();

                        for(ItemsModel itemsModel:itemsModelList){
                            if(itemsModel.getName().toLowerCase().contains(searchstr)){
                                resultData.add(itemsModel);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemsModelListfiltered=(List<ItemsModel>)results.values;
                    notifyDataSetChanged();

                }
            };

            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }


}

