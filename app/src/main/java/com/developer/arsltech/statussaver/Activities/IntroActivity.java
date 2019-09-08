package com.developer.arsltech.statussaver.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.developer.arsltech.statussaver.R;
import com.developer.arsltech.statussaver.Utils.Constants;



public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("STEP 1", "Tap to open WhatsApp.", R.drawable.intro_one, Color.parseColor("#00695c")));
        addSlide(AppIntroFragment.newInstance("STEP 2", "View Recent Status and Open Easy Status Saver.", R.drawable.intro_three, Color.parseColor("#2B3A3D")));
        addSlide(AppIntroFragment.newInstance("STEP 3", "Download Status you want!", R.drawable.intro_two, Color.parseColor("#2B3A3D")));
        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        SharedPreferences.Editor editor = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }
}
