package com.quintus.labs.datingapp.xmpp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.Utils.AppContext;
import com.quintus.labs.datingapp.Utils.JsonUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.xmpp.room.models.ChatMessage;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppSharedPreferences {

    private static AppSharedPreferences instance;

    private static final String MYU_APP_KEY_AUTH_TOKEN = "auth_token";
    private static final String PREF_USERINFO = "userinfo";
    private static final String USER_DEVICE_TOKEN = "user_device_token";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ID = "user_id";
    private static final String UNIVERSITY_ID = "university_id";
    private static final String NOTIFICATION_COUNT = "notificationcount";
    private static final String USER_DOB = "user_dob";
    private static final String USERISLOGIN = "user_login";
    private static final String USERNAME = "user_name";
    private static final String USERPIC = "user_pic";
    private static final String TWITTERNAME = "twitter_name";
    private static final String FACEBOOKNAME = "facebook_name";
    private static final String LAST_RECEIVED_DATA_TIME_XMPP = "last_received_data_time_xmpp ";
    private static final String ERROR_LAST_RECEIVED_DATA_TIME_XMPP = "error_last_received_data_time_xmpp ";
    private static final String LAST_SUCCESS_QUERY_TIME = "LastSuccessfulQueryTime ";
    private static final String REGISTRATIONSCREEN = "registration";
    private static final String IS_LOADING_FIRST_TIME = "is_loading_first_time";
    private static final String SHOULD_SEND_SEEN_RECEIPTS = "should_send_seen_receipts";
    private static final String IS_LUNCHED_FIRST_TIME = "is_lunched_first_time";
    private static final String IS_LUNCHED_INTRO_FIRST_TIME = "is_lunched_intro_first_time";
    private static final String FORCECHECK = "force";
    private static final String CHECKUNIVERSITYADD = "adduniversity";
    private static final String SEARCHCHATSEARCH = "searchchattext";
    private static final String PUSH_ID = "push_id";
    private static final String CHECKUNIVERSITyFROMGOOGLE = "checkuniversity";
    private static final String ISFIRSTTIMELOADINGFORV328 = "isFirstTimeLoadingForV328";
    private static final String ISFIRSTTIMELOADINGFORV329 = "isFirstTimeLoadingForV329";
    private static final String ISFIRSTTIMELOADINGFORV345 = "isFirstTimeLoadingForV345";
    private static final String API_BACKUP_BOOLEAN = "api_backup_boolean";
    private static final String IS_FIRST_TIME_USER = "is_firsttime_user";

    //From v2...
    private static final String PREF_NAME_USER_INFO = "userinfo";
    private static final String PREF_USERDEVICEID = "userdeviceid";

    //For 90 days logic and new Chat Api
    private static final String BACK_UP_TIME_STAMP = "back_up_time_stamp";
    private static final String CHAT_HISTORY_REQUIRED_AFTER_DAYS = "chat_history_required_after_days";
    private static final String BG_TIME_MILLIS = "bg_time_millis";
    private static final String TOTAL_BG_TIME_MILLIS = "total_bg_time_millis";
    private static final String LOGOUT_TIME_MILLIS = "logout_time_millis";
    private static final String LOGIN_SINCE_LAST_LOGOUT_TIME_MILLIS = "login_since_last_logout_time_millis";

    private String TAG = this.getClass().getSimpleName();
    private SharedPreferences mPrefs;
    private Editor mPrefsEditor;
    private String mCroppingToolValue = "mCroppingToolValue";
    private String mCroppedImagePath = "mCroppedImagePath";

    private Context context;
    private SharedPreferences chatPreferences;
    private Editor chatPreferencesEditor;
    private static final String PREF_NAME_CHAT_INFO = "pref_name_chat_info";

    private static final String AUTO_MANAGER_DIALOG_BOOLEAN = "auto_manager_dialog";
    private static final String CHAT_INTERACTIONS = "chat_interactions";

    private SharedPreferences getChatSharedPreferences() {
        if (chatPreferences == null) {
            chatPreferences = context.getSharedPreferences(PREF_NAME_CHAT_INFO, Context.MODE_PRIVATE);
        }
        return chatPreferences;
    }

    private Editor getChatSharedPreferencesEditor() {
        if (chatPreferencesEditor == null) {
            chatPreferencesEditor = getChatSharedPreferences().edit();
        }
        return chatPreferencesEditor;
    }

    public AppSharedPreferences(Context context) {
        this.context = context;
        this.mPrefs = context.getSharedPreferences(PREF_NAME_USER_INFO, Context.MODE_PRIVATE);
        this.mPrefsEditor = mPrefs.edit();
    }

    public static AppSharedPreferences getInstance() {

        if (instance == null) {
            instance = new AppSharedPreferences(AppContext.getInstance().getContext());
        }
        return instance;
    }

    public static AppSharedPreferences getInstance(Context context) {

        instance = new AppSharedPreferences(context);
        return instance;
    }


    public String getUserName() {
        return mPrefs.getString(USERNAME, "");
    }

    public void setUserName(String value) {
        mPrefsEditor.putString(USERNAME, value);
        mPrefsEditor.commit();
    }


    public String getPushId() {
        return mPrefs.getString(PUSH_ID, "0");
    }

    public void setPushId(String value) {
        mPrefsEditor.putString(PUSH_ID, value);
        mPrefsEditor.commit();
    }

    public String getFacebookName() {
        return mPrefs.getString(FACEBOOKNAME, "");
    }

    public void setFacebookName(String value) {
        mPrefsEditor.putString(FACEBOOKNAME, value);
        mPrefsEditor.commit();
    }

    public String getTwitterName() {
        return mPrefs.getString(TWITTERNAME, "");
    }

    public void setTwitternameName(String value) {
        mPrefsEditor.putString(TWITTERNAME, value);
        mPrefsEditor.commit();
    }

    public void setLoadingForFirstTime(boolean isFirstTime) {
        getChatSharedPreferencesEditor().putBoolean(IS_LOADING_FIRST_TIME, isFirstTime);
        getChatSharedPreferencesEditor().commit();

        if (isFirstTime) {
            setLastReceivedDataTimeXMPP(0);
        }

        //PreferencesManager.putBoolean(IS_LOADING_FIRST_TIME, isFirstTime);
    }

    public Boolean isLoadingForFirstTime() {
        return getChatSharedPreferences().getBoolean(IS_LOADING_FIRST_TIME, true);
        // return PreferencesManager.getBoolean(IS_LOADING_FIRST_TIME, true);
    }

    public boolean getShouldSentSeenReceipt() {
        return mPrefs.getBoolean(SHOULD_SEND_SEEN_RECEIPTS, true);
    }

    public void setShouldSentSeenReceipt(Boolean shouldSentSeenReceipt) {
        mPrefsEditor.putBoolean(SHOULD_SEND_SEEN_RECEIPTS, shouldSentSeenReceipt);
        mPrefsEditor.commit();
    }

    public boolean getFirstTimeChaeck() {
        return mPrefs.getBoolean(IS_FIRST_TIME_USER, true);
    }

    public void setFirstTimeCheck(Boolean shouldSentSeenReceipt) {
        mPrefsEditor.putBoolean(IS_FIRST_TIME_USER, shouldSentSeenReceipt);
        mPrefsEditor.commit();
    }

    public void setLunchedIntroForFirstTime(boolean isFirstTime) {
        PreferencesManager.putBoolean(IS_LUNCHED_INTRO_FIRST_TIME, isFirstTime);
    }

    public Boolean isLunchedIntroForFirstTime() {
        return PreferencesManager.getBoolean(IS_LUNCHED_INTRO_FIRST_TIME, true);
    }

    public void setLastReceivedDataTimeXMPP(long time) {
        PreferencesManager.putLong(LAST_RECEIVED_DATA_TIME_XMPP, time);
//        LogUtils.newMessagesXMPP("LastReceivedDataTimeXMPP time : " + TimeUtils.mamTimeFormat(time) + " timestamp " + time);
    }

    public void setErrorLastReceivedDataTimeXMPP(long time) {
        PreferencesManager.putLong(ERROR_LAST_RECEIVED_DATA_TIME_XMPP, time);
//        LogUtils.newMessagesXMPP("LastReceivedDataTimeXMPP time : " + TimeUtils.mamTimeFormat(time) + " timestamp " + time);
    }

    public long getLastReceivedDataTimeXMPP() {
        long val = PreferencesManager.getLong(LAST_RECEIVED_DATA_TIME_XMPP, 0);
        if (val == 0) {
            ChatMessage lastMessage = MyApplication.getChatDataBase().chatMessageDao().getLastMessage();
            if (lastMessage != null) {
                val = lastMessage.getTimestamp();
            }
        }
        return val;
    }

    public long getErrorLastReceivedDataTimeXMPP() {
        return PreferencesManager.getLong(ERROR_LAST_RECEIVED_DATA_TIME_XMPP, 0);
    }

    public void setLastQueryTimeMAM(long time) {
        PreferencesManager.putLong(LAST_SUCCESS_QUERY_TIME, time);
//        LogUtils.newMessagesXMPP("LastQueryTimeMAM time : " + TimeUtils.mamTimeFormat(time) + " timestamp " + time);
    }

    public long getLastQueryTimeMAM() {
        return PreferencesManager.getLong(LAST_SUCCESS_QUERY_TIME, -1);
    }

    public String getCheckuniversityadd() {
        return mPrefs.getString(CHECKUNIVERSITYADD, "");
    }

    public void setCheckuniversityadd(String value) {
        mPrefsEditor.putString(CHECKUNIVERSITYADD, value);
        mPrefsEditor.commit();
    }

    public String getCheckuniversityfromgoogle() {
        return mPrefs.getString(CHECKUNIVERSITyFROMGOOGLE, "");
    }

    public void setCheckuniversityfromgoogle(String value) {
        mPrefsEditor.putString(CHECKUNIVERSITyFROMGOOGLE, value);
        mPrefsEditor.commit();
    }

    public String getSearchChatText() {
        return mPrefs.getString(SEARCHCHATSEARCH, "");
    }

    public void setSearchChatText(String value) {
        mPrefsEditor.putString(SEARCHCHATSEARCH, value);
        mPrefsEditor.commit();
    }

    public String getRegistrationscreen() {
        return mPrefs.getString(REGISTRATIONSCREEN, "");
    }

    public void setRegistrationscreen(String value) {
        mPrefsEditor.putString(REGISTRATIONSCREEN, value);
        mPrefsEditor.commit();
    }

    public String getUserPic() {
        return mPrefs.getString(USERPIC, "");
    }

    public void setUserPic(String value) {
        mPrefsEditor.putString(USERPIC, value);
        mPrefsEditor.commit();
    }

    public String getAccessToken() {
        return mPrefs.getString(MYU_APP_KEY_AUTH_TOKEN, "");
    }

    public void setAccessToken(String value) {
        mPrefsEditor.putString(MYU_APP_KEY_AUTH_TOKEN, value);
        mPrefsEditor.commit();
    }

//    public UserDetailModel getSelectedCard() {
////        UserDetailModel mUserModel = null;
////        String details = mPrefs.getString(PREF_USERINFO, "");
////        if (details.length() != 0)
////            mUserModel = JsonUtils.fromJson(details, UserDetailModel.class);
//
//        return TempStorage.getUserDetailModel();
//    }
//
//    public void setSetSelected(UserDetailModel details) {
//        String value = "";
//        if (details != null)
//            value = JsonUtils.toJson(details);
//        mPrefsEditor.putString(PREF_USERINFO, value);
//        mPrefsEditor.commit();
//    }

    public String getUserDeviceToken() {
        String value = mPrefs.getString(USER_DEVICE_TOKEN, "");
        return value;
    }

    public int getUserId() {
        return mPrefs.getInt(USER_ID, 0);
    }

    public String getUniversityId() {
        try {
            return mPrefs.getString(UNIVERSITY_ID, "0");
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(mPrefs.getInt(UNIVERSITY_ID, 0));
        }
    }

    public void setUniversityId(int universityId) {
        mPrefsEditor.putString(UNIVERSITY_ID, String.valueOf(universityId));
        mPrefsEditor.commit();
    }

    public String getNotificationCount() {
        try {
            return mPrefs.getString(NOTIFICATION_COUNT, "0");
        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(mPrefs.getInt(NOTIFICATION_COUNT, 0));
        }
    }

    public void setNotificationCount(int count) {
        mPrefsEditor.putString(NOTIFICATION_COUNT, String.valueOf(count));
        mPrefsEditor.commit();
    }

    public void setUserDeviceToken(String value) {
        mPrefsEditor.putString(USER_DEVICE_TOKEN, value);
        mPrefsEditor.commit();
    }

    public String getUserDOB() {
        return mPrefs.getString(USER_DOB, "");
    }

    public void setUserUserDOB(String value) {
        mPrefsEditor.putString(USER_DOB, value);
        mPrefsEditor.commit();
    }

    public String getUserPassWord() {
        return mPrefs.getString(USER_PASSWORD, "");
    }

    public void setUserUserPassword(String value) {
        mPrefsEditor.putString(USER_PASSWORD, value);
        mPrefsEditor.commit();
    }

    public boolean isLogin() {
        return mPrefs.getBoolean(USERISLOGIN, false);
    }

    public void setLogin(boolean value) {
        mPrefsEditor.putBoolean(USERISLOGIN, value);
        mPrefsEditor.commit();
    }

    public void clearAllData(boolean val) {
        if (val) {
            mPrefsEditor.clear();
        } else {
            String username = AppSharedPreferences.getInstance().getUserName();
            String userpic = AppSharedPreferences.getInstance().getUserPic();
            String devicetoken = AppSharedPreferences.getInstance().getUserDeviceToken();
            String twittername = AppSharedPreferences.getInstance().getTwitterName();

            boolean isFirstTimeLoadingForV328 = AppSharedPreferences.getInstance().isFirstTimeLoadingForV328();
            boolean isFirstTimeLoadingForV329 = AppSharedPreferences.getInstance().isFirstTimeLoadingForV329();
            boolean autoManagerDialogShown = AppSharedPreferences.getInstance().getAutoManagerDialogShown();

            int userId = 0;
            try {
                userId = TempStorage.getUser().getId();
            } catch (Exception e) {
                e.printStackTrace();
            }

            TempStorage.setAuthToken(null);
            TempStorage.setUserId(0);

            boolean isIntro = AppSharedPreferences.getInstance().isLunchedIntroForFirstTime();

            mPrefsEditor.clear();

            mPrefsEditor.putInt(USER_ID, userId);
            mPrefsEditor.putString(USERNAME, username);
            mPrefsEditor.putString(USERPIC, userpic);
            mPrefsEditor.putString(USER_DEVICE_TOKEN, devicetoken);

            mPrefsEditor.putString(TWITTERNAME, twittername);
            mPrefsEditor.putBoolean(ISFIRSTTIMELOADINGFORV328, isFirstTimeLoadingForV328);
            mPrefsEditor.putBoolean(ISFIRSTTIMELOADINGFORV329, isFirstTimeLoadingForV329);
            mPrefsEditor.putBoolean(AUTO_MANAGER_DIALOG_BOOLEAN, autoManagerDialogShown);
            mPrefsEditor.putBoolean(IS_LUNCHED_INTRO_FIRST_TIME, isIntro);
        }

    }

    public void saveFavorites(Context context, List<Integer> unreadChat) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(unreadChat);

        mPrefsEditor.putString("Unreadchat", jsonFavorites);

        mPrefsEditor.commit();
    }

    public ArrayList<Integer> getFavorites(Context context) {
        List<Integer> favorites;
        if (mPrefs.contains("Unreadchat")) {
            String jsonFavorites = mPrefs.getString("Unreadchat", null);
            Gson gson = new Gson();
            Integer[] favoriteItems = gson.fromJson(jsonFavorites,
                    Integer[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Integer>(favorites);
        } else {
            return null;
        }
        return (ArrayList<Integer>) favorites;
    }

    public String getUserDeviceID() {
        return mPrefs.getString(PREF_USERDEVICEID, null);
    }

    public void setUserDeviceID(String userDeviceID) {
        mPrefsEditor.putString(PREF_USERDEVICEID, userDeviceID);
        mPrefsEditor.commit();
    }

    public void setFirstTimeLoadingForV328(boolean isFirstTimeLoadingForV328) {
        mPrefsEditor.putBoolean(ISFIRSTTIMELOADINGFORV328, isFirstTimeLoadingForV328);
        mPrefsEditor.commit();
    }

    public boolean isFirstTimeLoadingForV328() {
        return mPrefs.getBoolean(ISFIRSTTIMELOADINGFORV328, true);
    }

    public void setFirstTimeLoadingForV329(boolean isFirstTimeLoadingForV329) {
        mPrefsEditor.putBoolean(ISFIRSTTIMELOADINGFORV329, isFirstTimeLoadingForV329);
        mPrefsEditor.commit();
    }

    public boolean isFirstTimeLoadingForV329() {
        return mPrefs.getBoolean(ISFIRSTTIMELOADINGFORV329, true);
    }

//    public void setFirstTimeLoadingForChat345(boolean isFirstTimeLoadingForV328) {
//        mPrefsEditor.putBoolean(ISFIRSTTIMELOADINGFORV345, isFirstTimeLoadingForV328);
//        mPrefsEditor.commit();
//    }
//
//    public boolean isFirstTimeLoadingForV345() {
//        return mPrefs.getBoolean(ISFIRSTTIMELOADINGFORV345, true);
//    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// NEW CHAT API LOGIC /////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getChatHistoryRequiredAfterDays() {
        return getChatSharedPreferences().getInt(CHAT_HISTORY_REQUIRED_AFTER_DAYS, AppConstants.Limits.CHAT_INACTIVE_USER_DAYS_LIMIT);
    }

    public void setChatHistoryRequiredAfterDays(int chatHistoryRequiredAfterDays) {
        getChatSharedPreferencesEditor().putInt(CHAT_HISTORY_REQUIRED_AFTER_DAYS, chatHistoryRequiredAfterDays);
        getChatSharedPreferencesEditor().commit();
    }

    public long getLastBgTimeMillis() {
        return getChatSharedPreferences().getLong(BG_TIME_MILLIS, System.currentTimeMillis());
    }

    public void setLastBgTimeMillis(long bgTimeMillis) {
        getChatSharedPreferencesEditor().putLong(BG_TIME_MILLIS, bgTimeMillis);
        getChatSharedPreferencesEditor().commit();
    }

    public long getTotalBgTimeMillis() {
        return getChatSharedPreferences().getLong(TOTAL_BG_TIME_MILLIS, 0);
    }

    public void setTotalBgTimeMillis(long bgTotalTimeMillis) {
        getChatSharedPreferencesEditor().putLong(TOTAL_BG_TIME_MILLIS, bgTotalTimeMillis);
        getChatSharedPreferencesEditor().commit();
    }

    public long getLogoutTimeMillis() {
        return getChatSharedPreferences().getLong(LOGOUT_TIME_MILLIS, System.currentTimeMillis());
    }

    public void setLogoutTimeMillis(long logoutTimeMillis) {
        getChatSharedPreferencesEditor().putLong(LOGOUT_TIME_MILLIS, logoutTimeMillis);
        getChatSharedPreferencesEditor().commit();
    }

    public long getLoginSinceLastLogoutTimeMillis() {
        return getChatSharedPreferences().getLong(LOGIN_SINCE_LAST_LOGOUT_TIME_MILLIS, 0);
    }

    public void setLoginSinceLastLogoutTimeMillis(long loginSinceLastLogoutTimeMillis) {
        getChatSharedPreferencesEditor().putLong(LOGIN_SINCE_LAST_LOGOUT_TIME_MILLIS, loginSinceLastLogoutTimeMillis);
        getChatSharedPreferencesEditor().commit();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getAutoManagerDialogShown() {
        return mPrefs.getBoolean(AUTO_MANAGER_DIALOG_BOOLEAN, false);
    }

    public void setAutoManagerDialogShown(boolean shown) {
        mPrefsEditor.putBoolean(AUTO_MANAGER_DIALOG_BOOLEAN, shown);
        mPrefsEditor.commit();
    }

    public boolean getChatInteraction(int userId) {
        return mPrefs.getString(CHAT_INTERACTIONS, "").contains(userId + ";");
    }

    public void removeChatInteraction(int userId) {
        mPrefsEditor.putString(CHAT_INTERACTIONS, mPrefs.getString(CHAT_INTERACTIONS, "").replace(userId + ";", ""));
        mPrefsEditor.commit();
    }

    public void saveChatInteracted(int userId) {
        mPrefsEditor.putString(CHAT_INTERACTIONS, mPrefs.getString(CHAT_INTERACTIONS, "") + userId + ";");
        mPrefsEditor.commit();
    }
}
