package com.quintus.labs.datingapp.Utils;


import com.quintus.labs.datingapp.rest.Response.UserData;

import com.shawnlin.preferencesmanager.PreferencesManager;

/**
 * Created by MyU10 on 1/4/2017.
 */

public class TempStorage {

    private static int userId;
    public static String authToken = "";


    public static String version = "not available";

    public static LoginData loginData;
    public  static UserData userData;


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

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        PreferencesManager.putString(AppConstants.Pref.AUTH_TOKEN,authToken);
        TempStorage.authToken = authToken;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        TempStorage.userId = userId;
    }


}
