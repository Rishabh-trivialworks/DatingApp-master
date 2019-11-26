package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.SplashActivity;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.rest.RequestModel.EditProfileUpdateRequest;
import com.quintus.labs.datingapp.rest.RequestModel.RegisterRequest;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class EditProfileNewActivity extends AppCompatActivity implements OnSpinnerItemSelectedListener {
    private Context mContext;

    private SwitchCompat menSwitch,WomenSwitch;
    private CrystalRangeSeekbar ageSeekBar;
    private CrystalSeekbar maximumRangeBar;
    private ImageView imageViewOptions;
    TextView textViewMin,textViewMax,textViewAgeMin,textViewAgeMax,textViewAction;
    RelativeLayout logoutLayout;
    private Toolbar toolbar;
    private UserData userInfo;
    private NiceSpinner nice_spinner_zodiac,nice_spinner_religion,nice_spinner_politicalLeanings,
            nice_spinner_kids,nice_spinner_pets,nice_spinner_lookingFor,
            nice_spinner_smoking,nice_spinner_drinking,nice_spinner_exercise,nice_spinner_education;
    private String zodiac="",religion="",politicalLeanings="",kids="",pets="",
            lookingFor="",smoking="",drinking="",exercise="",education="";
    private EditText input_height;

    List<String> datasetRelogion = new LinkedList<>(Arrays.asList("Agnostic", "Atheist", "Buddhist", "Christian", "Hindu", "Jewish", "Zoroastrian", "Sikh", "Spiritual", "Other"));
    List<String> datasetZodiac = new LinkedList<>(Arrays.asList("Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer", "Leo"));
    List<String> datasetPoliticalLeanings = new LinkedList<>(Arrays.asList("Apolitical", "Moderate", "Left", "Right"));
    List<String> datasetKids = new LinkedList<>(Arrays.asList("Want Someday", "Don’t want", "Have and want more", "Have and don’t want more"));
    List<String> datasetPets = new LinkedList<>(Arrays.asList("Dog(s)", "Cat(s)", "None", "Don’t Want", "Lots"));
    List<String> datasetLookingFor = new LinkedList<>(Arrays.asList("Relationship", "Something Casual", "Don’t Know yet", "Marriage"));
    List<String> datasetsmoking = new LinkedList<>(Arrays.asList("Socially", "Regularly", "Never"));
    List<String> datasetDrinking = new LinkedList<>(Arrays.asList("Socially", "Frequently", "Never"));
    List<String>  datasetEducation = new LinkedList<>(Arrays.asList("High School", "Vocational School", "In College", "Undergraduate Degree", "In Grad School", "Graduate Degree"));
    List<String> datasetExercise = new LinkedList<>(Arrays.asList("Active", "Sometimes", "Almost Never"));





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_new);
        mContext = this;
        userInfo=TempStorage.getUser();

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
        logoutLayout = findViewById(R.id.logoutLayout);

        textViewAgeMax =(TextView) findViewById(R.id.textViewAgeMax);
        textViewAgeMin =(TextView) findViewById(R.id.textViewAgeMin);
        input_height = findViewById(R.id.input_height);


        textViewMax.setText(TempStorage.getUser().getMaxRange()+"");
        textViewMin.setText(TempStorage.getUser().getMinRange()+"");

        textViewAgeMax.setText(TempStorage.getUser().getMaxRange()+"");
        textViewAgeMin.setText(TempStorage.getUser().getMinRange()+"");


        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setUpSpinnerData();
        maximumRangeBar.setMaxValue(100);
        maximumRangeBar.setMinValue(1);

        ageSeekBar.setMaxValue(100);
        ageSeekBar.setMinValue(1);



       // ageSeekBar.setGap(TempStorage.getUser().getMaxRange()-TempStorage.getUser().getMinRange());

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

        logoutLayout.setOnClickListener(v -> {
            TempStorage.logoutUser();
            Intent in = new Intent(EditProfileNewActivity.this, Login.class);
            startActivity(in);
            finish();

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
        double height = input_height.getText().toString().length()>0?Double.parseDouble(input_height.getText().toString()):0;
        EditProfileUpdateRequest updateRequest = new EditProfileUpdateRequest(userInfo.getEmail(), userInfo.getFullName(),"USER",userInfo.getGender(),userInfo.getDob(),intrestedIn,
             Integer.valueOf(textViewAgeMin.getText().toString()), Integer.valueOf(textViewAgeMax.getText().toString()), Integer.valueOf(textViewMin.getText().toString()),
              exercise,education,drinking,smoking,lookingFor,pets,kids,politicalLeanings,religion,zodiac,height);
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

  private void setUpSpinnerData(){
      nice_spinner_zodiac = findViewById(R.id.nice_spinner_zodiac);
      nice_spinner_religion = findViewById(R.id.nice_spinner_religion);
      nice_spinner_politicalLeanings = findViewById(R.id.nice_spinner_politicalLeanings);
      nice_spinner_kids = findViewById(R.id.nice_spinner_kids);
      nice_spinner_pets = findViewById(R.id.nice_spinner_pets);
      nice_spinner_lookingFor = findViewById(R.id.nice_spinner_lookingFor);
      nice_spinner_smoking = findViewById(R.id.nice_spinner_smoking);
      nice_spinner_drinking = findViewById(R.id.nice_spinner_drinking);
      nice_spinner_education = findViewById(R.id.nice_spinner_education);
      nice_spinner_exercise = findViewById(R.id.nice_spinner_exercise);



      nice_spinner_zodiac.attachDataSource(datasetZodiac);

      nice_spinner_religion.attachDataSource(datasetRelogion);

      nice_spinner_politicalLeanings.attachDataSource(datasetPoliticalLeanings);

      nice_spinner_kids.attachDataSource(datasetKids);

      nice_spinner_pets.attachDataSource(datasetPets);

      nice_spinner_lookingFor.attachDataSource(datasetLookingFor);

      nice_spinner_smoking.attachDataSource(datasetsmoking);

      nice_spinner_drinking.attachDataSource(datasetDrinking);

      nice_spinner_education.attachDataSource(datasetEducation);

      nice_spinner_exercise.attachDataSource(datasetExercise);


      nice_spinner_zodiac.setOnSpinnerItemSelectedListener(this);
      nice_spinner_religion.setOnSpinnerItemSelectedListener(this);
      nice_spinner_politicalLeanings.setOnSpinnerItemSelectedListener(this);
      nice_spinner_kids.setOnSpinnerItemSelectedListener(this);
      nice_spinner_pets.setOnSpinnerItemSelectedListener(this);
      nice_spinner_lookingFor.setOnSpinnerItemSelectedListener(this);
      nice_spinner_smoking.setOnSpinnerItemSelectedListener(this);
      nice_spinner_drinking.setOnSpinnerItemSelectedListener(this);
      nice_spinner_education.setOnSpinnerItemSelectedListener(this);
      nice_spinner_exercise.setOnSpinnerItemSelectedListener(this);

      if(userInfo!=null){
          if(userInfo.getZodiac()!=null&&userInfo.getZodiac().length()>0){
              nice_spinner_drinking.setSelectedIndex(datasetDrinking.indexOf(userInfo.getZodiac()));
          }

          if(userInfo.getReligion()!=null&&userInfo.getReligion().length()>0){
              nice_spinner_religion.setSelectedIndex(datasetRelogion.indexOf(userInfo.getReligion()));
          }

          if(userInfo.getKids()!=null&&userInfo.getKids().length()>0){
              nice_spinner_kids.setSelectedIndex(datasetKids.indexOf(userInfo.getKids()));
          }

          if(userInfo.getPets()!=null&&userInfo.getPets().length()>0){
              nice_spinner_pets.setSelectedIndex(datasetPets.indexOf(userInfo.getPets()));
          }

          if(userInfo.getLookingFor()!=null&&userInfo.getLookingFor().length()>0){
              nice_spinner_lookingFor.setSelectedIndex(datasetLookingFor.indexOf(userInfo.getLookingFor()));
          }
          if(userInfo.getSmoking()!=null&&userInfo.getSmoking().length()>0){
              nice_spinner_smoking.setSelectedIndex(datasetsmoking.indexOf(userInfo.getSmoking()));
          }

          if(userInfo.getDrinking()!=null&&userInfo.getDrinking().length()>0){
              nice_spinner_drinking.setSelectedIndex(datasetDrinking.indexOf(userInfo.getDrinking()));
          }

          if(userInfo.getEducation()!=null&&userInfo.getEducation().length()>0){
              nice_spinner_education.setSelectedIndex(datasetEducation.indexOf(userInfo.getEducation()));
          }

          if(userInfo.getExercise()!=null&&userInfo.getExercise().length()>0){
              nice_spinner_exercise.setSelectedIndex(datasetExercise.indexOf(userInfo.getExercise()));
          }

          if(userInfo.getHeight()!=null&&userInfo.getHeight()>0){
              input_height.setText(userInfo.getHeight()+"");
          }
      }




  }

    @Override
    public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.nice_spinner_zodiac:
                zodiac = (String) parent.getItemAtPosition(position);
                break;
            case R.id.nice_spinner_religion:
                religion = (String) parent.getItemAtPosition(position);

                break;
                case R.id.nice_spinner_exercise:
                    exercise = (String) parent.getItemAtPosition(position);

                    break;
            case R.id.nice_spinner_education:
                education = (String) parent.getItemAtPosition(position);

                break;
            case R.id.nice_spinner_drinking:
                drinking = (String) parent.getItemAtPosition(position);

                break;
                case R.id.nice_spinner_smoking:
                    smoking = (String) parent.getItemAtPosition(position);

                    break;
            case R.id.nice_spinner_lookingFor:
                lookingFor = (String) parent.getItemAtPosition(position);

                break;
            case R.id.nice_spinner_pets:
                pets = (String) parent.getItemAtPosition(position);

                break;
            case R.id.nice_spinner_kids:
                kids = (String) parent.getItemAtPosition(position);

                break;
            case R.id.nice_spinner_politicalLeanings:
                politicalLeanings = (String) parent.getItemAtPosition(position);

                break;
        }
    }
}
