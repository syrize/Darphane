package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
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

public class Bize_Yazin extends AppCompatActivity {
    Button btn_gonder;
    AppCompatEditText konu,mesaj;
    String kullanici_id;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bize_yazin);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = sharedPreferences.getString("user_id","0");

        btn_gonder = findViewById(R.id.btn_gonder);
        konu = findViewById(R.id.konu);
        mesaj = findViewById(R.id.mesaj);


        initToolbar();

        btn_gonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (konu.getText().toString().length()>2 && mesaj.getText().toString().length()>5){

                    Log.d("mesaj", konu.getText().toString());

                    mesaj_gonder();

                } else {

                    Toast.makeText(getApplicationContext(), "Lütfen Konu ve Mesaj alanlarını doldurunuz", Toast.LENGTH_SHORT).show();

                }


            }
        });



    }

    private void mesaj_gonder(){

        String md5= AppConfig.md5(kullanici_id+"bize_yazinPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("user_id", kullanici_id);
            object.put("konu", konu.getText());
            object.put("mesaj", mesaj.getText());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/bize_yazin.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Toast.makeText(getApplicationContext(), "Mesaj Gönderildi", Toast.LENGTH_SHORT).show();
                                konu.setText("");
                                mesaj.setText("");

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



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bize Yazın");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_done, menu);
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
