package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.KrediPaketleriItem;
import com.turkogame.darphane.utils.Tools;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Kredi_Kazan extends AppCompatActivity  implements RewardedVideoAdListener {
    TextView kredi,reklam_kredisi,reklam_id,davet_kredisi,davet_id,takip_kredisi,takip_id,paylas_kredisi,paylas_id;
    SharedPreferences sharedPreferences,kayit_kontrol,kullanim_kontrol,kullanim_kayit;
    String kullanici_id,paket_id,paket_adi,paket_turux,kredi_bedeli,token;
    List<KrediPaketleriItem> list;
    LinearLayout reklam_izle,satinal,davet_et,takip_et,paylas,geri;

    private static final String APP_ID = AppConfig.APP_ID;
    private static final String AD_UNIT_ID = AppConfig.odullu_reklam_id;

    private RewardedVideoAd mRewardedVideoAd;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kredi_kazan);
        token= FirebaseInstanceId.getInstance().getToken();

        kredi = (TextView) findViewById(R.id.kredi);
        reklam_izle = findViewById(R.id.reklam_izle);
        satinal = findViewById(R.id.satinal);

        takip_et = findViewById(R.id.takip_et);
        paylas = findViewById(R.id.paylas);
        reklam_kredisi = findViewById(R.id.reklam_kredisi);
        reklam_id = findViewById(R.id.reklam_id);

        takip_kredisi = findViewById(R.id.takip_kredisi);
        takip_id = findViewById(R.id.takip_id);
        paylas_kredisi = findViewById(R.id.paylas_kredisi);
        paylas_id = findViewById(R.id.paylas_id);

        geri = findViewById(R.id.geri);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");


        kredi_oku();
        initToolbar();
        paketleri_oku();
        tiklama_kontrol();

        reklam_izle.setActivated(false);

        MobileAds.initialize(this, APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.setUserId(kullanici_id);
        adRequest = new AdRequest.Builder().build();
        loadRewardedVideoAd();

        kullanim_kontrol();

    }

    private void kullanim_kontrol(){
        paylas.setClickable(true);
        takip_et.setClickable(true);
        kullanim_kontrol = getApplicationContext().getSharedPreferences("kullanim", 0);
        if (kullanim_kontrol.getString("paylas","0").equals("1")){
            paylas.setClickable(false);
            paylas_kredisi.setText("Kullanıldı!");
        }
        if (kullanim_kontrol.getString("takip","0").equals("1")){
            takip_et.setClickable(false);
            takip_kredisi.setText("Kullanıldı!");
        }

    }

    private void tiklama_kontrol(){

        ImageView alt_menu_fallarim = findViewById(R.id.alt_menu_fallarim);
        ImageView alt_menu_duyurular = findViewById(R.id.alt_menu_duyurular);
        ImageView alt_menu_burclar = findViewById(R.id.alt_menu_burclar);
        ImageView alt_menu_home = findViewById(R.id.alt_menu_home);
        FloatingActionButton alt_menu_fal_istek = (FloatingActionButton) findViewById(R.id.alt_menu_fal_istek);

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //onBackPressed(); // önceki activity ye dönme kodu

                Intent intent = new Intent(Kredi_Kazan.this, deneme.class);
                startActivity(intent);


            }
        });


        alt_menu_fal_istek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFalDialog();
            }
        });


        alt_menu_fallarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Kredi_Kazan.this, Fallarim.class);
                startActivity(intent);
            }
        });


        alt_menu_burclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                kayitci.putString("user_id", kullanici_id);
                kayitci.commit();

                Intent intent = new Intent(Kredi_Kazan.this, Burclar.class);
                startActivity(intent);
            }
        });

        alt_menu_duyurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Kredi_Kazan.this, Duyurular.class);
                startActivity(intent);
            }
        });

        alt_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Kredi_Kazan.this, MainMenu.class));
                finishAffinity();
            }
        });

        reklam_izle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mRewardedVideoAd.show();
              //  Toast.makeText(getApplicationContext(), reklam_id.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        satinal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Kredi_Kazan.this, Kredi_Paketleri.class);
                startActivity(intent);

            }
        });



        takip_et.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri adres=Uri.parse("https://www.instagram.com/seferyazici/?hl=tr");
                Intent sayfa =new Intent(Intent.ACTION_VIEW,adres);
                startActivity(sayfa);

                Kredi_Girisi.kredi_satinalma(kullanici_id,takip_id.getText().toString() ,"2", takip_kredisi.getText().toString(),"0","3");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                kredi_oku();
                takip_et.setClickable(false);

                kullanim_kayit = getApplicationContext().getSharedPreferences("kullanim", 0);

                SharedPreferences.Editor editor = kullanim_kayit.edit();
                editor.putString("takip","1");
                editor.commit();

                takip_kredisi.setText("Kullanıldı!");

               // Toast.makeText(getApplicationContext(), takip_id.getText(), Toast.LENGTH_SHORT).show();

            }
        });

        paylas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                share();

                Kredi_Girisi.kredi_satinalma(kullanici_id,paylas_id.getText().toString() ,"2", paylas_kredisi.getText().toString(),"0","3");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                kredi_oku();
                paylas.setClickable(false);

                kullanim_kayit = getApplicationContext().getSharedPreferences("kullanim", 0);
                SharedPreferences.Editor editor = kullanim_kayit.edit();
                editor.putString("paylas","1");
                editor.commit();

                paylas_kredisi.setText("Kullanıldı!");

                //Toast.makeText(getApplicationContext(), paylas_id.getText(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(Kredi_Kazan.this, form);
        startActivity(intent);

    }


    void share() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.splash);


        File f =  new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
        Intent shareIntent;


        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

            outputStream.flush();
            outputStream.close();
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Beyaz Fincan");
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Merhaba, \nBeyaz Fincanı İncelediniz mi? \nhttps://www.beyazfincan.com");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        }catch (Exception e){
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(shareIntent,"Share Picture"));

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
        reklam_izle.setActivated(true);
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
        reklam_izle.setActivated(false);
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

        reklam_izle.setActivated(false);
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
        reklam_izle.setActivated(false);
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







    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setNavigationIcon(R.drawable.ic_menu);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kredi Kazan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
       // Tools.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.mdtp_white));
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


                                    kredi.setText( kontrol.getString("kredi_miktari"));

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

    private void paketi_oku(String paket_turu){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&paket_turu="+paket_turu+"&kontrol_key="+kontrol_key+"&islem=1";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    String gelenveri=kontrol.toString().replace("{\"hata\":false,\"kredi_paketleri\":","");
                                    gelenveri = gelenveri.replace("]}","]");

                                    //Log.d("mesaj", gelenveri);

                                    list = new ArrayList<>();
                                    list = okunanlariParseEt(gelenveri); // parse metodu çağrıldı

                                    paket_id=list.get(0).getPAKET_ID();
                                    paket_adi=list.get(0).getPAKET_ADI();

                                    Toast.makeText(getApplicationContext(), paket_adi, Toast.LENGTH_SHORT).show();

                                    Log.d("mesaj", list.toString());


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
                            Log.d("mesaj", response.toString());

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
                                            reklam_kredisi.setText(kredi_bedeli+" Kredi");
                                            reklam_id.setText(paket_id);
                                        } else { reklam_izle.setActivated(false);}
                                        if (paket_turux.equals("3")){
                                            davet_kredisi.setText(kredi_bedeli+" Kredi");
                                            davet_id.setText(paket_id);
                                        } else { davet_et.setActivated(false);}
                                        if (paket_turux.equals("4")){
                                            takip_kredisi.setText(kredi_bedeli+" Kredi");
                                            takip_id.setText(paket_id);
                                        } else { takip_et.setActivated(false);}
                                        if (paket_turux.equals("5")){
                                            paylas_kredisi.setText(kredi_bedeli+" Kredi");
                                            paylas_id.setText(paket_id);
                                        } else { paylas.setActivated(false);}

                                    }



                                    Log.d("mesaj", list.toString());


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
