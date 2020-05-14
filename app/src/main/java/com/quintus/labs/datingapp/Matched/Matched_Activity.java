package com.quintus.labs.datingapp.Matched;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.RequestMatch;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.SuperLikeModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.LocalBinder;
import com.quintus.labs.datingapp.xmpp.MyService;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.room.models.UserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Response;

/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class Matched_Activity extends AppCompatActivity {

    private static final String TAG = "Matched_Activity";
    private static final int ACTIVITY_NUM = 2;
    List<UserData> matchList = new ArrayList<>();
    List<UserData> copyList = new ArrayList<>();
    private Context mContext = Matched_Activity.this;

    private EditText search;
    private List<UserData> usersListWhoLiked = new ArrayList<>();
    private RecyclerView recyclerViewLiked, mRecyclerView;
    private ActiveUserAdapter adapter;
    private MatchUserAdapter mAdapter;
    private List<String> images = new ArrayList<>();
    private TextView textViewActive;




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatMessageSuccess(final Events.ChatMessageSuccess chatMessageSuccess) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
refreshList();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNewIncomingMessages(Events.NewIncomingChatMessage newIncomingChatMessage) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshList();
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendUserOnlineStatus(final Events.UserOnline userStatus) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshUserStatusList(userStatus);
                }
            });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBlockedUnblockedUsers(Events.UserBlockUnblocked userBlockUnblocked) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFriendList();
                getSuperLikeUsers();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUserEvent(Events.UserEvent userevent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertSuperLike(userevent.getUser());

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(Events.RefreshMatched refresh) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getFriendList();
                getSuperLikeUsers();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setupTopNavigationView();
        //searchFunc();
        GlobalBus.getBus().register(this);


        recyclerViewLiked = findViewById(R.id.active_recycler_view);
        mRecyclerView = findViewById(R.id.matche_recycler_view);
        textViewActive = findViewById(R.id.textViewActive);

        adapter = new ActiveUserAdapter(usersListWhoLiked, getApplicationContext(),this);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerViewLiked.setLayoutManager(mLayoutManager);
        recyclerViewLiked.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLiked.setAdapter(adapter);
       // prepareActiveData();

        mAdapter = new MatchUserAdapter(matchList, getApplicationContext(),this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //prepareMatchData();
        getFriendList();
        getSuperLikeUsers();
        //new connectXmpp().execute();


    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void getFriendList(){
        Call<ResponseModel<List<MatchedFriend>>> responseModelCall = RestServiceFactory.createService().getFriendsList();


        responseModelCall.enqueue(new RestCallBack<ResponseModel<List<MatchedFriend>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<MatchedFriend>>> call, String message) {

            }

            @Override
            public void onResponse(Call<ResponseModel<List<MatchedFriend>>> call, Response<ResponseModel<List<MatchedFriend>>> restResponse, ResponseModel<List<MatchedFriend>> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    matchList.clear();
                    for (MatchedFriend friend:response.data) {
                        prepareMatchDataOne(friend);
                    }

                }
            }
        });
    }
    private void prepareMatchDataOne(MatchedFriend friend) {

        UserData user;
        if(TempStorage.getUser().getId()==friend.getSenderId()){
            user =friend.getReceiver();
        }
        else{
            user =friend.getSender();
        }
        user.setBlocked(friend.isBlocked());
        matchList.add(user);
        mAdapter.notifyDataSetChanged();



    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);

    }


    public void refreshList(){
        try {
            LogUtils.debug("********************************************"+"  Message Sent");
//            List<UserData> userList = mAdapter.getList();
//            matchList.clear();
//            matchList.addAll(userList);
            mAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshUserStatusList(Events.UserOnline userStatus){
        try {
            for(int i=0;i<mAdapter.getList().size();i++){
                UserData data = mAdapter.getList().get(i);
                if (userStatus.getUserID() == data.getId() ) {
                    if (userStatus.isAvailable()) {
                        data.setUserPresenceStatus(true);
                    } else {
                        data.setUserPresenceStatus(false);

                    }
                    mAdapter.notifyItemChanged(i);

                }



                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter!=null&&mAdapter.getList().size()>0){
            refreshList();
        }
    }

    private void getSuperLikeUsers(){
        Call<ResponseModel<List<SuperLikeModel>>> responseModelCall = RestServiceFactory.createService().getSuperLikeUser();
        responseModelCall.enqueue(new RestCallBack<ResponseModel<List<SuperLikeModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<SuperLikeModel>>> call, String message) {
                textViewActive.setVisibility(View.GONE);
                recyclerViewLiked.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<SuperLikeModel>>> call, Response<ResponseModel<List<SuperLikeModel>>> restResponse, ResponseModel<List<SuperLikeModel>> response) {
                if(isSuccessFull(response)){
                    usersListWhoLiked.clear();
                    if(response.data.size()>0){
                        textViewActive.setVisibility(View.VISIBLE);
                        recyclerViewLiked.setVisibility(View.VISIBLE);
                        for (SuperLikeModel friend:response.data) {
                            usersListWhoLiked.add(friend.getLikedBy());
                            adapter.notifyDataSetChanged();
                        }
                    }else{
                        textViewActive.setVisibility(View.GONE);
                        recyclerViewLiked.setVisibility(View.GONE);
                    }



                }else{
                    textViewActive.setVisibility(View.GONE);
                    recyclerViewLiked.setVisibility(View.GONE);
                }

            }
        });

    }


    private void searchFunc() {
        search = findViewById(R.id.searchBar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText();
            }
        });
    }

    private void searchText() {
        String text = search.getText().toString().toLowerCase(Locale.getDefault());
        if (text.length() != 0) {
            if (matchList.size() != 0) {
                matchList.clear();
                for (UserData user : copyList) {
                    if (user.getUsername().toLowerCase(Locale.getDefault()).contains(text)) {
                        matchList.add(user);
                    }
                }
            }
        } else {
            matchList.clear();
            matchList.addAll(copyList);
        }

        mAdapter.notifyDataSetChanged();
    }

    private boolean checkDup(User user) {
        if (matchList.size() != 0) {
            for (UserData u : matchList) {
                if (u.getUsername() == user.getUsername()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void checkClickedItem(int position) {

        UserData user = matchList.get(position);
        //calculate distance
        Intent intent = new Intent(this, ProfileCheckinMatched.class);
        intent.putExtra("classUser", user);

        startActivity(intent);
    }

    private void showAlertSuperLike(UserData users){
        RequestMatch superLikeDialog = new RequestMatch(mContext, mContext.getString(R.string.super_like),String.format(mContext.getString(R.string.super_like_you),"<b>"+users.getFullName()+"</b>"),mContext.getString(R.string.accept),users, AppConstants.Navigation.HOME_PAGE);
        try {
            superLikeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
