package com.quintus.labs.datingapp.Main;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.Helper;
import com.quintus.labs.datingapp.rest.Response.CardList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileMainViewHolder extends RecyclerView.ViewHolder  {

    @BindView(R.id.flexBoxLayout)
    public FlexboxLayout flexBoxLayout;


    @BindView(R.id.name_main)
    public TextView profileName;
    @BindView(R.id.profileImage)
    public ImageView profileImage;
    @BindView(R.id.bio_beforematch)
    public TextView profileBio;
    @BindView(R.id.interests_beforematch)
    public TextView profileInterest;
    @BindView(R.id.distance_main)
    public TextView profileDistance;
    @BindView(R.id.layoutBio)
    public LinearLayout layoutBio;
    @BindView(R.id.layoutIntrest)
    public LinearLayout layoutIntrest;
    @BindView(R.id.layoutUserInfoOthers)
    public LinearLayout layoutUserInfoOthers;



    public View viewRoot;
    private ArrayList<View> views;




    public ProfileMainViewHolder(@NonNull View itemView) {
        super(itemView);
        viewRoot = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bind(Object item, Context context, int position){
        if(item!=null&&item instanceof CardList){
            CardList card = (CardList)item;
            flexBoxLayout.setFlexDirection(FlexDirection.ROW);
            views = new ArrayList<>();

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

            Helper.loadImage(context,card.getMedia(),card.getGender(),profileImage);

            if(flexBoxLayout.getChildCount()==0) {
                addViewToFlexLayout(card,context);
            }
        }


    }
    private SpannableString getSpanableString(String title){
        SpannableString ss1=  new SpannableString(title);
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
        return ss1;
    }

   private void  addViewToFlexLayout(CardList card,Context context){
       if (card.getInterested() != null && !card.getInterested().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);
           addTag(getSpanableString("Intrested In : ") + card.getInterested(), context, "Intrested");
       }
       if (card.getDob() != null && !card.getDob().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("DOB : ") + card.getDob(), context, "DOB");

       }
       if (card.getHeight() > 0) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Height : ") + "" + card.getHeight(), context, "Height");

       }

       if (card.getEducation() != null && !card.getEducation().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Education : ") + card.getEducation(), context, "Education");

       }

       if (card.getExercise() != null && !card.getExercise().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Exercise : ") + card.getExercise(), context, "Exercise");

       }

       if (card.getSmoking() != null && !card.getSmoking().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Smoking Habit : ") + card.getSmoking(), context, "Smoking");

       }
       if (card.getDrinking() != null && !card.getDrinking().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Drinking Habit : ") + card.getDrinking(), context, "Drinking");

       }
       if (card.getLookingFor() != null && !card.getLookingFor().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Looking For : ") + card.getLookingFor(), context, "Looking");

       }
       if (card.getPoliticalLeanings() != null && !card.getPoliticalLeanings().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Polotical Leanings : ") + card.getPoliticalLeanings(), context, "Polotical");

       }

       if (card.getReligion() != null && !card.getReligion().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Religion : ") + card.getReligion(), context, "Religion");

       }
       if (card.getZodiac() != null && !card.getZodiac().isEmpty()) {
           layoutUserInfoOthers.setVisibility(View.VISIBLE);

           addTag(getSpanableString("Zodiac : ") + card.getZodiac(), context, "Zodiac");

       }
       if (views != null && views.size() > 0) {
           for (View view : views) {
               flexBoxLayout.addView(view);
           }
       }

   }

    private void addTag(final String classesFilter,Context mContext,String tag) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.view_about_tag, flexBoxLayout, false);
        ImageView imageLeft = view.findViewById(R.id.imageLeft);
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(classesFilter);
        views.add(view);
    }

}
