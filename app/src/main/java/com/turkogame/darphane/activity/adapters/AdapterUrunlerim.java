package com.turkogame.darphane.activity.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.models.UrunlerimItem;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AdapterUrunlerim extends RecyclerView.Adapter<AdapterUrunlerim.tanimla> {

    List<UrunlerimItem> list;
    Context context;
    SharedPreferences aktarilacak;
    Activity activity ;
    String kayit_id,paket_id,tekil_urun_kodu,durum,satin_alan_kullanici,islem_zamani,aciklama,paket_adi;


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

        holder.kayit_id.setText(list.get(position).getKAYIT_ID());
        holder.paket_id.setText(list.get(position).getPAKET_ID());
        holder.tekil_urun_kodu.setText(list.get(position).getTEKIL_URUN_KODU());
        holder.durum.setText(list.get(position).getDURUM());
        holder.satin_alan_kullanici.setText(list.get(position).getSATIN_ALAN_KULLANICI());
        holder.islem_zamani.setText(list.get(position).getISLEM_ZAMANI());
        holder.aciklama.setText(list.get(position).getACIKLAMA());
        holder.paket_adi.setText(list.get(position).getPAKET_ADI());
        String foto = "https://www.turkogame.com/uygulamalar/bilgi_oyunu/img/product-list/"+list.get(position).getPAKET_RESMI();
        Picasso.get().load(foto).into(holder.paket_resmi);
/*
        holder.tiklanacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getDURUM().equals("1")   ) {

                    aktarilacak = getApplicationContext().getSharedPreferences("urunlerim", 0);
                    SharedPreferences.Editor kayitlar = aktarilacak.edit();
                    kayitlar.putString("kayit_id", holder.kayit_id.getText().toString());
                    kayitlar.putString("paket_id", holder.paket_id.getText().toString());
                    kayitlar.putString("tekil_urun_kodu", holder.tekil_urun_kodu.getText().toString());
                    kayitlar.putString("durum", holder.durum.getText().toString());
                    kayitlar.putString("satin_alan_kullanici", holder.satin_alan_kullanici.getText().toString());
                    kayitlar.putString("islem_zamani", holder.islem_zamani.getText().toString());
                    kayitlar.putString("aciklama", holder.aciklama.getText().toString());
                    kayitlar.commit();

                    //Intent intent = new Intent(activity, Fal_Sonucu.class);
                    //activity.startActivity(intent);
                }

            }
        });
*/
    }


    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView kayit_id,paket_id,tekil_urun_kodu,durum,satin_alan_kullanici,islem_zamani,aciklama,paket_adi;
        CircularImageView paket_resmi;
        LinearLayout tiklanacak;

        public tanimla(View itemView) {
            super(itemView);
            tiklanacak = (LinearLayout) itemView.findViewById(R.id.lyt_urunlerim);
            kayit_id = (TextView) itemView.findViewById(R.id.kayit_id);
            paket_id = (TextView) itemView.findViewById(R.id.paket_id);
            tekil_urun_kodu = (TextView) itemView.findViewById(R.id.tekil_urun_kodu);
            durum = (TextView) itemView.findViewById(R.id.durum);

            islem_zamani = (TextView) itemView.findViewById(R.id.islem_zamani);
            aciklama = (TextView) itemView.findViewById(R.id.aciklama);

            paket_adi = (TextView) itemView.findViewById(R.id.paket_adi);

            paket_resmi =  (CircularImageView) itemView.findViewById(R.id.paket_resmi);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }





}
