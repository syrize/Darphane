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
import com.turkogame.darphane.activity.models.DuyurularItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AdapterDuyurular extends RecyclerView.Adapter<AdapterDuyurular.tanimla> {

    List<DuyurularItem> list;
    Context context;
    SharedPreferences kayit_kontrol;
    Activity activity ;
    String yeni_tarih;



    public AdapterDuyurular(Context context, List<DuyurularItem> list, Activity activity) {

        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public tanimla onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_duyurular,parent,false);
        return new tanimla(view);
    }


    @Override
    public void onBindViewHolder(tanimla holder, final int position) {


        try {
            Date tarih = new SimpleDateFormat("yyyy-MM-dd").parse(list.get(position).getDUYURU_TARIHI());
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            yeni_tarih=String.valueOf(format.format(tarih));
        } catch (Exception e) {

        }


        holder.duyuru_id.setText(list.get(position).getDUYURU_ID());
        holder.duyuru_konusu.setText(list.get(position).getDUYURU_KONUSU());
        holder.kullanici_id.setText(list.get(position).getKULLANICI_ID());
        holder.duyuru_metni.setText(list.get(position).getDUYURU_METNI());
        holder.duyuru_tarihi.setText(yeni_tarih+" - "+list.get(position).getDUYURU_SAATI());


        holder.tiklanacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    kayit_kontrol = getApplicationContext().getSharedPreferences("duyuru_kontrol", 0);
                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                    kayitci.putString("kullanici_id", holder.kullanici_id.getText().toString());
                    kayitci.putString("duyuru_id", holder.duyuru_id.getText().toString());
                    kayitci.putString("duyuru_konusu", holder.duyuru_konusu.getText().toString());
                    kayitci.putString("duyuru_metni", holder.duyuru_metni.getText().toString());
                    kayitci.putString("duyuru_tarihi", holder.duyuru_tarihi.getText().toString());
                    kayitci.commit();

                    Intent intent = new Intent(activity, Duyuru_Detay.class);
                    activity.startActivity(intent);

            }
        });

    }


    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView duyuru_konusu,duyuru_metni,duyuru_id,kullanici_id,duyuru_tarihi;
        LinearLayout tiklanacak;

        public tanimla(View itemView) {
            super(itemView);
            duyuru_konusu = (TextView) itemView.findViewById(R.id.duyuru_konusu);
            duyuru_metni = (TextView) itemView.findViewById(R.id.duyuru_metni);
            duyuru_id = (TextView) itemView.findViewById(R.id.duyuru_id);
            kullanici_id = (TextView) itemView.findViewById(R.id.kullanici_id);
            duyuru_tarihi = (TextView) itemView.findViewById(R.id.duyuru_tarihi);
            tiklanacak = (LinearLayout) itemView.findViewById(R.id.tiklanacak);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }





}
