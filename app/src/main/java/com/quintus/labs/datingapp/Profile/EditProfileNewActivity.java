package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.rest.RequestModel.EditProfileUpdateRequest;
import com.quintus.labs.datingapp.rest.RequestModel.RegisterRequest;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import retrofit2.Call;
import retrofit2.Response;

public class EditProfileNewActivity extends AppCompatActivity {
    private Context mContext;

    private SwitchCompat menSwitch,WomenSwitch;
    private CrystalRangeSeekbar ageSeekBar;
    private CrystalSeekbar maximumRangeBar;
    private ImageView imageViewOptions;
    TextView textViewMin,textViewMax,textViewAgeMin,textViewAgeMax,textViewAction;
    private Toolbar toolbar;
    private UserData userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_new);
        mContext = this;
        initView();
        setToolBar();
    }

    private void initView() {

        menSwitch =(SwitchCompat)findViewById(R.id.men_switch);
        WomenSwitch =(SwitchCompat)findViewById(R.id.women_switch);
        maximumRangeBar =(CrystalSeekbar)findViewById(R.id.rangeSeekbarmaximum);
        ageSeekBar =(CrystalRangeSeekbar)findViewById(R.id.rangeSeekbaragerange);
        textViewMin =(TextView) findViewById(R.id.textViewMin);
        textViewMax =(TextView) findViewById(R.id.textViewMax);

        textViewAgeMax =(TextView) findViewById(R.id.textViewAgeMax);
        textViewAgeMin =(TextView) findViewById(R.id.textViewAgeMin);


        textViewMax.setText(TempStorage.getUser().getMaxRange()+"");
        textViewMin.setText(TempStorage.getUser().getMinRange()+"");

        textViewAgeMax.setText(TempStorage.getUser().getMaxRange()+"");
        textViewAgeMin.setText(TempStorage.getUser().getMinRange()+"");


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        maximumRangeBar.setMaxValue(100);
        maximumRangeBar.setMinValue(1);

        ageSeekBar.setMaxValue(100);
        ageSeekBar.setMinValue(1);



       // ageSeekBar.setGap(TempStorage.getUser().getMaxRange()-TempStorage.getUser().getMinRange());
        userInfo=TempStorage.getUser();

        if(userInfo.getDistance()>0){
            maximumRangeBar.setMinStartValue(userInfo.getDistance());
            maximumRangeBar.apply();
            textViewMin.setText(String.valueOf(userInfo.getDistance()));
        }
        if(userInfo.getMinRange()>0&&userInfo.getMaxRange()>0){
            ageSeekBar.setMinStartValue(userInfo.getMinRange());
            ageSeekBar.setMaxStartValue(userInfo.getMaxRange());
            ageSeekBar.apply();
            textViewAgeMin.setText(String.valueOf(userInfo.getMinRange()));
            textViewAgeMax.setText(String.valueOf(userInfo.getMaxRange()));


        }

        if(userInfo.getInterested()!=null){
            if(userInfo.getInterested().equalsIgnoreCase("Female")){
                WomenSwitch.setChecked(true);
                menSwitch.setChecked(false);

            }else{
                WomenSwitch.setChecked(false);
                menSwitch.setChecked(true);
            }
        }

        menSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    menSwitch.setChecked(true);
                    WomenSwitch.setChecked(false);
                }else{
                    menSwitch.setChecked(false);
                    WomenSwitch.setChecked(true);
                }


            }
        });
        WomenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    menSwitch.setChecked(true);
                    WomenSwitch.setChecked(false);
                }else{
                    menSwitch.setChecked(false);
                    WomenSwitch.setChecked(true);
                }


            }
        });
        ageSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                textViewAgeMin.setText(String.valueOf(minValue));
                textViewAgeMax.setText(String.valueOf(maxValue));
            }
        });

        maximumRangeBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                textViewMin.setText(String.valueOf(value));
            }
        });
    }

    private void setToolBar() {

        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(mContext).inflate(R.layout.view_app_bar, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        textViewAction = v.findViewById(R.id.textViewAction);
        imageViewOptions.setVisibility(View.GONE);
        textViewAction.setVisibility(View.VISIBLE);

        imageViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        updateProfile();
            }
        });
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText("Profile");

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
       getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
  private void updateProfile(){
        String intrestedIn = menSwitch.isChecked()?"Male":"Female";
      EditProfileUpdateRequest updateRequest = new EditProfileUpdateRequest(userInfo.getEmail(), userInfo.getFullName(),"USER",userInfo.getGender(),userInfo.getDob(),intrestedIn,
             Integer.valueOf(textViewAgeMin.getText().toString()), Integer.valueOf(textViewAgeMax.getText().toString()), Integer.valueOf(textViewMin.getText().toString()) );
      Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().updateUser(updateRequest);
      responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
          @Override
          public void onFailure(Call<ResponseModel<UserData>> call, String message) {
              ToastUtils.show(mContext, message);
             // hobbiesContinueButton.setText("Register");

          }

          @Override
          public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
              //hobbiesContinueButton.setText("Register");
              if (RestCallBack.isSuccessFull(response)) {
                  TempStorage.setUserData(response.data);
                  TempStorage.userData = response.data;
                  ToastUtils.show(mContext, "Profile updated successfully");

              } else {
                  ToastUtils.show(mContext, response.errorMessage);
              }
          }
      });
  }

}
