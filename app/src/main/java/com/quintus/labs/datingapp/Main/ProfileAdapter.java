package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.BoostPaidPlans.PlanListAdapter;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.rest.Response.ImageModel;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private PlanListAdapter.ItemClickListener mClickListener;
    private Context mContext;


    ProfileAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mContext=context;
    }

    public void addItem(CardList card){
        mData.add(card);
        notifyDataSetChanged();
    }

    public void addItem(ImageModel image){
        mData.add(image);
        notifyDataSetChanged();
    }


    public void clearAdapter(){
        mData.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if(getItem(position) instanceof CardList){
        return 0;
        }
        else{
           return 1;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View v = mInflater.inflate(R.layout.item_view_profile, parent, false);
            return new ProfileMainViewHolder(v);
        } else {
            View v = mInflater.inflate(R.layout.item_view_profile_media, parent, false);
            return new ProfileMediaViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProfileMainViewHolder) {
            ((ProfileMainViewHolder) holder).bind( getItem(position), mContext, position);
        } else if (holder instanceof ProfileMediaViewHolder) {
            ((ProfileMediaViewHolder) holder).bind(getItem(position),mContext, position);
        }
    }
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
