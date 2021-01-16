package edu.aucegypt.mazzikny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import edu.aucegypt.mazzikny.Item;
import edu.aucegypt.mazzikny.R;

import java.util.ArrayList;

import java.util.ArrayList;

public class studioAdapter extends ArrayAdapter {
    ArrayList<Item> names = new ArrayList<Item>();
    ArrayList<Item> stat = new ArrayList<Item>();
    ArrayList<Item> rating = new ArrayList<Item>();
    private studio context;

    public studioAdapter(studio context, int textViewResourceId, ArrayList objects, ArrayList objects2, ArrayList objects3) {
        super(context, textViewResourceId, objects);
        this.context = context;
        names = objects;
        stat = objects2;
        rating = objects3;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.studio_item, null, true);
        TextView textView = (TextView) row.findViewById(R.id.studioName);
        TextView textView2 = (TextView) row.findViewById(R.id.studioStat);
        TextView textView3 = (TextView) row.findViewById(R.id.studioRating);
        textView.setText(names.get(position).getbuttonName());
        textView2.setText(stat.get(position).getbuttonName());
        textView3.setText(rating.get(position).getbuttonName());
        return row;

    }
}
