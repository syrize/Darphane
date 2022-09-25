package com.turkogame.darphane.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    CircularImageView resim;
    String kullanici_id,profil_foto;
    TextView ad_soyad,email,cinsiyet,dogum_tarihi,nick,iban,alici,odeme_yontemi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        resim =  findViewById(R.id.resim);
        ad_soyad= findViewById(R.id.ad_soyad);
        email= findViewById(R.id.email);
        cinsiyet= findViewById(R.id.cinsiyet);
        dogum_tarihi= findViewById(R.id.dogum_tarihi);

        nick = findViewById(R.id.nick_name);
        iban = findViewById(R.id.iban);
        alici = findViewById(R.id.alici);
        odeme_yontemi = findViewById(R.id.odeme_yontemi);

        FloatingActionButton btn_profile = findViewById(R.id.btn_profile);

        initToolbar();
        initComponent();

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        profil_foto = sharedPreferences.getString("resim","0");

        //Log.d("mesaj", "aaaa :"+profil_foto);


        if (kullanici_id != ""){

            kullanici_oku();

        }




        btn_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Profile_Edit.class);
                startActivity(intent);
                finish();

            }
        });

    }



    public void kullanici_oku(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kullaniciGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/kullanici.php?user_id="+kullanici_id+"&kontrol_key="+kontrol_key;


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("mesaj", response.toString());


                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){
                                    JSONObject bilgiler = new JSONObject(kontrol.getString("uye-bilgileri"));

                                    ad_soyad.setText( bilgiler.getString("ADI")+ " "+ bilgiler.getString("SOYADI"));
                                    nick.setText( bilgiler.getString("NICK"));
                                    email.setText( bilgiler.getString("EMAIL"));

                                    if(bilgiler.getString("CINSIYET").equals("1")){ cinsiyet.setText("Erkek"); }
                                    if(bilgiler.getString("CINSIYET").equals("2")){ cinsiyet.setText("Kadın"); }


                                    if(bilgiler.getString("ODEME_YONTEMI").equals("1")){ odeme_yontemi.setText("Banka Hesabı"); }
                                    if(bilgiler.getString("ODEME_YONTEMI").equals("2")){ odeme_yontemi.setText("Papara Hesabı");  }
                                    if(bilgiler.getString("ODEME_YONTEMI").equals("3")){ odeme_yontemi.setText("İnninal Kart"); }

                                    iban.setText( bilgiler.getString("IBAN_NO"));
                                    alici.setText( bilgiler.getString("ALICI_ADSOYAD"));


                                    if(bilgiler.getString("DOGUM_TARIHI") != "null") {
                                        Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(bilgiler.getString("DOGUM_TARIHI"));
                                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        String yeni_tarih=String.valueOf(format.format(tarih));
                                        dogum_tarihi.setText(yeni_tarih);
                                    }


                                    Log.d("mesaj", bilgiler.getString("GOOGLE_ID") );
                                    Log.d("mesaj", bilgiler.getString("FACEBOOK_ID") );

                                    Log.d("mesaj", profil_foto);

                                    if (bilgiler.getString("GOOGLE_ID") != "null") {

                                        Picasso.get().load(profil_foto).into(resim);

                                        Log.d("mesaj", "resim"+ profil_foto );

                                    }

                                    if (bilgiler.getString("FACEBOOK_ID") != "null") {
                                        Picasso.get().load(profil_foto).into(resim);
                                    }
                                    if (bilgiler.getString("GOOGLE_ID").equals("null")  && bilgiler.getString("FACEBOOK_ID").equals("null")){

                                        Picasso.get().load("https://turkogame.com/uygulamalar/bilgi_oyunu/kullanici_resimleri/" + bilgiler.getString("RESIM")).into(resim);
                                    }

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
       // toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    private void initComponent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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



