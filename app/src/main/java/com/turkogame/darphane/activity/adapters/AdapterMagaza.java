package com.turkogame.darphane.activity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.Kredi_Girisi;
import com.turkogame.darphane.activity.Magaza;
import com.turkogame.darphane.activity.app.AppConfig;
import com.turkogame.darphane.activity.models.MagazaItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.android.billingclient.api.BillingClient.BillingResponseCode.OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class AdapterMagaza extends RecyclerView.Adapter<AdapterMagaza.tanimla> implements PurchasesUpdatedListener {
    private BillingClient mBillingClient;
    List<MagazaItem> list;
    Context context;
    SharedPreferences kayit_kontrol;
    Activity activity ;
    String kullanici_id,paket_id,paket_tutari,aciklama,kredi_tutari,kredi_miktari;
    int satinalma_kontrol=0;
    private BillingResult billingResult;
    private List<Purchase> purchases;


    public AdapterMagaza(Context context, List<MagazaItem> list, Activity activity) {

        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public tanimla onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_magaza,parent,false);

        ButterKnife.bind(activity);
        mBillingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(this).build();

        return new tanimla(view);
    }


    @Override
    public void onBindViewHolder(tanimla holder, final int position) {
        kayit_kontrol = getApplicationContext().getSharedPreferences("giris", 0);
        kullanici_id = kayit_kontrol.getString("user_id","0");

        holder.paket_adi.setText(list.get(position).getPAKET_ADI());
        holder.paket_id.setText(list.get(position).getPAKET_ID());
        holder.adsense_id.setText(list.get(position).getADSENSE_ID());
        holder.parayla_satinal.setText(list.get(position).getTUTAR());
        holder.krediyle_satinal.setText(list.get(position).getKREDI_TUTAR());
        String foto = "https://www.turkogame.com/uygulamalar/bilgi_oyunu/img/product-list/"+list.get(position).getPAKET_RESMI();
        Picasso.get().load(foto).into(holder.paket_resmi);
        holder.kullanici_id.setText(kullanici_id);
        holder.aciklama.setText(list.get(position).getACIKLAMA());

        kredi_oku(kullanici_id);




        holder.krediyle_satinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mesaj", "kredi ile satın alma");

             if(Integer.parseInt(kredi_miktari) >= Integer.parseInt(holder.krediyle_satinal.getText().toString())){

                 Toast.makeText(context,holder.krediyle_satinal.getText(), Toast.LENGTH_LONG).show();


             } else {

                 Toast.makeText(context,"Kredi bakiyeniz yetersiz "+kredi_miktari, Toast.LENGTH_LONG).show();
             }

            }
        });

        holder.parayla_satinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mesaj", "Para ile satın alma");
                Toast.makeText(context,holder.adsense_id.getText().toString(), Toast.LENGTH_LONG).show();

                /*
                        paket_id=holder.paket_id.getText().toString();
                        paket_tutari=holder.tutar.getText().toString();
                        kredi_tutari= holder.kredi_tutari.getText().toString();
                        kullanici_id=holder.kullanici_id.getText().toString();
                        aciklama=holder.aciklama.getText().toString();
                        satinalma_kontrol=1;

                        buySubscription(holder.adsense_id.getText().toString());

                */

            }
        });

    }





    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView paket_id,paket_adi,aciklama,tutar,adsense_id,kullanici_id;
        CircularImageView paket_resmi;
        Button krediyle_satinal,parayla_satinal;



        public tanimla(View itemView) {
            super(itemView);
            paket_id = (TextView) itemView.findViewById(R.id.paket_id);
            paket_adi = (TextView) itemView.findViewById(R.id.paket_adi);
            aciklama = (TextView) itemView.findViewById(R.id.aciklama);
            paket_resmi =  (CircularImageView) itemView.findViewById(R.id.kredi_resim);
            tutar = (TextView) itemView.findViewById(R.id.ucret);

            kullanici_id = (TextView) itemView.findViewById(R.id.kullanici_id);
            adsense_id = (TextView) itemView.findViewById(R.id.adsense_id);


            parayla_satinal = (Button) itemView.findViewById(R.id.parayla_satinal);
            krediyle_satinal = (Button) itemView.findViewById(R.id.krediyle_satinal);

        }


    }

    private void buySubscription(final String productID) {

        Log.e(TAG, "mesaj " + productID);

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                Log.e(TAG, "mesaj " + billingResult.getResponseCode());

                if (billingResult.getResponseCode() == OK && !productID.equals("")) {
                    List<String> skuList = new ArrayList<>();
                    skuList.add(productID);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    //Ödeme detaylarını almak için bu bölümü kullanılır
                    mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            Log.e(TAG, "mesaj " + billingResult.getResponseCode()+"  -  "+skuDetailsList.toString());
                            if (billingResult.getResponseCode() == OK && skuDetailsList != null) {
                                for (SkuDetails skuDetails : skuDetailsList) {
                                    String sku = skuDetails.getSku();
                                    String price = skuDetails.getPrice();


                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetails)
                                            .build();
                                    BillingResult responseCode = mBillingClient.launchBillingFlow(activity, flowParams);

                                    Log.e(TAG, "mesaj " + "Koda giriyor");
                                }
                            } else
                                Log.e(TAG, " mesaj error: " + billingResult.getDebugMessage());
                        }
                    });
                } else
                    Log.e(TAG, "mesaj onBillingSetupFinished() error code: " + billingResult.getDebugMessage());
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.w(TAG, "mesaj " + "bağlantı başarısız");
            }


        });
    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {

        Log.e(TAG, "mesaj " + "  Sonuc Kodu="+ billingResult.getResponseCode() +"    Satınalma Sonucu="+
                purchases+"   aa: "+BillingClient.BillingResponseCode.OK);


            if (billingResult.getResponseCode() == OK && purchases != null) {

            for (Purchase purchase : purchases) {
                Log.e(TAG, "mesaj " + "Satınalma Başarılı");

                if (satinalma_kontrol==1) {
                   // Kredi_Girisi.kredi_satinalma(kullanici_id, paket_id, "1", kredi_miktari, paket_tutari, "4");
                    satinalma_kontrol=0;
                }

                try {
                    handlePurchase(purchase);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {

            Log.e(TAG, "mesaj " + "Satınalma İptal Edildi");
            // Kullanıcı iptal ettiği durumlarda yapacağınız işlemler burada yer alacak.
        } else {
            Log.e(TAG, "mesaj " + "Satınalma Başarısız");
        }

    }


    void handlePurchase(Purchase satinalma) throws InterruptedException {



            ConsumeParams consumeParams =
                    ConsumeParams.newBuilder()
                            .setPurchaseToken(satinalma.getPurchaseToken())
                            .build();
            mBillingClient.consumeAsync(consumeParams,
                    new ConsumeResponseListener() {
                        @Override
                        public void onConsumeResponse(BillingResult billingResult, String s) {
                            Log.d(TAG, "Purchase Consumed");
                        }
                    });

        Thread.sleep(1000);
        kredi_oku(kullanici_id);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void kredi_oku(String kullanici_id){
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



                                    kredi_miktari= kontrol.getString("kredi_miktari");


                                     Log.d("mesaj", kontrol.getString("kredi_miktari") );


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
