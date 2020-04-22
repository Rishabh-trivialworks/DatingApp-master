package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.rest.Response.ImageModel;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ImageModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        this.mContext=context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        GlideUtils.loadImage(mContext,mData.get(position).getUrl(),viewHolder.image_view_4,R.drawable.default_man);

    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = 1;
        return itemViewType;
    }

    public void addImages(ArrayList<ImageModel> mData){
        this.mData.addAll(mData);
    }

    public void clearImages(){
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
        Button delete;
        ImageView image_view_4;

        ViewHolder(View itemView) {
            super(itemView);
            image_view_4 = itemView.findViewById(R.id.image_view_4);
            delete = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.image_view_4:

                case R.id.delete:
                    if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(),getItem(getAdapterPosition()));

                    break;

            }
        }
    }

    // convenience method for getting data at click position
    ImageModel getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,ImageModel model);
    }
}