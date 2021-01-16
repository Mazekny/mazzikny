package com.example.mazzikny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Inst_list extends AppCompatActivity {

    GridView gridView;

    String[] items={"Clarinet", "Drums", "Flute", "Guitar", "Piano", "Trumpet"};
    String prices[]={"4999 EGP", "9999 EGP", "1499 EGP","3499 EGP","19999 EGP","6999 EGP"};
    String desc[]={"This is a \"Clarinet\"", "This is a \"Drums\"", "This is a \"Flute\"", "This is a \"Guitar\"", "This is a \"Piano\"", "This is a \"Trumpet\""};
    String cont[]={"01xxxxxxxxx", "01xxxxxxxxx", "01xxxxxxxxx", "01xxxxxxxxx", "01xxxxxxxxx", "01xxxxxxxxx"};
    int images[]={R.drawable.clarinet,R.drawable.drum,R.drawable.flute,R.drawable.guitar,R.drawable.piano,R.drawable.trumpet};

    List<ItemsModel>itemslist=new ArrayList<>();

    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_list);

        gridView = findViewById(R.id.grid);

        for(int i=0; i<items.length;i++)
        {
            ItemsModel itemsModel=new ItemsModel(items[i], prices[i], desc[i], cont[i], images[i]);
            itemslist.add(itemsModel);
        }

        adapter = new MainAdapter(this, itemslist);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Inst_list.this, inst_view.class).putExtra("item",itemslist.get(position)));
            }
        });
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

