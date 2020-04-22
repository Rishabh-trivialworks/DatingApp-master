package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;

import java.util.List;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class PhotoAdapter extends ArrayAdapter<Cards> {
    Context mContext;


    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<Cards> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);
        ImageButton btnInfo = convertView.findViewById(R.id.checkInfoBeforeMatched);

        name.setText(card_item.getName() + ", " + card_item.getAge());
        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ProfileCheckinMain.class);
            intent.putExtra("name", card_item.getName() + ", " + card_item.getAge());
            intent.putExtra("photo", card_item.getProfileImageUrl());
            intent.putExtra("bio", card_item.getBio());
            intent.putExtra("interest", card_item.getInterest());
            intent.putExtra("distance", card_item.getDistance());
            intent.putExtra("gender", card_item.getGender());

            mContext.startActivity(intent);
        });

        name.setText(card_item.getName() + ", " + card_item.getAge());



        switch (card_item.getGender()) {
            case "Female":
                GlideUtils.loadImage(getContext(),card_item.getProfileImageUrl(),image,R.drawable.default_woman);
                break;
            case "Male":
                GlideUtils.loadImage(getContext(),card_item.getProfileImageUrl(),image,R.drawable.default_man);
                break;
            default:
                GlideUtils.loadImage(getContext(),card_item.getProfileImageUrl(),image,R.drawable.default_man);
                break;
        }

        return convertView;
    }
}
