package com.quintus.labs.datingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_splash);
        mContext=this;
        if(TempStorage.getUser()!=null){
            getUser();

        }
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if(TempStorage.getUser()!=null){
                    Intent in=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }else{
                    Intent in = new Intent(SplashActivity.this,Login.class);
                    startActivity(in);
                    finish();
                }
            }
        }, 3000);


    }
    private void getUser(){
        Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().getUserDetails();
        responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
            @Override
            public void onFailure(Call<ResponseModel<UserData>> call, String message) {
                ToastUtils.show(mContext, message);

            }

            @Override
            public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    TempStorage.setUserData(response.data);
                    TempStorage.userData = response.data;

                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }
}
