package com.turkogame.darphane.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Kisisel_Bilgiler extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private String cinsiyet[];
    private String is_durumu[];
    private String iliski_durumu[];
    private String egitim_durumu[];
    private String kimin_icin[];
    TextView dogum_tarihi,adi,soyadi;
    String kullanici_id,falci_id,token;
    int fal_turu=0;
    Spinner spinner_cinsiyet,spinner_isdurumu,spinner_iliski_durumu,spinner_egitim_durumu,spinner_kimin_icin;

    Button btn_devam,btn_kendim,btn_baskasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kisisel_bilgiler);
        btn_devam = findViewById(R.id.btn_devam);
        btn_kendim = findViewById(R.id.btn_kendim);
        btn_baskasi = findViewById(R.id.btn_baskasi);
        dogum_tarihi = findViewById(R.id.dogum_tarihi);
        adi = findViewById(R.id.adi);
        soyadi = findViewById(R.id.soyadi);



        sharedPreferences = getApplicationContext().getSharedPreferences("fal_kontrol", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        fal_turu = sharedPreferences.getInt("fal_turu",0);
        falci_id = sharedPreferences.getString("falci_id","0");
        token = sharedPreferences.getString("token","0");

        //Toast.makeText(Kisisel_Bilgiler.this, falci_id, Toast.LENGTH_LONG).show();

        spinner_doldur();
        initToolbar();
        initComponent();

        btn_kendim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                kullanici_oku();

            }
        });

        btn_baskasi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                adi.setText("");
                soyadi.setText("");
                dogum_tarihi.setText("");
                spinner_cinsiyet.setSelection(0);
                spinner_isdurumu.setSelection(0);
                spinner_iliski_durumu.setSelection(0);
                spinner_egitim_durumu.setSelection(0);

            }
        });


        btn_devam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!adi.getText().equals("") && !soyadi.getText().equals("") && !dogum_tarihi.getText().equals("")
                && spinner_cinsiyet.getSelectedItemId()>0 &&  spinner_isdurumu.getSelectedItemId()>0
                && spinner_iliski_durumu.getSelectedItemId()>0  && spinner_egitim_durumu.getSelectedItemId()>0) {

                    sharedPreferences = getApplicationContext().getSharedPreferences("istek_bilgileri", 0);
                    SharedPreferences.Editor bilgiler = sharedPreferences.edit();
                    bilgiler.putString("user_id", kullanici_id);
                    bilgiler.putString("falci_id", falci_id);
                    bilgiler.putString("token", token);
                    bilgiler.putString("adi", adi.getText().toString());
                    bilgiler.putString("soyadi", soyadi.getText().toString());
                    bilgiler.putString("cinsiyet", String.valueOf(spinner_cinsiyet.getSelectedItemId()));
                    bilgiler.putString("is_durumu", String.valueOf(spinner_isdurumu.getSelectedItemId()));
                    bilgiler.putString("iliski_durumu", String.valueOf(spinner_iliski_durumu.getSelectedItemId()));
                    bilgiler.putString("egitim_durumu", String.valueOf(spinner_egitim_durumu.getSelectedItemId()));
                    bilgiler.putString("dogum_tarihi", String.valueOf(dogum_tarihi.getText()));

                    bilgiler.commit();



                    if (fal_turu == 3) {
                        Intent intent = new Intent(Kisisel_Bilgiler.this, El_Istek.class);
                        startActivity(intent);
                    }


                    if (fal_turu == 4) {
                        Intent intent = new Intent(Kisisel_Bilgiler.this, Ruya_Istek.class);
                        startActivity(intent);
                    }



                } else {

                    Toast.makeText(Kisisel_Bilgiler.this, "Lütfen kişisel bilgilerinizi girin.", Toast.LENGTH_LONG).show();

                }


            }
        });

        kullanici_oku();
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
        //datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Expiration Date");
    }

    private void spinner_doldur(){

        kimin_icin= new String[2];
        kimin_icin[0]="Kendim İçin";
        kimin_icin[1]="Başkası İçin";


        spinner_kimin_icin = (Spinner) findViewById(R.id.kimin_icin);
        ArrayAdapter adapter_kimin_icin= new ArrayAdapter(this,  android.R.layout.simple_spinner_item, kimin_icin);
        spinner_kimin_icin.setAdapter(adapter_kimin_icin);

        spinner_kimin_icin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner_kimin_icin.getSelectedItemId()==1) {
                    adi.setText("");
                    soyadi.setText("");
                    dogum_tarihi.setText("");
                    spinner_cinsiyet.setSelection(0);
                    spinner_isdurumu.setSelection(0);
                    spinner_iliski_durumu.setSelection(0);
                    spinner_egitim_durumu.setSelection(0);

                } else if (spinner_kimin_icin.getSelectedItemId()==0) {
                    kullanici_oku();

                }

                try {
                    //Your task here
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        cinsiyet= new String[4];
        cinsiyet[0]="Seçiniz";
        cinsiyet[1]="Erkek";
        cinsiyet[2]="Kadın";
        cinsiyet[3]="LGBT";

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
        getSupportActionBar().setTitle("Kişisel Bilgiler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.pink_900);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate(R.menu.menu_done, menu);
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


                                    //email.setText( bilgiler.getString("EMAIL"));

                                    if(bilgiler.getString("CINSIYET").equals("1")){ spinner_cinsiyet.setSelection(1); }
                                    if(bilgiler.getString("CINSIYET").equals("2")){ spinner_cinsiyet.setSelection(2);}
                                    if(bilgiler.getString("CINSIYET").equals("3")){ spinner_cinsiyet.setSelection(3); }

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

                                    if(!bilgiler.getString("DOGUM_TARIHI").equals("null")) {

                                        Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(bilgiler.getString("DOGUM_TARIHI"));

                                        Log.d("mesaj", String.valueOf(tarih));

                                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                        String yeni_tarih = String.valueOf(format.format(tarih));
                                        Log.d("mesaj", String.valueOf(yeni_tarih));
                                        dogum_tarihi.setText(yeni_tarih);
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




}
