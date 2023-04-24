package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;

import static com.turkogame.darphane.activity.app.AppConfig.APP_ID;

public class Bulkazan_Start extends AppCompatActivity implements RewardedVideoAdListener {
    Button start;
    SharedPreferences bakiye_kontrol,kayit_kontrol;
    TextView kredi,oyun_hakki;
    String kullanici_id;

    private static final String APP_ID = AppConfig.APP_ID;
    private static final String AD_UNIT_ID = AppConfig.odullu_reklam_id;

    private RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulkazan_start);
        start = (Button) findViewById(R.id.start);
        kredi = (TextView) findViewById(R.id.kredi);
        oyun_hakki = (TextView) findViewById(R.id.oyun_hakki);
        initToolbar();

        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.setUserId(kullanici_id);
        adRequest = new AdRequest.Builder().build();
        loadRewardedVideoAd();

        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
        kredi.setText(bakiye_kontrol.getString("kredi","0"));

        kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

        if (kayit_kontrol.getString("bulkazan_hak","0") != null &&
                kayit_kontrol.getString("bulkazan_hak","0")!="") {

            oyun_hakki.setText(kayit_kontrol.getString("bulkazan_hak", "0"));
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(oyun_hakki.getText().toString())>0 ) {

                    int hak=0;
                    kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

                    hak = Integer.parseInt(kayit_kontrol.getString("bulkazan_hak", "0"));
                    hak = hak - 1;

                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                    kayitci.putString("bulkazan_hak" , String.valueOf(hak));
                    kayitci.commit();
                    oyun_hakki.setText(String.valueOf(hak));


                    bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
                    if( Integer.parseInt(bakiye_kontrol.getString("kredi","0"))>0 ){

                        Intent intent = new Intent(Bulkazan_Start.this, Bulkazan.class);
                        startActivity(intent);
                        finish();

                    } else{
                        Toast.makeText(Bulkazan_Start.this, "Bakiyeniz Yetersiz! " ,
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Bulkazan_Start.this, Kredi_Kazan.class);
                        startActivity(intent);
                        finish();

                    }

                }else {
                    oynama_hakki_kazan();

                }
            }
        });


    }

    private void oynama_hakki_kazan() {
        loadRewardedVideoAd();
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
        oyunsonu.setText("Oyun Hakkınız Bitti");
        reklam_izle_kredisi.setText("5");

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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bul Kazan");
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

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        String miktar = String.valueOf(rewardItem.getAmount());

        int hak=0;
        kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);

        if (kayit_kontrol.getString("bulkazan_hak","0") != null && kayit_kontrol.getString("bulkazan_hak","0")!="") {
            hak = Integer.parseInt(kayit_kontrol.getString("bulkazan_hak", "0"));
            hak = hak + 5;
        }else{
            hak=5;
        }

        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("bulkazan_hak" , String.valueOf(hak));
        kayitci.commit();

        oyun_hakki.setText(String.valueOf(hak));


        loadRewardedVideoAd();
        //Toast.makeText(this, "onRewarded: "+rewardItem.getAmount(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoCompleted() {
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
