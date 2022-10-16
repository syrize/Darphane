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
import com.turkogame.darphane.activity.Duyuru_Detay;
import com.turkogame.darphane.activity.models.OdemeTalepleriItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AdapterOdemeTalepleri extends RecyclerView.Adapter<AdapterOdemeTalepleri.tanimla> {

    List<OdemeTalepleriItem> list;
    Context context;
    SharedPreferences kayit_kontrol;
    Activity activity ;
    String yeni_tarih,durum_aciklama,genel_aciklama;



    public AdapterOdemeTalepleri(Context context, List<OdemeTalepleriItem> list, Activity activity) {

        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public tanimla onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_odeme_talepleri,parent,false);
        return new tanimla(view);
    }


    @Override
    public void onBindViewHolder(tanimla holder, final int position) {


        try {
            Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(position).getTARIH());
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            yeni_tarih=String.valueOf(format.format(tarih));
        } catch (Exception e) {

        }
        durum_aciklama="";
        if(list.get(position).getDURUM().equals("1")) {durum_aciklama="Beklemede";}
        if(list.get(position).getDURUM().equals("2")) {durum_aciklama="İşleme Alındı";}
        if(list.get(position).getDURUM().equals("3")) {durum_aciklama="Ödeme Tamamlandı";}
        if(list.get(position).getDURUM().equals("4")) {durum_aciklama="Ödeme Reddedildi";}

        genel_aciklama="";
        if(list.get(position).getACIKLAMA()!="" && list.get(position).getACIKLAMA()!="null"){
            genel_aciklama=list.get(position).getACIKLAMA();
        }


        holder.talep_id.setText(list.get(position).getTALEP_ID());
        holder.tutar.setText(list.get(position).getTUTAR());
        holder.durum.setText(durum_aciklama);
        holder.aciklama.setText(genel_aciklama);
        holder.kullanici_id.setText(list.get(position).getKULLANICI_ID());
        holder.talep_tarihi.setText(yeni_tarih);



        holder.tiklanacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView talep_id,tutar,durum,aciklama,talep_tarihi,kullanici_id;
        LinearLayout tiklanacak;

        public tanimla(View itemView) {
            super(itemView);
            talep_id = (TextView) itemView.findViewById(R.id.talep_id);
            tutar = (TextView) itemView.findViewById(R.id.tutar);
            durum = (TextView) itemView.findViewById(R.id.durum);
            aciklama = (TextView) itemView.findViewById(R.id.aciklama);
            kullanici_id = (TextView) itemView.findViewById(R.id.kullanici_id);
            talep_tarihi = (TextView) itemView.findViewById(R.id.tarih);
            tiklanacak = (LinearLayout) itemView.findViewById(R.id.tiklanacak);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }





}
