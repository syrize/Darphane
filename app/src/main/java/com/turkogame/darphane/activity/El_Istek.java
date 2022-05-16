package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class El_Istek extends AppCompatActivity {
    SharedPreferences sharedPreferences,kayit_kontrol;
    Spinner spinner_istek_turu;
    private static final int CAMERA_REQUEST = 1888;
    int kontrol=0;
    ImageView resim1,resim2;
    String kullanici_id,falci_id,adi,soyadi,cinsiyet,is_durumu,iliski_durumu,egitim_durumu,dogum_tarihi,token;
    Bitmap photo;
    String imagebase64_1="";
    String imagebase64_2="";
    MainMenu mainMenu;
    Uri imageUri;
    private final int SELECT_IMAGE = 12;

    private String istek_turu[];

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.el_istek);

        reklam_yukle();

        sharedPreferences = getApplicationContext().getSharedPreferences("istek_bilgileri", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        falci_id = sharedPreferences.getString("falci_id","0");
        adi = sharedPreferences.getString("adi","0");
        soyadi = sharedPreferences.getString("soyadi","0");
        cinsiyet = sharedPreferences.getString("cinsiyet","0");
        is_durumu = sharedPreferences.getString("is_durumu","0");
        iliski_durumu = sharedPreferences.getString("iliski_durumu","0");
        egitim_durumu = sharedPreferences.getString("egitim_durumu","0");
        dogum_tarihi = sharedPreferences.getString("dogum_tarihi","0");
        token = sharedPreferences.getString("token","0");

        initToolbar();
        spinner_doldur();


        Button  gonder = findViewById(R.id.btn_gonder);

        resim1 = findViewById(R.id.el_resim1);
        resim2 = findViewById(R.id.el_resim2);


        resim1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                kontrol=1;
                showSecimDialog();
            }
        });

        resim2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                kontrol=2;
                showSecimDialog();
            }
        });


        gonder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.

                        startActivity(new Intent(El_Istek.this, MainMenu.class));
                        finishAffinity();

                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });


                istek_gonder();
            }
        });

    }

    private void reklam_yukle(){

        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(this,AppConfig.gecis_reklam_id, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.d("Mesaj:", "Reklam Yüklendi");

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d("Mesaj:", loadAdError.getMessage());
                mInterstitialAd = null;
            }


        });



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            if (kontrol == 1) {
                resim1.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = photo;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagebase64_1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }

            if (kontrol == 2) {
                resim2.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = photo;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagebase64_2 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }

        }


        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (kontrol == 1) {

                resim1.setImageURI(imageUri);

                BitmapDrawable drawable = (BitmapDrawable) resim1.getDrawable();
                photo = drawable.getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = photo;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagebase64_1 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            }

            if (kontrol == 2) {

                resim2.setImageURI(imageUri);

                BitmapDrawable drawable = (BitmapDrawable) resim2.getDrawable();
                photo = drawable.getBitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = photo;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imagebase64_2 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            }


        } else if (resultCode == RESULT_CANCELED) {


        }
    }

    private void spinner_doldur() {

        istek_turu = new String[4];
        istek_turu[0] = "Genel";
        istek_turu[1] = "Aşk";
        istek_turu[2] = "Kariyer/Para";
        istek_turu[3] = "Sağlık";


        spinner_istek_turu = (Spinner) findViewById(R.id.istek_turu);
        ArrayAdapter adapter_istek_turu = new ArrayAdapter(this, android.R.layout.simple_spinner_item, istek_turu);
        spinner_istek_turu.setAdapter(adapter_istek_turu);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("El Falı İstek");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
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

        private void showSecimDialog() {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_foto);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final ImageView galeri = (ImageView) dialog.findViewById(R.id.galeri);
            final ImageView kamera = (ImageView) dialog.findViewById(R.id.kamera);

            ((ImageView) dialog.findViewById(R.id.galeri)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("image/*");
                    chooseFile.addCategory(Intent.CATEGORY_OPENABLE);

                    startActivityForResult(Intent.createChooser(chooseFile, "Choose a photo"), SELECT_IMAGE);


                    dialog.dismiss();
                }
            });

            ((ImageView) dialog.findViewById(R.id.kamera)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    dialog.dismiss();

                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }


        public void istek_gonder(){

        Date eski_tarih;
        String yeni_tarih;


        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(kullanici_id+"el_faliPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kullanici_id", kullanici_id);
            object.put("cinsiyet", cinsiyet);
            object.put("fal_turu", "3");
            object.put("istek_turu", spinner_istek_turu.getSelectedItemId()+1);
            object.put("resim1", imagebase64_1);
            object.put("resim1_uzanti", "jpg");
            object.put("resim2", imagebase64_2);
            object.put("resim2_uzanti", "jpg");
            object.put("secilen_falci_id", falci_id);
            object.put("adi", adi);
            object.put("soyadi", soyadi);
            object.put("cinsiyet", cinsiyet);
            object.put("token", token);

            try {

                eski_tarih = new SimpleDateFormat("dd.MM.yyyy").parse(dogum_tarihi);

                Log.d("mesaj", String.valueOf(eski_tarih));

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                yeni_tarih = String.valueOf(format.format(eski_tarih));

                object.put("dogum_tarihi", String.valueOf(yeni_tarih));

                Log.d("mesaj", String.valueOf(yeni_tarih));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            object.put("is_durumu", is_durumu);
            object.put("iliski_durumu", iliski_durumu);
            object.put("egitim_durumu", egitim_durumu);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/el_fali.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Log.d("Mesaj:", kontrol.toString());
                                Log.d("Mesaj:", kontrol.getString("istek_id"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("ElFali", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("istek_id",kontrol.getString("istek_id"));

                                editor.commit();


                                Toast toast = Toast.makeText(getApplicationContext(), "Fal isteği gönderildi", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();


                               kredi_oku();

                                if (mInterstitialAd != null) {

                                    Log.d("Mesaj:", "Çalışma alanına girdi");

                                    mInterstitialAd.show(El_Istek.this);

                                } else {

                                    Log.d("Mesaj:", "Reklam Yüklenemedi");

                                    startActivity(new Intent(El_Istek.this, MainMenu.class));
                                    finishAffinity();
                                }

                            } else {

                                //Log.d("Mesaj:", kontrol.getString("hataMesaj"));

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
                Log.d("show", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    public void kredi_oku(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&islem=2&kontrol_key="+kontrol_key;


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            //Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){



                                    kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                    kayitci.putString("kredi",  kontrol.getString("kredi_miktari"));
                                    kayitci.putInt("bakiye_sorgula",1);
                                    kayitci.commit();


                                    // Log.d("mesaj", kontrol.getString("kredi_miktari") );


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



}
