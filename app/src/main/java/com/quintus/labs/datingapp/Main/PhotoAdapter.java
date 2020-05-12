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
import com.quintus.labs.datingapp.rest.Response.CardList;

import java.util.Calendar;
import java.util.List;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class PhotoAdapter extends ArrayAdapter<CardList> {
    Context mContext;


    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<CardList> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CardList card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);
        ImageButton btnInfo = convertView.findViewById(R.id.checkInfoBeforeMatched);
        btnInfo.setOnClickListener(v -> {
            ProfileCheckinMain.open(mContext,card_item);

        });

        name.setText(card_item.getFullName() + ", " +getAgeString(card_item.getDob()));
         String url="";
         if(card_item.getMedia()!=null&&card_item.getMedia().size()>0){
             url = card_item.getMedia().get(0).getUrl();
         }


        switch (card_item.getGender()) {
            case "Female":
                GlideUtils.loadImage(getContext(),url,image,R.drawable.default_woman);
                break;
            case "Male":
                GlideUtils.loadImage(getContext(),url,image,R.drawable.default_man);
                break;
            default:
                GlideUtils.loadImage(getContext(),url,image,R.drawable.default_man);
                break;
        }

        return convertView;
    }

    private String getAgeString(String dob){
        String segments[] = dob.split("-");
        String year = segments[0];
        String month = segments[1];
        String day = segments[2];

        String age =getAge(Integer.valueOf(year),Integer.valueOf(month),Integer.valueOf(day));
        return age;
    }
    private String getAge (int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
