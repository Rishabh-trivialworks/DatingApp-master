package com.quintus.labs.datingapp.Utils;


import com.quintus.labs.datingapp.rest.Response.LoginData;
import com.quintus.labs.datingapp.rest.Response.UserData;

import com.quintus.labs.datingapp.xmpp.XMPPHelper;
import com.shawnlin.preferencesmanager.PreferencesManager;

/**
 * Created by MyU10 on 1/4/2017.
 */

public class TempStorage {

    private static int userId;
    public static String authToken = "";
    public static String fcmToken = "";


    public static String version = "not available";

    public static LoginData loginData;
    public  static UserData userData;
    private static XMPPHelper xmppHelper;


    public static LoginData getLoginData() {
        return loginData;
    }

    public static void setLoginData(LoginData loginData) {
        PreferencesManager.putObject(AppConstants.Pref.LOGIN_MODEL_OBJECT, loginData);


    }

    public static UserData getUserData() {
        return userData;
    }


    public static UserData getUser() {
        return PreferencesManager.getObject(AppConstants.Pref.USER_MODEL_OBJECT,UserData.class);
    }



    public static void setUserData(UserData userData) {
        PreferencesManager.putObject(AppConstants.Pref.USER_MODEL_OBJECT, userData);
        TempStorage.userData=userData;
    }
    public static String getFcmToken() {
        fcmToken = PreferencesManager.getString(AppConstants.Pref.FCM_TOKEN);
        return fcmToken;
    }

    public static void setFCMToken(String authToken) {
        PreferencesManager.putString(AppConstants.Pref.FCM_TOKEN,authToken);
        TempStorage.fcmToken = authToken;
    }
    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        PreferencesManager.putString(AppConstants.Pref.AUTH_TOKEN,authToken);
        TempStorage.authToken = authToken;
    }
    public static XMPPHelper getXMPPHelper() {
        if (xmppHelper == null) {
            xmppHelper = XMPPHelper.getInstance(AppContext.getInstance().getContext());
        }
        return xmppHelper;
    }

    public static void setXMPPHelper(XMPPHelper xmppHelper) {
        TempStorage.xmppHelper = xmppHelper;
    }


    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        TempStorage.userId = userId;
    }


    public static void logoutUser() {
        setUserData(null);
    }
}
