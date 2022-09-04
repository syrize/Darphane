package com.turkogame.darphane.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;

import java.text.DecimalFormat;

public class Para_Cekme extends AppCompatActivity {
    Button start;
    SharedPreferences bakiye_kontrol;
    TextView kredi,bakiye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.para_cekme);
        start = (Button) findViewById(R.id.start);
        kredi = (TextView) findViewById(R.id.kredi);
        bakiye = (TextView) findViewById(R.id.bakiye);
        initToolbar();
        DecimalFormat df = new DecimalFormat("#.##");

        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
        kredi.setText(bakiye_kontrol.getString("kredi","0"));
        bakiye.setText(String.valueOf(df.format(Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005)) + " TL");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( Float.parseFloat(bakiye_kontrol.getString("kredi","0"))/10*0.005 >= 50 ){

                    Toast.makeText(Para_Cekme.this, "Para Çekme Talebiniz Alındı " ,
                            Toast.LENGTH_SHORT).show();

                } else{
                    Toast.makeText(Para_Cekme.this, "Para Çekmek için Bakiyeniz Yetersiz! " ,
                            Toast.LENGTH_SHORT).show();



                }


            }
        });


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bil Kazan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.system_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_setting, menu);
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
