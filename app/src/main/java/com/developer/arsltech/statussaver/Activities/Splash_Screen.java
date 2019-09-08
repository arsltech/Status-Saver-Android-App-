package com.developer.arsltech.statussaver.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developer.arsltech.statussaver.R;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        Thread splash=new Thread(){

            @Override
            public void run() {
                try {
                    sleep(1500);

                    Intent sp =new Intent(Splash_Screen.this,MainActivity.class);
                    startActivity(sp);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splash.start();

    }
}
