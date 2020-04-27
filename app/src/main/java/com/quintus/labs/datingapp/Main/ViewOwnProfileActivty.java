package com.quintus.labs.datingapp.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.rest.Response.UserData;

public class ViewOwnProfileActivty extends Activity {
    private Context mContext;
    String profileImageUrl;
    UserData userInfo;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = this;

       /* ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/

        TextView profileName = findViewById(R.id.name_main);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileBio = findViewById(R.id.bio_beforematch);
        TextView profileInterest = findViewById(R.id.interests_beforematch);
        TextView profileDistance = findViewById(R.id.distance_main);
        LinearLayout layoutBio = findViewById(R.id.layoutBio);
        LinearLayout layoutIntrest = findViewById(R.id.layoutIntrest);
        Button close = findViewById(R.id.close);
        findViewById(R.id.bottamLayout).setVisibility(View.GONE);

        Intent intent = getIntent();

        userInfo = (UserData)intent.getSerializableExtra("userinfo");

        id = userInfo.getId();


        String append = (userInfo.getDistance() == 1) ? "mile away" : "miles away";

        profileDistance.setText(userInfo.getDistance() + " " + append);
        profileName.setText(userInfo.getFullName());
        if(userInfo.getBio()!=null&&userInfo.getBio().length()>0){
            profileBio.setText(userInfo.getBio());
        }else{
            layoutBio.setVisibility(View.GONE);
        }
        if(userInfo.getInterests()!=null&&userInfo.getInterests().size()>0){
            StringBuffer sb =new StringBuffer();
            for( int i=0;i<userInfo.getInterests().size();i++) {
                sb.append(userInfo.getInterests().get(i).getInterest());
            }
            profileInterest.setText(sb);
        }else{
            layoutIntrest.setVisibility(View.GONE);
        }

        if(userInfo.getMedia()!=null && userInfo.getMedia().size()>0) {
            profileImageUrl = userInfo.getMedia().get(0).getUrl();
        }

        switch (userInfo.getGender()) {
            case "Female":
                GlideUtils.loadImage(mContext,profileImageUrl,profileImage, R.drawable.default_woman);
                break;
            case "Male":
                GlideUtils.loadImage(mContext,profileImageUrl,profileImage,R.drawable.default_man);
                break;
            default:
                GlideUtils.loadImage(mContext,profileImageUrl,profileImage,R.drawable.default_man);
                break;
        }
        close.setOnClickListener(v -> {
            finish();
        });
    }


}
