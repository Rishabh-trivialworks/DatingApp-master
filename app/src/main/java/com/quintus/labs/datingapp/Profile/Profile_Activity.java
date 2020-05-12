package com.quintus.labs.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.quintus.labs.datingapp.BoostPaidPlans.BoostPlans;
import com.quintus.labs.datingapp.Main.ProfileCheckinMain;
import com.quintus.labs.datingapp.Main.ViewOwnProfileActivty;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.GlideUtils;
import com.quintus.labs.datingapp.Utils.InfoScreenData;
import com.quintus.labs.datingapp.Utils.LogUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.TopNavigationViewHelper;
import com.quintus.labs.datingapp.Utils.ViewPagerIndicator;
import com.quintus.labs.datingapp.chatview.activities.ImageFFActivity;
import com.quintus.labs.datingapp.rest.ProgressRequestBody;
import com.quintus.labs.datingapp.rest.Response.ImageModel;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_Activity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Profile_Activity";
    private static final int ACTIVITY_NUM = 0;
    static boolean active = false;

    private Context mContext = Profile_Activity.this;
    private CircleImageView imagePerson;
    private TextView name;

    private String userId;

    public Button buttonRegister,btn_activate;
    public Context context;
    private ChoosePhotoHelper choosePhotoHelper;
    private UserData userInfo;
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: create the page");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        buttonRegister = findViewById(R.id.btn_login);
        btn_activate = findViewById(R.id.btn_activate);
        parentLayout =findViewById(R.id.parentLayout);

        context=this;

        setupTopNavigationView();
        userInfo=TempStorage.getUser();

        imagePerson = findViewById(R.id.circle_profile_image);
        name =  findViewById(R.id.name);
        choosePhotoHelper = ChoosePhotoHelper.with(this)
                .asFilePath()
                .build(photo -> {
//                    Glide.with(context)
//                            .load(photo)
//                            .into(imagePerson);
                    fileUpload(photo);
                });
        name.setText(userInfo.getFullName());

        ImageButton edit_btn = findViewById(R.id.edit_profile);
        edit_btn.setOnClickListener(v -> {
            Intent intent = new Intent(Profile_Activity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        imagePerson.setOnClickListener(v -> {
            choosePhotoHelper.showChooser();

        });
        imagePerson.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(userInfo.getMedia()!=null&&userInfo.getMedia().size()>0){
                    imagePerson.setTransitionName("profileImageTransion");
                    ImageFFActivity.open(context,userInfo.getMedia().get(userInfo.getMedia().size()-1).getUrl(),imagePerson);

                }

                return false;
            }
        });

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(v -> {
                Intent intent = new Intent(Profile_Activity.this, EditProfileNewActivity.class);
                startActivity(intent);
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewOwnProfileActivty.class);
                intent.putExtra("userinfo",userInfo);
                 mContext.startActivity(intent);
            }
        });
        btn_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoostPlans.open(context,userInfo);
            }
        });
        setUserInfo();
    }

    private void setUserInfo(){
        if(userInfo!=null&&userInfo.getMedia()!=null&&userInfo.getMedia().size()>0){
           // GlideUtils.loadImage(context,userInfo.getMedia().get(userInfo.getMedia().size()-1).getUrl(),imagePerson);
            Glide.with(context)
                    .load(userInfo.getMedia().get(userInfo.getMedia().size()-1).getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imagePerson);

        }
        name.setText(userInfo.getFullName());
        if(!userInfo.isPremiumUser()){
            parentLayout.setVisibility(View.VISIBLE);

        }else{
            parentLayout.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume to the page");

    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    public void fileUpload(String filePath) {
        File file = new File(filePath);
           uploadImage(file);
    }

    private void uploadImage(File file){

        try {
            file = new Compressor.Builder(this)
                    .setMaxWidth(1280)
                    .setMaxHeight(1280)
                    .setQuality(90)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(context.getCacheDir().getPath())
                    .build()
                    .compressToFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                LogUtils.debug("Progress:- "+percentage);

            }

            @Override
            public void onError() {
            }

            @Override
            public void onFinish() {
            }
        });

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), fileBody);


        Call<ResponseModel<ImageModel>> call = RestServiceFactory.createService().uploadImage(body);
        call.enqueue(new Callback<ResponseModel<ImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Response<ResponseModel<ImageModel>> response) {
                Log.d("Response: " , response.message());
                getUser();


            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel<ImageModel>> call, @NonNull Throwable t) {
                Log.d("Exception: ",t.getMessage());
            }
        });
    }


    private void getUser(){

        Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().getUserDetails();
        responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
            @Override
            public void onFailure(Call<ResponseModel<UserData>> call, String message) {
                ToastUtils.show(mContext, message);

            }

            @Override
            public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
                if (RestCallBack.isSuccessFull(response)) {
                    TempStorage.setUserData(response.data);
                    TempStorage.userData = response.data;
                    userInfo = TempStorage.getUser();
                    setUserInfo();

                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }






}
