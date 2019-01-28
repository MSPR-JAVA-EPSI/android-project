package com.example.mspr_java;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Main_Activity extends AppCompatActivity {

    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_scrolling);
        container = findViewById(R.id.linear_container_scroll);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        container = findViewById(R.id.linear_container_scroll);
        for (int i =0; i<2; i++)
            inflate(i);
        Toast.makeText(this, "NB view = "+container.getChildCount(),Toast.LENGTH_LONG).show();
    }

    private void inflate(int j) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.inflatable_layout_object, null);
        v.setId(View.generateViewId());
        for (int i = 0; i < v.getChildCount(); i++) {
            View child = v.getChildAt(i);
            child.setId(View.generateViewId());
            Log.e("sheh","CHILD of "+j+" ARE "+child.toString());
            if (child instanceof TextView)
                ((TextView) child).setText("Numero = "+j);
        }
        v.setVisibility(View.VISIBLE);

        container.addView(v);

        LinearLayout v2 = (LinearLayout) getLayoutInflater().inflate(R.layout.inflatable_layout_object2, null);
        v2.setId(View.generateViewId());

        v2.setVisibility(View.VISIBLE);

        container.addView(v2);
    }


}
01