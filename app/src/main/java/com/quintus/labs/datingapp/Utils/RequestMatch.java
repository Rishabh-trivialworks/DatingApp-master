package com.quintus.labs.datingapp.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.quintus.labs.datingapp.Matched.Matched_Activity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.rest.RequestModel.AcceptRejectModel;
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;


public class RequestMatch extends Dialog implements View.OnClickListener {


    private final int navigatinFrom;

    @BindView(R.id.textViewthank_you)
    TextView textViewthankYou;
    @BindView(R.id.textViewReveiwSubmitted)
    TextView textViewReveiwSubmitted;
    @BindView(R.id.linearlayoutTopPart)
    LinearLayout linearlayoutTopPart;
    private boolean isThereOtherClassToRate;
    private Context mContext;
    @BindView(R.id.textViewNotNow)
    TextView textViewNotNow;

    @BindView(R.id.textViewReject)
    TextView textViewReject;

    String title,message,buttonTitle;
    UserData user;

    public RequestMatch(@NonNull Context context, String title, String message, String buttonTitle, UserData user, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
        this.title = title;
        this.message = message;
        this.buttonTitle = buttonTitle;
        this.user = user;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.request_match_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        textViewNotNow.setText(Html.fromHtml(buttonTitle));
        textViewReveiwSubmitted.setText(Html.fromHtml(message));
        textViewthankYou.setText(Html.fromHtml(title));

    }

    private void setListeners() {
        textViewNotNow.setOnClickListener(this);
        textViewReject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewNotNow: {
                requestFriend(AppConstants.ACCEPTED,user.getId());
                dismiss();
            }
            break;
            case R.id.textViewReject: {
                requestFriend(AppConstants.REJECTED,user.getId());
                dismiss();
            }
            break;

        }

    }

    private void requestFriend(String status,int id){
        Call<ResponseModel<MatchedFriend>> responseModelCall = RestServiceFactory.createService().requestFriend( new AcceptRejectModel(id,status));
        responseModelCall.enqueue(new RestCallBack<ResponseModel<MatchedFriend>>() {
            @Override
            public void onFailure(Call<ResponseModel<MatchedFriend>> call, String message) {

            }

            @Override
            public void onResponse(Call<ResponseModel<MatchedFriend>> call, Response<ResponseModel<MatchedFriend>> restResponse, ResponseModel<MatchedFriend> response) {
                if(isSuccessFull(response)){
                    if(response.data.getReceiverStatus().equalsIgnoreCase(AppConstants.ACCEPTED)&&response.data.getSenderStatus().equalsIgnoreCase(AppConstants.ACCEPTED)){

                    }

                }
               EventBroadcastHelper.sendMatchedRefresh();

            }
        });



    }


}

