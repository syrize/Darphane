package com.turkogame.darphane.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.adapters.FallarimAdapter;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.FallarimItem;
import com.turkogame.darphane.adapter.AdapterListInbox;
import com.turkogame.darphane.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fallarim extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private View parent_view;
    private AdapterListInbox mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private RecyclerView listView;
    String kullanici_id;
    List<FallarimItem> list;
    FallarimAdapter adapterFallarim;
    public static String kontrolcu;
    int sayac=0;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fallarim);


        listView = (RecyclerView) findViewById(R.id.fallarim_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setHasFixedSize(true);


        // parent_view = findViewById(R.id.lyt_parent);

        initToolbar();
        initComponent();


        sharedPreferences = getApplicationContext().getSharedPreferences("fal_kontrol", 0);

        kullanici_id = sharedPreferences.getString("user_id","0");

        falsonucu_oku();
        //Toast.makeText(this, "Long press for multi selection", Toast.LENGTH_SHORT).show();
    }



    private void falsonucu_oku(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"fal_sonucuGET");
        String kontrol_key = md5.toUpperCase();

        try {
            String url = AppConfig.URL + "/fal_sonucu.php?user_id="+kullanici_id+"&kontrol_key="+kontrol_key;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("mesaj", response.toString());

                             try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){

                                    String gelenveri=kontrol.toString().replace("{\"hata\":false,\"fal-bilgileri\":","");
                                    gelenveri = gelenveri.replace("]}","]");

                                    Log.d("mesaj", gelenveri);

                                   list = new ArrayList<>();
                                   list = okunanlariParseEt(gelenveri); // parse metodu çağrıldı

                                    Log.d("mesaj", list.toString());


                                    adapterFallarim = new FallarimAdapter( Fallarim.this, list , Fallarim.this);
                                    listView.setAdapter(adapterFallarim);


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



    ArrayList<FallarimItem> okunanlariParseEt(String okunanJson) {
        ArrayList<FallarimItem> fallarimList = new ArrayList<>();
        try {
            JSONArray arrayKullanici = new JSONArray(okunanJson);
            for (int i = 0; i < arrayKullanici.length(); ++i) {
                String istek_id =arrayKullanici.getJSONObject(i).get("ISTEK_ID").toString();
                String istek_turu =arrayKullanici.getJSONObject(i).get("ISTEK_TURU").toString();
                String durum =arrayKullanici.getJSONObject(i).get("DURUM").toString();
                String giris =arrayKullanici.getJSONObject(i).get("GIRIS").toString();
                String cevap_genel =arrayKullanici.getJSONObject(i).get("CEVAP_GENEL").toString();
                String cevap_ask =arrayKullanici.getJSONObject(i).get("CEVAP_ASK").toString();
                String cevap_kariyer =arrayKullanici.getJSONObject(i).get("CEVAP_KARIYER").toString();
                String cevap_saglik =arrayKullanici.getJSONObject(i).get("CEVAP_SAGLIK").toString();
                String sonuc =arrayKullanici.getJSONObject(i).get("SONUC").toString();
                String istek_tarihi =arrayKullanici.getJSONObject(i).get("ISTEK_TARIHI").toString();
                String istek_saati =arrayKullanici.getJSONObject(i).get("ISTEK_SAATI").toString();
                String cevap_tarihi =arrayKullanici.getJSONObject(i).get("CEVAP_TARIHI").toString();
                String cevap_saati =arrayKullanici.getJSONObject(i).get("CEVAP_SAATI").toString();
                String cevap_kullanici_id =arrayKullanici.getJSONObject(i).get("CEVAP_KULLANICI_ID").toString();
                String fal_turu =arrayKullanici.getJSONObject(i).get("FAL_TURU").toString();
                String ruya_metni =arrayKullanici.getJSONObject(i).get("RUYA_METNI").toString();
                String yorum =arrayKullanici.getJSONObject(i).get("YORUM").toString();
                String istenen_falci =arrayKullanici.getJSONObject(i).get("ISTENEN_FALCI").toString();
                String kullanici_id =arrayKullanici.getJSONObject(i).get("KULLANICI_ID").toString();
                String falci_adi =arrayKullanici.getJSONObject(i).get("FALCI_ADI").toString();
                String falci_soyadi =arrayKullanici.getJSONObject(i).get("FALCI_SOYADI").toString();

                FallarimItem satir = new FallarimItem( istek_id,kullanici_id,istek_turu,durum,giris,cevap_genel,cevap_ask,
                        cevap_kariyer,cevap_saglik,sonuc,istek_tarihi,istek_saati,cevap_tarihi,cevap_saati,cevap_kullanici_id,
                        fal_turu,ruya_metni,yorum,istenen_falci,falci_adi,falci_soyadi);

                fallarimList.add(satir);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return fallarimList;
    }




    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fallarım");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    private void initComponent() {



        /*
        List<Inbox> items = DataGenerator.getInboxData(this);

        //set data and list adapter
        mAdapter = new AdapterListInbox(this, items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new AdapterListInbox.OnClickListener() {
            @Override
            public void onItemClick(View view, Inbox obj, int pos) {
                if (mAdapter.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    // read the inbox which removes bold from the row
                    Inbox inbox = mAdapter.getItem(pos);
                    Toast.makeText(getApplicationContext(), "Read: " + inbox.from, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(View view, Inbox obj, int pos) {
                enableActionMode(pos);
            }
        });

        actionModeCallback = new ActionModeCallback();

         */

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
            Tools.setSystemBarColor(Fallarim.this, R.color.blue_grey_700);
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
            Tools.setSystemBarColor(Fallarim.this, R.color.red_600);
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