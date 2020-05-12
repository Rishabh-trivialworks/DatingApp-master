package com.quintus.labs.datingapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.Interest;
import com.quintus.labs.datingapp.xmpp.utils.MediaModel;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Varun John on 4/20/2018.
 */

public class Helper {


    public static void showPopMenu(Context context, View view, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.chat_menu, popup.getMenu());

        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
        }

        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.show();
    }

public static String getIntrestString(List<Interest> list){
        String result ="";
        StringBuilder intrestString=new StringBuilder();
    for (Interest intrest:list) {
        intrestString.append(intrest.getInterest()+",");
        result = intrestString.toString();
    }
    return result.substring(0,result.length()-1);
    }

public static void loadImage(Context context, List<ImageModel> media, String gender,ImageView profileImage){
    String url = "";
    if(media!=null&&media.size()>0){
        url = media.get(0).getUrl();
    }
    switch (gender) {
        case "Female":
            GlideUtils.loadImage(context,url,profileImage,R.drawable.default_woman);
            break;
        case "Male":
            GlideUtils.loadImage(context,url,profileImage,R.drawable.default_man);
            break;
        default:
            GlideUtils.loadImage(context,url,profileImage,R.drawable.default_man);
            break;
    }
}
    public static void loadImage(Context context, ImageModel media, String gender,ImageView profileImage){
        String url = "";
        if(media!=null){
            url = media.getUrl();
        }
        switch (gender) {
            case "Female":
                GlideUtils.loadImage(context,url,profileImage,R.drawable.default_woman);
                break;
            case "Male":
                GlideUtils.loadImage(context,url,profileImage,R.drawable.default_man);
                break;
            default:
                GlideUtils.loadImage(context,url,profileImage,R.drawable.default_man);
                break;
        }
    }

    public static void loadImage(Context context, ImageModel media, ImageView profileImage){
        String url = "";
            url = media.getUrl();


                GlideUtils.loadImage(context,url,profileImage,R.drawable.default_man);

    }
}
