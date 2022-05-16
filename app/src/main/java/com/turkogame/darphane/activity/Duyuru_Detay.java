package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Duyuru_Detay extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private ListView listView;
    private TextView txduyuru_konusu,txduyuru_metni,txduyuru_tarihi;
    private ImageView fal_resmi;
    private List<String> items = new ArrayList<>();
    private ArrayAdapter adapter;
    String kullanici_id,duyuru_id,duyuru_konusu,duyuru_metni,duyuru_tarihi;
    float puan;
    int puann;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duyuru_detay);

        sharedPreferences = getApplicationContext().getSharedPreferences("duyuru_kontrol", 0);
        kullanici_id = sharedPreferences.getString("kullanici_id","0");
        duyuru_id = sharedPreferences.getString("duyuru_id","0");
        duyuru_konusu = sharedPreferences.getString("duyuru_konusu","0");
        duyuru_metni = sharedPreferences.getString("duyuru_metni","0");
        duyuru_tarihi = sharedPreferences.getString("duyuru_tarihi","0");

        initToolbar();


        txduyuru_konusu = (TextView) findViewById(R.id.duyuru_konusu);
        txduyuru_metni = (TextView) findViewById(R.id.duyuru_metni);
        txduyuru_tarihi = (TextView) findViewById(R.id.duyuru_tarihi);

        txduyuru_konusu.setText(duyuru_konusu);
        txduyuru_metni.setText(duyuru_metni);
        txduyuru_tarihi.setText(duyuru_tarihi);

        okundu_yap();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Duyuru Detay");
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




    public  void okundu_yap(){

        String md5= AppConfig.md5(kullanici_id+"duyuru_islemleriPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("duyuru_id", duyuru_id);
            object.put("user_id", kullanici_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/duyuru_islemleri.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                //Toast.makeText(getApplicationContext(), "Duyuru Okundu", Toast.LENGTH_SHORT).show();


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

