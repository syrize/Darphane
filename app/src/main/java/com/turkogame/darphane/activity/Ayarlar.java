package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;

public class Ayarlar extends AppCompatActivity {
    SwitchCompat acilis_muzigi,bildirim_sesleri,uygulama_sesleri;
    SharedPreferences sharedPreferences;
    int acilis,bildirim,uygulama,deneme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);
        deneme=1;
        acilis_muzigi = (SwitchCompat) findViewById(R.id.acilis_muzigi);
        bildirim_sesleri = (SwitchCompat) findViewById(R.id.bildirim_sesleri);
        uygulama_sesleri = (SwitchCompat) findViewById(R.id.uygulama_sesleri);

        sharedPreferences = getApplicationContext().getSharedPreferences("ayarlar", 0);
        acilis = sharedPreferences.getInt("acilis",0);
        bildirim = sharedPreferences.getInt("bildirim",0);
        uygulama = sharedPreferences.getInt("uygulama",0);

        if (acilis==1){acilis_muzigi.setChecked(true);} else {acilis_muzigi.setChecked(false);}
        if (bildirim==1){bildirim_sesleri.setChecked(true);} else {bildirim_sesleri.setChecked(false);}
        if (uygulama==1){uygulama_sesleri.setChecked(true);} else {uygulama_sesleri.setChecked(false);}



        acilis_muzigi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplicationContext().getSharedPreferences("ayarlar", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (acilis_muzigi.isChecked()){
                    editor.putInt("acilis",1);
                } else {
                    editor.putInt("acilis",0);
                }

                editor.commit();
            }
        });

        bildirim_sesleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplicationContext().getSharedPreferences("ayarlar", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (bildirim_sesleri.isChecked()){
                    editor.putInt("bildirim",1);
                } else {
                    editor.putInt("bildirim",0);
                }

                editor.commit();
            }
        });

        uygulama_sesleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplicationContext().getSharedPreferences("ayarlar", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (uygulama_sesleri.isChecked()){
                    editor.putInt("uygulama",1);
                } else {
                    editor.putInt("uygulama",0);
                }

                editor.commit();
            }
        });



        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ayarlar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
