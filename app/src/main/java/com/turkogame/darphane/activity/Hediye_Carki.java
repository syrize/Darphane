package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;

import java.util.Random;

public class Hediye_Carki extends AppCompatActivity implements RewardedVideoAdListener{

    private static final String [] sectors ={"20","1","10","30","5","50","20","1","10","30","5","50"};
    private static final int [] sectorDegrees = new int[sectors.length];
    private static final Random random = new Random();
    private int degree= 0;
    private boolean isSpinning = false;


    private ImageView wheel;
    SharedPreferences sharedPreferences,kayit_kontrol,bakiye_kontrol;
    String kullanici_id,paket_id,paket_adi,paket_turux,kredi_bedeli,token;
    TextView kredi,cevirme_hakki;

    private static final String APP_ID = AppConfig.APP_ID;
    private static final String AD_UNIT_ID = AppConfig.odullu_reklam_id;

    private RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;



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
        cevirme_hakki = (TextView) findViewById(R.id.cevirme_hakki);

        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.setUserId(kullanici_id);
        adRequest = new AdRequest.Builder().build();
        loadRewardedVideoAd();

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");

        Kredi_Girisi.kredi_oku(kullanici_id,"2" );


        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

        kredi.setText(bakiye_kontrol.getString("kredi","0"));




        kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

            if (kayit_kontrol.getString("hak","0") != null &&
                    kayit_kontrol.getString("hak","0")!="") {

                cevirme_hakki.setText(kayit_kontrol.getString("hak", "0"));
            }



        spinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (Integer.parseInt(cevirme_hakki.getText().toString())>0 ) {

                    getDegreeForSectors();

                    wheel.setRotation(ilk_rota);

                    degree = random.nextInt(sectors.length - 1);

                    wheel.animate().rotation(360 * sectors.length + sectorDegrees[degree]).setDuration(360 * sectors.length).start();

                    new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            if (wheel.getRotation() == 360 * sectors.length + sectorDegrees[degree]) {

                                Kredi_Girisi.kredi_satinalma(kullanici_id, "9", "6", sectors[degree], "0", "3");


                                int hak=0;
                                kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

                                hak = Integer.parseInt(kayit_kontrol.getString("hak", "0"));
                                hak = hak - 1;

                                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                kayitci.putString("hak" , String.valueOf(hak));
                                kayitci.commit();
                                cevirme_hakki.setText(String.valueOf(hak));


                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

                                        kredi.setText(bakiye_kontrol.getString("kredi", "0"));


                                    }
                                }, 2000);


                            }

                        }
                    }.start();


                } else {
                    cevirme_hakki_kazan();

                }


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

    private void cevirme_hakki_kazan() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.cevirmehakkikazan);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.verticalMargin=50;

        final LinearLayout reklam_izle = (LinearLayout) dialog.findViewById(R.id.reklam_izle);
        final Button yeni_oyun = (Button) dialog.findViewById(R.id.yeni_oyun);
        final TextView reklam_izle_kredisi = (TextView) dialog.findViewById(R.id.reklam_izle_kredisi);
        final TextView oyunsonu = (TextView) dialog.findViewById(R.id.oyunsonu);
        final ImageView home = (ImageView) dialog.findViewById(R.id.home);

        ((ImageView) dialog.findViewById(R.id.home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });

        ((Button) dialog.findViewById(R.id.yeni_oyun)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        ((LinearLayout) dialog.findViewById(R.id.reklam_izle)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRewardedVideoAd.show();
            }
        });




        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    //Video Reklam yükle
    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {

            mRewardedVideoAd.loadAd(AD_UNIT_ID, adRequest);

        }
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        // Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "Ödüllü Video Reklam Yüklendi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
       // Toast.makeText(this, "Ödüllü Video Reklam Açıldı", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
       // Toast.makeText(this, "Ödüllü Video Başladı", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
       // Toast.makeText(this, "Ödüllü Video Reklamında Kapatıldı", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        String miktar = String.valueOf(rewardItem.getAmount());

        int hak=0;
        kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

        if (kayit_kontrol.getString("hak","0") != null && kayit_kontrol.getString("hak","0")!="") {
            hak = Integer.parseInt(kayit_kontrol.getString("hak", "0"));
            hak = hak + 3;
        }else{
            hak=3;
        }

        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("hak" , String.valueOf(hak));
        kayitci.commit();

        cevirme_hakki.setText(String.valueOf(hak));


        loadRewardedVideoAd();
        //Toast.makeText(this, "onRewarded: "+rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRewardedVideoAdLeftApplication() {
       // Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
       // Toast.makeText(this, "Ödüllü Video Reklam Yüklenemedi", Toast.LENGTH_SHORT).show();
          loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoCompleted() {
       // Toast.makeText(this, "Ödüllü Video Tamamlandı", Toast.LENGTH_SHORT).show();
        loadRewardedVideoAd();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }



}
