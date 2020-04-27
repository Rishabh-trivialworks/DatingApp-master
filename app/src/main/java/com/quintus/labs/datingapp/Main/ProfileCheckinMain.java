package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;


public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    String profileImageUrl;
    int id;
    CardList card;

    public static void open(Context context,CardList card){
        context.startActivity(new Intent(context, ProfileCheckinMain.class).putExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT, card));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = ProfileCheckinMain.this;
        if (getIntent().hasExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT)) {

            card = (CardList) getIntent().getSerializableExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT);


        }

        TextView profileName = findViewById(R.id.name_main);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileBio = findViewById(R.id.bio_beforematch);
        TextView profileInterest = findViewById(R.id.interests_beforematch);
        TextView profileDistance = findViewById(R.id.distance_main);
        LinearLayout layoutBio = findViewById(R.id.layoutBio);
        LinearLayout layoutIntrest = findViewById(R.id.layoutIntrest);
        LinearLayout layoutUserInfoOthers = findViewById(R.id.layoutUserInfoOthers);
        TextView infoOthers = findViewById(R.id.infoOthers);
        Button close = findViewById(R.id.close);


        String append = (card.getHowFar() == 1) ? "mile away" : "miles away";

        profileDistance.setText(card.getHowFar() + " " + append);
        profileName.setText(card.getFullName());
        if(card.getAbout()!=null&&card.getAbout().length()>0){
            profileBio.setText(card.getAbout());
        }else{
            layoutBio.setVisibility(View.GONE);
        }
        if(card.getInterests()!=null&&card.getInterests().size()>0){

            profileInterest.setText(Helper.getIntrestString(card.getInterests()));
        }else{
            layoutIntrest.setVisibility(View.GONE);
        }
        String url = "";
        if(card.getMedia()!=null&&card.getMedia().size()>0){
         url = card.getMedia().get(0).getUrl();
        }
        switch (card.getGender()) {
            case "Female":
                GlideUtils.loadImage(mContext,url,profileImage,R.drawable.default_woman);
                break;
            case "Male":
                GlideUtils.loadImage(mContext,url,profileImage,R.drawable.default_man);
                break;
            default:
                GlideUtils.loadImage(mContext,url,profileImage,R.drawable.default_man);
                break;
        }

        if(card.getInterested()!=null&&!card.getInterested().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Intrested In : "));
            infoOthers.append(card.getInterested()+" ");
        }
        if(card.getDob()!=null&&!card.getDob().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("DOB : "));
            infoOthers.append(card.getDob()+" ");
        }
        if(card.getHeight()>0){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Height : "));
            infoOthers.append(card.getHeight()+"CM ");
        }
        if(card.getExercise()!=null&&!card.getExercise().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Exercise : "));
            infoOthers.append(card.getExercise()+" ");
        }
        if(card.getEducation()!=null&&!card.getEducation().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Education : "));
            infoOthers.append(card.getEducation()+" ");
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
    private SpannableString getSpanableString(String title){
        SpannableString ss1=  new SpannableString(title);
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
        return ss1;
    }

}
