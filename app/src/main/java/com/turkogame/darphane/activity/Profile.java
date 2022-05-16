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
    TextView ad_soyad,email,cinsiyet,dogum_tarihi,is_durumu,iliski_durumu,egitim_durumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        resim =  findViewById(R.id.resim);
        ad_soyad= findViewById(R.id.ad_soyad);
        email= findViewById(R.id.email);
        cinsiyet= findViewById(R.id.cinsiyet);
        dogum_tarihi= findViewById(R.id.dogum_tarihi);
        is_durumu= findViewById(R.id.is_durumu);
        iliski_durumu= findViewById(R.id.iliski_durumu);
        egitim_durumu= findViewById(R.id.egitim_durumu);

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
                                    email.setText( bilgiler.getString("EMAIL"));

                                    if(bilgiler.getString("CINSIYET").equals("1")){ cinsiyet.setText("Erkek"); }
                                    if(bilgiler.getString("CINSIYET").equals("2")){ cinsiyet.setText("Kadın"); }
                                    if(bilgiler.getString("CINSIYET").equals("3")){ cinsiyet.setText("LGBT"); }

                                    if(bilgiler.getString("IS_DURUMU").equals("1")){ is_durumu.setText("Çalışıyor"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("2")){ is_durumu.setText("Okuyor"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("3")){ is_durumu.setText("İş Arıyor"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("4")){ is_durumu.setText("İlgilenmiyor"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("5")){ is_durumu.setText("Ev Hanımı"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("6")){ is_durumu.setText("Öğrenci"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("7")){ is_durumu.setText("Kendi İşini Yapıyor"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("8")){ is_durumu.setText("Akademisyen"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("9")){ is_durumu.setText("Kamu Sektörü"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("10")){ is_durumu.setText("Özel Sektör"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("11")){ is_durumu.setText("Emekli"); }
                                    if(bilgiler.getString("IS_DURUMU").equals("12")){ is_durumu.setText("Çalışmıyor"); }

                                    if(bilgiler.getString("ILISKI_DURUMU").equals("1")){ iliski_durumu.setText("İlişkisi Yok"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("2")){ iliski_durumu.setText("Evli"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("3")){ iliski_durumu.setText("Boşanmış"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("4")){ iliski_durumu.setText("Dul"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("5")){ iliski_durumu.setText("İlişkisi Var"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("6")){ iliski_durumu.setText("Nişanlı"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("7")){ iliski_durumu.setText("Platonik"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("8")){ iliski_durumu.setText("Ayrılmış"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("9")){ iliski_durumu.setText("Flört Halinde"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("10")){ iliski_durumu.setText("Karmaşık"); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("11")){ iliski_durumu.setText("Ayrı Yaşıyor"); }

                                    if(bilgiler.getString("EGITIM_DURUMU").equals("1")){ egitim_durumu.setText("İlkokul"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("2")){ egitim_durumu.setText("Ortaokul"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("3")){ egitim_durumu.setText("Lise"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("4")){ egitim_durumu.setText("Yüksek Okul"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("5")){ egitim_durumu.setText("Üniversite"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("6")){ egitim_durumu.setText("Yüksek Lisans"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("7")){ egitim_durumu.setText("Doktora"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("8")){ egitim_durumu.setText("Okur Yazar"); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("9")){ egitim_durumu.setText("Okuma Yazma Bilmiyor"); }

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

                                        Picasso.get().load("https://beyazfincan.com/yonetim/kullanici_resimleri/" + bilgiler.getString("RESIM")).into(resim);
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
        Tools.setSystemBarColor(this, R.color.purple_600);
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



