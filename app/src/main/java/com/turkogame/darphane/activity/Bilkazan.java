package com.turkogame.darphane.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;

public class Bilkazan extends AppCompatActivity {

    Button onayla;
    TextView soru,cevap_a,cevap_b,cevap_c,cevap_d,zaman,puan;
    LinearLayout lyt_a,lyt_b,lyt_c,lyt_d;
    int verilen_cevap=0;
    int sayac=15;
    int dogru_cevap=2;
    int soru_puani=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bilkazan);
        onayla = (Button) findViewById(R.id.onayla);
        soru = (TextView) findViewById(R.id.soru);
        cevap_a = (TextView) findViewById(R.id.cevap_a);
        cevap_b = (TextView) findViewById(R.id.cevap_b);
        cevap_c = (TextView) findViewById(R.id.cevap_c);
        cevap_d = (TextView) findViewById(R.id.cevap_d);
        zaman = (TextView) findViewById(R.id.zaman);
        puan = (TextView) findViewById(R.id.puan);

        lyt_a = (LinearLayout) findViewById(R.id.lyt_a);
        lyt_b = (LinearLayout) findViewById(R.id.lyt_b);
        lyt_c = (LinearLayout) findViewById(R.id.lyt_c);
        lyt_d = (LinearLayout) findViewById(R.id.lyt_d);

        initToolbar();
        zaman_sayaci();

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verilen_cevap==0) {
                    Toast.makeText(Bilkazan.this, "Lütfen bir cevap seçiniz " ,
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (verilen_cevap==dogru_cevap){
                        soru_puani=soru_puani+50;
                        puan.setText("Puan: " + soru_puani);

                    }
                    Toast.makeText(Bilkazan.this, "Verilen Cevap : " + verilen_cevap,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        lyt_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.RED);
                lyt_b.setBackgroundColor(Color.WHITE);
                lyt_c.setBackgroundColor(Color.WHITE);
                lyt_d.setBackgroundColor(Color.WHITE);
                verilen_cevap=1;

            }
        });

        lyt_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.RED);
                lyt_c.setBackgroundColor(Color.WHITE);
                lyt_d.setBackgroundColor(Color.WHITE);
                verilen_cevap=2;

            }
        });

        lyt_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.WHITE);
                lyt_c.setBackgroundColor(Color.RED);
                lyt_d.setBackgroundColor(Color.WHITE);
                verilen_cevap=3;

            }
        });

        lyt_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.WHITE);
                lyt_c.setBackgroundColor(Color.WHITE);
                lyt_d.setBackgroundColor(Color.RED);
                verilen_cevap=4;

            }
        });

    }

    private void zaman_sayaci(){
        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                zaman.setText("Zaman: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

                if (verilen_cevap==0) {
                    Toast.makeText(Bilkazan.this, "Lütfen bir cevap seçiniz " ,
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (verilen_cevap==dogru_cevap){
                        soru_puani=soru_puani+50;
                        puan.setText("Puan: " + soru_puani);

                    }
                    Toast.makeText(Bilkazan.this, "Verilen Cevap : " + verilen_cevap,
                            Toast.LENGTH_SHORT).show();
                }

            }
        }.start();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bil Kazan");
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
