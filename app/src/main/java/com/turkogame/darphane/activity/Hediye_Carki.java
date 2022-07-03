package com.turkogame.darphane.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;

import java.util.Random;

public class Hediye_Carki extends AppCompatActivity {

    private static final String [] sectors ={"20","1","10","30","5","50","20","1","10","30","5","50"};
    private static final int [] sectorDegrees = new int[sectors.length];
    private static final Random random = new Random();
    private int degree= 0;
    private boolean isSpinning = false;

    private ImageView wheel;



    /*
    Log.d(TAG, "onCreate: " + "Metodun, döngünün veya herhangi bir kod bloğunun çalışıp çalışmadığını kontrol eder.");
        Log.w(TAG, "onCreate: " + "Uyarı mesajlarını verir");
        Log.e(TAG, "onCreate: " + "Hata mesajlarını verir");
        Log.i(TAG, "onCreate: " + "Bilgilendirme mesajlarını verir");

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hediye_carki);
        initToolbar();

        final ImageView spinBtn = findViewById(R.id.spinBtn);
        ImageView wheel = findViewById(R.id.wheel);
        float ilk_rota = wheel.getRotation();



        spinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDegreeForSectors();

                wheel.setRotation(ilk_rota);


                degree = random.nextInt(sectors.length-1);

                wheel.animate().rotation(360*sectors.length+sectorDegrees[degree]).setDuration(360*sectors.length).start();


                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if ( wheel.getRotation()==360*sectors.length+sectorDegrees[degree]) {
                            Toast.makeText(Hediye_Carki.this, "Konumunuz " + (degree + 1) + ". Sektördür.  Puanınız " + sectors[degree],
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }.start();





            }



        });
    }


    private void getDegreeForSectors(){

        int sectorDegree =360/sectors.length;

        for (int i=0; i<sectors.length; i++){
            sectorDegrees[i] = (i+1) * sectorDegree;
        }


    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hediye Çarkı");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_setting, menu);
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
