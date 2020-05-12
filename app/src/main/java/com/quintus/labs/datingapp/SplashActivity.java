package com.quintus.labs.datingapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.LocalBinder;
import com.quintus.labs.datingapp.xmpp.MyService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private MyService mService;
    private boolean mBounded;
    private static final String TAG = "Splash_Activity";


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
    public void getXMPPStatus(Events.XMPP xmpp) {

        LogUtils.debug("getXMPPStatus Call " + xmpp.callback.toString());
    }
        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_splash);
        GlobalBus.getBus().register(this);

        mContext=this;
        if(TempStorage.getUser()!=null){
            getUser();
          //  doBindService();


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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //doUnbindService();
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

}
