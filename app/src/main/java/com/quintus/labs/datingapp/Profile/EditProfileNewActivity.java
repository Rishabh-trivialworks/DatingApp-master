package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class EditProfileNewActivity extends AppCompatActivity {
    private Context context;

    private SwitchCompat menSwitch,WomenSwitch;
    private CrystalRangeSeekbar ageSeekBar;
    private CrystalSeekbar maximumRangeBar;
    private ImageView imageViewOptions;
    TextView textViewMin,textViewMax,textViewAgeMin,textViewAgeMax;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_new);
        context = this;
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
        maximumRangeBar.setMaxValue(TempStorage.getUser().getMaxRange());
        maximumRangeBar.setMinValue(TempStorage.getUser().getMinRange());

        ageSeekBar.setMaxValue(TempStorage.getUser().getMaxRange());
        ageSeekBar.setMinValue(TempStorage.getUser().getMinRange());

        ageSeekBar.setGap(TempStorage.getUser().getMaxRange()-TempStorage.getUser().getMinRange());


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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
       getSupportActionBar().setDisplayShowTitleEnabled(false);
       getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_app_bar, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        imageViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText("Profile");

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
       getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
}
