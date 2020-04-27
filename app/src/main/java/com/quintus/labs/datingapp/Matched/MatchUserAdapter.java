package com.quintus.labs.datingapp.Matched;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.Main.Cards;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.ImageUtils;
import com.quintus.labs.datingapp.chat.ChatActivity;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.rest.Response.UserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class MatchUserAdapter extends RecyclerView.Adapter<MatchUserAdapter.MyViewHolder> {
    List<Users> usersList;
    Context context;
    Matched_Activity activity;

    public MatchUserAdapter(List<Users> usersList, Context context, Matched_Activity activity) {
        this.usersList = usersList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MatchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchUserAdapter.MyViewHolder holder, int position) {
        Users users = usersList.get(position);
        Cards cards = new Cards(String.valueOf(users.getUserId()), users.getName(), Integer.valueOf(users.getAge()),users.getProfileImageUrl() , users.getBio(), users.getInterest(), users.getDistance(),users.getGender());

        holder.name.setText(users.getName());
        holder.profession.setText(users.getBio());

        if (users.getProfileImageUrl() != null) {
            //ImageUtils.setImage(context,users.getProfileImageUrl(),holder.imageView);
            GlideUtils.loadImage(context,users.getProfileImageUrl(),holder.imageView,R.drawable.default_man);
        }
        holder.layoutMatchedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChattingActivity.openActivity(activity,new UserData(users));
//                Intent in = new Intent(context, ChatActivity.class);
//                in.setFlags(FLAG_ACTIVITY_NEW_TASK);
//                in.putExtra("user",users);
//                in.putExtra("card",cards);
//
//                context.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, profession;
        LinearLayout layoutMatchedUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
            layoutMatchedUser = itemView.findViewById(R.id.layoutMatchedUser);
        }
    }
}
