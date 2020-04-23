package com.quintus.labs.datingapp.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.rest.Response.UserData;

import org.jivesoftware.smack.chat.Chat;

public class MyService extends Service {

    private static final String DOMAIN = "149.28.149.131";
    public static ConnectivityManager cm;
    public static MyXMPP xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";
    private Messenger messageHandler;
    private UserData myuser;

    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<MyService>(this);

    }

    public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        myuser = TempStorage.getUser();
        String user = myuser.getId()+"@localhost";
        String password = myuser.getId()+"";
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = MyXMPP.getInstance(MyService.this, DOMAIN, user, password);
        xmpp.connect("onCreate");
    }



    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {


        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.connection.disconnect();
    }

    public static boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null;
    }


    public void sendMessage(int i) {
        Message message = Message.obtain();
        switch (i) {
            case 1:
                message.arg1 = 1;
                break;
        }
        try {
            messageHandler.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}