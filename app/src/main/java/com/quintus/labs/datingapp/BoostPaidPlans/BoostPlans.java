package com.quintus.labs.datingapp.BoostPaidPlans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quintus.labs.datingapp.Checkout.CheckoutActivity;
import com.quintus.labs.datingapp.Main.Cards;
import com.quintus.labs.datingapp.MyApplication;
import com.quintus.labs.datingapp.Profile.MyRecyclerViewAdapter;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.ZoomOutPageTransformer;
import com.quintus.labs.datingapp.chat.ChattingActivity;
import com.quintus.labs.datingapp.eventbus.Events;
import com.quintus.labs.datingapp.eventbus.GlobalBus;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class BoostPlans extends AppCompatActivity implements PlanListAdapter.ItemClickListener {
    Context context;
    Activity activity;
    PlanListAdapter adapter;

    @BindView(R.id.recyclerPlansList)
    RecyclerView recyclerPlansList;

    ImageView imageViewOptions;
    TextView textViewAction;


    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.layoutNoData)
    LinearLayout layoutNoData;

    @BindView(R.id.view_pager2)
    ViewPager2 viewPager2;

    @BindView(R.id.dots_indicator)
    DotsIndicator dots_indicator;

    ArrayList<PlanModel> planList;
    CardList card;
    UserData userData;
    private Handler handler;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserUpdate(final Events.UserUpdate userUpdate) {
        closeActivity();
    }

    private void closeActivity() {
        finish();
    }

    public static void open(Context context, CardList card){
        context.startActivity(new Intent(context, BoostPlans.class).putExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT, card));

    }
    public static void open(Context context, UserData card){
        context.startActivity(new Intent(context, BoostPlans.class).putExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT, card));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_plans);
        context = this;
        activity = this;
        planList = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper());

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);
        setToolBar();
        if (getIntent().hasExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT)) {

            card = (CardList) getIntent().getSerializableExtra(AppConstants.DataKey.CARD_DETAIL_MODEL_OBJECT);


        }
        if (getIntent().hasExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT)) {

            userData = (UserData) getIntent().getSerializableExtra(AppConstants.DataKey.USER_DETAIL_MODEL_OBJECT);


        }
        setUpViewPagerAdapter();
        setUpRecyclerView();
        getPlanList();




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
        textViewAction = v.findViewById(R.id.textViewAction);
        imageViewOptions.setVisibility(View.GONE);
        textViewAction.setVisibility(View.GONE);

        imageViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textViewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateProfile();
            }
        });
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText("Plans");

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
    private void setUpRecyclerView() {

        adapter = new PlanListAdapter(this);
        adapter.setClickListener(this);
        recyclerPlansList.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerPlansList.setHasFixedSize(true);
        recyclerPlansList.setAdapter(adapter);
        try {
            ((SimpleItemAnimator) recyclerPlansList.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.debug(e.getMessage());
        }
    }

    private void setUpViewPagerAdapter(){
        ArrayList<Object> list = new ArrayList<>();
        list.add("First");
        list.add("Second");
        list.add("Third");
        BoostViewPagerAdapter viewPagerAdapter =  new BoostViewPagerAdapter(this,list);
        viewPagerAdapter.setClickListener(this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setAdapter(viewPagerAdapter);

        ZoomOutPageTransformer zoomOutPageTransformer = new  ZoomOutPageTransformer();
        viewPager2.setPageTransformer((page, position) -> zoomOutPageTransformer.transformPage(page,position));
        dots_indicator.setViewPager2(viewPager2);


    }

    private void getPlanList(){
        Call<ResponseModel<List<PlanModel>>> responseModelCall = RestServiceFactory.createService().getPlanList();


        responseModelCall.enqueue(new RestCallBack<ResponseModel<List<PlanModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<PlanModel>>> call, String message) {
                checkEmptyData();
            }

            @Override
            public void onResponse(Call<ResponseModel<List<PlanModel>>> call, Response<ResponseModel<List<PlanModel>>> restResponse, ResponseModel<List<PlanModel>> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    planList = (ArrayList<PlanModel>) response.data;
                    if(planList!=null&&planList.size()>0){
                        adapter.addPlans(planList);
                        adapter.notifyDataSetChanged();
                    }
                    checkEmptyData();


                }
            }
        });
    }

    public void checkEmptyData() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (adapter == null || adapter.getItemCount() ==0) {
                    layoutNoData.setVisibility(View.VISIBLE);
                    recyclerPlansList.setVisibility(View.GONE);
                } else
                    layoutNoData.setVisibility(View.GONE);
                recyclerPlansList.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setLocalData(){
        planList.add(new PlanModel(1,"1 month","650.00 INR /mo ","POPULAR"));
        planList.add(new PlanModel(2,"Lifetime","5000.00 INR ","One Time Payment"));
        planList.add(new PlanModel(2,"6 Month","400.00 INR /mo","One Time Payment"));
        planList.add(new PlanModel(2,"3 Month","450.00 INR /mo","One Time Payment"));
        planList.add(new PlanModel(2,"3 Month","450.00 INR /mo","One Time Payment"));


    }

    @Override
    public void onItemClick(View view, int position, Object model) {
        CheckoutActivity.open(context, (PlanModel) model);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);

    }
}
