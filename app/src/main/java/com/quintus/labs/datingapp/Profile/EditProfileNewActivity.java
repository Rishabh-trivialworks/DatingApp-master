package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.crystal.crystalrangeseekbar.widgets.BubbleThumbSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.quintus.labs.datingapp.R;

public class EditProfileNewActivity extends AppCompatActivity {
    private Context context;

    private SwitchCompat menSwitch,WomenSwitch;
    private CrystalRangeSeekbar ageSeekBar;
    private BubbleThumbSeekbar maximumRangeBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_new);

        context = this;

        initView();
    }

    private void initView() {

        menSwitch =(SwitchCompat)findViewById(R.id.men_switch);
        WomenSwitch =(SwitchCompat)findViewById(R.id.women_switch);
        maximumRangeBar =(BubbleThumbSeekbar)findViewById(R.id.rangeSeekbarmaximum);
        ageSeekBar =(CrystalRangeSeekbar)findViewById(R.id.rangeSeekbaragerange);
    }
}
