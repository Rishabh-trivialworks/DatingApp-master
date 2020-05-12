package com.quintus.labs.datingapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.receivers.NetworkChangeReceiver;
import com.quintus.labs.datingapp.rest.Response.LoginData;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.xmpp.XMPPHelper;
import com.quintus.labs.datingapp.xmpp.room.models.ChatDataBase;
import com.quintus.labs.datingapp.xmpp.utils.AppSharedPreferences;
import com.quintus.labs.datingapp.xmpp.utils.NetworkUtil;
import com.shawnlin.preferencesmanager.PreferencesManager;
import com.stripe.android.PaymentConfiguration;

import org.jivesoftware.smack.packet.Presence;

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
    public static ChatDataBase chatDataBase;
    private static long bgTime;
    public static String STRIPE_EXAMPLE_PUBLISHABLE_KEY = "pk_test_2lIaCJoBMsiId5L5Pd30mtwZ00X9UqRN49";
    public static String       STRIPE_ACCOUNT_ID = "acct_1GSZ0yFKSVpb9Yku";
    public static String       STRIPE_SECRET_KEY = "sk_test_TKfmtCvEGgYqxJJoawpZASMe00uSuWS6o2";

    public static String BACKEND_URL = "";

    public enum ApiMode {
        TESTING_ALPHA,
        TESTING_BETA,
        LIVE
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        PaymentConfiguration.init(
                getApplicationContext(),
                STRIPE_EXAMPLE_PUBLISHABLE_KEY
        );
        LogUtils.debug("MyApplication onCreate");

        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        AppContext.getInstance().setContext(this);
        registerActivityLifecycleCallbacks(this);
        MultiDex.install(this);
        FirebaseApp.initializeApp(context);




        initialize(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new NetworkChangeReceiver(), intentFilter);
        chatDataBase = Room.databaseBuilder(getApplicationContext(),
                ChatDataBase.class, "myu_chat_db").allowMainThreadQueries().addMigrations(new Migration(8, 9) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE user_info "
                        + " ADD COLUMN on_whose_side text");
            }
        }).build();


        mNetWorkChangeReciver = new NetworkChangeReceiver();
        NetworkChangeReceiver.register(this, mNetWorkChangeReciver);
        setTempStorage();
        NetworkUtil.getInstance(context).initialize();



    }
    public static ChatDataBase getChatDataBase() {
        return chatDataBase;
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
    public static void startXMPP(Context context) {
        try {
            if (TempStorage.getUser()==null) {
                return;
            }

            LogUtils.newCheckerXMPP("startXMPP request call....");
            if (TempStorage.getXMPPHelper() == null) {
                TempStorage.setXMPPHelper(XMPPHelper.getInstance(context));
            }
            TempStorage.getXMPPHelper().connect();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.newCheckerXMPP("startXMPP request call ERROR " + e.getMessage());
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
        LogUtils.debug("MyApplication onTrimMemory " + level);

    }

    private void onAppForeground() {
        LogUtils.debug("AppStatus: Foreground");
        AppSharedPreferences.getInstance(context).setTotalBgTimeMillis(System.currentTimeMillis() - AppSharedPreferences.getInstance(context).getLastBgTimeMillis());
        try {
            TempStorage.getXMPPHelper().onAppForeground();
            TempStorage.getXMPPHelper().getMessages();

            if (TempStorage.getXMPPHelper().isConnected()) {
                TempStorage.getXMPPHelper().goOnline();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onAppBackground() {
        LogUtils.debug("AppStatus: Background");
        bgTime = System.currentTimeMillis();

        AppSharedPreferences.getInstance(context).setLastBgTimeMillis(bgTime);

        try {
            TempStorage.getXMPPHelper().onAppBackground();
            TempStorage.getXMPPHelper().sendPresence(new Presence(Presence.Type.unavailable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
