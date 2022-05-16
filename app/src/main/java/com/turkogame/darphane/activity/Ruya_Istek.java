package com.turkogame.darphane.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class Ruya_Istek extends AppCompatActivity {
    SharedPreferences sharedPreferences,kayit_kontrol;
    private static final int CAMERA_REQUEST = 1888;
    String kullanici_id,falci_id,adi,soyadi,cinsiyet,is_durumu,iliski_durumu,egitim_durumu,dogum_tarihi,token;
    MainMenu mainMenu;
    AppCompatEditText ruya_metni;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ruya_istek);
        ruya_metni = findViewById(R.id.ruya_metni);
        Button gonder = findViewById(R.id.btn_gonder);

        reklam_yukle();

        sharedPreferences = getApplicationContext().getSharedPreferences("istek_bilgileri", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        falci_id = sharedPreferences.getString("falci_id","0");
        adi = sharedPreferences.getString("adi","0");
        soyadi = sharedPreferences.getString("soyadi","0");
        cinsiyet = sharedPreferences.getString("cinsiyet","0");
        is_durumu = sharedPreferences.getString("is_durumu","0");
        iliski_durumu = sharedPreferences.getString("iliski_durumu","0");
        egitim_durumu = sharedPreferences.getString("egitim_durumu","0");
        dogum_tarihi = sharedPreferences.getString("dogum_tarihi","0");
        token = sharedPreferences.getString("token","0");



        initToolbar();

        gonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        startActivity(new Intent(Ruya_Istek.this, MainMenu.class));
                        finishAffinity();

                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

                istek_gonder();
            }
        });


    }

    private void reklam_yukle(){

        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(this,AppConfig.gecis_reklam_id, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.d("Mesaj:", "Reklam Yuklendi");

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d("Mesaj:", loadAdError.getMessage());
                mInterstitialAd = null;
            }


        });



    }

    public void istek_gonder(){

        Date eski_tarih;
        String yeni_tarih;


        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(kullanici_id+"ruya_yorumlariPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kullanici_id", kullanici_id);
            object.put("cinsiyet", cinsiyet);
            object.put("ruya_metni", ruya_metni.getText());
            object.put("secilen_falci_id", falci_id);
            object.put("adi", adi);
            object.put("soyadi", soyadi);
            object.put("cinsiyet", cinsiyet);
            object.put("token", token);

            try {

                eski_tarih = new SimpleDateFormat("dd.MM.yyyy").parse(dogum_tarihi);

                Log.d("mesaj", String.valueOf(eski_tarih));

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                yeni_tarih = String.valueOf(format.format(eski_tarih));

                object.put("dogum_tarihi", String.valueOf(yeni_tarih));

                Log.d("mesaj", String.valueOf(yeni_tarih));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            object.put("is_durumu", is_durumu);
            object.put("iliski_durumu", iliski_durumu);
            object.put("egitim_durumu", egitim_durumu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/ruya_yorumlari.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Log.d("Mesaj:", kontrol.toString());
                                Log.d("Mesaj:", kontrol.getString("istek_id"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("RuyaYorumu", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("istek_id",kontrol.getString("istek_id"));

                                editor.commit();


                                Toast toast = Toast.makeText(getApplicationContext(), "Fal isteği gönderildi", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                                kredi_oku();

                                if (mInterstitialAd != null) {

                                    mInterstitialAd.show(Ruya_Istek.this);


                                } else {

                                    Log.d("Mesaj:", "Reklam Yüklenemedi");

                                    startActivity(new Intent(Ruya_Istek.this, MainMenu.class));
                                    finishAffinity();
                                }



                            } else {

                                //Log.d("Mesaj:", kontrol.getString("hataMesaj"));

                                Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                            }


                        } catch (Exception e) {

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("show", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);


    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rüya Tabiri İste");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_done, menu);
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


    public void kredi_oku(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&islem=2&kontrol_key="+kontrol_key;


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            //Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){



                                    kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                    kayitci.putString("kredi",  kontrol.getString("kredi_miktari"));
                                    kayitci.putInt("bakiye_sorgula",1);
                                    kayitci.commit();


                                    // Log.d("mesaj", kontrol.getString("kredi_miktari") );


                                } else {


                                    Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
                                    toast.show();

                                }


                            } catch (Exception e) {

                            }





                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }



            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
