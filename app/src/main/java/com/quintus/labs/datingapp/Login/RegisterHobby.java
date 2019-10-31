package com.quintus.labs.datingapp.Login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.R;
import com.quintus.labs.datingapp.Utils.AppConstants;
import com.quintus.labs.datingapp.Utils.GpsUtils;
import com.quintus.labs.datingapp.Utils.TempStorage;
import com.quintus.labs.datingapp.Utils.ToastUtils;
import com.quintus.labs.datingapp.Utils.User;
import com.quintus.labs.datingapp.rest.RequestModel.RegisterRequest;
import com.quintus.labs.datingapp.rest.Response.ResponseModel;
import com.quintus.labs.datingapp.rest.Response.UserData;
import com.quintus.labs.datingapp.rest.RestCallBack;
import com.quintus.labs.datingapp.rest.RestServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


/**
 * DatingApp
 * https://github.com/quintuslabs/DatingApp
 * Created on 25-sept-2018.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

public class RegisterHobby extends AppCompatActivity {
    private static final String TAG = "RegisterHobby";

    //User Info
    User userInfo;
    String password;

    private Context mContext;
    private Button hobbiesContinueButton;
    private Button sportsSelectionButton;
    private Button travelSelectionButton;
    private Button musicSelectionButton;
    private Button fishingSelectionButton;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    private boolean isGPS = false;
    private LocationCallback locationCallback;
    private String append = "";
    private double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hobby);
        mContext = RegisterHobby.this;

        Log.d(TAG, "onCreate: started");

        Intent intent = getIntent();
        userInfo = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        initWidgets();

        init();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
        getLocation();
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {

                    }
                }
            }
        };
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(RegisterHobby.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RegisterHobby.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterHobby.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.LOCATION_REQUEST);

        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(RegisterHobby.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                       latitude = location.getLatitude();
                       longitude = location.getLongitude();
                    } else {
                        if (ActivityCompat.checkSelfPermission(RegisterHobby.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegisterHobby.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                }
            });
        }
    }

    private void initWidgets() {
        sportsSelectionButton = findViewById(R.id.sportsSelectionButton);
        travelSelectionButton = findViewById(R.id.travelSelectionButton);
        musicSelectionButton = findViewById(R.id.musicSelectionButton);
        fishingSelectionButton = findViewById(R.id.fishingSelectionButton);
        hobbiesContinueButton = findViewById(R.id.hobbiesContinueButton);

        // Initially all the buttons needs to be grayed out so this code is added, on selection we will enable it later



        sportsSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportsButtonClicked();
            }
        });

        travelSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travelButtonClicked();
            }
        });

        musicSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicButtonClicked();
            }
        });

        fishingSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fishingButtonClicked();
            }
        });


    }

    public void sportsButtonClicked() {
        // this is to toggle between selection and non selection of button
        toggleView(sportsSelectionButton,userInfo.isSports());

        if (userInfo.isSports()==true) {
            userInfo.setSports(false);
        } else {
            userInfo.setSports(true);
        }

    }

    public void travelButtonClicked() {
        // this is to toggle between selection and non selection of button
        toggleView(travelSelectionButton,userInfo.isTravel());

        if (userInfo.isTravel()==true) {
            userInfo.setTravel(false);
        } else {
            userInfo.setTravel(true);
        }
        toggleView(travelSelectionButton,userInfo.isTravel());

    }

    public void musicButtonClicked() {
        // this is to toggle between selection and non selection of button
        toggleView(musicSelectionButton,userInfo.isMusic());

        if (userInfo.isMusic()==true) {
            userInfo.setMusic(false);
         } else {
            userInfo.setMusic(true);
        }

    }

    public void fishingButtonClicked() {
        // this is to toggle between selection and non selection of button
        toggleView(fishingSelectionButton,userInfo.isFishing());
        if (userInfo.isFishing()==true) {
            userInfo.setFishing(false);
        } else {
            userInfo.setFishing(true);

        }

    }
    private void toggleView(Button view,boolean isSelected){
        if (isSelected==true) {
            view.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(getResources().getDrawable(R.drawable.background_rounded_border));
            }
            else{
                view.setBackgroundResource(R.drawable.background_rounded_border);

            }
        } else {
            view.setTextColor(getResources().getColor(R.color.white));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(getResources().getDrawable(R.drawable.white_rounded_button));
            }
            else{
                view.setBackgroundResource(R.drawable.white_rounded_button);

            }

        }

    }
    private void registerToApp() {
        ArrayList<String> interest= new ArrayList<>();
        if(userInfo.isMusic()){
            interest.add("Music");
        }
        if(userInfo.isSports()){
            interest.add("Sports");
        }
        if(userInfo.isFishing()){
            interest.add("Fishing");
        }
        if(userInfo.isTravel()){
            interest.add("Travel");
        }
        RegisterRequest registerRequest = new RegisterRequest(userInfo.getEmail(), password, userInfo.getUsername(),"USER",userInfo.getSex(),userInfo.getDateOfBirth(),userInfo.getPreferSex(),longitude,latitude,interest);
        Call<ResponseModel<UserData>> responseModelCall = RestServiceFactory.createService().signup(registerRequest);
        responseModelCall.enqueue(new RestCallBack<ResponseModel<UserData>>() {
            @Override
            public void onFailure(Call<ResponseModel<UserData>> call, String message) {
                ToastUtils.show(mContext, message);
                hobbiesContinueButton.setText("Register");

            }

            @Override
            public void onResponse(Call<ResponseModel<UserData>> call, Response<ResponseModel<UserData>> restResponse, ResponseModel<UserData> response) {
                hobbiesContinueButton.setText("Register");
                if (RestCallBack.isSuccessFull(response)) {
                    TempStorage.setUserData(response.data);
                    TempStorage.userData = response.data;
                    ToastUtils.show(mContext, response.data.getFullName());
                    Intent in=new Intent(mContext, MainActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    ToastUtils.show(mContext, response.errorMessage);
                }
            }
        });
    }
    public void init() {
        hobbiesContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hobbiesContinueButton.setText("Please Wait...");
                registerToApp();


            }
        });
    }


    //----------------------------------------Firebase----------------------------------------


}
