package com.quintus.labs.datingapp.BoostPaidPlans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.rest.Response.ImageModel;

import java.util.ArrayList;

public class PlanListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    PlanListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mContext=context;
    }

    // inflates the cell layout_activate_boost from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if(mData.get(position) instanceof PlanModel){
            PlanModel model = (PlanModel) mData.get(position);

            viewHolder.planName.setText(model.getName());

            String planPriceString = String.format(mContext.getResources().getString(R.string.plan_price),model.getPrice(),"INR",getPlanExpiryString(model.getExpiry()));

            viewHolder.planPrice.setText(planPriceString);

            if(position==1){
                viewHolder.planTag.setVisibility(View.VISIBLE);
            }else{
                viewHolder.planTag.setVisibility(View.INVISIBLE);

            }



        }


    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = 1;
        return itemViewType;
    }

    public void addPlans(ArrayList<PlanModel> mData){
        this.mData.addAll(mData);
    }

    public void clearPlans(){
        mData.clear();
    }
    // binds the data to the TextView in each cell


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout planLayout;
        TextView planName,planPrice,planTag;

        ViewHolder(View itemView) {
            super(itemView);
            planName = itemView.findViewById(R.id.planName);
            planPrice = itemView.findViewById(R.id.planPrice);
            planTag = itemView.findViewById(R.id.planTag);
            planLayout = itemView.findViewById(R.id.planLayout);

            itemView.setOnClickListener(this);
            planLayout.setOnClickListener(this);
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
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,Object model);
    }

    public String getPlanExpiryString(long expiry){
        long oneDayExpiryOneDay = 86400;
        if(expiry/oneDayExpiryOneDay==1){
            return mContext.getResources().getString(R.string.day);
        }
        if(expiry/oneDayExpiryOneDay==7){
            return mContext.getResources().getString(R.string.week);
        }
        if(expiry/oneDayExpiryOneDay==30){
            return mContext.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==90){
            return mContext.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==180){
            return mContext.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==360){
            return mContext.getResources().getString(R.string.year);
        }
        return "";
    }
}