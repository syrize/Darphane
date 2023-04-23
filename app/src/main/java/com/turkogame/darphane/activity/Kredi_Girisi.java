package com.turkogame.darphane.activity;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.turkogame.darphane.activity.app.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Kredi_Girisi {


  // İşlem Türü:  1- SATINALMA  2- ÖDÜLLÜ REKLAM  3- ARKADAŞ DAVETİ  4- TAKİP ET  5- PAYLAŞ  6- HEDİYE KREDİ
  // Ödeme Tipi:  1-Kredi kartı  2-Havele - EFT  3-Bedelsiz  4-Adsense

    public static SharedPreferences kayit_kontrol;

    public static void kredi_satinalma(String user_id,String paket_id,String islem_turu,String miktar,String tutar,String odeme_tipi){

        String md5= AppConfig.md5(user_id+"kredi_islemleriPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("paket_id", paket_id);
            object.put("user_id", user_id);
            object.put("islem_turu", islem_turu);
            object.put("miktar", miktar);
            object.put("tutar", tutar);
            object.put("odeme_tipi", odeme_tipi);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/kredi_islemleri.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                Kredi_Girisi.kredi_oku(user_id,"2" );
                               // Log.d("mesaj", kontrol.getString("mesaj"));


                            } else {

                                Log.d("mesaj", kontrol.getString("hataMesaj"));

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


    public static void kredi_cikisi(String user_id,String oyun_id,String miktar ){

        String md5= AppConfig.md5(user_id+"kredi_cikis_islemleriPOST");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            object.put("kontrol_key", kontrol_key);
            object.put("oyun_id", oyun_id);
            object.put("user_id", user_id);
            object.put("miktar", miktar);

            Log.d("mesaj", oyun_id+"  -  "+user_id+"  -  "+miktar);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/kredi_cikis_islemleri.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mesaj", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){


                                Log.d("mesaj", kontrol.getString("mesaj"));


                            } else {

                                Log.d("mesaj", kontrol.getString("hataMesaj"));

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


    public static void kredi_oku(String kullanici_id,String islem ){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        String md5= AppConfig.md5(kullanici_id+"kredi_islemleriGET");
        String kontrol_key = md5.toUpperCase();

        try {

            String url = AppConfig.URL + "/kredi_islemleri.php?user_id="+kullanici_id+"&islem="+islem+"&kontrol_key="+kontrol_key;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            // Log.d("mesaj", response.toString());

                            try {
                                JSONObject kontrol = new JSONObject(response.toString());

                                if (kontrol.getString("hata")=="false"){


                                    kayit_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
                                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                                    kayitci.putString("kredi",  kontrol.getString("kredi_miktari"));
                                    kayitci.putInt("bakiye_sorgula",1);
                                    kayitci.commit();

                                   return;


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


    public static void urun_satinalma(String user_id,String paket_id){

        String md5= AppConfig.md5(paket_id+"urun_detaylarPUT");
        String kontrol_key = md5.toUpperCase();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {

            Log.w(TAG, "mesaj " + paket_id);


            object.put("kontrol_key", kontrol_key);
            object.put("paket_id", paket_id);
            object.put("user_id", user_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = AppConfig.URL + "/urun_detaylar.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Veri", response.toString());

                        try {
                            JSONObject kontrol = new JSONObject(response.toString());

                            if (kontrol.getString("hata")=="false"){

                                //Kredi_Girisi.kredi_oku(user_id,"2" );
                                 Log.d("mesaj", kontrol.getString("mesaj"));


                            } else {

                                Log.d("mesaj", kontrol.getString("hataMesaj"));

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





}
