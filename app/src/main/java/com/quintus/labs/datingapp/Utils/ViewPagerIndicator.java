package com.quintus.labs.datingapp.Utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class ViewPagerIndicator {

    public static int STYLE_SMALL = 1;
    public static int STYLE_NORMAL = 2;
    public static int STYLE_BIG = 3;

    private ViewPager viewPager;
    private int selectedImageResource, unselectedImageResource;
    private int indicatorCount;
    private ViewGroup indicatorView;
    private List<ImageView> indicatorList;
    private int indicatorPosition;

    private int size;
    private int style;

    private int dp, padding, indicatorSize;
    private Context context;

    public ViewPagerIndicator(Context context, int style) {
        this.context = context;
        this.style = style;
        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public void setup(ViewPager viewPager, ViewGroup indicatorView, int selectedImageResource, int unselectedImageResource) {
        this.viewPager = viewPager;
        this.indicatorView = indicatorView;
        this.selectedImageResource = selectedImageResource;
        this.unselectedImageResource = unselectedImageResource;

        indicatorCount = viewPager.getAdapter().getCount();
        indicatorList = new ArrayList<>(indicatorCount);

        if (style == STYLE_SMALL) {
            padding = 2 * dp;
            indicatorSize = 10 * dp;
        } else if (style == STYLE_NORMAL) {
            padding = 3 * dp;
            indicatorSize = 14 * dp;
        } else if (style == STYLE_BIG) {
            padding = 4 * dp;
            indicatorSize = 18 * dp;
        }

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(padding, padding, padding, padding);

        indicatorView.addView(linearLayout);

        for (int count = 0; count < indicatorCount; count++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(indicatorSize, indicatorSize));
            imageView.setPadding(padding, padding, padding, padding);
            linearLayout.addView(imageView);
            indicatorList.add(imageView);
        }

        indicatorPosition = viewPager.getCurrentItem();
        setIndicator(indicatorPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorPosition = position;
                setIndicator(indicatorPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setIndicator(int position) {
        for (int index = 0; index < indicatorList.size(); index++) {
            ImageView indicator = indicatorList.get(index);
            if (index == position)
                indicator.setImageResource(selectedImageResource);
            else
                indicator.setImageResource(unselectedImageResource);
        }
    }
}
