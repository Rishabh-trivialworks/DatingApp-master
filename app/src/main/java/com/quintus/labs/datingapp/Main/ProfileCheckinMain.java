package com.quintus.labs.datingapp.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    String profileImageUrl;
    int id;
    CardList card;
    @BindView(R.id.flexBoxLayout)
    public FlexboxLayout flexBoxLayout;
    @BindView(R.id.scrollView)
    public ScrollView scrollView;

    public Activity activity;


    public static void open(Context context,CardList card){
        context.startActivity(new Intent(context, ProfileCheckinMain.class).putExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT, card));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);
        activity = this;

        ButterKnife.bind(activity);

        flexBoxLayout.setFlexDirection(FlexDirection.ROW);
        mContext = ProfileCheckinMain.this;
        if (getIntent().hasExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT)) {

            card = (CardList) getIntent().getSerializableExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT);


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

        profileDistance.setText(String.format("%.2f", card.getHowFar()) + " " + append);
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

            Helper.loadImage(mContext,card.getMedia(),card.getGender(),profileImage);


        if(card.getInterested()!=null&&!card.getInterested().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Intrested In : "));
            infoOthers.append(card.getInterested()+" \n");
            addTag(getSpanableString("Intrested In : ")+card.getInterested());
        }
        if(card.getDob()!=null&&!card.getDob().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("DOB : "));
            infoOthers.append(card.getDob()+" \n");
            addTag(getSpanableString("DOB : ")+card.getDob());

        }
        if(card.getHeight()>0){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Height : "));
            infoOthers.append(card.getHeight()+" CM "+" \n");
            addTag(getSpanableString("Height : ")+""+card.getHeight());

        }

        if(card.getEducation()!=null&&!card.getEducation().isEmpty()){
            layoutUserInfoOthers.setVisibility(View.VISIBLE);
            infoOthers.append(getSpanableString("Education : "));
            infoOthers.append(card.getEducation()+" \n");
            addTag(getSpanableString("Education : ")+card.getEducation());

        }

            if(card.getExercise()!=null&&!card.getExercise().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Exercise : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Exercise : ")+card.getExercise());

            }

            if(card.getSmoking()!=null&&!card.getSmoking().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Smoking Habit : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Smoking Habit : ")+card.getSmoking());

            }
            if(card.getDrinking()!=null&&!card.getDrinking().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Drinking Habit : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Drinking Habit : ")+card.getDrinking());

            }
            if(card.getLookingFor()!=null&&!card.getLookingFor().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Looking For : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Looking For : ")+card.getLookingFor());

            }
            if(card.getPoliticalLeanings()!=null&&!card.getPoliticalLeanings().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Polotical Leanings : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Polotical Leanings : ")+card.getPoliticalLeanings());

            }

            if(card.getReligion()!=null&&!card.getReligion().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Religion : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Religion : ")+card.getReligion());

            }
            if(card.getZodiac()!=null&&!card.getZodiac().isEmpty()){
                layoutUserInfoOthers.setVisibility(View.VISIBLE);
                infoOthers.append(getSpanableString("Zodiac : "));
                infoOthers.append(card.getEducation()+" \n");
                addTag(getSpanableString("Zodiac : ")+card.getZodiac());

            }
            close.setOnClickListener(v -> {
           finish();
        });
    }
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

    private void addTag(final String classesFilter) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.view_about_tag, flexBoxLayout, false);
        ImageView imageLeft = view.findViewById(R.id.imageLeft);
        TextView textView = view.findViewById(R.id.textView);
         textView.setText(classesFilter);



        checkTags();



        flexBoxLayout.addView(view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void checkTags() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(scrollView);
        }

//        if (filterAdapter.getClassesFiltersSelected().isEmpty()) {
//            if (scrollView.getVisibility() != View.GONE) {
//                scrollView.setVisibility(View.GONE);
//            }
//        } else {
//            if (scrollView.getVisibility() != View.VISIBLE) {
//                scrollView.setVisibility(View.VISIBLE);
//            }
//        }
    }

}

