package com.quintus.labs.datingapp.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.User;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterGenderPrefection extends AppCompatActivity {

    String password;
    User user;
    String preferMale;
    private Button preferenceContinueButton;
    private Button hobbiesContinueButton;
    private Button maleSelectionButton;
    private Button femaleSelectionButton;
    private Button bothSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gender_prefection);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");
        maleSelectionButton = findViewById(R.id.maleSelectionButton);
        femaleSelectionButton = findViewById(R.id.femaleSelectionButton);
        bothSelectionButton = findViewById(R.id.bothSelectionButton);
        preferenceContinueButton = findViewById(R.id.genderContinueButton);
        maleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButtonSelected();
            }
        });

        femaleSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleButtonSelected();
            }
        });
        bothSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bothButtonSelected();
            }
        });

        preferenceContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!preferMale.isEmpty()){
                    openAgeEntryPage();

                }else{
                    ToastUtils.show(RegisterGenderPrefection.this,"Please select your preference");
                }
            }
        });


    }

    public void maleButtonSelected() {
        preferMale = "Male";
        toggleView(maleSelectionButton,femaleSelectionButton,bothSelectionButton);

    }

    public void femaleButtonSelected() {
        preferMale = "Female";
        toggleView(femaleSelectionButton,maleSelectionButton,bothSelectionButton);

    }
    public void bothButtonSelected() {
        preferMale = "Both";
        toggleView(bothSelectionButton,maleSelectionButton,femaleSelectionButton);

    }
    private void toggleView(Button view,Button view1,Button view2){
            view.setTextColor(getResources().getColor(R.color.white));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(getResources().getDrawable(R.drawable.white_rounded_button));
            }
            else{
                view.setBackgroundResource(R.drawable.white_rounded_button);

            }
            view1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            view2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view1.setBackground(getResources().getDrawable(R.drawable.background_rounded_border));
                view2.setBackground(getResources().getDrawable(R.drawable.background_rounded_border));
            }
            else{
                view1.setBackgroundResource(R.drawable.background_rounded_border);
                view2.setBackgroundResource(R.drawable.background_rounded_border);


            }



    }

    public void openAgeEntryPage() {

        user.setPreferSex(preferMale);
        Intent intent = new Intent(this, RegisterAge.class);
        intent.putExtra("password", password);
        intent.putExtra("classUser", user);
        startActivity(intent);
    }
}
