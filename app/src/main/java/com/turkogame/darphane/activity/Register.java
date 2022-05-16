package com.turkogame.darphane.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import eightbitlab.com.blurview.BlurView;

public class Register extends AppCompatActivity {

    private View parent_view;
    SharedPreferences sharedPreferences;
    TextInputEditText adi,soyadi,email,parola,parola_tekrar;
    CheckBox sartlar;
    Button kayit_buton;
    ImageView logo_mobil;
    RelativeLayout ana_view;
    CardView form_alani;
    int blurred = 0;
    BlurView blurView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        adi = findViewById(R.id.ad);
        soyadi = findViewById(R.id.soyad);
        email = findViewById(R.id.email);
        parola = findViewById(R.id.parola);
        parola_tekrar = findViewById(R.id.parola_tekrar);
        sartlar =  findViewById(R.id.sartlar);
        kayit_buton =  findViewById(R.id.kayit_buton);
        logo_mobil =  findViewById(R.id.logo_mobil);
        ana_view =  findViewById(R.id.ana_view);
        form_alani =  findViewById(R.id.form_alani);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        parent_view = findViewById(android.R.id.content);




        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });


        kayit_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(sartlar.isChecked()) {
                   if (parola.getText().toString().equals(parola_tekrar.getText().toString())) {


                       YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.menu_baslik)); //sallanan nesne kodu

                      // Blurry.with(Register.this).capture(view).into(logo_mobil);

                     //  Blurry.with(Register.this).capture(view).into(findViewById(R.id.logo_mobil));

                     /*   if (blurred==0) {
                            ana_view.measure(100, 100);
                            //Blurry.with(Register.this).radius(25).sampling(2).onto(ana_view);
                            //Blurry.with(Register.this).radius(25).sampling(2).async().animate(500).onto(ana_view);
                            Blurry.with(Register.this)
                                    .radius(5)
                                    .sampling(2)
                                    .async()
                                    .animate(200)
                                    .onto(form_alani);

                            blurred=1;
                        } else {
                           Blurry.delete(findViewById(R.id.ana_view));
                            blurred=0;
                       }*/





                       kullanici_kayit();
                   } else {

                       Toast toast = Toast.makeText(getApplicationContext(), "Parola ve Tekrarı uyuşmuyor", Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                       toast.show();
                   }
               }else{
                   Toast toast = Toast.makeText(getApplicationContext(), "Lütfen Şartları Kabul edin", Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                   toast.show();

               }


            }
        });


        Tools.setSystemBarColor(this, R.color.colorBanner);




    }





    public void kullanici_kayit() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(email.getText().toString()+"kullaniciPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kayit_tipi", "1");
            object.put("ad", adi.getText().toString());
            object.put("soyad", soyadi.getText().toString());
            object.put("email", email.getText().toString());
            object.put("sifre", parola.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/kullanici.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){


                                Log.d("snow", kontrol.getString("mesaj"));

                                Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("mesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

                              /*  sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",email);
                                editor.putString("user_id",kontrol.getString("user_id"));
                                editor.putString("adi",name);
                                editor.putString("soyadi",last_name);
                                editor.putString("login","1");
                                editor.commit();*/


                            } else {

                                Log.d("snow", kontrol.getString("hataMesaj"));

                                Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();

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
    }



}
