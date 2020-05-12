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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.LocalBinder;
import com.quintus.labs.datingapp.xmpp.MyService;
import com.quintus.labs.datingapp.xmpp.room.models.MessageData;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

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
    private String userId, userSex, lookforSex;
    private double latitude = 37.349642;
    private double longtitude = -121.938987;
    private EditText search;
    private List<Users> usersList = new ArrayList<>();
    private RecyclerView recyclerView, mRecyclerView;
    private ActiveUserAdapter adapter;
    private MatchUserAdapter mAdapter;
    private List<String> images = new ArrayList<>();
    private MyService mService;
    private boolean mBounded;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<MyService>) service).getService();
            mBounded = true;
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
            Log.d(TAG, "onServiceDisconnected");
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        setupTopNavigationView();
        searchFunc();
        GlobalBus.getBus().register(this);


        recyclerView = findViewById(R.id.active_recycler_view);
        mRecyclerView = findViewById(R.id.matche_recycler_view);

        adapter = new ActiveUserAdapter(usersList, getApplicationContext());
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
       // prepareActiveData();

        mAdapter = new MatchUserAdapter(matchList, getApplicationContext(),this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager1);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        setUpImagesList();

        //prepareMatchData();
        getFriendList();
        //new connectXmpp().execute();


    }

    public  class connectXmpp extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            doBindService();

            return null;
        }
    }
//
//    private void prepareActiveData() {
//        Users users = new Users("1", "Swati Tripathy", 21, "https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg", "Simple and beautiful Girl", "Acting", 200);
//        usersList.add(users);
//        users = new Users("2", "Ananaya Pandy", 20, "https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg", "cool Minded Girl", "Dancing", 800);
//        usersList.add(users);
//        users = new Users("3", "Anjali Kasyap", 22, "https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg", "Simple and beautiful Girl", "Singing", 400);
//        usersList.add(users);
//        users = new Users("7", "Sudeshna Roy", 19, "https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg", "Papa's Pari", "Art", 5000);
//        usersList.add(users);
//
//        adapter.notifyDataSetChanged();
//    }
//
//    private void prepareMatchData() {
//        Users users = new Users("1", "Swati Tripathy", 21, "https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg", "Simple and beautiful Girl", "Acting", 200);
//        matchList.add(users);
//        users = new Users("2", "Ananaya Pandy", 20, "https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg", "cool Minded Girl", "Dancing", 800);
//        matchList.add(users);
//        users = new Users("3", "Anjali Kasyap", 22, "https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg", "Simple and beautiful Girl", "Singing", 400);
//        matchList.add(users);
//        users = new Users("4", "Preety Deshmukh", 19, "http://profilepicturesdp.com/wp-content/uploads/2018/07/fb-real-girls-dp-3.jpg", "dashing girl", "swiming", 1308);
//        matchList.add(users);
//        users = new Users("5", "Srutimayee Sen", 20, "https://dp.profilepics.in/profile_pictures/selfie-girls-profile-pics-dp/selfie-pics-dp-for-whatsapp-facebook-profile-25.jpg", "chulbuli nautankibaj ", "Drawing", 1200);
//        matchList.add(users);
//        users = new Users("6", "Dikshya Agarawal", 21, "https://pbs.twimg.com/profile_images/485824669732200448/Wy__CJwU.jpeg", "Simple and beautiful Girl", "Sleeping", 700);
//        matchList.add(users);
//        users = new Users("7", "Sudeshna Roy", 19, "https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg", "Papa's Pari", "Art", 5000);
//        matchList.add(users);
//
//        mAdapter.notifyDataSetChanged();
//
//
//    }

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
                    for (MatchedFriend friend:response.data) {
                        prepareMatchDataOne(friend);
                    }

                }
            }
        });
    }
    private void prepareMatchDataOne(MatchedFriend friend) {
        if(TempStorage.getUser().getId()==friend.getSenderId()){
            Users users = new Users(String.valueOf(friend.getReceiver().getId()), friend.getReceiver().getFullName(), 21, getRandomImage(), friend.getReceiver().getAbout(), friend.getReceiver().getInterested(), friend.getReceiver().getDistance(),friend.getReceiver().getGender());
            matchList.add(friend.getReceiver());
            mAdapter.notifyDataSetChanged();
        }
        else{
            Users users = new Users(String.valueOf(friend.getSender().getId()), friend.getSender().getFullName(), 21, getRandomImage(), friend.getSender().getAbout(), friend.getSender().getInterested()
                    , friend.getSender().getDistance(),friend.getSender().getGender());
            matchList.add(friend.getSender());
            mAdapter.notifyDataSetChanged();
        }
        copyList.clear();
        copyList.addAll(matchList);

    }

    private void setUpImagesList(){
        images.add("https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg");
        images.add("https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg");
        images.add("https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg");
        images.add("http://profilepicturesdp.com/wp-content/uploads/2018/07/fb-real-girls-dp-3.jpg");
        images.add("https://dp.profilepics.in/profile_pictures/selfie-girls-profile-pics-dp/selfie-pics-dp-for-whatsapp-facebook-profile-25.jpg");
        images.add("https://pbs.twimg.com/profile_images/485824669732200448/Wy__CJwU.jpeg");
        images.add("https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg");
        images.add("https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg");
        images.add("https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg");
        images.add("https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg");
        images.add("http://profilepicturesdp.com/wp-content/uploads/2018/07/fb-real-girls-dp-3.jpg");
        images.add("https://dp.profilepics.in/profile_pictures/selfie-girls-profile-pics-dp/selfie-pics-dp-for-whatsapp-facebook-profile-25.jpg");
        images.add("https://pbs.twimg.com/profile_images/485824669732200448/Wy__CJwU.jpeg");
        images.add("https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg");

    }
    public String getRandomImage()
    {
        Random rand = new Random();
        return images.get(rand.nextInt(images.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        GlobalBus.getBus().unregister(this);

    }

    void doBindService() {
        bindService(new Intent(this, MyService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }

    public MyService getmService() {
        return mService;
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
}
