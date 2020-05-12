package com.quintus.labs.datingapp.Checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.BoostPaidPlans.PlanModel;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.EventBroadcastHelper;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.TransparentProgressDialog;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentIntentRequest;
import com.quintus.labs.datingapp.rest.RequestModel.PaymentResultRequest;
import com.quintus.labs.datingapp.rest.Response.CardList;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;
import com.quintus.labs.datingapp.rest.model.PaymentIntentModel;
import com.quintus.labs.datingapp.rest.model.PaymentResultModel;
import com.quintus.labs.datingapp.xmpp.utils.AppConstants;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.lang.ref.WeakReference;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    public static void open(Context context, PlanModel plan){
        context.startActivity(new Intent(context, CheckoutActivity.class).putExtra(AppConstants.DataKey.PLAN_DETAIL_MODEL_OBJECT, plan));

    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.planName)
    TextView planName;
    @BindView(R.id.planPrice)
    TextView planPrice;
    @BindView(R.id.planTag)
    TextView planTag;

    @BindView(R.id.cardInputWidget)
    CardInputWidget cardInputWidget;

    @BindView(R.id.payButton)
    Button payButton;

    private Stripe stripe;
    private String paymentIntentClientSecret;





    ImageView imageViewOptions;
    TextView textViewAction;

    PlanModel plan;
    Context context;
    private TransparentProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        context = this;
        ButterKnife.bind(this);

        setToolBar();
        pd = new TransparentProgressDialog(this, R.drawable.spinner);

        if (getIntent().hasExtra(AppConstants.DataKey.PLAN_DETAIL_MODEL_OBJECT)) {

            plan = (PlanModel) getIntent().getSerializableExtra(AppConstants.DataKey.PLAN_DETAIL_MODEL_OBJECT);
            setPlan();
            getPaymentIntentClientSecret();


        }else{
            finish();
        }
        payButton.setOnClickListener((View view) -> {
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
               pd.show();
               ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                final Context context = getApplicationContext();
                stripe = new Stripe(
                        context,
                        PaymentConfiguration.getInstance(context).getPublishableKey()
                );
                stripe.confirmPayment(this, confirmParams);

            }
        });
    }

    private void setPlan(){
       planName.setText(plan.getName());

        String planPriceString = String.format(context.getResources().getString(R.string.plan_price),plan.getPrice(),"INR",getPlanExpiryString(plan.getExpiry()));

        planPrice.setText(planPriceString);
        planTag.setVisibility(View.VISIBLE);

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
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText("Checkout");

        getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
    public String getPlanExpiryString(long expiry){
        long oneDayExpiryOneDay = 86400;
        if(expiry/oneDayExpiryOneDay==1){
            return context.getResources().getString(R.string.day);
        }
        if(expiry/oneDayExpiryOneDay==7){
            return context.getResources().getString(R.string.week);
        }
        if(expiry/oneDayExpiryOneDay==30){
            return context.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==90){
            return context.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==180){
            return context.getResources().getString(R.string.month);
        }
        if(expiry/oneDayExpiryOneDay==360){
            return context.getResources().getString(R.string.year);
        }
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<CheckoutActivity> activityRef;

        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        gson.toJson(paymentIntent),
                        true
                );
                paymentIntent.getId();
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage(),
                        false
                );
            }
            String id =  paymentIntent.getId();
            activity.assignPackageToCustomer(id);
        }

        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString(), false);
            activity.dismissProgress();
           // activity.finish();

        }
    }

    private void displayAlert(String payment_completed, String toJson, boolean b) {
        if(b){
            ToastUtils.show(context,payment_completed);
        }else{
            ToastUtils.show(context,payment_completed);

        }
    }

    private void getPaymentIntentClientSecret(){
        pd.show();

        Call<ResponseModel<PaymentIntentModel>> responseModelCall = RestServiceFactory.createService().getPaymentIntent(new PaymentIntentRequest(plan.getId()));

        responseModelCall.enqueue(new RestCallBack<ResponseModel<PaymentIntentModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<PaymentIntentModel>> call, String message) {
                dismissProgress();
                ToastUtils.show(context, message);
                finish();

            }

            @Override
            public void onResponse(Call<ResponseModel<PaymentIntentModel>> call, Response<ResponseModel<PaymentIntentModel>> restResponse, ResponseModel<PaymentIntentModel> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    paymentIntentClientSecret = response.data.getClient_secret();
                } else {
                    ToastUtils.show(context, response.errorMessage);
                    finish();
                }
                dismissProgress();

            }
        });

    }
    private void assignPackageToCustomer(String id){
        Call<ResponseModel<PaymentResultModel>> responseModelCall = RestServiceFactory.createService().payment(new PaymentResultRequest(id));
        responseModelCall.enqueue(new RestCallBack<ResponseModel<PaymentResultModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<PaymentResultModel>> call, String message) {
                dismissProgress();
                ToastUtils.show(context, message);
                finish();

            }

            @Override
            public void onResponse(Call<ResponseModel<PaymentResultModel>> call, Response<ResponseModel<PaymentResultModel>> restResponse, ResponseModel<PaymentResultModel> response) {
                dismissProgress();
                if (RestCallBack.isSuccessFull(response)) {
                    EventBroadcastHelper.sendUserUpdate();
                    finish();
                } else {
                    ToastUtils.show(context, response.errorMessage);
                    finish();
                }
            }
        });
    }
    private void dismissProgress() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
