package com.turkogame.darphane.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.KrediPaketleriItem;
import com.turkogame.darphane.utils.Tools;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.android.billingclient.api.BillingClient.BillingResponseCode.OK;

public class MainMenu extends AppCompatActivity implements PurchasesUpdatedListener {

    private ActionBar actionBar;
    private Toolbar toolbar;
    DrawerLayout drawer;
    GoogleSignInClient mGoogleSignInClient;
    ImageView profil_resmi;
    TextView uygulama_adi,kredi;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;
    SharedPreferences sharedPreferences,kayit_kontrol,reklam_kontrol,bakiye_kontrol;
    String kullanici_id,token,paket_id,paket_adi,tutar;
    FloatingActionButton alt_menu_fal_istek;
    private BillingClient mBillingClient;
    List<KrediPaketleriItem> list;
    private Handler mHandler = new Handler();
    int timer_kontrol=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();

       // Log.d("token", "token :"+ FirebaseInstanceId.getInstance().getToken());
        token= FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");

        alt_menu_fal_istek = (FloatingActionButton) findViewById(R.id.alt_menu_fal_istek);
        profil_resmi = (ImageView) findViewById(R.id.logo_mobil);
        uygulama_adi=(TextView) findViewById(R.id.uygulama_adi);
        kredi=(TextView) findViewById(R.id.kredi);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initToolbar();
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
            //Log.d("mesaj", "resim :"+String.valueOf(personPhoto));
            login();

            /*
            //google bilgileri burada alınıyor
            uygulama_adi.setText(personName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(profil_resmi);

             */
        }


        initNavigationMenu();
        initComponent();
       // card_2.setChecked(true);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");


        tiklama_kontrol();
        kredi_oku();


        startTime();


    }

    private void startTime() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 500);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            bakiye_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
            int bakiye_sorgula = bakiye_kontrol.getInt("bakiye_sorgula",0);

            if (bakiye_sorgula==1){

                kredi.setText( bakiye_kontrol.getString("kredi","0"));


                SharedPreferences.Editor kayitci = bakiye_kontrol.edit();
                kayitci.putInt("bakiye_sorgula",0);
                kayitci.commit();

            }

            mHandler.postDelayed(this, 5000);
        }
    };




    private void showFalDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_fallar);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.verticalMargin=50;

        final ImageView kahve = (ImageView) dialog.findViewById(R.id.dialog_kahve);
        final ImageView tarot = (ImageView) dialog.findViewById(R.id.dialog_tarot);
        final ImageView el = (ImageView) dialog.findViewById(R.id.dialog_el);
        final ImageView yuz = (ImageView) dialog.findViewById(R.id.dialog_yuz);
        final ImageView ruya = (ImageView) dialog.findViewById(R.id.dialog_ruya);

        ((ImageView) dialog.findViewById(R.id.dialog_kahve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,1, Falcilar.class);
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.dialog_tarot)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,2, Falcilar.class);
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.dialog_el)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,3, Falcilar.class);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.dialog_yuz)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,5, Falcilar.class);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.dialog_ruya)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,4, Falcilar.class);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }






    private void GoogleSignOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email","");
                        editor.putString("user_id","");
                        editor.putString("adi","");
                        editor.putString("soyadi","");
                        editor.putString("login","0");
                        editor.commit();
                        
                        Toast.makeText(MainMenu.this,"Çıkış Yapıldı",Toast.LENGTH_LONG).show();


                    }
                });
    }


    private void tiklama_kontrol(){

        ImageButton btn_menu = findViewById(R.id.bt_menu);
        MaterialCardView kahve_buton = findViewById(R.id.kahve_buton);
        MaterialCardView bilkazan_buton = findViewById(R.id.bilkazan_buton);
        MaterialCardView el_buton = findViewById(R.id.el_buton);
        MaterialCardView sans_carki = findViewById(R.id.sans_carki);

        MaterialCardView card_7 = findViewById(R.id.card_7);
        MaterialCardView card_8 = findViewById(R.id.card_8);
        MaterialCardView card_9 = findViewById(R.id.card_9);
        MaterialCardView card_10 = findViewById(R.id.card_10);
        MaterialCardView card_11 = findViewById(R.id.card_11);
        MaterialCardView card_12 = findViewById(R.id.card_12);

        ImageButton menu_close = findViewById(R.id.menu_close);
        ImageView alt_menu_fallarim = findViewById(R.id.alt_menu_fallarim);
        ImageView alt_menu_duyurular = findViewById(R.id.alt_menu_duyurular);
        ImageView alt_menu_burclar = findViewById(R.id.alt_menu_burclar);

        LinearLayout menu_kahvefali=findViewById(R.id.menu_kahvefali);
        LinearLayout menu_tarotfali=findViewById(R.id.menu_tarotfali);
        LinearLayout menu_elfali=findViewById(R.id.menu_Elfali);
        LinearLayout menu_yuzfali=findViewById(R.id.menu_yuzfali);
        LinearLayout menu_ruyayorumu=findViewById(R.id.menu_ruyayorumu);
        LinearLayout menu_gunlukburc=findViewById(R.id.menu_gunlukburc);
        LinearLayout menu_fallarim=findViewById(R.id.menu_fallarim);
        LinearLayout menu_kredisatinal=findViewById(R.id.menu_kredisatinal);
        LinearLayout menu_kredikazan=findViewById(R.id.menu_kredikazan);
        LinearLayout menu_reklamkaldir=findViewById(R.id.menu_reklamkaldir);
        LinearLayout menu_bizeyazin=findViewById(R.id.menu_bizeyazin);
        LinearLayout menu_profile=findViewById(R.id.menu_profile);
        LinearLayout menu_ayarlar=findViewById(R.id.menu_ayarlar);
        LinearLayout hakkinda=findViewById(R.id.hakkinda);
        LinearLayout cikis_yap=findViewById(R.id.cikis_yap);

        cikis_yap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    // ...
                    case R.id.cikis_yap:
                        GoogleSignOut();
                        break;
                    // ...
                }



                Intent intent = new Intent(MainMenu.this, Login.class);
                startActivity(intent);

                finish();
            }
        });

        alt_menu_fal_istek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFalDialog();
            }
        });


        alt_menu_fallarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this, Fallarim.class);
                startActivity(intent);
            }
        });

        menu_fallarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this, Fallarim.class);
                startActivity(intent);
            }
        });


        alt_menu_burclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                kayitci.putString("user_id", kullanici_id);
                kayitci.commit();

                Intent intent = new Intent(MainMenu.this, Burclar.class);
                startActivity(intent);
            }
        });

        alt_menu_duyurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Duyurular.class);
                startActivity(intent);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        menu_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        hakkinda.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Hakkinda.class);
                startActivity(intent);

            }
        });

        menu_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Profile.class);
                startActivity(intent);

            }
        });



        kahve_buton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,1, Falcilar.class);

            }
        });

        menu_kahvefali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,1, Falcilar.class);

            }
        });


        bilkazan_buton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainMenu.this, Bilkazan_Start.class);
                startActivity(intent);


            }
        });

        menu_tarotfali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,2, Falcilar.class);

            }
        });

        el_buton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,3, Falcilar.class);

            }
        });

        menu_elfali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id,token, 3, Falcilar.class);

            }
        });

        sans_carki.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MainMenu.this, Hediye_Carki.class);
                    startActivity(intent);

                }

        });

        menu_yuzfali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,5, Falcilar.class);

            }
        });






        menu_ruyayorumu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fal_baslat( kullanici_id, token,4, Falcilar.class);

            }
        });




        menu_gunlukburc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                kayitci.putString("user_id", kullanici_id);
                kayitci.commit();

                Intent intent = new Intent(MainMenu.this, Burclar.class);
                startActivity(intent);

            }
        });

        card_7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Kredi_Paketleri.class);
                startActivity(intent);

            }
        });

        menu_kredisatinal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Kredi_Paketleri.class);
                startActivity(intent);

            }
        });

        card_8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Kredi_Kazan.class);
                startActivity(intent);

            }
        });

        menu_kredikazan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Kredi_Kazan.class);
                startActivity(intent);

            }
        });



        card_9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paketi_oku("3");

                buySubscription("reklamlari_kaldir1");

            }
        });

        card_10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Bize_Yazin.class);
                startActivity(intent);

            }
        });

        menu_bizeyazin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Bize_Yazin.class);
                startActivity(intent);

            }
        });

        card_11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Profile.class);
                startActivity(intent);

            }
        });

        menu_profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Profile.class);
                startActivity(intent);

            }
        });

        card_12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Ayarlar.class);
                startActivity(intent);

            }
        });

        menu_ayarlar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Ayarlar.class);
                startActivity(intent);

            }
        });

    }


    private void fal_baslat(String kullanici_id, String fb_token, int fal_turu, Class form){

        kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("user_id", kullanici_id);
        kayitci.putString("token",fb_token);
        kayitci.putInt("fal_turu",fal_turu);

        kayitci.commit();

        Intent intent = new Intent(MainMenu.this, form);
        startActivity(intent);

    }


    private void initToolbar() {
        Tools.setSystemBarColor(this, R.color.system_bar);


    }

    private void initComponent() {
        // display image

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

    private void initNavigationMenu() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }


    public void kullanici_kayit() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String md5= AppConfig.md5(personEmail+"kullaniciPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("kayit_tipi", "2");
            object.put("ad", personGivenName);
            object.put("soyad", personFamilyName);
            object.put("email", personEmail);
            object.put("sifre", personId);
            object.put("google_id", personId);
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

                                sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",personEmail);
                                editor.putString("user_id",kontrol.getString("user_id"));
                                editor.putString("adi",personGivenName);
                                editor.putString("soyadi",personFamilyName);
                                editor.putString("login","1");
                                editor.commit();


                            } else {

                                Log.d("snow", kontrol.getString("hataMesaj"));

                                /*Toast toast = Toast.makeText(getApplicationContext(), kontrol.getString("hataMesaj"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();*/

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

    public void login() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String md5=AppConfig.md5(personEmail+"loginPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("email", personEmail);
            object.put("sifre", personId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/login.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.d("snow", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){
                                JSONObject bilgiler = new JSONObject(kontrol.getString("uye-bilgileri"));

                                sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email",bilgiler.getString("EMAIL"));
                                editor.putString("user_id",bilgiler.getString("KULLANICI_ID"));
                                editor.putString("adi",bilgiler.getString("ADI"));
                                editor.putString("soyadi",bilgiler.getString("SOYADI"));
                                editor.putString("resim", String.valueOf(personPhoto));
                                editor.putString("login","1");

                                Log.d("mesaj", "resim :"+String.valueOf(personPhoto));

                                editor.commit();



                            } else {

                                kullanici_kayit();

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


                                    kredi.setText( kontrol.getString("kredi_miktari"));

                                    kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                    kayitci.putString("kredi",  kontrol.getString("kredi_miktari"));
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




    private void buySubscription(final String productID) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == OK && !productID.equals("")) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(productID);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    //Ödeme detaylarını almak için bu bölümü kullanılır
                    mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {

                            Log.e(TAG, "querySkuDetailsAsync " + billingResult.getResponseCode());

                            if (billingResult.getResponseCode() == OK && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();

                                    //Ekrana ödeme işleminin çıkması ve ödemeyi tamamlamak için kullanılır
                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetails)
                                            .build();

                                    BillingResult responseCode = mBillingClient.launchBillingFlow(MainMenu.this, flowParams);

                                    //int donen_kod = mBillingClient.launchBillingFlow(MainMenu.this, flowParams).getResponseCode();

                                    Log.e(TAG, " mesaj: donen_kod = " + responseCode.getResponseCode());
                                }
                            } else
                                Log.e(TAG, " error: " + billingResult.getDebugMessage());
                        }
                    });
                } else
                    Log.e(TAG, "onBillingSetupFinished() error code: " + billingResult.getDebugMessage());
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.w(TAG, "onBillingServiceDisconnected()");
            }
        });
    }


    @Override
    public void onPurchasesUpdated(@androidx.annotation.NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

        Log.e(TAG, "mesaj " + "  Sonuc Kodu="+ billingResult.getResponseCode() +"    Satınalma Sonucu="+
                purchases+"   aa: "+BillingClient.BillingResponseCode.OK);


        if (billingResult.getResponseCode() == OK && purchases != null) {

            for (Purchase purchase : purchases) {
                Log.e(TAG, "mesaj " + "Satınalma Başarılı");

                handlePurchase(purchase);

                Kredi_Girisi.kredi_satinalma(kullanici_id,paket_id ,"1", "0",tutar,"4");

                reklam_kontrol = getApplicationContext().getSharedPreferences("reklam_kontrol", 0);
                SharedPreferences.Editor editor = reklam_kontrol.edit();
                editor.putString("reklam_kaldir","1");
                editor.commit();

            }

        } else if(billingResult.getResponseCode()==7){
            //daha önce satın alındıysa reklam kaldırı 1 yap
            reklam_kontrol = getApplicationContext().getSharedPreferences("reklam_kontrol", 0);
            SharedPreferences.Editor editor = reklam_kontrol.edit();
            editor.putString("reklam_kaldir","1");
            editor.commit();

        }

        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {

            Log.e(TAG, "mesaj " + "Satınalma İptal Edildi");

        } else {
            Log.e(TAG, "mesaj " + "Satınalma Başarısız");
        }

    }


    void handlePurchase(Purchase purchase) {

            //This is for Non-Consumable product
            AcknowledgePurchaseParams acknowledgePurchaseParams =
                    AcknowledgePurchaseParams
                            .newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();
        mBillingClient.acknowledgePurchase(acknowledgePurchaseParams,
                new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult  billingResult) {
                        Log.d(TAG, "Purchase Acknowledged");
                    }
                });

}



    private void paketi_oku(String paket_turu){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&paket_turu="+paket_turu+"&kontrol_key="+kontrol_key+"&islem=1";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    String gelenveri=kontrol.toString().replace("{\"hata\":false,\"kredi_paketleri\":","");
                                    gelenveri = gelenveri.replace("]}","]");

                                    //Log.d("mesaj", gelenveri);

                                    list = new ArrayList<>();
                                    list = okunanlariParseEt(gelenveri); // parse metodu çağrıldı

                                    paket_id=list.get(0).getPAKET_ID();
                                    paket_adi=list.get(0).getPAKET_ADI();
                                    tutar=list.get(0).getTUTAR();

                                   // Toast.makeText(getApplicationContext(), paket_adi, Toast.LENGTH_SHORT).show();

                                    Log.d("mesaj", list.toString());


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


    ArrayList<KrediPaketleriItem> okunanlariParseEt(String okunanJson) {
        ArrayList<KrediPaketleriItem> kullaniciList = new ArrayList<>();
        try {
            JSONArray arrayKullanici = new JSONArray(okunanJson);

            for (int i = 0; i < arrayKullanici.length(); ++i) {
                String paket_id =arrayKullanici.getJSONObject(i).get("PAKET_ID").toString();
                String paket_adi =arrayKullanici.getJSONObject(i).get("PAKET_ADI").toString();
                String aciklama =arrayKullanici.getJSONObject(i).get("ACIKLAMA").toString();
                String miktar =arrayKullanici.getJSONObject(i).get("MIKTAR").toString();
                String tutar =arrayKullanici.getJSONObject(i).get("TUTAR").toString();
                String aktif =arrayKullanici.getJSONObject(i).get("AKTIF").toString();
                String paket_turu =arrayKullanici.getJSONObject(i).get("PAKET_TURU").toString();
                String paket_resmi =arrayKullanici.getJSONObject(i).get("PAKET_RESMI").toString();
                String adsense_id =arrayKullanici.getJSONObject(i).get("ADSENSE_ID").toString();

                KrediPaketleriItem satir = new KrediPaketleriItem( paket_id,paket_adi,aciklama,miktar,tutar,aktif,paket_turu,paket_resmi,adsense_id );
                kullaniciList.add(satir);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return kullaniciList;
    }



}