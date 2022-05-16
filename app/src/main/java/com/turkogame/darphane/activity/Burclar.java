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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Burclar extends AppCompatActivity {
    CircularImageView btn_koc,btn_boa,btn_ikizler,btn_yengec,btn_aslan,btn_basak,btn_terazi,btn_akrep,btn_yay,btn_oglak,btn_kova,btn_balik;
    SharedPreferences sharedPreferences,kayit_kontrol;
    String kullanici_id,burc_id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burclar);
        initToolbar();

        token= FirebaseInstanceId.getInstance().getToken();


        sharedPreferences = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");

        btn_koc = findViewById(R.id.btn_koc);
        btn_boa = findViewById(R.id.btn_boa);
        btn_ikizler = findViewById(R.id.btn_ikizler);
        btn_yengec = findViewById(R.id.btn_yengec);
        btn_aslan = findViewById(R.id.btn_aslan);
        btn_basak = findViewById(R.id.btn_basak);
        btn_terazi = findViewById(R.id.btn_terazi);
        btn_akrep = findViewById(R.id.btn_akrep);
        btn_yay = findViewById(R.id.btn_yay);
        btn_oglak = findViewById(R.id.btn_oglak);
        btn_kova = findViewById(R.id.btn_kova);
        btn_balik = findViewById(R.id.btn_balik);

        tiklama_kontrol();

        btn_koc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="1";
                burc_goster();
            }
        });
        btn_boa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="2";
                burc_goster();
            }
        });
        btn_ikizler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="3";
                burc_goster();
            }
        });
        btn_yengec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="4";
                burc_goster();
            }
        });

        btn_aslan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="5";
                burc_goster();
            }
        });
        btn_basak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="6";
                burc_goster();
            }
        });
        btn_terazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="7";
                burc_goster();
            }
        });

        btn_akrep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="8";
                burc_goster();
            }
        });

        btn_yay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="9";
                burc_goster();
            }
        });

        btn_oglak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="10";
                burc_goster();
            }
        });

        btn_kova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="11";
                burc_goster();
            }
        });

        btn_balik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                burc_id="12";
                burc_goster();
            }
        });


    }


    private void tiklama_kontrol(){

        ImageView alt_menu_fallarim = findViewById(R.id.alt_menu_fallarim);
        ImageView alt_menu_duyurular = findViewById(R.id.alt_menu_duyurular);
        ImageView alt_menu_burclar = findViewById(R.id.alt_menu_burclar);
        ImageView alt_menu_home = findViewById(R.id.alt_menu_home);
        FloatingActionButton alt_menu_fal_istek = (FloatingActionButton) findViewById(R.id.alt_menu_fal_istek);


        alt_menu_fal_istek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFalDialog();
            }
        });


        alt_menu_fallarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Burclar.this, Fallarim.class);
                startActivity(intent);
            }
        });

/*
        alt_menu_burclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                kayitci.putString("user_id", kullanici_id);
                kayitci.commit();

                Intent intent = new Intent(Burclar.this, Burclar.class);
                startActivity(intent);
            }
        });*/

        alt_menu_duyurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Burclar.this, Duyurular.class);
                startActivity(intent);
            }
        });

        alt_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Burclar.this, MainMenu.class));
               finishAffinity();
            }
        });



    }


    private void showFalDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_fallar);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.verticalMargin=50;

        final ImageView kahve = (ImageView) dialog.findViewById(R.id.dialog_kahve);
        final ImageView tarot = (ImageView) dialog.findViewById(R.id.dialog_tarot);
        final ImageView el = (ImageView) dialog.findViewById(R.id.dialog_el);
        final ImageView yuz = (ImageView) dialog.findViewById(R.id.dialog_yuz);
        final ImageView ruya = (ImageView) dialog.findViewById(R.id.dialog_ruya);

        ((ImageView) dialog.findViewById(R.id.dialog_kahve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,1, Falcilar.class);
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.dialog_tarot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,2, Falcilar.class);
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.dialog_el)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,3, Falcilar.class);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.dialog_yuz)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,4, Falcilar.class);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.dialog_ruya)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,5, Falcilar.class);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void fal_baslat(String kullanici_id, String fb_token, int fal_turu, Class form){

        kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("user_id", kullanici_id);
        kayitci.putString("token",fb_token);
        kayitci.putInt("fal_turu",fal_turu);

        kayitci.commit();

        Intent intent = new Intent(Burclar.this, form);
        startActivity(intent);

    }


    private void burc_goster(){
        kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("user_id", kullanici_id);
        kayitci.putString("burc_id", burc_id);
        kayitci.commit();

        Intent intent = new Intent(Burclar.this, Burc_Detay.class);
        startActivity(intent);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Burçlar");
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
