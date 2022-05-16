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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Random;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class Tarot_Istek extends AppCompatActivity {
    SharedPreferences sharedPreferences,kayit_kontrol;
    Spinner spinner_istek_turu;
    ImageView kart1,kart2,kart3,kart4,kart5,kart6,kart7,kart8,kart9,kart10,kart11,kart12,kart13,kart14,kart15,kart16,kart17,kart18,kart19,kart20,
    kart21,kart22,kart23,kart24,kart25,kart26,kart27,kart28,kart29,kart30,kart31,kart32,kart33,kart34,kart35,kart36,kart37,kart38,kart39,kart40,
    kart41,kart42,kart43,kart44,kart45,kart46,kart47,kart48,kart49,kart50,kart51,kart52,kart53,kart54,kart55,kart56,kart57,kart58,kart59,kart60,
    kart61,kart62,kart63,kart64,kart65,kart66,kart67,kart68,kart69,kart70,kart71,kart72,kart73,kart74,kart75,kart76,kart77,kart78;

    String kullanici_id,falci_id,adi,soyadi,cinsiyet,is_durumu,iliski_durumu,egitim_durumu,dogum_tarihi,token;

    int dosya;
    private String istek_turu[];
    String kart_id,kart_adi,secilen_kart_id1,secilen_kart_id2,secilen_kart_id3,secilen_kart_adi1,secilen_kart_adi2,secilen_kart_adi3;
    int secilen_kart1=0;
    int secilen_kart2=0;
    int secilen_kart3=0;
    ImageView resim1,resim2,resim3;
    int secilen_kart,kart1_durum,kart2_durum,kart3_durum,durum;
    Button rastgele_sec;
    MainMenu mainMenu;
    private InterstitialAd mInterstitialAd;

    int [ ] sayilar ;

    Random r = new Random();
    int sayi;
    int kontrol=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarot_istek);
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
        spinner_doldur();

        Button  rastgele_sec = findViewById(R.id.btn_rastgele);
        Button  gonder = findViewById(R.id.btn_gonder);

         resim1 = findViewById(R.id.image1);
         resim2 = findViewById(R.id.image2);
         resim3 = findViewById(R.id.image3);
         
         secilen_kart_id1="";
         secilen_kart_id2="";
         secilen_kart_id3="";


        // 1 den 78 e kadar sayıları diziye rastgele sıralıyorum

        sayilar = new int [78];

        for (int i = 0; i < sayilar.length; i++) {

            do{
                kontrol=0;
             sayi=r.nextInt(78)+1;

              for (int a=0; a < sayilar.length; a++) {
                 if( sayilar[a] == sayi){
                     kontrol=1;
                 }
              }
            } while(kontrol == 1);


               if ( kontrol == 0 ){
                   sayilar[i]=sayi;
               }
        }



        gonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (!secilen_kart_id1.equals("") && !secilen_kart_id2.equals("") && !secilen_kart_id3.equals("")){

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.

                            startActivity(new Intent(Tarot_Istek.this, MainMenu.class));
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

                    
                } else {

                    Toast.makeText(getApplicationContext(), "Lütfen Kartlarınızı seçiniz", Toast.LENGTH_SHORT).show();
                }
                
                
                
            }
        });


        rastgele_sec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int[] rastgele;
                int knt=0;
                rastgele = new int [3];
                for (int i = 0; i < rastgele.length; i++) {
                    sayi = r.nextInt(78) + 1;

                    do {
                        knt = 0;
                        sayi = r.nextInt(78) + 1;

                        for (int a = 0; a < rastgele.length; a++) {
                            if (rastgele[a] == sayi) {
                                knt = 1;
                            }
                        }
                    } while (knt == 1);


                    if (knt == 0) {
                        rastgele[i] = sayi;
                    }
                }

                for (int b= 0; b < rastgele.length; b++) {

                    durum = r.nextInt(2);
                    secilen_kart = rastgele[b];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();

                }

                secilen_kart1=0;
                secilen_kart2=0;
                secilen_kart3=0;
                kartlari_goster();

            }
        });



        tiklama_kontrol();

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
        String md5= AppConfig.md5(kullanici_id+"tarot_faliPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kullanici_id", kullanici_id);
            object.put("cinsiyet", cinsiyet);
            object.put("fal_turu", "2");
            object.put("istek_turu", spinner_istek_turu.getSelectedItemId()+1);
            object.put("resim1", secilen_kart_id1);
            object.put("resim_yonu1", String.valueOf(kart1_durum));
            object.put("resim2", secilen_kart_id2);
            object.put("resim_yonu2", String.valueOf(kart2_durum));
            object.put("resim3", secilen_kart_id3);
            object.put("resim_yonu3", String.valueOf(kart3_durum));
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

        String url = AppConfig.URL + "/tarot_fali.php";
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

                                sharedPreferences = getApplicationContext().getSharedPreferences("TarotFali", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("istek_id",kontrol.getString("istek_id"));

                                editor.commit();


                                Toast toast = Toast.makeText(getApplicationContext(), "Fal isteği gönderildi", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                                kredi_oku();

                                if (mInterstitialAd != null) {

                                    mInterstitialAd.show(Tarot_Istek.this);


                                } else {

                                    Log.d("Mesaj:", "Reklam Yüklenemedi");

                                    startActivity(new Intent(Tarot_Istek.this, MainMenu.class));
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


    private void resim_al(int durum, int id){
        if (id==1){ kart_id="1"; if(durum==0){dosya=R.drawable.adalet_tarot_karti; kart_adi="Tarot Adalet Kartı";}
        else{dosya=R.drawable.adalet_tarot_karti_ters; kart_adi="Tarot Adalet Kartı Ters";}}
        if (id==2){ kart_id="2"; if(durum==0){dosya=R.drawable.savas_araba_tarot_karti; kart_adi="Savaş Arabası Tarot Kartı";}
        else{dosya=R.drawable.savas_araba_tarot_karti_ters; kart_adi="Savaş Arabası Tarot Kartı Ters";}}
        if (id==3){ kart_id="3"; if(durum==0){dosya=R.drawable.asalarin_altilisi_tarot_karti; kart_adi="Asaların Altılısı Tarot Kartı";}
        else{dosya=R.drawable.asalarin_altilisi_tarot_karti_ters; kart_adi="Asaların Altılısı Tarot Kartı Ters";}}
        if (id==4){ kart_id="4"; if(durum==0){dosya=R.drawable.asalarin_beslisi_tarot_karti; kart_adi="Asaların Beşlisi Tarot Kartı";}
        else{dosya=R.drawable.asalarin_beslisi_tarot_karti_ters; kart_adi="Asaların Beşlisi Tarot Kartı Ters";}}
        if (id==5){ kart_id="5"; if(durum==0){dosya=R.drawable.asalarin_dokuzlusu_tarot_karti; kart_adi="Asaların Dokuzlusu Tarot Kartı";}
        else{dosya=R.drawable.asalarin_dokuzlusu_tarot_karti_ters; kart_adi="Asaların Dokuzlusu Tarot Kartı Ters";}}
        if (id==6){ kart_id="6"; if(durum==0){dosya=R.drawable.asalarin_dortlusu_tarot_karti; kart_adi="Asaların Dörtlüsü Tarot Kartı";}
        else{dosya=R.drawable.asalarin_dortlusu_tarot_karti_ters; kart_adi="Asaların Dörtlüsü Tarot Kartı Ters";}}
        if (id==7){ kart_id="7"; if(durum==0){dosya=R.drawable.asalarin_ikilisi_tarot_karti; kart_adi="Asaların İkilisi Tarot Kartı";}
        else{dosya=R.drawable.asalarin_ikilisi_tarot_karti_ters; kart_adi="Asaların İkilisi Tarot Kartı Ters";}}
        if (id==8){ kart_id="8"; if(durum==0){dosya=R.drawable.asalarin_kralicesi_tarot_karti; kart_adi="Asaların Kraliçesi Tarot Kartı";}
        else{dosya=R.drawable.asalarin_kralicesi_tarot_karti_ters; kart_adi="Asaların Kraliçesi Tarot Kartı Ters";}}
        if (id==9){ kart_id="9"; if(durum==0){dosya=R.drawable.asalarin_onlusu_tarot_karti; kart_adi="Asaların Onlusu Tarot Kartı";}
        else{dosya=R.drawable.asalarin_onlusu_tarot_karti_ters; kart_adi="Asaların Onlusu Tarot Kartı Ters";}}
        if (id==10){ kart_id="10"; if(durum==0){dosya=R.drawable.asalarin_sekizlisi_tarot_karti; kart_adi="Asaların Sekizlisi Tarot Kartı";}
        else{dosya=R.drawable.asalarin_sekizlisi_tarot_karti_ters; kart_adi="Asaların Sekizlisi Tarot Kartı Ters";}}
        if (id==11){ kart_id="11"; if(durum==0){dosya=R.drawable.asalarin_uclusu_tarot_karti; kart_adi="Asaların Üçlüsü Tarot Kartı";}
        else{dosya=R.drawable.asalarin_uclusu_tarot_karti_ters; kart_adi="Asaların Üçlüsü Tarot Kartı Ters";}}
        if (id==12){ kart_id="12"; if(durum==0){dosya=R.drawable.asalarin_yedilisi_tarot_karti; kart_adi="Asaların Yedilisi Tarot Kartı";}
        else{dosya=R.drawable.asalarin_yedilisi_tarot_karti_ters; kart_adi="Asaların Yedilisi Tarot Kartı Ters";}}
        if (id==13){ kart_id="13"; if(durum==0){dosya=R.drawable.asiklar_tarot_karti; kart_adi="Aşıklar Tarot Kartı";}
        else{dosya=R.drawable.asiklar_tarot_karti_ters; kart_adi="Aşıklar Tarot Kartı Ters";}}
        if (id==14){ kart_id="14"; if(durum==0){dosya=R.drawable.asilan_adam_tarot_karti; kart_adi="Asılan Adam Tarot Kartı";}
        else{dosya=R.drawable.asilan_adam_tarot_karti_ters; kart_adi="Asılan Adam Tarot Kartı Ters";}}
        if (id==15){ kart_id="15"; if(durum==0){dosya=R.drawable.ay_tarot_karti; kart_adi="Ay Tarot Kartı";}
        else{dosya=R.drawable.ay_tarot_karti_ters; kart_adi="Ay Tarot Kartı Ters";}}
        if (id==16){ kart_id="16"; if(durum==0){dosya=R.drawable.azize_tarot_karti; kart_adi="Azize Tarot Kartı";}
        else{dosya=R.drawable.azize_tarot_karti_ters; kart_adi="Azize Tarot Kartı Ters";}}
        if (id==17){ kart_id="17"; if(durum==0){dosya=R.drawable.aziz_tarot_karti; kart_adi="Aziz Tarot Kartı";}
        else{dosya=R.drawable.aziz_tarot_karti_ters; kart_adi="Aziz Tarot Kartı Ters";}}
        if (id==18){ kart_id="18"; if(durum==0){dosya=R.drawable.buyucu_tarot_karti; kart_adi="Büyücü Tarot Kartı";}
        else{dosya=R.drawable.buyucu_tarot_karti_ters; kart_adi="Büyücü Tarot Kartı Ters";}}
        if (id==19){ kart_id="19"; if(durum==0){dosya=R.drawable.degnek_asi_tarot_karti; kart_adi="Asaların Ası Tarot Kartı";}
        else{dosya=R.drawable.degnek_asi_tarot_karti_ters; kart_adi="Asaların Ası Tarot Kartı Ters";}}
        if (id==20){ kart_id="20"; if(durum==0){dosya=R.drawable.degnek_krali_tarot_karti; kart_adi="Asaların Kralı Tarot Kartı";}
        else{dosya=R.drawable.degnek_krali_tarot_karti_ters; kart_adi="Asaların Kralı Tarot Kartı Ters";}}
        if (id==21){ kart_id="21"; if(durum==0){dosya=R.drawable.degnek_prensi_tarot_karti; kart_adi="Asaların Uşağı Tarot Kartı";}
        else{dosya=R.drawable.degnek_prensi_tarot_karti_ters; kart_adi="Asaların Uşağı Tarot Kartı Ters";}}
        if (id==22){ kart_id="22"; if(durum==0){dosya=R.drawable.degnek_sovalyesi_tarot_karti; kart_adi="Asaların Şövalyesi Tarot Kartı";}
        else{dosya=R.drawable.degnek_sovalyesi_tarot_karti_ters; kart_adi="Asaların Şövalyesi Tarot Kartı Ters";}}
        if (id==23){ kart_id="23"; if(durum==0){dosya=R.drawable.denge_tarot_karti; kart_adi="Denge Tarot Kartı";}
        else{dosya=R.drawable.denge_tarot_karti_ters; kart_adi="Denge Tarot Kartı Ters";}}
        if (id==24){ kart_id="24"; if(durum==0){dosya=R.drawable.dunya_tarot_karti; kart_adi="Dünya Tarot Kartı";}
        else{dosya=R.drawable.dunya_tarot_karti_ters; kart_adi="Dünya Tarot Kartı Ters";}}
        if (id==25){ kart_id="25"; if(durum==0){dosya=R.drawable.ermis_tarot_karti; kart_adi="Ermiş Tarot Kartı";}
        else{dosya=R.drawable.ermis_tarot_karti_ters; kart_adi="Ermiş Tarot Kartı Ters";}}
        if (id==26){ kart_id="26"; if(durum==0){dosya=R.drawable.guc_tarot_karti; kart_adi="Güç Tarot Kartı";}
        else{dosya=R.drawable.guc_tarot_karti_ters; kart_adi="Güç Tarot Kartı Ters";}}
        if (id==27){ kart_id="27"; if(durum==0){dosya=R.drawable.gunes_tarot_karti; kart_adi="Güneş Tarot Kartı";}
        else{dosya=R.drawable.gunes_tarot_karti_ters; kart_adi="Güneş Tarot Kartı Ters";}}
        if (id==28){ kart_id="28"; if(durum==0){dosya=R.drawable.imparatorice_tarot_karti; kart_adi="İmparatoriçe Tarot Kartı";}
        else{dosya=R.drawable.imparatorice_tarot_karti_ters; kart_adi="İmparatoriçe Tarot Kartı Ters";}}
        if (id==29){ kart_id="29"; if(durum==0){dosya=R.drawable.imparator_tarot_karti; kart_adi="İmparator Tarot Kartı";}
        else{dosya=R.drawable.imparator_tarot_karti_ters; kart_adi="İmparator Tarot Kartı Ters";}}
        if (id==30){ kart_id="30"; if(durum==0){dosya=R.drawable.joker_tarot_karti; kart_adi="Joker Tarot Kartı";}
        else{dosya=R.drawable.joker_tarot_karti_ters; kart_adi="Joker Tarot Kartı Ters";}}
        if (id==31){ kart_id="31"; if(durum==0){dosya=R.drawable.kader_carki_tarot_karti; kart_adi="Kader Çarkı Tarot Kartı";}
        else{dosya=R.drawable.kader_carki_tarot_karti_ters; kart_adi="Kader Çarkı Tarot Kartı Ters";}}
        if (id==32){ kart_id="32"; if(durum==0){dosya=R.drawable.kiliclarin_altilisi_tarot_karti; kart_adi="Kılıçların Altılısı Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_altilisi_tarot_karti_ters; kart_adi="Kılıçların Altılısı Tarot Kartı Ters";}}
        if (id==33){ kart_id="33"; if(durum==0){dosya=R.drawable.kiliclarin_asi_tarot_karti; kart_adi="Kılıçların Ası Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_asi_tarot_karti_ters; kart_adi="Kılıçların Ası Tarot Kartı Ters";}}
        if (id==34){ kart_id="34"; if(durum==0){dosya=R.drawable.kiliclarin_beslisi_tarot_karti; kart_adi="Kılıçların Beşlisi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_beslisi_tarot_karti_ters; kart_adi="Kılıçların Beşlisi Tarot Kartı Ters";}}
        if (id==35){ kart_id="35"; if(durum==0){dosya=R.drawable.kiliclarin_dokuzlusu_tarot_karti; kart_adi="Kılıçların Dokuzlusu Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_dokuzlusu_tarot_karti_ters; kart_adi="Kılıçların Dokuzlusu Tarot Kartı Ters";}}
        if (id==36){ kart_id="36"; if(durum==0){dosya=R.drawable.kiliclarin_dortlusu_tarot_karti; kart_adi="Kılıçların Dörtlüsü Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_dortlusu_tarot_karti_ters; kart_adi="Kılıçların Dörtlüsü Tarot Kartı Ters";}}
        if (id==37){ kart_id="37"; if(durum==0){dosya=R.drawable.kiliclarin_ikilisi_tarot_karti; kart_adi="Kılıçların İkilisi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_ikilisi_tarot_karti_ters; kart_adi="Kılıçların İkilisi Tarot Kartı Ters";}}
        if (id==38){ kart_id="38"; if(durum==0){dosya=R.drawable.kiliclarin_kralicesi_tarot_karti; kart_adi="Kılıçların Kraliçesi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_kralicesi_tarot_karti_ters; kart_adi="Kılıçların Kraliçesi Tarot Kartı Ters";}}
        if (id==39){ kart_id="39"; if(durum==0){dosya=R.drawable.kiliclarin_krali_tarot_karti; kart_adi="Kılıçların Kralı Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_krali_tarot_karti_ters; kart_adi="Kılıçların Kralı Tarot Kartı Ters";}}
        if (id==40){ kart_id="40"; if(durum==0){dosya=R.drawable.kiliclarin_onlusu_tarot_karti; kart_adi="Kılıçların Onlusu Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_onlusu_taro_karti_ters; kart_adi="Kılıçların Onlusu Tarot Kartı Ters";}}
        if (id==41){ kart_id="41"; if(durum==0){dosya=R.drawable.kiliclarin_sekizlisi_tarot_karti; kart_adi="Kılıçların Sekizlisi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_sekizlisi_tarot_karti_ters; kart_adi="Kılıçların Sekizlisi Tarot Kartı Ters";}}
        if (id==42){ kart_id="42"; if(durum==0){dosya=R.drawable.kiliclarin_sovalyesi_tarot_karti; kart_adi="Kılıçların Şövalyesi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_sovalyesi_tarot_karti_ters; kart_adi="Kılıçların Şövalyesi Tarot Kartı Ters";}}
        if (id==43){ kart_id="43"; if(durum==0){dosya=R.drawable.kiliclarin_uclusu_tarot_karti; kart_adi="Kılıçların Üçlüsü Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_uclusu_tarot_karti_ters; kart_adi="Kılıçların Üçlüsü Tarot Kartı Ters";}}
        if (id==44){ kart_id="44"; if(durum==0){dosya=R.drawable.kiliclarin_yedilisi_tarot_karti; kart_adi="Kılıçların Yedilisi Tarot Kartı";}
        else{dosya=R.drawable.kiliclarin_yedilisi_tarot_karti_ters; kart_adi="Kılıçların Yedilisi Tarot Kartı Ters";}}
        if (id==45){ kart_id="45"; if(durum==0){dosya=R.drawable.kilic_prensi_tarot_karti; kart_adi="Kılıçların Uşağı Tarot Kartı";}
        else{dosya=R.drawable.kilic_prensi_tarot_karti_ters; kart_adi="Kılıçların Uşağı Tarot Kartı Ters";}}
        if (id==46){ kart_id="46"; if(durum==0){dosya=R.drawable.kule_tarot_karti; kart_adi="Kule Tarot Kartı";}
        else{dosya=R.drawable.kule_tarot_karti_ters; kart_adi="Kule Tarot Kartı Ters";}}
        if (id==47){ kart_id="47"; if(durum==0){dosya=R.drawable.kupa_krali_tarot_karti; kart_adi="Kupaların Kralı Tarot Kartı";}
        else{dosya=R.drawable.kupa_krali_tarot_karti_ters; kart_adi="Kupaların Kralı Tarot Kartı Ters";}}
        if (id==48){ kart_id="48"; if(durum==0){dosya=R.drawable.kupalarin_kralicesi_tarot_karti; kart_adi="Kupaların Kraliçesi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_kralicesi_tarot_karti_ters; kart_adi="Kupaların Kraliçesi Tarot Kartı Ters";}}
        if (id==49){ kart_id="49"; if(durum==0){dosya=R.drawable.kupalarin_altilisi_tarot_karti; kart_adi="Kupaların Altılısı Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_altilisi_tarot_karti_ters; kart_adi="Kupaların Altılısı Tarot Kartı Ters";}}
        if (id==50){ kart_id="50"; if(durum==0){dosya=R.drawable.kupalarin_asi_tarot_karti; kart_adi="Kupaların Ası Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_asi_tarot_karti_ters; kart_adi="Kupaların Ası Tarot Kartı Ters";}}
        if (id==51){ kart_id="51"; if(durum==0){dosya=R.drawable.kupalarin_beslisi_tarot_karti; kart_adi="Kupaların Beşlisi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_beslisi_tarot_karti_ters; kart_adi="Kupaların Beşlisi Tarot Kartı Ters";}}
        if (id==52){ kart_id="52"; if(durum==0){dosya=R.drawable.kupalarin_dokuzlusu_tarot_karti; kart_adi="Kupaların Dokuzlusu Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_dokuzlusu_tarot_karti_ters; kart_adi="Kupaların Dokuzlusu Tarot Kartı Ters";}}
        if (id==53){ kart_id="53"; if(durum==0){dosya=R.drawable.kupalarin_dortlusu_tarot_karti; kart_adi="Kupaların Dörtlüsü Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_dortlusu_tarot_karti_ters; kart_adi="Kupaların Dörtlüsü Tarot Kartı Ters";}}
        if (id==54){ kart_id="54"; if(durum==0){dosya=R.drawable.kupalarin_ikilisi_tarot_karti; kart_adi="Kupaların İkilisi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_ikilisi_tarot_karti_ters; kart_adi="Kupaların İkilisi Tarot Kartı Ters";}}
        if (id==55){ kart_id="55"; if(durum==0){dosya=R.drawable.kupalarin_onlusu_tarot_karti; kart_adi="Kupaların Onlusu Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_onlusu_tarot_karti_ters; kart_adi="Kupaların Onlusu Tarot Kartı Ters";}}
        if (id==56){ kart_id="56"; if(durum==0){dosya=R.drawable.kupalarin_sekizlisi_tarot_karti; kart_adi="Kupaların Sekizlisi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_sekizlisi_tarot_karti_ters; kart_adi="Kupaların Sekizlisi Tarot Kartı Ters";}}
        if (id==57){ kart_id="57"; if(durum==0){dosya=R.drawable.kupalarin_sovalyesi_tarot_karti; kart_adi="Kupaların Şövalyesi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_sovalyesi_tarot_karti_ters; kart_adi="Kupaların Şövalyesi Tarot Kartı Ters";}}
        if (id==58){ kart_id="58"; if(durum==0){dosya=R.drawable.kupalarin_uclusu_tarot_karti; kart_adi="Kupaların Üçlüsü Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_uclusu_tarot_karti_ters; kart_adi="Kupaların Üçlüsü Tarot Kartı Ters";}}
        if (id==59){ kart_id="59"; if(durum==0){dosya=R.drawable.kupalarin_yedilisi_tarot_karti; kart_adi="Kupaların Yedilisi Tarot Kartı";}
        else{dosya=R.drawable.kupalarin_yedilisi_tarot_karti_ters; kart_adi="Kupaların Yedilisi Tarot Kartı Ters";}}
        if (id==60){ kart_id="60"; if(durum==0){dosya=R.drawable.kupa_prensi_tarot_karti; kart_adi="Kupaların Uşağı Tarot Kartı";}
        else{dosya=R.drawable.kupa_prensi_tarot_karti_ters; kart_adi="Kupaların Uşağı Tarot Kartı Ters";}}
        if (id==61){ kart_id="61"; if(durum==0){dosya=R.drawable.mahkeme_tarot_karti; kart_adi="Uyanış Tarot Kartı";}
        else{dosya=R.drawable.mahkeme_tarot_karti_ters; kart_adi="Uyanış Tarot Kartı Ters";}}
        if (id==62){ kart_id="62"; if(durum==0){dosya=R.drawable.olum_tarot_karti; kart_adi="Ölüm Tarot Kartı";}
        else{dosya=R.drawable.olum_tarot_karti_ters; kart_adi="Ölüm Tarot Kartı Ters";}}
        if (id==63){ kart_id="63"; if(durum==0){dosya=R.drawable.seytan_tarot_karti; kart_adi="Şeytan Tarot Kartı";}
        else{dosya=R.drawable.seytan_tarot_karti_ters; kart_adi="Şeytan Tarot Kartı Ters";}}
        if (id==64){ kart_id="64"; if(durum==0){dosya=R.drawable.tilsimlarin_altilisi_tarot_karti; kart_adi="Tılsımların Altılısı Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_altilisi_tarot_karti_ters; kart_adi="Tılsımların Altılısı Tarot Kartı Ters";}}
        if (id==65){ kart_id="65"; if(durum==0){dosya=R.drawable.tilsimlarin_asi_tarot_karti; kart_adi="Tılsımların Ası Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_asi_tarot_karti_ters; kart_adi="Tılsımların Ası Tarot Kartı Ters";}}
        if (id==66){ kart_id="66"; if(durum==0){dosya=R.drawable.tilsimlarin_beslisi_tarot_karti; kart_adi="Tılsımların Beşlisi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_beslisi_tarot_karti_ters; kart_adi="Tılsımların Beşlisi Tarot Kartı Ters";}}
        if (id==67){ kart_id="67"; if(durum==0){dosya=R.drawable.tilsimlarin_dokuzlusu_tarot_karti; kart_adi="Tılsımların Dokuzlusu Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_dokuzlusu_tarot_karti_ters; kart_adi="Tılsımların Dokuzlusu Tarot Kartı Ters";}}
        if (id==68){ kart_id="68"; if(durum==0){dosya=R.drawable.tilsimlarin_dortlusu_tarot_karti; kart_adi="Tılsımların Dörtlüsü Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_dortlusu_tarot_karti_ters; kart_adi="Tılsımların Dörtlüsü Tarot Kartı Ters";}}
        if (id==69){ kart_id="69"; if(durum==0){dosya=R.drawable.tilsimlarin_ikilisi_tarot_karti; kart_adi="Tılsımların İkilisi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_ikilisi_tarot_karti_ters; kart_adi="Tılsımların İkilisi Tarot Kartı Ters";}}
        if (id==70){ kart_id="70"; if(durum==0){dosya=R.drawable.tilsimlarin_krali_tarot_karti; kart_adi="Tılsımların Kralı Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_krali_tarot_karti_ters; kart_adi="Tılsımların Kralı Tarot Kartı Ters";}}
        if (id==71){ kart_id="71"; if(durum==0){dosya=R.drawable.tilsimlarin_onlusu_tarot_karti; kart_adi="Tılsımların Onlusu Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_onlusu_tarot_karti_ters; kart_adi="Tılsımların Onlusu Tarot Kartı Ters";}}
        if (id==72){ kart_id="72"; if(durum==0){dosya=R.drawable.tilsimlarin_sovalyesi_tarot_karti; kart_adi="Tılsımların Şövalyesi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_sovalyesi_tarot_karti_ters; kart_adi="Tılsımların Şövalyesi Tarot Kartı Ters";}}
        if (id==73){ kart_id="73"; if(durum==0){dosya=R.drawable.tilsimlarin_uclusu_tarot_karti; kart_adi="Tılsımların Üçlüsü Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_uclusu_tarot_karti_ters; kart_adi="Tılsımların Üçlüsü Tarot Kartı Ters";}}
        if (id==74){ kart_id="74"; if(durum==0){dosya=R.drawable.tilsimlarin_yedilisi_tarot_karti; kart_adi="Tılsımların Yedilisi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlarin_yedilisi_tarot_karti_ters; kart_adi="Tılsımların Yedilisi Tarot Kartı Ters";}}
        if (id==75){ kart_id="75"; if(durum==0){dosya=R.drawable.tilsimlerin_kralicesi_tarot_karti; kart_adi="Tılsımların Kraliçesi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlerin_kralicesi_tarot_karti_ters; kart_adi="Tılsımların Kraliçesi Tarot Kartı Ters";}}
        if (id==76){ kart_id="76"; if(durum==0){dosya=R.drawable.tilsimlerin_sekizlisi_tarot_karti; kart_adi="Tılsımların Sekizlisi Tarot Kartı";}
        else{dosya=R.drawable.tilsimlerin_sekizlisi_tarot_karti_ters; kart_adi="Tılsımların Sekizlisi Tarot Kartı Ters";}}
        if (id==77){ kart_id="77"; if(durum==0){dosya=R.drawable.tilsim_prensi_tarot_karti; kart_adi="Tılsımların Uşağı Tarot Kartı";}
        else{dosya=R.drawable.tilsim_prensi_tarot_karti_ters; kart_adi="Tılsımların Uşağı Tarot Kartı Ters";}}
        if (id==78){ kart_id="78"; if(durum==0){dosya=R.drawable.yildiz_tarot_karti; kart_adi="Yıldız Tarot Kartı";}
        else {dosya=R.drawable.yildiz_tarot_karti_ters; kart_adi="Yıldız Tarot Kartı Ters";}}

    }

    private void spinner_doldur() {

        istek_turu = new String[4];
        istek_turu[0] = "Genel";
        istek_turu[1] = "Aşk";
        istek_turu[2] = "Kariyer/Para";
        istek_turu[3] = "Sağlık";


        spinner_istek_turu = (Spinner) findViewById(R.id.istek_turu);
        ArrayAdapter adapter_istek_turu = new ArrayAdapter(this, android.R.layout.simple_spinner_item, istek_turu);
        spinner_istek_turu.setAdapter(adapter_istek_turu);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tarot Falı İstek");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);

        kart1=findViewById(R.id.kart1);
        kart2 =findViewById(R.id.kart2 );
        kart3=findViewById(R.id.kart3);
        kart4=findViewById(R.id.kart4);
        kart5=findViewById(R.id.kart5);
        kart6=findViewById(R.id.kart6);
        kart7=findViewById(R.id.kart7);
        kart8=findViewById(R.id.kart8);
        kart9=findViewById(R.id.kart9);
        kart10=findViewById(R.id.kart10);
        kart11=findViewById(R.id.kart11);
        kart12=findViewById(R.id.kart12);
        kart13=findViewById(R.id.kart13);
        kart14=findViewById(R.id.kart14);
        kart15=findViewById(R.id.kart15);
        kart16=findViewById(R.id.kart16);
        kart17=findViewById(R.id.kart17);
        kart18=findViewById(R.id.kart18);
        kart19=findViewById(R.id.kart19);
        kart20=findViewById(R.id.kart20);
        kart21=findViewById(R.id.kart21);
        kart22=findViewById(R.id.kart22);
        kart23=findViewById(R.id.kart23);
        kart24=findViewById(R.id.kart24);
        kart25=findViewById(R.id.kart25);
        kart26=findViewById(R.id.kart26);
        kart27=findViewById(R.id.kart27);
        kart28=findViewById(R.id.kart28);
        kart29=findViewById(R.id.kart29);
        kart30=findViewById(R.id.kart30);
        kart31=findViewById(R.id.kart31);
        kart32=findViewById(R.id.kart32);
        kart33=findViewById(R.id.kart33);
        kart34=findViewById(R.id.kart34);
        kart35=findViewById(R.id.kart35);
        kart36=findViewById(R.id.kart36);
        kart37=findViewById(R.id.kart37);
        kart38=findViewById(R.id.kart38);
        kart39=findViewById(R.id.kart39);
        kart40=findViewById(R.id.kart40);
        kart41=findViewById(R.id.kart41);
        kart42=findViewById(R.id.kart42);
        kart43=findViewById(R.id.kart43);
        kart44=findViewById(R.id.kart44);
        kart45=findViewById(R.id.kart45);
        kart46=findViewById(R.id.kart46);
        kart47=findViewById(R.id.kart47);
        kart48=findViewById(R.id.kart48);
        kart49=findViewById(R.id.kart49);
        kart50=findViewById(R.id.kart50);
        kart51=findViewById(R.id.kart51);
        kart52=findViewById(R.id.kart52);
        kart53=findViewById(R.id.kart53);
        kart54=findViewById(R.id.kart54);
        kart55=findViewById(R.id.kart55);
        kart56=findViewById(R.id.kart56);
        kart57=findViewById(R.id.kart57);
        kart58=findViewById(R.id.kart58);
        kart59=findViewById(R.id.kart59);
        kart60=findViewById(R.id.kart60);
        kart61=findViewById(R.id.kart61);
        kart62=findViewById(R.id.kart62);
        kart63=findViewById(R.id.kart63);
        kart64=findViewById(R.id.kart64);
        kart65=findViewById(R.id.kart65);
        kart66=findViewById(R.id.kart66);
        kart67=findViewById(R.id.kart67);
        kart68=findViewById(R.id.kart68);
        kart69=findViewById(R.id.kart69);
        kart70=findViewById(R.id.kart70);
        kart71=findViewById(R.id.kart71);
        kart72=findViewById(R.id.kart72);
        kart73=findViewById(R.id.kart73);
        kart74=findViewById(R.id.kart74);
        kart75=findViewById(R.id.kart75);
        kart76=findViewById(R.id.kart76);
        kart77=findViewById(R.id.kart77);
        kart78=findViewById(R.id.kart78);



    }

    private void tiklama_kontrol(){

        kart1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                   kart1.setVisibility(View.INVISIBLE);
                   durum = r.nextInt(2);
                   secilen_kart = sayilar[1];
                   resim_al(durum, secilen_kart);
                   resmi_yukle();
                   }
                 // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
               }
        });

        kart2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart2.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[2];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart3.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[3];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart4.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[4];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart5.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[5];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart6.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[6];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart7.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[7];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart8.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[8];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart9.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[9];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart10.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[10];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart11.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[11];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart12.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[12];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });
        kart13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart13.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[13];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart14.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[14];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart15.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[15];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart16.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[16];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart17.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart17.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[17];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart18.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart18.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[18];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart19.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart19.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[19];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart20.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart20.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[20];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart21.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[21];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart22.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart22.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[22];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart23.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[23];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart24.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart24.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[24];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart25.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart25.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[25];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart26.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart26.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[26];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart27.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart27.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[27];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart28.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart28.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[28];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart29.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart29.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[29];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart30.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart30.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[30];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart31.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[31];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart32.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[32];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart33.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[33];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart34.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[34];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart35.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart35.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[35];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart36.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart36.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[36];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart37.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart37.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[37];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart38.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart38.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[38];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart39.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart39.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[39];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart40.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart40.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[40];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart41.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[41];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart42.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart42.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[42];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart43.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[43];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart44.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart44.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[44];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart45.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart45.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[45];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart46.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart46.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[46];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart47.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart47.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[47];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart48.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart48.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[48];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart49.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart49.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[49];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart50.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart50.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[50];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });
        kart51.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart51.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[51];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart52.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart52.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[52];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart53.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart53.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[53];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart54.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart54.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[54];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart55.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart55.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[55];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart56.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart56.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[56];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart57.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart57.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[57];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart58.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart58.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[58];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart59.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart59.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[59];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart60.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart60.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[60];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart61.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart61.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[61];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart62.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart62.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[62];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart63.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart63.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[63];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart64.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart64.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[64];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart65.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart65.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[65];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart66.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart66.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[66];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart67.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart67.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[67];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart68.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart68.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[68];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart69.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart69.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[69];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart70.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart70.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[70];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart71.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart71.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[71];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart72.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart72.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[72];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart73.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart73.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[73];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart74.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart74.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[74];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart75.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart75.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[75];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });


        kart76.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart76.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[76];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart77.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart77.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[77];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });

        kart78.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(secilen_kart1==0 || secilen_kart2==0 || secilen_kart3==0) {
                    kart78.setVisibility(View.INVISIBLE);
                    durum = r.nextInt(2);
                    secilen_kart = sayilar[78];
                    resim_al(durum, secilen_kart);
                    resmi_yukle();
                }
                // Toast.makeText(getApplicationContext(), kart_id + " - " + dosya + " - " + kart_adi, Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void resmi_yukle()
    {
        if (secilen_kart1 == 0) {
            secilen_kart_id1=String.valueOf(secilen_kart);
            secilen_kart_adi1=kart_adi;
            resim1.setImageResource(dosya);
            kart1_durum=durum+1;
            secilen_kart1=1;
        } else if (secilen_kart2 == 0) {
            secilen_kart_id2=String.valueOf(secilen_kart);
            secilen_kart_adi2=kart_adi;
            resim2.setImageResource(dosya);
            kart2_durum=durum+1;
            secilen_kart2=1;
        } else if (secilen_kart3 == 0) {
            secilen_kart_id3 = String.valueOf(secilen_kart);
            secilen_kart_adi3 = kart_adi;
            resim3.setImageResource(dosya);
            kart3_durum=durum+1;
            secilen_kart3 = 1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_done, menu);
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

    private void kartlari_goster(){
        kart1.setVisibility(View.VISIBLE);
        kart2.setVisibility(View.VISIBLE);
        kart3.setVisibility(View.VISIBLE);
        kart4.setVisibility(View.VISIBLE);
        kart5.setVisibility(View.VISIBLE);
        kart6.setVisibility(View.VISIBLE);
        kart7.setVisibility(View.VISIBLE);
        kart8.setVisibility(View.VISIBLE);
        kart9.setVisibility(View.VISIBLE);
        kart10.setVisibility(View.VISIBLE);
        kart11.setVisibility(View.VISIBLE);
        kart12.setVisibility(View.VISIBLE);
        kart13.setVisibility(View.VISIBLE);
        kart14.setVisibility(View.VISIBLE);
        kart15.setVisibility(View.VISIBLE);
        kart16.setVisibility(View.VISIBLE);
        kart17.setVisibility(View.VISIBLE);
        kart18.setVisibility(View.VISIBLE);
        kart19.setVisibility(View.VISIBLE);
        kart20.setVisibility(View.VISIBLE);
        kart21.setVisibility(View.VISIBLE);
        kart22.setVisibility(View.VISIBLE);
        kart23.setVisibility(View.VISIBLE);
        kart24.setVisibility(View.VISIBLE);
        kart25.setVisibility(View.VISIBLE);
        kart26.setVisibility(View.VISIBLE);
        kart27.setVisibility(View.VISIBLE);
        kart28.setVisibility(View.VISIBLE);
        kart29.setVisibility(View.VISIBLE);
        kart30.setVisibility(View.VISIBLE);
        kart31.setVisibility(View.VISIBLE);
        kart32.setVisibility(View.VISIBLE);
        kart33.setVisibility(View.VISIBLE);
        kart34.setVisibility(View.VISIBLE);
        kart35.setVisibility(View.VISIBLE);
        kart36.setVisibility(View.VISIBLE);
        kart37.setVisibility(View.VISIBLE);
        kart38.setVisibility(View.VISIBLE);
        kart39.setVisibility(View.VISIBLE);
        kart40.setVisibility(View.VISIBLE);
        kart41.setVisibility(View.VISIBLE);
        kart42.setVisibility(View.VISIBLE);
        kart43.setVisibility(View.VISIBLE);
        kart44.setVisibility(View.VISIBLE);
        kart45.setVisibility(View.VISIBLE);
        kart46.setVisibility(View.VISIBLE);
        kart47.setVisibility(View.VISIBLE);
        kart48.setVisibility(View.VISIBLE);
        kart49.setVisibility(View.VISIBLE);
        kart50.setVisibility(View.VISIBLE);
        kart51.setVisibility(View.VISIBLE);
        kart52.setVisibility(View.VISIBLE);
        kart53.setVisibility(View.VISIBLE);
        kart54.setVisibility(View.VISIBLE);
        kart55.setVisibility(View.VISIBLE);
        kart56.setVisibility(View.VISIBLE);
        kart57.setVisibility(View.VISIBLE);
        kart58.setVisibility(View.VISIBLE);
        kart59.setVisibility(View.VISIBLE);
        kart60.setVisibility(View.VISIBLE);
        kart61.setVisibility(View.VISIBLE);
        kart62.setVisibility(View.VISIBLE);
        kart63.setVisibility(View.VISIBLE);
        kart64.setVisibility(View.VISIBLE);
        kart65.setVisibility(View.VISIBLE);
        kart66.setVisibility(View.VISIBLE);
        kart67.setVisibility(View.VISIBLE);
        kart68.setVisibility(View.VISIBLE);
        kart69.setVisibility(View.VISIBLE);
        kart70.setVisibility(View.VISIBLE);
        kart71.setVisibility(View.VISIBLE);
        kart72.setVisibility(View.VISIBLE);
        kart73.setVisibility(View.VISIBLE);
        kart74.setVisibility(View.VISIBLE);
        kart75.setVisibility(View.VISIBLE);
        kart76.setVisibility(View.VISIBLE);
        kart77.setVisibility(View.VISIBLE);
        kart78.setVisibility(View.VISIBLE);


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
