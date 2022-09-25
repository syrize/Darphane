package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

import java.text.DecimalFormat;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Para_Cekme extends AppCompatActivity {
    Button btn_paracek,btn_talepler;
    SharedPreferences bakiye_kontrol;
    TextView kredi,bakiye;
    private ProgressBar progress;
    SharedPreferences sharedPreferences,kayit_kontrol;
    String user_id;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.para_cekme);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        user_id = sharedPreferences.getString("user_id","0");


        btn_paracek = (Button) findViewById(R.id.btn_paracek);
        kredi = (TextView) findViewById(R.id.kredi);
        bakiye = (TextView) findViewById(R.id.bakiye);
        progress = (ProgressBar) findViewById(R.id.progress);
        initToolbar();
        df = new DecimalFormat("#.##");

        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
        kredi.setText(bakiye_kontrol.getString("kredi","0"));
        bakiye.setText(String.valueOf(df.format(Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005)) + " TL");

        int odenecek_kredi = Integer.parseInt(bakiye_kontrol.getString("kredi","0"))-100;

        float tutar = Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005f;

        int pg_bakiye= (int) Math.round(tutar);

        progress.setProgress(pg_bakiye);

        btn_paracek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005 >= 50 ){


                    String md5= AppConfig.md5(user_id+"odeme_talepleriPOST");
                    String kontrol_key = md5.toUpperCase();

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JSONObject object = new JSONObject();
                    try {
                        object.put("kontrol_key", kontrol_key);
                        object.put("user_id", user_id);
                        object.put("tutar", tutar);
                        object.put("islem_turu", 1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = AppConfig.URL + "/odeme_talepleri.php";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Veri", response.toString());

                                    try {
                                        JSONObject kontrol = new JSONObject(response.toString());

                                        if (kontrol.getString("hata")=="false"){

                                                Kredi_Girisi.kredi_cikisi(user_id,"10" ,Integer.toString(odenecek_kredi));
                                                try {
                                                    Thread.sleep(500);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                            showOnayDialog("Ödeme Talebiniz Alındı");



                                        } else {

                                            Log.d("mesaj", kontrol.getString("hataMesaj"));



                                        }


                                    } catch (Exception e) {

                                    }


                                }
                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("snow", error.toString());
                        }
                    });
                    requestQueue.add(jsonObjectRequest);







                } else{
                    Toast.makeText(Para_Cekme.this, "Para Çekmek için Bakiyeniz Yetersiz! " ,
                            Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

    public void kredi_oku(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        String md5= AppConfig.md5(user_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+user_id+"&islem=2&kontrol_key="+kontrol_key;

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


                                    bakiye.setText(String.valueOf(df.format(Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005)) + " TL");


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


    private void showOnayDialog(String mesaj_metni) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_onay);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final TextView mesaj = (TextView) dialog.findViewById(R.id.mesaj);
        mesaj.setText(mesaj_metni);
        //  lp.verticalMargin=50;

        final AppCompatButton bt_close = (AppCompatButton) dialog.findViewById(R.id.bt_close);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                kredi_oku();

            }
        });



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
}
