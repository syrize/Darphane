package com.turkogame.darphane.activity;

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
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;

import org.json.JSONObject;

public class Bilkazan extends AppCompatActivity {

    Button onayla;
    TextView soru,cevap_a,cevap_b,cevap_c,cevap_d,zaman,puan,soru_no;
    LinearLayout lyt_a,lyt_b,lyt_c,lyt_d;
    int soru_sayaci=0;
    int soru_puani=0;
    int seviye=1;
    int soru_id;
    int oyun_durumu=0;
    String verilen_cevap,dogru_cevap,dogru_sik;

   final CountDownTimer Count = new CountDownTimer(20000, 1000){
        @Override
        public void onTick(long millisUntilFinished) {
            zaman.setText("Zaman: " + millisUntilFinished / 1000);
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

        lyt_a = (LinearLayout) findViewById(R.id.lyt_a);
        lyt_b = (LinearLayout) findViewById(R.id.lyt_b);
        lyt_c = (LinearLayout) findViewById(R.id.lyt_c);
        lyt_d = (LinearLayout) findViewById(R.id.lyt_d);

        initToolbar();
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

    private void cevap_kontrol(){
        if (verilen_cevap == "" || verilen_cevap == null ) {
            Toast.makeText(Bilkazan.this, "Lütfen bir cevap seçiniz " ,
                    Toast.LENGTH_SHORT).show();
        } else {
            if (verilen_cevap.equals(dogru_sik)){
                soru_puani=soru_puani+50;
                puan.setText("Puan: " + soru_puani);

                Toast.makeText(Bilkazan.this, "Verilen Cevap : " + verilen_cevap+" Cevap Doğru " ,
                        Toast.LENGTH_SHORT).show();

                yeni_soru();

            }else {
                Toast.makeText(Bilkazan.this, "Cevap Yanlış. Oyunu Kaybettiniz! " ,
                        Toast.LENGTH_SHORT).show();
                Count.cancel();

                soru_puani=0;
                puan.setText("Puan: " + soru_puani);
                soru_sayaci=0;
                onayla.setText("Yeniden Oyna");
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

        String md5= AppConfig.md5("1yeni_soruGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/yeni_soru.php?user_id=1&kategori_id=1&kontrol_key="+kontrol_key+"&seviye="+seviye;



            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("mesaj", response.toString());

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

                                    onayla.setText("Onayla");
                                    oyun_durumu=0;

                                    zaman_sayaci();


                                     Log.d("mesaj", dogru_sik );


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
}
