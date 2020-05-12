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
import com.quintus.labs.datingapp.rest.Response.MatchedFriend;

import butterknife.BindView;
import butterknife.ButterKnife;



public class SuperLikeDialog extends Dialog implements View.OnClickListener {


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

String title,message,buttonTitle;

    public SuperLikeDialog(@NonNull Context context, String title,String message,String buttonTitle, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
        this.title = title;
        this.message = message;
        this.buttonTitle = buttonTitle;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_thank_you_dialog);
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewNotNow: {
                Intent intent3 = new Intent(mContext, Matched_Activity.class);
                mContext.startActivity(intent3);
                dismiss();
            }
            break;
        }

    }


}
