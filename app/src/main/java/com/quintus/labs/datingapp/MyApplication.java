package com.quintus.labs.datingapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.HandlerThread;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.NetworkUtil;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.receivers.NetworkChangeReceiver;
import com.quintus.labs.datingapp.rest.Response.LoginData;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends MultiDexApplication implements NetworkChangeReceiver.OnNetworkChangeListener, Application.ActivityLifecycleCallbacks {

    private Context context;

    public final static ApiMode apiMode = ApiMode.LIVE;
    public final static boolean USE_CRASH_ANALYTICS = false;

    public final static boolean SHOW_LOG = true;
    public final static boolean RETROFIT_SHOW_LOG = true;
    public final static boolean TOAST_ERROR_LIVE = false;
    public final static boolean API_DEBUG = false;

    public final static List<Activity> ACTIVITIES = new ArrayList<>();
    public static boolean isAppForeground;
    private NetworkChangeReceiver mNetWorkChangeReciver;

    public enum ApiMode {
        TESTING_ALPHA,
        TESTING_BETA,
        LIVE
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        LogUtils.debug("MyuApplication onCreate");

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        AppContext.getInstance().setContext(this);
        registerActivityLifecycleCallbacks(this);
        MultiDex.install(this);




        initialize(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new NetworkChangeReceiver(), intentFilter);



        mNetWorkChangeReciver = new NetworkChangeReceiver();
        NetworkChangeReceiver.register(this, mNetWorkChangeReciver);
        setTempStorage();



    }

    public static void initialize(Application context) {
        try {
            NetworkUtil.getInstance(context).initialize();
            new PreferencesManager(context).setName(AppConstants.Pref.NAME).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTempStorage() {
        LoginData loginData = PreferencesManager.getObject(AppConstants.Pref.LOGIN_MODEL_OBJECT, LoginData.class);
        UserData userData = PreferencesManager.getObject(AppConstants.Pref.USER_MODEL_OBJECT, UserData.class);
        String authToken = PreferencesManager.getString(AppConstants.Pref.AUTH_TOKEN, "");
       if(userData!=null){
            TempStorage.userData=userData;
            TempStorage.authToken=authToken;
        }
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        ACTIVITIES.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isAppForeground) {
            isAppForeground = true;
            LogUtils.debug("App is in Foreground");
            onAppForeground();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ACTIVITIES.remove(activity);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.debug("MyuApplication onTrimMemory " + level);

    }

    private void onAppForeground() {
        LogUtils.debug("AppStatus: Foreground");
    }

    private void onAppBackground() {
        LogUtils.debug("AppStatus: Background");
    }



}
