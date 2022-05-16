package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fal_Sonucu extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private ListView listView;
    private TextView txt_no_item,fal_cevabi,fal_adi;
    private ImageView fal_resmi;
    private List<String> items = new ArrayList<>();
    private ArrayAdapter adapter;
    String kullanici_id,istenen_falci,cevap_metni,fal_turu_id,fal_istek_tarihi,istek_id,review,falci_id;
    float puan;
    int puann;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fal_sonucu);
        sharedPreferences = getApplicationContext().getSharedPreferences("fallarim", 0);
        kullanici_id = sharedPreferences.getString("kullanici_id","0");
        istenen_falci = sharedPreferences.getString("istenen_falci","0");
        cevap_metni = sharedPreferences.getString("cevap_metni","0");
        fal_turu_id = sharedPreferences.getString("fal_turu_id","0");
        fal_istek_tarihi = sharedPreferences.getString("fal_istek_tarihi","0");
        istek_id = sharedPreferences.getString("istek_id","0");
        falci_id = sharedPreferences.getString("falci_id","0");

        fal_cevabi = (TextView) findViewById(R.id.fal_cevabi);
        fal_adi = (TextView) findViewById(R.id.fal_adi);
        fal_resmi = (ImageView) findViewById(R.id.fal_resmi);

        try {
        Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(fal_istek_tarihi);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        fal_istek_tarihi=String.valueOf(format.format(tarih));
        } catch (Exception e) {

        }

        if (fal_turu_id.equals("1")){fal_adi.setText("Kahve Falı  -  "+fal_istek_tarihi); fal_resmi.setImageResource(R.drawable.kahve_fali);}
        if (fal_turu_id.equals("2")){fal_adi.setText("Tarot Falı  -  "+fal_istek_tarihi); fal_resmi.setImageResource(R.drawable.tarot_fali);}
        if (fal_turu_id.equals("3")){fal_adi.setText("El Falı  -  "+fal_istek_tarihi); fal_resmi.setImageResource(R.drawable.el_fali);}
        if (fal_turu_id.equals("4")){fal_adi.setText("Rüya Yorumu  -  "+fal_istek_tarihi); fal_resmi.setImageResource(R.drawable.ruya_tabirleri);}
        if (fal_turu_id.equals("5")){fal_adi.setText("Yüz Falı  -  "+fal_istek_tarihi); fal_resmi.setImageResource(R.drawable.yuz_fali);}


        fal_cevabi.setText(cevap_metni);




        initToolbar();

        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fal Sonucu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    private void initComponent() {
        ((FloatingActionButton) findViewById(R.id.fab_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });



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



    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.oyver);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = (EditText) dialog.findViewById(R.id.et_post);
        final AppCompatRatingBar rating_bar = (AppCompatRatingBar) dialog.findViewById(R.id.rating_bar);
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review = et_post.getText().toString().trim();
                puan = rating_bar.getRating();
                puann = Math.round(puan);

                if (review.isEmpty() && puann<4) {

                    Toast.makeText(getApplicationContext(), "Lütfen bir not yazın", Toast.LENGTH_SHORT).show();

                } else {

                    puan_ver();

                }

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public  void puan_ver(){

        String md5= AppConfig.md5(kullanici_id+"falci_puanlamaPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("istek_id", istek_id);
            object.put("user_id", kullanici_id);
            object.put("fal_turu", fal_turu_id);
            object.put("puan", String.valueOf(puann));
            object.put("istenen_falci", falci_id);
            object.put("yorum", review);

            Log.d("mesaj : kontrol_key - ", kontrol_key);
            Log.d("mesaj : istek_id - ", istek_id);
            Log.d("mesaj : kullanici_id - ", kullanici_id);
            Log.d("mesaj : fal_turu - ", fal_turu_id);
            Log.d("mesaj : puan - ", String.valueOf(puann));
            Log.d("mesaj : istenen_falci - ", falci_id);
            Log.d("mesaj : yorum - ", review);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/falci_puanlama.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Toast.makeText(getApplicationContext(), "Puan Gönderildi", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), review+" - "+String.valueOf(puan), Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_SHORT).show();


                            }


                        } catch (Exception e) {

                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("mesaj", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);


    }



}

