package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.ItsAMatchDialog;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.rest.RequestModel.AcceptRejectModel;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import retrofit2.Call;
import retrofit2.Response;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class BtnLikeActivity extends AppCompatActivity {
    private static final String TAG = "BtnLikeActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = BtnLikeActivity.this;
    private ImageView like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn_like);

        setupTopNavigationView();
        like = findViewById(R.id.like);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("url");
        int id = intent.getIntExtra("id",-1);
        if(id>0){
            requestFriend(AppConstants.ACCEPTED,id);
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent mainIntent = new Intent(BtnLikeActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
            }).start();
        }

        switch (profileUrl) {
            case "defaultFemale":
                Glide.with(mContext).load(R.drawable.default_woman).into(like);
                break;
            case "defaultMale":
                Glide.with(mContext).load(R.drawable.default_man).into(like);
                break;
            default:
                Glide.with(mContext).load(R.drawable.default_woman).into(like);
                break;
        }


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

    private void requestFriend(String status,int id){
        // pd.show();
        Call<ResponseModel<MatchedFriend>> responseModelCall = RestServiceFactory.createService().requestFriend( new AcceptRejectModel(id,status));


        responseModelCall.enqueue(new RestCallBack<ResponseModel<MatchedFriend>>() {
            @Override
            public void onFailure(Call<ResponseModel<MatchedFriend>> call, String message) {
                Intent mainIntent = new Intent(BtnLikeActivity.this, MainActivity.class);
                startActivity(mainIntent);

            }

            @Override
            public void onResponse(Call<ResponseModel<MatchedFriend>> call, Response<ResponseModel<MatchedFriend>> restResponse, ResponseModel<MatchedFriend> response) {
                if(isSuccessFull(response)){

                    if(response.data.getReceiverStatus().equalsIgnoreCase(AppConstants.ACCEPTED)&&response.data.getSenderStatus().equalsIgnoreCase(AppConstants.ACCEPTED)){

                        ItsAMatchDialog itsAMatchDialog = new ItsAMatchDialog(mContext, response.data, AppConstants.Navigation.HOME_PAGE);
                        try {
                            itsAMatchDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else {
                        Intent mainIntent = new Intent(BtnLikeActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                    }

                }

            }
        });



    }
}
