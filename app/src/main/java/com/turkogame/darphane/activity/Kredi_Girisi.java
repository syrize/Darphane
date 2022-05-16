package com.turkogame.darphane.activity;

import android.util.Log;

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
