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

public class Bulkazan_Start extends AppCompatActivity {
    Button start;
    SharedPreferences bakiye_kontrol;
    TextView kredi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bulkazan_start);
        start = (Button) findViewById(R.id.start);
        kredi = (TextView) findViewById(R.id.kredi);
        initToolbar();

        bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
        kredi.setText(bakiye_kontrol.getString("kredi","0"));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bakiye_kontrol = getApplicationContext().getSharedPreferences("darphane_kontrol", 0);
                if( Integer.parseInt(bakiye_kontrol.getString("kredi","0"))>0 ){

                    Intent intent = new Intent(Bulkazan_Start.this, Bulkazan.class);
                    startActivity(intent);
                    finish();

                } else{
                    Toast.makeText(Bulkazan_Start.this, "Bakiyeniz Yetersiz! " ,
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Bulkazan_Start.this, Kredi_Kazan.class);
                    startActivity(intent);
                    finish();

                }


            }
        });


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bul Kazan");
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
