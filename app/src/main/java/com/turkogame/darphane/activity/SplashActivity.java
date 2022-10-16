package com.turkogame.darphane.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tools.setSystemBarColor(this, R.color.system_bar);

        Intent intent = new Intent(SplashActivity.this,Login.class);
        startActivity(intent);
        finish();
    }
}