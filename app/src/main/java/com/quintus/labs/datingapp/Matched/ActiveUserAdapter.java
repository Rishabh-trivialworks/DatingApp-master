package com.quintus.labs.datingapp.Matched;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.Utils.ImageUtils;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;
import com.quintus.labs.datingapp.xmpp.utils.UserDeviceInfoModel;
import com.quintus.labs.datingapp.xmpp.utils.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class ActiveUserAdapter extends RecyclerView.Adapter<ActiveUserAdapter.MyViewHolder> {
    List<UserData> usersList;
    Context context;
    Matched_Activity activity;


    public ActiveUserAdapter(List<UserData> usersList, Context context, Matched_Activity activity) {
        this.usersList = usersList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ActiveUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.active_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveUserAdapter.MyViewHolder holder, int position) {
        UserData users = usersList.get(position);
        holder.name.setText(users.getFullName());
        Helper.loadImage(context,users.getMedia(),users.getGender(),holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UserDeviceInfoModel> list = new ArrayList<>();
                list.add(new UserDeviceInfoModel("android","1.0.0"));
                users.setPremiumUser(true);
                users.setReceivePrivateMsg(true);
                users.setOnWhoseSide("OTHER");
                users.setDeviceInfo(list);
                users.setHideReadReceipt(false);
                ChattingActivity.openActivity(activity,users);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    public List<UserData> getList(){
        return usersList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.aui_image);
            name = itemView.findViewById(R.id.aui_name);
        }
    }
}
