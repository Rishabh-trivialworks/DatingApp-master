package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.quintus.labs.datingapp.R;

public class EditProfileNewActivity extends AppCompatActivity {
    private Context context;

    private SwitchCompat menSwitch,WomenSwitch;
    private CrystalRangeSeekbar ageSeekBar;
    private BubbleThumbSeekbar maximumRangeBar;
    private ImageView imageViewOptions;
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
        maximumRangeBar =(BubbleThumbSeekbar)findViewById(R.id.rangeSeekbarmaximum);
        ageSeekBar =(CrystalRangeSeekbar)findViewById(R.id.rangeSeekbaragerange);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
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
