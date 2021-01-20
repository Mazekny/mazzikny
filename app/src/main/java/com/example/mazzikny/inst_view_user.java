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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class inst_view_user extends AppCompatActivity {

    ItemsModel item;
    ImageView imageView;
    TextView name;
    TextView price;
    TextView desc;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_view_user);

        imageView=findViewById(R.id.photo);
        name=findViewById(R.id.product_name);
        price=findViewById(R.id.price);
        desc=findViewById(R.id.item_details);
        button=findViewById(R.id.button);

        Intent intent=getIntent();

        if(intent.getExtras()!=null)
        {
            item= (ItemsModel) intent.getSerializableExtra("item");
            imageView.setImageResource(item.getImage());
            name.setText(item.getName());
            price.setText("Price: "+item.getPrice());
            desc.setText("Description: "+item.getDesc());
        }

        button.setOnClickListener(view->{
            RequestQueue queue1 = Volley.newRequestQueue(this);

            String URL="http://10.0.2.2:3000/deleteitem?id="+item.getId();
            Log.e("heyy", URL);
            StringRequest example = new StringRequest(Request.Method.GET,
                    URL,
                    response1 -> {Log.e("Rest Response", response1);
                    Toast.makeText(inst_view_user.this, "Item Successfully Deleted!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(inst_view_user.this, home_page.class));
                    },
                    error -> Log.e("Rest Response", error.toString())
            );

            queue1.add(example);
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