package com.turkogame.darphane.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;

import afu.org.checkerframework.checker.nullness.qual.Nullable;

public class deneme extends Fragment {
   Button buton;
    DrawerLayout drawer;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.deneme,container,false);
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initToolbar();
        initNavigationMenu();


        drawer = (DrawerLayout)getActivity(). findViewById(R.id.drawer_layout1);

        buton = (Button) getActivity(). findViewById(R.id.button);




        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initToolbar() {
        Tools.setSystemBarColor(getActivity(), R.color.colorBanner);


    }


    private void initNavigationMenu() {



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        // open drawer at start
        //drawer.openDrawer(GravityCompat.START);
    }








}
