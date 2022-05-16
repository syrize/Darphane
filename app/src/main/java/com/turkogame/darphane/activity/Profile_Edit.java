package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String is_durumu[];
    private String iliski_durumu[];
    private String egitim_durumu[];
    TextView dogum_tarihi,adi,soyadi;
    ImageView resim;
    FloatingActionButton kamera_buton;
    Button kaydet;
    Bitmap photo;
    String imagebase64="";

    String kullanici_id,profil_foto,facebook_id,google_id,resim_url;
    Spinner spinner_cinsiyet,spinner_isdurumu,spinner_iliski_durumu,spinner_egitim_durumu;

    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);
        dogum_tarihi = findViewById(R.id.dogum_tarihi);
        adi = findViewById(R.id.adi);
        soyadi = findViewById(R.id.soyadi);
        resim = findViewById(R.id.resim);

        kamera_buton = findViewById(R.id.kamera_buton);
        kaydet = findViewById(R.id.btn_kaydet);

        spinner_doldur();
        initToolbar();
        initComponent();

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        profil_foto = sharedPreferences.getString("resim","0");

        //Log.d("mesaj", "aaaa :"+profil_foto);


        if (kullanici_id != ""){

            kullanici_oku();

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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                                    resim_url=bilgiler.getString("RESIM");

                                    //email.setText( bilgiler.getString("EMAIL"));

                                    if(bilgiler.getString("CINSIYET").equals("1")){ spinner_cinsiyet.setSelection(0); }
                                    if(bilgiler.getString("CINSIYET").equals("2")){ spinner_cinsiyet.setSelection(1);}
                                    if(bilgiler.getString("CINSIYET").equals("3")){ spinner_cinsiyet.setSelection(2); }

                                    if(bilgiler.getString("IS_DURUMU").equals("1")){ spinner_isdurumu.setSelection(1); }
                                    if(bilgiler.getString("IS_DURUMU").equals("2")){ spinner_isdurumu.setSelection(2); }
                                    if(bilgiler.getString("IS_DURUMU").equals("3")){ spinner_isdurumu.setSelection(3); }
                                    if(bilgiler.getString("IS_DURUMU").equals("4")){ spinner_isdurumu.setSelection(4);}
                                    if(bilgiler.getString("IS_DURUMU").equals("5")){ spinner_isdurumu.setSelection(5); }
                                    if(bilgiler.getString("IS_DURUMU").equals("6")){ spinner_isdurumu.setSelection(6); }
                                    if(bilgiler.getString("IS_DURUMU").equals("7")){ spinner_isdurumu.setSelection(7); }
                                    if(bilgiler.getString("IS_DURUMU").equals("8")){ spinner_isdurumu.setSelection(8); }
                                    if(bilgiler.getString("IS_DURUMU").equals("9")){ spinner_isdurumu.setSelection(9); }
                                    if(bilgiler.getString("IS_DURUMU").equals("10")){ spinner_isdurumu.setSelection(10); }
                                    if(bilgiler.getString("IS_DURUMU").equals("11")){ spinner_isdurumu.setSelection(11); }
                                    if(bilgiler.getString("IS_DURUMU").equals("12")){ spinner_isdurumu.setSelection(12); }

                                    if(bilgiler.getString("ILISKI_DURUMU").equals("1")){ spinner_iliski_durumu.setSelection(1); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("2")){ spinner_iliski_durumu.setSelection(2); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("3")){ spinner_iliski_durumu.setSelection(3);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("4")){ spinner_iliski_durumu.setSelection(4);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("5")){ spinner_iliski_durumu.setSelection(5);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("6")){ spinner_iliski_durumu.setSelection(6); }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("7")){ spinner_iliski_durumu.setSelection(7);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("8")){ spinner_iliski_durumu.setSelection(8);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("9")){ spinner_iliski_durumu.setSelection(9);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("10")){ spinner_iliski_durumu.setSelection(10);  }
                                    if(bilgiler.getString("ILISKI_DURUMU").equals("11")){ spinner_iliski_durumu.setSelection(11);  }

                                    if(bilgiler.getString("EGITIM_DURUMU").equals("1")){ spinner_egitim_durumu.setSelection(1);  }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("2")){ spinner_egitim_durumu.setSelection(2); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("3")){ spinner_egitim_durumu.setSelection(3); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("4")){ spinner_egitim_durumu.setSelection(4); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("5")){ spinner_egitim_durumu.setSelection(5); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("6")){ spinner_egitim_durumu.setSelection(6); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("7")){ spinner_egitim_durumu.setSelection(7); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("8")){ spinner_egitim_durumu.setSelection(8); }
                                    if(bilgiler.getString("EGITIM_DURUMU").equals("9")){ spinner_egitim_durumu.setSelection(9); }

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
            object.put("is_durumu", spinner_isdurumu.getSelectedItemId());
            object.put("iliski_durumu", spinner_iliski_durumu.getSelectedItemId());
            object.put("egitim_durumu", spinner_egitim_durumu.getSelectedItemId());



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

                                showOnayDialog();


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



    private void showOnayDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_onay);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
      //  lp.verticalMargin=50;

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

        cinsiyet= new String[3];
        cinsiyet[0]="Erkek";
        cinsiyet[1]="Kadın";
        cinsiyet[2]="LGBT";

        spinner_cinsiyet = (Spinner) findViewById(R.id.cinsiyet);
        ArrayAdapter adapter_cinsiyet = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, cinsiyet);
        spinner_cinsiyet.setAdapter(adapter_cinsiyet);


        is_durumu= new String[13];
        is_durumu[0]="İş Durumunuzu Seçiniz";
        is_durumu[1]="Çalışıyor";
        is_durumu[2]="Okuyor";
        is_durumu[3]="İş Arıyor";
        is_durumu[4]="İlgilenmiyor";
        is_durumu[5]="Ev Hanımı";
        is_durumu[6]="Öğrenci";
        is_durumu[7]="Kendi İşini Yapıyor";
        is_durumu[8]="Akademisyen";
        is_durumu[9]="Kamu Sektörü";
        is_durumu[10]="Özel Sektör";
        is_durumu[11]="Emekli";
        is_durumu[12]="Çalışmıyor";


        spinner_isdurumu = (Spinner) findViewById(R.id.is_durumu);
        ArrayAdapter adapter_is_durumu = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, is_durumu);
        spinner_isdurumu.setAdapter(adapter_is_durumu);

        iliski_durumu= new String[12];
        iliski_durumu[0]="İlişki Durumunuzu Seçiniz";
        iliski_durumu[1]="İlişkisi Yok";
        iliski_durumu[2]="Evli";
        iliski_durumu[3]="Boşanmış";
        iliski_durumu[4]="Dul";
        iliski_durumu[5]="İlişkisi Var";
        iliski_durumu[6]="Nişanlı";
        iliski_durumu[7]="Platonik";
        iliski_durumu[8]="Ayrılmış";
        iliski_durumu[9]="Flört Halinde";
        iliski_durumu[10]="Karmaşık";
        iliski_durumu[11]="Ayrı Yaşıyor";


        spinner_iliski_durumu = (Spinner) findViewById(R.id.iliski_durumu);
        ArrayAdapter adapter_iliski_durumu = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, iliski_durumu);
        spinner_iliski_durumu.setAdapter(adapter_iliski_durumu);


        egitim_durumu= new String[10];
        egitim_durumu[0]="Eğitim Durumunuzu Seçiniz";
        egitim_durumu[1]="İlk OKul";
        egitim_durumu[2]="Orta Okul";
        egitim_durumu[3]="Lise";
        egitim_durumu[4]="Yüksek Okul";
        egitim_durumu[5]="Üniversite";
        egitim_durumu[6]="Yüksek Lisans";
        egitim_durumu[7]="Doktora";
        egitim_durumu[8]="Okur Yazar";
        egitim_durumu[9]="Okuma Yazma Bilmiyor";

        spinner_egitim_durumu = (Spinner) findViewById(R.id.egitim_durumu);
        ArrayAdapter adapter_egitim_durumu = new ArrayAdapter(this,  android.R.layout.simple_spinner_item, egitim_durumu);
        spinner_egitim_durumu.setAdapter(adapter_egitim_durumu);



    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profil Güncelle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.pink_900);
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
