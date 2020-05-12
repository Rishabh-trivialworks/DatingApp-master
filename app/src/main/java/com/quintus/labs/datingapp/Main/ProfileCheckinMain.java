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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;
    String profileImageUrl;
    int id;
    CardList card;

    @BindView(R.id.profileRecyclerView)
    RecyclerView profileRecyclerView;

    @BindView(R.id.close)
    Button close;

    public Activity activity;
    private ProfileAdapter adapter;


    public static void open(Context context,CardList card){
        context.startActivity(new Intent(context, ProfileCheckinMain.class).putExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT, card));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        activity = this;
        mContext = ProfileCheckinMain.this;
        ButterKnife.bind(activity);
        close.setOnClickListener(v -> {
            finish();
        });
        if (getIntent().hasExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT)) {
            card = (CardList) getIntent().getSerializableExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT);
            setAdapter();
        }
    }

    private void setAdapter(){
        adapter = new ProfileAdapter(this);
       // adapter.setClickListener(this);
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileRecyclerView.setHasFixedSize(true);
        profileRecyclerView.setAdapter(adapter);
        try {
            ((SimpleItemAnimator) profileRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug(e.getMessage());
        }
        adapter.addItem(card);
        if(card.getMedia()!=null&&card.getMedia().size()>1){
            for(int i =1;i<card.getMedia().size();i++){
                if(card.getMedia().get(i).getType().equals(AppConstants.ApiParamKey.IMAGE))
                adapter.addItem(card.getMedia().get(i));
            }
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


}

