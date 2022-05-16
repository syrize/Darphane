package com.turkogame.darphane.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.turkogame.darphane.R;
import com.turkogame.darphane.utils.Tools;
import com.turkogame.darphane.utils.ViewAnimation;

public class Soru_Cevap extends AppCompatActivity {

    private View parent_view;

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_text,bt_toggle_text2,bt_toggle_text3,bt_toggle_text4;
    private Button bt_hide_text,bt_hide_text2,bt_hide_text3,bt_hide_text4,  bt_hide_input;
    private View lyt_expand_text,lyt_expand_text2,lyt_expand_text3,lyt_expand_text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soru_cevap);
        parent_view = findViewById(android.R.id.content);

        initToolbar();

        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sık Sorulan Sorular");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorBanner);
    }

    private void initComponent() {

        // text section
        bt_toggle_text = (ImageButton) findViewById(R.id.bt_toggle_text);
        bt_toggle_text2 = (ImageButton) findViewById(R.id.bt_toggle_text2);
        bt_toggle_text3 = (ImageButton) findViewById(R.id.bt_toggle_text3);
        bt_toggle_text4 = (ImageButton) findViewById(R.id.bt_toggle_text4);
        bt_hide_text = (Button) findViewById(R.id.bt_hide_text);
        bt_hide_text2 = (Button) findViewById(R.id.bt_hide_text2);
        bt_hide_text3 = (Button) findViewById(R.id.bt_hide_text3);
        bt_hide_text4 = (Button) findViewById(R.id.bt_hide_text4);
        lyt_expand_text = (View) findViewById(R.id.lyt_expand_text);
        lyt_expand_text2 = (View) findViewById(R.id.lyt_expand_text2);
        lyt_expand_text3 = (View) findViewById(R.id.lyt_expand_text3);
        lyt_expand_text4 = (View) findViewById(R.id.lyt_expand_text4);
        lyt_expand_text.setVisibility(View.GONE);
        lyt_expand_text2.setVisibility(View.GONE);
        lyt_expand_text3.setVisibility(View.GONE);
        lyt_expand_text4.setVisibility(View.GONE);

        bt_toggle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });

        bt_toggle_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(bt_toggle_text2);
            }
        });

        bt_toggle_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText3(bt_toggle_text3);
            }
        });

        bt_toggle_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText4(bt_toggle_text4);
            }
        });

        bt_hide_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(bt_toggle_text);
            }
        });
        bt_hide_text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText2(bt_toggle_text2);
            }
        });
        bt_hide_text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText3(bt_toggle_text3);
            }
        });
        bt_hide_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText4(bt_toggle_text4);
            }
        });

        // input section
       // bt_toggle_input = (ImageButton) findViewById(R.id.bt_toggle_input);
       // bt_hide_input = (Button) findViewById(R.id.bt_hide_input);
       // bt_save_input = (Button) findViewById(R.id.bt_save_input);
       // lyt_expand_input = (View) findViewById(R.id.lyt_expand_input);
       // lyt_expand_input.setVisibility(View.GONE);
/*
        bt_toggle_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        bt_hide_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });
*/


        // nested scrollview
        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    private void toggleSectionText(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text);
        }
    }

    private void toggleSectionText2(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text2, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text2);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text2);
        }
    }

    private void toggleSectionText3(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text3, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text3);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text3);
        }
    }

    private void toggleSectionText4(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_text4, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_text4);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_text4);
        }
    }





    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
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
