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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.turkogame.darphane.R;
import com.turkogame.darphane.activity.Kisisel_Bilgiler;
import com.turkogame.darphane.activity.Kredi_Kazan;
import com.turkogame.darphane.activity.models.AktifFalcilarItem;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Integer.parseInt;


public class AdapterFalcilarRec extends RecyclerView.Adapter<AdapterFalcilarRec.tanimla> {

    List<AktifFalcilarItem> list;
    Context context;
    SharedPreferences kayit_kontrol;
    Activity activity ;
    String kredi;


    public AdapterFalcilarRec(Context context, List<AktifFalcilarItem> list, Activity activity) {

        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public tanimla onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.model_falci,parent,false);
        return new tanimla(view);
    }


    @Override
    public void onBindViewHolder(tanimla holder, final int position) {

        holder.adsoyad.setText(list.get(position).getADI()+" "+list.get(position).getSOYADI());
        holder.ucret.setText(list.get(position).getKREDI_BEDELI());
        if (list.get(position).getDURUM().equals("0")) {
            holder.durum.setText("Çevrim Dışı");
        } else {holder.durum.setText("Çevrim İçi");}
        String foto = "https://www.beyazfincan.com/yonetim/kullanici_resimleri/"+list.get(position).getRESIM();
        Picasso.get().load(foto).into(holder.resim);
        holder.kullanici_id.setText(list.get(position).getKULLANICI_ID());
        holder.mesaj.setText("Falcı Mesajı");

        holder.tiklanacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kayit_kontrol = getApplicationContext().getSharedPreferences("fal_kontrol", 0);
                kredi = kayit_kontrol.getString("kredi","0");
                if (parseInt(kredi)>parseInt(holder.ucret.getText().toString())) {


                    SharedPreferences.Editor kayitci = kayit_kontrol.edit();
                    kayitci.putString("falci_id", holder.kullanici_id.getText().toString());
                    kayitci.commit();

                    Intent intent = new Intent(activity, Kisisel_Bilgiler.class);
                    activity.startActivity(intent);
                } else {

                    Intent intent = new Intent(activity, Kredi_Kazan.class);
                    activity.startActivity(intent);
                    Toast.makeText(context,"Kredi bakiyeniz yetersiz", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public  class tanimla extends RecyclerView.ViewHolder
    {
        TextView adsoyad,ucret,durum,mesaj,kullanici_id;
        CircularImageView resim;
        LinearLayout tiklanacak;

        public tanimla(View itemView) {
            super(itemView);
            adsoyad = (TextView) itemView.findViewById(R.id.adsoyad);
            ucret = (TextView) itemView.findViewById(R.id.ucret);
            durum = (TextView) itemView.findViewById(R.id.durum);
            resim =  (CircularImageView) itemView.findViewById(R.id.liste_resim);
            mesaj = (TextView) itemView.findViewById(R.id.mesaj);
            kullanici_id = (TextView) itemView.findViewById(R.id.kullanici_id);
            tiklanacak = (LinearLayout) itemView.findViewById(R.id.lyt_parent);

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }





}
