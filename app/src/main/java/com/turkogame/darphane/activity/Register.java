package com.turkogame.darphane.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    String firebase_user_id;



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
        mAuth=FirebaseAuth.getInstance();

        Tools.setSystemBarColor(this, R.color.system_bar);

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

                       String adix=adi.getText().toString();
                       String soyadix=soyadi.getText().toString();
                       String emailx=email.getText().toString();
                       String parolax=parola.getText().toString();
                       String parola_tekrarx=parola_tekrar.getText().toString();

                      int kontrol=0;

                       if (TextUtils.isEmpty(adix)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.ad));
                           kontrol=1;
                       } else if (TextUtils.isEmpty(soyadix)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.soyad));
                           kontrol=2;
                       } else  if (TextUtils.isEmpty(emailx)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.email));
                           kontrol=3;
                       }else  if (TextUtils.isEmpty(parolax)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.parola));
                           kontrol=4;
                       } else if (TextUtils.isEmpty(parola_tekrarx)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.parola_tekrar));
                           kontrol=5;
                       } else if (!parolax.equals(parola_tekrarx)) {
                           YoYo.with(Techniques.Tada).duration(700).repeat(5).playOn(findViewById(R.id.parola_tekrar));
                           kontrol=6;
                       }

                       Toast toast;

                       if (kontrol>0){
                           toast = Toast.makeText(getApplicationContext(),"Verilerde Hata", Toast.LENGTH_LONG);
                           if (kontrol==1) { toast = Toast.makeText(getApplicationContext(),"Ad boş olamaz", Toast.LENGTH_LONG);}
                           if (kontrol==2) { toast = Toast.makeText(getApplicationContext(),"Soyad boş olamaz", Toast.LENGTH_LONG);}
                           if (kontrol==3) { toast = Toast.makeText(getApplicationContext(),"Email boş olamaz", Toast.LENGTH_LONG);}
                           if (kontrol==4) { toast = Toast.makeText(getApplicationContext(),"Parola boş olamaz", Toast.LENGTH_LONG);}
                           if (kontrol==5) { toast = Toast.makeText(getApplicationContext(),"Parola Tekrarı boş olamaz", Toast.LENGTH_LONG);}
                           if (kontrol==6) { toast = Toast.makeText(getApplicationContext(),"Parola ve Tekrarı uyuşmuyor", Toast.LENGTH_LONG);}



                           toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                           toast.show();


                       }else{
                           register_user(adix+" "+soyadix,parolax,emailx);

                       }





               }else{
                   Toast toast = Toast.makeText(getApplicationContext(), "Lütfen Şartları Kabul edin", Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                   toast.show();

               }


            }
        });







    }

    private void register_user(String name, String password, String email){

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebase_user_id=mAuth.getUid();

                    kullanici_kayit();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "Hata: "+task.getException().getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                }
            }
        });
    }





    public void kullanici_kayit() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(email.getText().toString()+"kullaniciPOST");
        String kontrol_key = md5.toUpperCase();

        Log.d("mesaj-kullaniciPOST1: ", "aaaaaaaaaaaaaaaaaaaa");

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kayit_tipi", "1");
            object.put("ad", adi.getText().toString());
            object.put("soyad", soyadi.getText().toString());
            object.put("email", email.getText().toString());
            object.put("sifre", parola.getText().toString());
            object.put("firebase_user_id", firebase_user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("mesaj-kullaniciPOST2: ", "bbbbbbbbbbbbbbbbbbbb");

        String url = AppConfig.URL + "/kullanici.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mesaj-kullaniciPOST3: ", response.toString());
                        try {
                            JSONObject kontrol = new JSONObject(response.toString());


                            if (kontrol.getString("hata")=="false"){

                                Toast toast = Toast.makeText(getApplicationContext(), "Kullanıcı Kaydı Yapıldı", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();


                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                                finish();


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
