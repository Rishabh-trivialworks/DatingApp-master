package com.quintus.labs.datingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quintus.labs.datingapp.Login.Login;
import com.quintus.labs.datingapp.Main.MainActivity;
import com.quintus.labs.datingapp.Utils.TempStorage;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_splash);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                if(TempStorage.getUser()!=null){
                    Intent in=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }else{
                    Intent in = new Intent(SplashActivity.this,Login.class);
                    startActivity(in);
                    finish();
                }
            }
        }, 3000);


    }
}
