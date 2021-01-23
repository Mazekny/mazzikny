package com.example.mazzikny;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class interested_adapter extends RecyclerView.Adapter<interested_adapter.ExampleViewHolder>{

    private ArrayList<ProfileCard> mExampleList;
    private String item_names;

    private interested_adapter.OnItemClickListener mListener;
    public interface OnItemClickListener
    {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(interested_adapter.OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView3;

        public ExampleViewHolder (View itemView, interested_adapter.OnItemClickListener listener)
        {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.not_a_member);
            mTextView3 = itemView.findViewById(R.id.textView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
    public interested_adapter(ArrayList<ProfileCard> exampleList, String item_name)
    {
        mExampleList = exampleList;
        item_names=item_name;
    }

    @NonNull
    @Override
    public interested_adapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interested_adapter, parent, false);
        interested_adapter.ExampleViewHolder evh = new interested_adapter.ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull interested_adapter.ExampleViewHolder holder, int position) {
        ProfileCard currentItem = mExampleList.get(position);

        String item=item_names;

        holder.mImageView.setImageResource(currentItem.getUserImage());
        holder.mTextView1.setText(currentItem.getUserName());
        holder.mTextView3.setText(item);

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}