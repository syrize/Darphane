package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.KrediPaketleriItem;
import com.turkogame.darphane.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bulkazan extends AppCompatActivity implements RewardedVideoAdListener {

    private static final Random random = new Random();
    Button onayla;
    TextView puan,reklam_id,reklam_kredisi,kredi;
    TextView cevap_1,cevap_2,cevap_3,cevap_4,cevap_5,cevap_6,cevap_7,cevap_8,cevap_9,cevap_10,cevap_11,cevap_12;
    LinearLayout kutu1,kutu2,kutu3,kutu4,kutu5,kutu6,kutu7,kutu8,kutu9,kutu10,kutu11,kutu12;
    int sayac=0;
    int soru_puani=0;
    int seviye=1;
    int soru_id;
    int oyun_durumu=0;
    int dogru_kutu;
    String verilen_cevap,dogru_cevap,dogru_sik;
    String kullanici_id,paket_id,paket_adi,paket_turux,kredi_bedeli,token;
    SharedPreferences sharedPreferences,kayit_kontrol,kullanim_kontrol,kullanim_kayit;
    List<KrediPaketleriItem> list;

    private static final String APP_ID = AppConfig.APP_ID;
    private static final String AD_UNIT_ID = AppConfig.odullu_reklam_id;

    private RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulkazan);

        cevap_1 = (TextView) findViewById(R.id.cevap_1);
        cevap_2 = (TextView) findViewById(R.id.cevap_2);
        cevap_3 = (TextView) findViewById(R.id.cevap_3);
        cevap_4 = (TextView) findViewById(R.id.cevap_4);
        cevap_5 = (TextView) findViewById(R.id.cevap_5);
        cevap_6 = (TextView) findViewById(R.id.cevap_6);
        cevap_7 = (TextView) findViewById(R.id.cevap_7);
        cevap_8 = (TextView) findViewById(R.id.cevap_8);
        cevap_9 = (TextView) findViewById(R.id.cevap_9);
        cevap_10 = (TextView) findViewById(R.id.cevap_10);
        cevap_11 = (TextView) findViewById(R.id.cevap_11);
        cevap_12 = (TextView) findViewById(R.id.cevap_12);
        puan = (TextView) findViewById(R.id.puan);
        kredi = (TextView) findViewById(R.id.kredi);

        kutu1 = (LinearLayout) findViewById(R.id.kutu1);
        kutu2 = (LinearLayout) findViewById(R.id.kutu2);
        kutu3 = (LinearLayout) findViewById(R.id.kutu3);
        kutu4 = (LinearLayout) findViewById(R.id.kutu4);
        kutu5 = (LinearLayout) findViewById(R.id.kutu5);
        kutu6 = (LinearLayout) findViewById(R.id.kutu6);
        kutu7 = (LinearLayout) findViewById(R.id.kutu7);
        kutu8 = (LinearLayout) findViewById(R.id.kutu8);
        kutu9 = (LinearLayout) findViewById(R.id.kutu9);
        kutu10 = (LinearLayout) findViewById(R.id.kutu10);
        kutu11 = (LinearLayout) findViewById(R.id.kutu11);
        kutu12 = (LinearLayout) findViewById(R.id.kutu12);



        reklam_id = findViewById(R.id.reklam_id);
        reklam_kredisi=(TextView)  findViewById(R.id.reklam_kredisi);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");



        paketleri_oku();

        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.setUserId(kullanici_id);
        adRequest = new AdRequest.Builder().build();
        loadRewardedVideoAd();


        initToolbar();
        kredi_oku();


        yeni_soru();



        kutu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(1);

            }
        });

        kutu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(2);

            }
        });

        kutu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(3);

            }
        });

        kutu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(4);

            }
        });

        kutu5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(5);

            }
        });

        kutu6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(6);

            }
        });

        kutu7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(7);

            }
        });

        kutu8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(8);

            }
        });

        kutu9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(9);

            }
        });

        kutu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(10);

            }
        });

        kutu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(11);

            }
        });

        kutu12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cevap_kontrol(12);

            }
        });


    }





    private void kredi_cikisi(){
        Kredi_Girisi.kredi_cikisi(kullanici_id,"1" ,"10");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        kredi_oku();
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

        Kredi_Girisi.kredi_satinalma(kullanici_id,reklam_id.getText().toString() ,"2", miktar,"0","3");
        //Kredi_Girisi.kredi_oku(kullanici_id);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        kredi_oku();

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

                            // Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    kredi.setText( kontrol.getString("kredi_miktari"));

                                    kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
                                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                    kayitci.putString("kredi",  kontrol.getString("kredi_miktari"));
                                    kayitci.putInt("bakiye_sorgula",1);
                                    kayitci.commit();


                                    // Log.d("mesaj", kontrol.getString("kredi_miktari") );


                                } else {

                                    Log.d("mesaj kredi_oku",kontrol.getString("hataMesaj"));
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

    private void paketleri_oku(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());



        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&kontrol_key="+kontrol_key+"&islem=3";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    String gelenveri=kontrol.toString().replace("{\"hata\":false,\"kredi_paketleri\":","");
                                    gelenveri = gelenveri.replace("]}","]");

                                    //Log.d("mesaj", gelenveri);

                                    list = new ArrayList<>();
                                    list = okunanlariParseEt(gelenveri); // parse metodu çağrıldı

                                    for (int i = 0; i < list.size(); ++i) {

                                        paket_id = list.get(i).getPAKET_ID();
                                        paket_adi = list.get(i).getPAKET_ADI();
                                        paket_turux = list.get(i).getPAKET_TURU();
                                        kredi_bedeli = list.get(i).getMIKTAR();


                                        if (paket_turux.equals("2")){

                                            reklam_kredisi.setText(kredi_bedeli);
                                            reklam_id.setText(paket_id);
                                            // Log.d("mesaj", "paket_turu : "+paket_turux);
                                            // Log.d("mesaj", "Kredi Miktarı : "+kredi_bedeli);

                                        }


                                    }



                                    // Log.d("mesaj", list.toString());


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

    private void yeni_soru(){

        sayac=0;

        dogru_kutu = random.nextInt(12)+1;

        Log.d("mesaj", "Doğru Cevap: "+Integer.toString(dogru_kutu) );

        kredi_cikisi();


    }

    private void cevap_kontrol(int kutuno){

        sayac=sayac+1;

        if (sayac<=3) {

            if (dogru_kutu == kutuno) {

                if (kutuno == 1) cevap_1.setText("*");
                if (kutuno == 2) cevap_2.setText("*");
                if (kutuno == 3) cevap_3.setText("*");
                if (kutuno == 4) cevap_4.setText("*");
                if (kutuno == 5) cevap_5.setText("*");
                if (kutuno == 6) cevap_6.setText("*");
                if (kutuno == 7) cevap_7.setText("*");
                if (kutuno == 8) cevap_8.setText("*");
                if (kutuno == 9) cevap_9.setText("*");
                if (kutuno == 10) cevap_10.setText("*");
                if (kutuno == 11) cevap_11.setText("*");
                if (kutuno == 12) cevap_12.setText("*");

                new CountDownTimer(1000, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                            Kredi_Girisi.kredi_satinalma(kullanici_id,"10","7", "500","0","3");

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            kredi_oku();

                            oyun_sonu(".: Kazandınız :.");



                    }
                }.start();



            } else {

                if (kutuno == 1) cevap_1.setText("X");
                if (kutuno == 2) cevap_2.setText("X");
                if (kutuno == 3) cevap_3.setText("X");
                if (kutuno == 4) cevap_4.setText("X");
                if (kutuno == 5) cevap_5.setText("X");
                if (kutuno == 6) cevap_6.setText("X");
                if (kutuno == 7) cevap_7.setText("X");
                if (kutuno == 8) cevap_8.setText("X");
                if (kutuno == 9) cevap_9.setText("X");
                if (kutuno == 10) cevap_10.setText("X");
                if (kutuno == 11) cevap_11.setText("X");
                if (kutuno == 12) cevap_12.setText("X");

                if (sayac>=3){

                    new CountDownTimer(1000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            oyun_sonu("Oyunu Kaybettiniz!");



                        }
                    }.start();




                }

            }

        }

    }

    private void oyun_sonu(String mesaj) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.bilkazan_end);
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
        reklam_izle_kredisi.setText(reklam_kredisi.getText());
        oyunsonu.setText(mesaj);

        ((ImageView) dialog.findViewById(R.id.home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });

        ((Button) dialog.findViewById(R.id.yeni_oyun)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bulkazan.this, Bulkazan_Start.class);
                startActivity(intent);
                finish();
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

    ArrayList<KrediPaketleriItem> okunanlariParseEt(String okunanJson) {
        ArrayList<KrediPaketleriItem> kullaniciList = new ArrayList<>();
        try {
            JSONArray arrayKullanici = new JSONArray(okunanJson);

            for (int i = 0; i < arrayKullanici.length(); ++i) {
                String paket_id =arrayKullanici.getJSONObject(i).get("PAKET_ID").toString();
                String paket_adi =arrayKullanici.getJSONObject(i).get("PAKET_ADI").toString();
                String aciklama =arrayKullanici.getJSONObject(i).get("ACIKLAMA").toString();
                String miktar =arrayKullanici.getJSONObject(i).get("MIKTAR").toString();
                String tutar =arrayKullanici.getJSONObject(i).get("TUTAR").toString();
                String aktif =arrayKullanici.getJSONObject(i).get("AKTIF").toString();
                String paket_turu =arrayKullanici.getJSONObject(i).get("PAKET_TURU").toString();
                String paket_resmi =arrayKullanici.getJSONObject(i).get("PAKET_RESMI").toString();
                String adsense_id =arrayKullanici.getJSONObject(i).get("ADSENSE_ID").toString();

                KrediPaketleriItem satir = new KrediPaketleriItem( paket_id,paket_adi,aciklama,miktar,tutar,aktif,paket_turu,paket_resmi,adsense_id );
                kullaniciList.add(satir);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return kullaniciList;
    }



}
