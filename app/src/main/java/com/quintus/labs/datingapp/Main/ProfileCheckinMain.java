package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;


public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    String profileImageUrl;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = ProfileCheckinMain.this;

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

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String bio = intent.getStringExtra("bio");
        String interest = intent.getStringExtra("interest");
        int distance = intent.getIntExtra("distance", 1);
         id = intent.getIntExtra("id", -1);
         String gender = intent.getStringExtra("gender");

        String append = (distance == 1) ? "mile away" : "miles away";

        profileDistance.setText(distance + " " + append);
        profileName.setText(name);
        if(bio!=null&&bio.length()>0){
            profileBio.setText(bio);
        }else{
            layoutBio.setVisibility(View.GONE);
        }
        if(interest!=null&&interest.length()>0){
            profileInterest.setText(interest);
        }else{
            layoutIntrest.setVisibility(View.GONE);
        }


        profileImageUrl = intent.getStringExtra("photo");

        switch (gender) {
            case "Female":
                GlideUtils.loadImage(mContext,profileImageUrl,profileImage,R.drawable.default_woman);
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


    public void DislikeBtn(View v) {

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
        btnClick.putExtra("id", id);

        startActivity(btnClick);

    }

    public void LikeBtn(View v) {
            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", profileImageUrl);
        btnClick.putExtra("id", id);

        startActivity(btnClick);

    }

}
