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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.adapters.AdapterMagaza;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.MagazaItem;
import com.turkogame.darphane.adapter.AdapterListInbox;
import com.turkogame.darphane.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Magaza extends AppCompatActivity {
    SharedPreferences sharedPreferences,kayit_kontrol;
    private View parent_view;
    private AdapterListInbox mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private RecyclerView listView;
    String kullanici_id,token;
    int fal_turu=0;
    List<MagazaItem> list;
    AdapterMagaza adapterMagaza;
    public static String kontrolcu;
    int sayac=0;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magaza);
        token= FirebaseInstanceId.getInstance().getToken();

        listView = (RecyclerView) findViewById(R.id.liste_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);


        // parent_view = findViewById(R.id.lyt_parent);

        initToolbar();
        initComponent();


        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");
        fal_turu = sharedPreferences.getInt("fal_turu",0);

        paketleri_oku();
        //Toast.makeText(this, "Long press for multi selection", Toast.LENGTH_SHORT).show();
        tiklama_kontrol();
    }


    private void tiklama_kontrol(){

        ImageView alt_menu_fallarim = findViewById(R.id.alt_menu_fallarim);
        ImageView alt_menu_duyurular = findViewById(R.id.alt_menu_duyurular);
        ImageView alt_menu_burclar = findViewById(R.id.alt_menu_burclar);
        ImageView alt_menu_home = findViewById(R.id.alt_menu_home);
        FloatingActionButton alt_menu_fal_istek = (FloatingActionButton) findViewById(R.id.alt_menu_fal_istek);


        alt_menu_fal_istek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFalDialog();
            }
        });


        alt_menu_fallarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Magaza.this, Fallarim.class);
                startActivity(intent);
            }
        });


        alt_menu_burclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayit_kontrol = getApplicationContext().getSharedPreferences("giris", 0);
                SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                kayitci.putString("user_id", kullanici_id);
                kayitci.commit();


            }
        });

        alt_menu_duyurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Magaza.this, Duyurular.class);
                startActivity(intent);
            }
        });

        alt_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Magaza.this, MainMenu.class));
                finishAffinity();
            }
        });



    }


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
                fal_baslat( kullanici_id, token,4, Falcilar.class);
                dialog.dismiss();
            }
        });
        ((ImageView) dialog.findViewById(R.id.dialog_ruya)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fal_baslat( kullanici_id, token,5, Falcilar.class);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void fal_baslat(String kullanici_id, String fb_token, int fal_turu, Class form){

        kayit_kontrol = getApplicationContext().getSharedPreferences("giris", 0);
        SharedPreferences.Editor kayitci = kayit_kontrol.edit();
        kayitci.putString("user_id", kullanici_id);
        kayitci.putString("token",fb_token);
        kayitci.putInt("fal_turu",fal_turu);

        kayitci.commit();

        Intent intent = new Intent(Magaza.this, form);
        startActivity(intent);

    }




    private void paketleri_oku(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&paket_turu=0&kontrol_key="+kontrol_key+"&islem=1";

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

                                    Log.d("mesaj", list.toString());


                                    adapterMagaza = new AdapterMagaza( Magaza.this, list, Magaza.this);
                                    listView.setAdapter(adapterMagaza);


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



    ArrayList<MagazaItem> okunanlariParseEt(String okunanJson) {
        ArrayList<MagazaItem> kullaniciList = new ArrayList<>();
        try {
            JSONArray arrayKullanici = new JSONArray(okunanJson);

            for (int i = 0; i < arrayKullanici.length(); ++i) {
                String paket_id =arrayKullanici.getJSONObject(i).get("PAKET_ID").toString();
                String paket_adi =arrayKullanici.getJSONObject(i).get("PAKET_ADI").toString();
                String aciklama =arrayKullanici.getJSONObject(i).get("ACIKLAMA").toString();
                String miktar =arrayKullanici.getJSONObject(i).get("MIKTAR").toString();
                String tutar =arrayKullanici.getJSONObject(i).get("TUTAR").toString();
                String kredi_tutar =arrayKullanici.getJSONObject(i).get("KREDI_TUTAR").toString();
                String aktif =arrayKullanici.getJSONObject(i).get("AKTIF").toString();
                String paket_turu =arrayKullanici.getJSONObject(i).get("PAKET_TURU").toString();
                String paket_resmi =arrayKullanici.getJSONObject(i).get("PAKET_RESMI").toString();
                String adsense_id =arrayKullanici.getJSONObject(i).get("ADSENSE_ID").toString();
                DecimalFormat df=new DecimalFormat("#.##");

                tutar=df.format(Float.parseFloat(tutar));
                tutar=tutar.replace(".",",");

                MagazaItem satir = new MagazaItem( paket_id,paket_adi,aciklama,miktar,tutar,kredi_tutar,aktif,paket_turu,paket_resmi,adsense_id );
                kullaniciList.add(satir);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return kullaniciList;
    }




    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mağaza");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    private void initComponent() {


    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(Magaza.this, R.color.blue_grey_700);
            //mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                deleteInboxes();
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(Magaza.this, R.color.red_600);
        }
    }

    private void deleteInboxes() {
        List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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