package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.rest.Response.ImageModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileMediaViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.profileImage)
    ImageView profileImage;

    public ProfileMediaViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

   public void bind(Object item, Context context, int position){
        if(item!=null){
            ImageModel model = (ImageModel)item;
            Helper.loadImage(context,model,profileImage);
        }

   }
}
