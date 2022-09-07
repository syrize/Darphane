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

public class Bilkazan extends AppCompatActivity implements RewardedVideoAdListener {

    Button onayla;
    TextView soru,cevap_a,cevap_b,cevap_c,cevap_d,zaman,puan,soru_no,reklam_id,reklam_kredisi,kredi;
    LinearLayout lyt_a,lyt_b,lyt_c,lyt_d;
    int soru_sayaci=0;
    int soru_puani=0;
    int seviye=1;
    int soru_id;
    int oyun_durumu=0;
    String verilen_cevap,dogru_cevap,dogru_sik;
    String kullanici_id,paket_id,paket_adi,paket_turux,kredi_bedeli,token;
    SharedPreferences sharedPreferences,kayit_kontrol,kullanim_kontrol,kullanim_kayit;
    List<KrediPaketleriItem> list;


    private static final String APP_ID = AppConfig.APP_ID;
    private static final String AD_UNIT_ID = AppConfig.odullu_reklam_id;

    private RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;

   final CountDownTimer Count = new CountDownTimer(20000, 1000){
        @Override
        public void onTick(long millisUntilFinished) {
            zaman.setText(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            cevap_kontrol();
        }
    }.start();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bilkazan);
        onayla = (Button) findViewById(R.id.onayla);
        soru = (TextView) findViewById(R.id.soru);
        soru_no = (TextView) findViewById(R.id.soru_no);
        cevap_a = (TextView) findViewById(R.id.cevap_a);
        cevap_b = (TextView) findViewById(R.id.cevap_b);
        cevap_c = (TextView) findViewById(R.id.cevap_c);
        cevap_d = (TextView) findViewById(R.id.cevap_d);
        zaman = (TextView) findViewById(R.id.zaman);
        puan = (TextView) findViewById(R.id.puan);
        kredi = (TextView) findViewById(R.id.kredi);

        lyt_a = (LinearLayout) findViewById(R.id.lyt_a);
        lyt_b = (LinearLayout) findViewById(R.id.lyt_b);
        lyt_c = (LinearLayout) findViewById(R.id.lyt_c);
        lyt_d = (LinearLayout) findViewById(R.id.lyt_d);
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
        zaman_sayaci();


        yeni_soru();

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oyun_durumu==0) {
                    cevap_kontrol();
                } else {
                    yeni_soru();
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
                verilen_cevap="A";

            }
        });

        lyt_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.RED);
                lyt_c.setBackgroundColor(Color.WHITE);
                lyt_d.setBackgroundColor(Color.WHITE);
                verilen_cevap="B";

            }
        });

        lyt_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.WHITE);
                lyt_c.setBackgroundColor(Color.RED);
                lyt_d.setBackgroundColor(Color.WHITE);
                verilen_cevap="C";

            }
        });

        lyt_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_a.setBackgroundColor(Color.WHITE);
                lyt_b.setBackgroundColor(Color.WHITE);
                lyt_c.setBackgroundColor(Color.WHITE);
                lyt_d.setBackgroundColor(Color.RED);
                verilen_cevap="D";

            }
        });

    }


    public void zaman_sayaci(){
        Count.cancel();
        Count.start();

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

    private void cevap_kontrol(){
        onayla.setEnabled(false);
        lyt_a.setEnabled(false);
        lyt_b.setEnabled(false);
        lyt_c.setEnabled(false);
        lyt_d.setEnabled(false);

        if ((verilen_cevap == "" || verilen_cevap == null) && Integer.parseInt(zaman.getText().toString())>0) {
            Toast.makeText(Bilkazan.this, "Lütfen bir cevap seçiniz " , Toast.LENGTH_SHORT).show();
        } else {
            if (verilen_cevap!=null && verilen_cevap!="" && verilen_cevap.equals(dogru_sik)){

                if (dogru_sik.equals("A")) lyt_a.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("B")) lyt_b.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("C")) lyt_c.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("D")) lyt_d.setBackgroundColor(Color.GREEN);

                soru_puani=soru_puani+50;
                puan.setText(String.valueOf(soru_puani));

               // Toast.makeText(Bilkazan.this, "Verilen Cevap : " + verilen_cevap+" Cevap Doğru " , Toast.LENGTH_SHORT).show();

                new CountDownTimer(1000, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if(soru_sayaci==10){

                            Kredi_Girisi.kredi_satinalma(kullanici_id,"10","7", "500","0","3");

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            kredi_oku();


                            oyun_sonu(".: Kazandınız :.", "+500 Kredi",1);



                        } else {
                            yeni_soru();
                        }

                    }
                }.start();



            }else {

                Count.cancel();

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


                        oyun_sonu("Oyunu Kaybettiniz!","-10 Kredi",2);



                    }
                }.start();



                soru_puani=0;
                puan.setText(String.valueOf(soru_puani));
                soru_sayaci=0;
                oyun_durumu=1;
                soru_id=0;
                dogru_cevap="";

                if (dogru_sik.equals("A")) lyt_a.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("B")) lyt_b.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("C")) lyt_c.setBackgroundColor(Color.GREEN);
                if (dogru_sik.equals("D")) lyt_d.setBackgroundColor(Color.GREEN);

                dogru_sik="";

            }

        }
    }

    private void yeni_soru(){

        soru_sayaci=soru_sayaci+1;
        if (soru_sayaci==1){
            kredi_cikisi();
        }

        if (soru_sayaci<3){
            seviye=1;
        } else if(soru_sayaci>=3 && soru_sayaci<5){
            seviye=2;
        } else if(soru_sayaci>=5 && soru_sayaci<7){
            seviye=3;
        } else if(soru_sayaci>=7 && soru_sayaci<9){
            seviye=4;
        }else if(soru_sayaci>=9){
            seviye=5;
        }

        cevap_sifirla();

        soru_oku();

    }

    public void soru_oku(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Log.d("mesaj 2",kullanici_id);

        String md5= AppConfig.md5(kullanici_id+"yeni_soruGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/yeni_soru.php?user_id="+kullanici_id+"&kategori_id=1&kontrol_key="+kontrol_key+"&seviye="+seviye;



            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                          //  Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    JSONObject sorular = new JSONObject(kontrol.getString("sorular"));

                                    soru_no.setText(Integer.toString(soru_sayaci));
                                    soru.setText( sorular.getString("SORU"));
                                    cevap_a.setText(sorular.getString("CEVAP_A"));
                                    cevap_b.setText(sorular.getString("CEVAP_B"));
                                    cevap_c.setText(sorular.getString("CEVAP_C"));
                                    cevap_d.setText(sorular.getString("CEVAP_D"));
                                    soru_id=Integer.parseInt(sorular.getString("SORU_ID"));
                                    dogru_cevap=sorular.getString("DOGRU_CEVAP");
                                    dogru_sik=sorular.getString("DOGRU_SIK");

                                    onayla.setText("Cevapla");
                                    oyun_durumu=0;

                                    zaman_sayaci();

                                    onayla.setEnabled(true);
                                    lyt_a.setEnabled(true);
                                    lyt_b.setEnabled(true);
                                    lyt_c.setEnabled(true);
                                    lyt_d.setEnabled(true);


                                } else {

                                    Log.d("mesaj Soru Oku",kontrol.getString("hataMesaj"));
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

    private void oyun_sonu(String mesaj,String kazanilankredi,int durum) {
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
        final TextView kazanilan_kredi = (TextView) dialog.findViewById(R.id.kazanilankredi);
        final ImageView home = (ImageView) dialog.findViewById(R.id.home);
        reklam_izle_kredisi.setText(reklam_kredisi.getText());

        if (durum==1) {
            oyunsonu.setTextColor(Color.GREEN);
            kazanilan_kredi.setTextColor(Color.GREEN);
        } else {
            oyunsonu.setTextColor(Color.RED);
            kazanilan_kredi.setTextColor(Color.RED);

        }

        oyunsonu.setText(mesaj);
        kazanilan_kredi.setText(kazanilankredi);

        ((ImageView) dialog.findViewById(R.id.home)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();


            }
        });

        ((Button) dialog.findViewById(R.id.yeni_oyun)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bilkazan.this, Bilkazan_Start.class);
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

    private void cevap_sifirla(){
        lyt_a.setBackgroundColor(Color.WHITE);
        lyt_b.setBackgroundColor(Color.WHITE);
        lyt_c.setBackgroundColor(Color.WHITE);
        lyt_d.setBackgroundColor(Color.WHITE);
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
