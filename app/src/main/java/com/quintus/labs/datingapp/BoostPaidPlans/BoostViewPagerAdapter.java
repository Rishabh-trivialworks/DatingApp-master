package com.quintus.labs.datingapp.BoostPaidPlans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.R;

import java.util.ArrayList;

public class BoostViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private PlanListAdapter.ItemClickListener mClickListener;
    private Context mContext;

    BoostViewPagerAdapter(Context context,ArrayList<Object> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.mContext=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_pager_item, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       ViewHolder viewHolder = (ViewHolder) holder;


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.planLayout:
                    if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(),getItem(getAdapterPosition()));
                    break;

            }
        }
    }
    // convenience method for getting data at click position
    Object getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(PlanListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,Object model);
    }

}
