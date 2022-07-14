package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    SharedPreferences sharedPreferences,bakiye_kontrol,kayit_kontrol,kullanim_kontrol,kullanim_kayit;
    String kullanici_id,paket_id,paket_adi,paket_turux,kredi_bedeli,token;
    TextView kredi;



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

        final Handler handler = new Handler();

        final Button spinBtn = findViewById(R.id.cevir);
        ImageView wheel = findViewById(R.id.wheel);
        float ilk_rota = wheel.getRotation();
        kredi = (TextView) findViewById(R.id.kredi);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");

        Kredi_Girisi.kredi_oku(kullanici_id,"2" );

        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

        kredi.setText(bakiye_kontrol.getString("kredi","0"));



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

                            Kredi_Girisi.kredi_satinalma(kullanici_id,"9" ,"2", sectors[degree],"0","4");



                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

                                    kredi.setText(bakiye_kontrol.getString("kredi","0"));


                                }
                            }, 1000);



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
        Tools.setSystemBarColor(this, R.color.system_bar);
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
