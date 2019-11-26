package com.quintus.labs.datingapp.chat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Matched.Users;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.SplashActivity;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.chatview.data.Message;
import com.quintus.labs.datingapp.chatview.widget.ChatView;
import com.quintus.labs.datingapp.rest.Response.User;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

private ChatView chatView;
private Toolbar toolbar;
    private Context mContext;
    private ImageView imageViewOptions;
    TextView textViewAction;
    Users user;
    UserData myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatView = (ChatView) findViewById(R.id.chatView);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        mContext = this;
        if(getIntent()!=null){
         user= (Users) getIntent().getSerializableExtra("user");
        }
        else{
            finish();
        }
        myuser = TempStorage.getUser();
        addLeftMessage("Hi How are You?");
        addRightMessage("I am Fine and You");
        setListeners();
        setToolBar(user.getName());

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

               // ToastUtils.show(mContext,"Unable to Connect with server");
            }
        }, 3000);

    }

    private void addLeftMessage(String messageStr){

        Message message = new Message();
        message.setBody(messageStr.trim()); //message body
        message.setMessageType(Message.MessageType.LeftSimpleMessage);
        message.setType(Message.MessageType.LeftSimpleMessage.toString()); //message type
        message.setTime("Just Now"); //message time (String)
        message.setUserName(user.getName()); //sender name
        //sender icon
        message.setUserIcon(user.getProfileImageUrl());
        chatView.addMessage(message);
    }

    private void addRightMessage(String messageStr){

        Message message = new Message();
        message.setBody(messageStr.trim()); //message body
        message.setMessageType(Message.MessageType.RightSimpleImage);
        message.setType(Message.MessageType.RightSimpleImage.toString()); //message type
        message.setTime("Just Now"); //message time (String)
        message.setUserName(myuser.getName()); //sender name
        //sender icon
        message.setUserIcon("http://"+myuser.getMedia().get(0).getUrl());
        chatView.addMessage(message);
    }
    private void setListeners(){
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                addRightMessage(body);
            }
        });


    }

    private void setToolBar(String title) {

        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(mContext).inflate(R.layout.view_app_bar, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        textViewAction = v.findViewById(R.id.textViewAction);
        imageViewOptions.setVisibility(View.GONE);
        textViewAction.setVisibility(View.VISIBLE);

        imageViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewAction.setVisibility(View.GONE);

        ((TextView) (v.findViewById(R.id.textViewTitle))).setText(title);

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }


}
