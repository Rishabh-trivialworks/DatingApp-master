package com.quintus.labs.datingapp.Matched;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.quintus.labs.datingapp.Main.Cards;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.Utils.ImageUtils;
import com.quintus.labs.datingapp.chat.ChatActivity;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.room.models.ConversationData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.quintus.labs.datingapp.xmpp.utils.ChatMedia;
import com.quintus.labs.datingapp.xmpp.utils.TimeUtils;

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
    List<UserData> usersList;
    Context context;
    Matched_Activity activity;
    private Gson gson;


    public MatchUserAdapter(List<UserData> usersList, Context context, Matched_Activity activity) {
        this.usersList = usersList;
        this.context = context;
        this.activity = activity;
        this.gson = new Gson();

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
        UserData users = usersList.get(position);

        holder.name.setText(users.getFullName());
      ConversationData chatModelNotification = MyApplication.getChatDataBase().chatConversationDao().getChatConversationDialog(users.getId(), AppConstants.Chat.TYPE_SINGLE_CHAT);
       if(chatModelNotification!=null&&chatModelNotification.chatMessage!=null){
           if (chatModelNotification.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_TEXT)) {
               holder.profession.setText(chatModelNotification.chatMessage.getBody());
               holder.profession.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

           } else if (chatModelNotification.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_IMAGE)) {
               holder.profession.setText(context.getString(R.string.chat_image));
               holder.profession.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chat_image, 0, 0, 0);

           } else if (chatModelNotification.chatMessage.getSubject().equals(AppConstants.Chat.TYPE_CHAT_AUDIO)) {
               final ChatMedia chatMedia = gson.fromJson(chatModelNotification.chatMessage.getBody(), ChatMedia.class);
               holder.profession.setText(TimeUtils.getTimeFromMillis(chatMedia.getDuration() * 1000));
               holder.profession.setCompoundDrawablesWithIntrinsicBounds(R.drawable.chat_audio, 0, 0, 0);

           }
           if (chatModelNotification.chatConversation.getUnreadCount() == 0) {
               holder.profession.setTypeface(Typeface.SANS_SERIF);
               holder.textViewNewMessageCount.setVisibility(View.GONE);
           } else {
               holder.profession.setTypeface(Typeface.DEFAULT_BOLD);
               holder.textViewNewMessageCount.setVisibility(View.VISIBLE);
               if (chatModelNotification.chatConversation.getUnreadCount() == -1) {
                   holder.textViewNewMessageCount.setText("");
               } else {
                   holder.textViewNewMessageCount.setText(chatModelNotification.chatConversation.getUnreadCount() + "");
               }
           }
           chatModelNotification.chatConversation.getUnreadCount();

           if(users.isUserPresenceStatus()){
               holder.imageViewStatus.setVisibility(View.VISIBLE);
               holder.imageViewStatus.setImageResource(R.drawable.ic_lens_black_24dp);
           }else{
               holder.imageViewStatus.setVisibility(View.INVISIBLE);

           }

       }else{
           holder.profession.setText(users.getAbout());

       }



        Helper.loadImage(context,users.getMedia(),users.getGender(),holder.imageView);

        holder.layoutMatchedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChattingActivity.openActivity(activity,new UserData(users));

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
        TextView name, profession,textViewNewMessageCount;
        LinearLayout layoutMatchedUser;
        ImageView imageViewStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mui_image);
            name = itemView.findViewById(R.id.mui_name);
            profession = itemView.findViewById(R.id.mui_profession);
            layoutMatchedUser = itemView.findViewById(R.id.layoutMatchedUser);
            imageViewStatus = itemView.findViewById(R.id.imageViewStatus);
            textViewNewMessageCount = itemView.findViewById(R.id.textViewNewMessageCount);

        }
    }
}
