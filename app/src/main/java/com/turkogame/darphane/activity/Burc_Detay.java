package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class Burc_Detay extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String kullanici_id,burc_id;
    TextView burc_yorumu,burc_baslik;
    ImageView burc_resmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.burc_detay);
        burc_yorumu = findViewById(R.id.burc_yorumu);
        burc_baslik = findViewById(R.id.burc_baslik);
        burc_resmi = findViewById(R.id.burc_resmi);

        sharedPreferences = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");
        burc_id = sharedPreferences.getString("burc_id","0");

        initToolbar();

        burc_oku();
    }

    private void burc_oku(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"burc_yorumlariGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/burc_yorumlari.php?user_id="+kullanici_id+"&kontrol_key="+kontrol_key+"&burc_id="+burc_id;


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("mesaj", response.toString());


                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){
                                    JSONObject bilgiler = new JSONObject(kontrol.getString("gunluk_yorum"));

                                    Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(bilgiler.getString("TARIH"));
                                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                    String yeni_tarih=String.valueOf(format.format(tarih));


                                    burc_yorumu.setText( bilgiler.getString("YORUM"));
                                    if (burc_id.equals("1")){ burc_baslik.setText("Koç Burcu - " + yeni_tarih);
                                    burc_resmi.setImageResource(R.drawable.koc_burcu);}
                                    if (burc_id.equals("2")){ burc_baslik.setText("Boğa Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.boa_burcu);}
                                    if (burc_id.equals("3")){ burc_baslik.setText("İkizler Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.ikizler_burcu);}
                                    if (burc_id.equals("4")){ burc_baslik.setText("Yengeç Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.yengec_burcu);}
                                    if (burc_id.equals("5")){ burc_baslik.setText("Aslan Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.aslan_burcu);}
                                    if (burc_id.equals("6")){ burc_baslik.setText("Başak Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.basak_burcu);}
                                    if (burc_id.equals("7")){ burc_baslik.setText("Terazi Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.terazi_burcu);}
                                    if (burc_id.equals("8")){ burc_baslik.setText("Akrep Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.akrep_burcu);}
                                    if (burc_id.equals("9")){ burc_baslik.setText("Yay Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.yay_burcu);}
                                    if (burc_id.equals("10")){ burc_baslik.setText("Oğlak Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.oglak_burcu);}
                                    if (burc_id.equals("11")){ burc_baslik.setText("Kova Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.kova_burcu);}
                                    if (burc_id.equals("12")){ burc_baslik.setText("Balık Burcu - " + yeni_tarih);
                                        burc_resmi.setImageResource(R.drawable.balik_burcu);}


                                    Log.d("mesaj", bilgiler.getString("YORUM") );


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
        getSupportActionBar().setTitle("Günlük Burç Yorumu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_article_share_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

