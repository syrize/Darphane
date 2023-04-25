package com.turkogame.darphane.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.utils.Tools;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Profile_Edit extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private String cinsiyet[];
    private String odeme_yontemleri[];

    TextView dogum_tarihi,adi,soyadi,nick,iban,alici;
    ImageView resim;
    FloatingActionButton kamera_buton;
    Button kaydet;
    Bitmap photo;
    String imagebase64="";

    String kullanici_id,profil_foto,facebook_id,google_id,resim_url;
    Spinner spinner_cinsiyet,spinner_odeme_yontemleri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        dogum_tarihi = findViewById(R.id.dogum_tarihi);
        adi = findViewById(R.id.adi);
        soyadi = findViewById(R.id.soyadi);
        resim = findViewById(R.id.resim);

        nick = findViewById(R.id.nick);
        iban = findViewById(R.id.iban);
        alici = findViewById(R.id.alici);

        kamera_buton = findViewById(R.id.kamera_buton);
        kaydet = findViewById(R.id.btn_kaydet);

        spinner_doldur();

        initToolbar();
        initComponent();

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        profil_foto = sharedPreferences.getString("resim","0");

        //Log.d("mesaj", "aaaa :"+profil_foto);


        if (!kullanici_id.equals("")) {
            kullanici_oku();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Kamera erişimi izni istemek için kullanılan kod
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }



        kamera_buton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });





        kaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                kullanici_guncelle();

            }
        });


    }

  /*

  // Kullanıcı tarafından Kamera izni verildiğinde kameranın otomatik açılmasını sağlar

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Kamera erişimi izni verildi, kamera açılıyor
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                // Kullanıcı izin vermedi, işlemler iptal ediliyor
                Toast.makeText(this, "Kamera erişimi izni verilmedi", Toast.LENGTH_LONG).show();
            }
        }
    }

   */


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            resim.setImageBitmap(photo);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = photo;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imagebase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
    }

    private void kullanici_oku(){
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

                                    adi.setText( bilgiler.getString("ADI"));
                                    soyadi.setText( bilgiler.getString("SOYADI"));
                                    facebook_id=bilgiler.getString("FACEBOOK_ID");
                                    google_id=bilgiler.getString("GOOGLE_ID");

                                    nick.setText( bilgiler.getString("NICK"));
                                    iban.setText( bilgiler.getString("IBAN_NO"));
                                    alici.setText( bilgiler.getString("ALICI_ADSOYAD"));

                                    resim_url=bilgiler.getString("RESIM");

                                    //email.setText( bilgiler.getString("EMAIL"));

                                    if(bilgiler.getString("CINSIYET").equals("1")){ spinner_cinsiyet.setSelection(0); }
                                    if(bilgiler.getString("CINSIYET").equals("2")){ spinner_cinsiyet.setSelection(1);}
                                    if(bilgiler.getString("CINSIYET").equals("3")){ spinner_cinsiyet.setSelection(2); }

                                    if(bilgiler.getString("ODEME_YONTEMI").equals("1")){ spinner_odeme_yontemleri.setSelection(1); }
                                    if(bilgiler.getString("ODEME_YONTEMI").equals("2")){ spinner_odeme_yontemleri.setSelection(2); }
                                    if(bilgiler.getString("ODEME_YONTEMI").equals("3")){ spinner_odeme_yontemleri.setSelection(3); }



                                    if(bilgiler.getString("DOGUM_TARIHI") != "null") {
                                        Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(bilgiler.getString("DOGUM_TARIHI"));

                                        Log.d("mesaj", String.valueOf(tarih));

                                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        String yeni_tarih = String.valueOf(format.format(tarih));
                                        Log.d("mesaj", String.valueOf(yeni_tarih));
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

    private void kullanici_guncelle() {


        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(kullanici_id+"kullaniciPUT");
        String kontrol_key = md5.toUpperCase();


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kayit_tipi", "1");

            if(google_id != null && !google_id.equals("")){

                object.put("kayit_tipi", "2");
                object.put("resim_url", profil_foto);


            }
            if(facebook_id != null && !facebook_id.equals("")){
                object.put("kayit_tipi", "3");
                object.put("resim_url", profil_foto);
            }

            if (!imagebase64.equals("")){
                object.put("resim", imagebase64);
                object.put("uzanti", "jpg");
            }

            object.put("user_id", kullanici_id);
            object.put("adi", adi.getText());
            object.put("soyadi", soyadi.getText());

            object.put("iban_no", iban.getText());
            object.put("alici_adsoyad", alici.getText());
            object.put("nick", nick.getText());

            Date eski_tarih;
            String yeni_tarih;

            if(!dogum_tarihi.getText().equals( "null") && !dogum_tarihi.getText().equals( "")) {

                try {


                    eski_tarih = new SimpleDateFormat("dd.MM.yyyy").parse(dogum_tarihi.getText().toString());

                    Log.d("mesaj", String.valueOf(eski_tarih));

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    yeni_tarih = String.valueOf(format.format(eski_tarih));

                    Log.d("mesaj", String.valueOf(yeni_tarih));

                    object.put("dogum_tarihi", String.valueOf(yeni_tarih) );

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


            object.put("cinsiyet", spinner_cinsiyet.getSelectedItemId()+1);
            object.put("odeme_yontemi", spinner_odeme_yontemleri.getSelectedItemId());




        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/kullanici.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Log.d("mesaj", kontrol.getString("mesaj"));

                                showOnayDialog("Kayıt Yapıldı");


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


        final AppCompatButton bt_close = (AppCompatButton) dialog.findViewById(R.id.bt_close);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                Intent intent = new Intent(Profile_Edit.this, Profile.class);
                startActivity(intent);

                finish();


            }
        });



        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void initComponent() {

            (findViewById(R.id.dogum_tarihi)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDatePickerLight(v);
                }
            });
        }

    private void dialogDatePickerLight(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date = calendar.getTimeInMillis();
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                        ((EditText) v).setText(df.format(date));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
       // datePicker.setMinDate(cur_calender); //minimum günün tarihi giriliyor
        datePicker.show(getFragmentManager(), "Expiration Date");
    }

    private void spinner_doldur(){

        cinsiyet= new String[2];
        cinsiyet[0]="Erkek";
        cinsiyet[1]="Kadın";


        spinner_cinsiyet = (Spinner) findViewById(R.id.cinsiyet);
        ArrayAdapter adapter_cinsiyet = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, cinsiyet);
        spinner_cinsiyet.setAdapter(adapter_cinsiyet);


        odeme_yontemleri= new String[4];
        odeme_yontemleri[0]="Ödeme Yönteminizi Seçiniz";
        odeme_yontemleri[1]="Banka Hesabı";
        odeme_yontemleri[2]="Papara Hesabı";
        odeme_yontemleri[3]="İnninal Kart";


        spinner_odeme_yontemleri = (Spinner) findViewById(R.id.odeme_yontemi);
        ArrayAdapter adapter_is_durumu = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, odeme_yontemleri);
        spinner_odeme_yontemleri.setAdapter(adapter_is_durumu);


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil Güncelle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_done, menu);
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
