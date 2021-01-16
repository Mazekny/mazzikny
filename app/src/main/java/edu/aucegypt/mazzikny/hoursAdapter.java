package edu.aucegypt.mazzikny;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class hoursAdapter extends BaseAdapter {
    Context context;
    String[] days;
    String[] hours;
    LayoutInflater inflter;
    public hoursAdapter(Context applicationContext, int textViewResourceId, String[] days, String[] hours) {
        this.context = applicationContext;
        this.days = days;
        this.hours = hours;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return days.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.hours_view, null); // inflate the layout
        TextView textView = (TextView) view.findViewById(R.id.textView2); // get the reference of ImageView
        TextView textView2 = (TextView) view.findViewById(R.id.textView2); // get the reference of ImageView
        textView.setText(days[i]); // set logo images
        textView2.setText(hours[i]); // set logo images
        return view;
    }
}
