package com.quintus.labs.datingapp.Utils;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.quintus.labs.datingapp.BuildConfig;
import com.quintus.labs.datingapp.R;


public class FirebaseRemoteConfigHelper {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 0;
    private Context mContext;
    public static FirebaseRemoteConfigHelper firebaseRemoteConfigHelper;



    public static FirebaseRemoteConfigHelper getFirebaseRemoteConfigHelper(Context mContext) {
        if (firebaseRemoteConfigHelper == null) {
            firebaseRemoteConfigHelper = new FirebaseRemoteConfigHelper(mContext);
        }
        return firebaseRemoteConfigHelper;
    }

private FirebaseRemoteConfigHelper(){

}
    private FirebaseRemoteConfigHelper(Context mContext) {
     this.mContext = mContext;
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_configure_defaults);
    }

    public void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetch(getCacheExpiration())
                    .addOnCompleteListener((Activity) mContext, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If is successful, activated fetched
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.fetchAndActivate();
                            }
                        }
                    });



    }

    public long getCacheExpiration() {
        // If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }

    public String getRemoteConfigValue(String key){
        return mFirebaseRemoteConfig.getString(key);
    }
}
