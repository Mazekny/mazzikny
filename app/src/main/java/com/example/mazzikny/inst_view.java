package com.example.mazzikny;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class inst_view extends AppCompatActivity {

    ItemsModel item;
    ImageView imageView;
    TextView name;
    TextView price;
    TextView desc;
    TextView cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_view);

        imageView=findViewById(R.id.photo);
        name=findViewById(R.id.product_name);
        price=findViewById(R.id.price);
        desc=findViewById(R.id.item_details);
        cont=findViewById(R.id.contact_details);

        Intent intent=getIntent();

        if(intent.getExtras()!=null)
        {
            item= (ItemsModel) intent.getSerializableExtra("item");
            imageView.setImageResource(item.getImage());
            name.setText(item.getName());
            price.setText("Price: "+item.getPrice());
            desc.setText("Description: "+item.getDesc());
            cont.setText("Contact details: "+item.getCont());
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}