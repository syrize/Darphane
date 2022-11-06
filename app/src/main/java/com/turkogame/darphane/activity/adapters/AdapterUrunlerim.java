package com.turkogame.darphane.activity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.Fal_Sonucu;
import com.turkogame.darphane.activity.models.UrunlerimItem;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AdapterUrunlerim extends RecyclerView.Adapter<AdapterUrunlerim.tanimla> {

    List<UrunlerimItem> list;
    Context context;
    SharedPreferences aktarilacak;
    Activity activity ;
    String cevap_metni,fal_turu_id,fal_istek_tarihi,istenen_falci,kullanici_id;


    public AdapterUrunlerim(Context context, List<UrunlerimItem> list, Activity activity) {

        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public tanimla onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_urunlerim,parent,false);
        return new tanimla(view);
    }


    @Override
    public void onBindViewHolder(tanimla holder, final int position) {

        if(list.get(position).getFAL_TURU().equals("4")) {
            cevap_metni = list.get(position).getGIRIS() + " \n " + list.get(position).getRUYA_METNI()
                    + " \n " + list.get(position).getSONUC();
        } else {
            cevap_metni = list.get(position).getGIRIS() + " \n " + list.get(position).getCEVAP_GENEL()
                    + " \n " + list.get(position).getCEVAP_ASK() + " \n " + list.get(position).getCEVAP_KARIYER()
                    + " \n " + list.get(position).getCEVAP_SAGLIK() + " \n " + list.get(position).getSONUC();
        }

        if(list.get(position).getFAL_TURU().equals("1")){holder.fal_turu.setText("Kahve Falı");
        holder.resim.setImageResource(R.drawable.economy); fal_turu_id="1"; }
        if(list.get(position).getFAL_TURU().equals("2")){holder.fal_turu.setText("Tarot Falı");
            holder.resim.setImageResource(R.drawable.ic_el); fal_turu_id="2"; }



        if (list.get(position).getDURUM().equals("0")) { holder.durum.setText("Bekliyor");}
        if (list.get(position).getDURUM().equals("1")) { holder.durum.setText("Cevaplandı");}
        if (list.get(position).getDURUM().equals("3")) { holder.durum.setText("Reddedildi");}


        holder.kullanici_id.setText(list.get(position).getKULLANICI_ID());
        kullanici_id=list.get(position).getKULLANICI_ID();
        holder.istek_tarihi.setText(list.get(position).getISTEK_TARIHI());
        holder.istek_id.setText(list.get(position).getISTEK_ID());
        holder.falci_id.setText(list.get(position).getISTENEN_FALCI());
        holder.fal_turu_id.setText(list.get(position).getFAL_TURU());
        holder.istek_saati.setText(list.get(position).getISTEK_SAATI());
        holder.istenen_falci.setText(list.get(position).getFALCI_ADI()+" "+list.get(position).getFALCI_SOYADI());
        istenen_falci=list.get(position).getFALCI_ADI()+" "+list.get(position).getFALCI_SOYADI();
        holder.cevap.setText(cevap_metni);


        holder.tiklanacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getDURUM().equals("1")   ) {

                    aktarilacak = getApplicationContext().getSharedPreferences("fallarim", 0);

                    istenen_falci=holder.istenen_falci.getText().toString();
                    fal_turu_id=holder.fal_turu_id.getText().toString();

                    SharedPreferences.Editor kayitlar = aktarilacak.edit();
                    kayitlar.putString("istenen_falci", holder.istenen_falci.getText().toString());
                    kayitlar.putString("cevap_metni", holder.cevap.getText().toString());
                    kayitlar.putString("fal_turu_id", fal_turu_id);
                    kayitlar.putString("fal_istek_tarihi", holder.istek_tarihi.getText().toString());
                    kayitlar.putString("kullanici_id", holder.kullanici_id.getText().toString());
                    kayitlar.putString("istek_id", holder.istek_id.getText().toString());
                    kayitlar.putString("falci_id", holder.falci_id.getText().toString());
                    kayitlar.commit();

                    Intent intent = new Intent(activity, Fal_Sonucu.class);
                    activity.startActivity(intent);
                }

            }
        });

    }


    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView fal_turu,istek_tarihi,istek_saati,istenen_falci,kullanici_id,durum,cevap,istek_id,fal_turu_id,falci_id;
        CircularImageView resim;
        LinearLayout tiklanacak;

        public tanimla(View itemView) {
            super(itemView);
            fal_turu = (TextView) itemView.findViewById(R.id.fal_turu);
            istek_tarihi = (TextView) itemView.findViewById(R.id.istek_tarihi);
            istek_saati = (TextView) itemView.findViewById(R.id.istek_saati);
            resim =  (CircularImageView) itemView.findViewById(R.id.falturu_resim);
            istenen_falci = (TextView) itemView.findViewById(R.id.istenen_falci);
            kullanici_id = (TextView) itemView.findViewById(R.id.kullanici_id);
            durum = (TextView) itemView.findViewById(R.id.durum);
            cevap = (TextView) itemView.findViewById(R.id.cevap);
            istek_id = (TextView) itemView.findViewById(R.id.istek_id);
            fal_turu_id = (TextView) itemView.findViewById(R.id.fal_turu_id);
            falci_id = (TextView) itemView.findViewById(R.id.falci_id);
            tiklanacak = (LinearLayout) itemView.findViewById(R.id.lyt_fallarim);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }





}
