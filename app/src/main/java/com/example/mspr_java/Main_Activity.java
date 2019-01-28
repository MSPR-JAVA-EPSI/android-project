package com.example.mspr_java;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EquipementItemComponent;

public class Main_Activity extends AppCompatActivity {

    public String token;
    LinearLayout container;
    LayoutInflater inflater;
    Map<View, EquipementItemComponent> listeObjetView;
    OnClickDownListener listenerDown;
    OnClickUpListener listenerUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("token");
        setContentView(R.layout.main_activity_scrolling);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        //Passage de la liste par reference
        listenerDown = new OnClickDownListener(listeObjetView);
        listenerUp = new OnClickUpListener(listeObjetView);
        ///////////////////////////////////////////////////////
        listeObjetView = new HashMap<View,EquipementItemComponent>();
        container = (LinearLayout) findViewById(R.id.linear_container_scroll);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        retrieveObjects();
    }

    public void retrieveObjects(){
        ComServerRecup comServeurRecup = new ComServerRecup();
        List<EquipementItemComponent> listeObject = null;
        try {
            listeObject = comServeurRecup.parse(comServeurRecup.get());
            for (EquipementItemComponent object : listeObject){
                inflate(object);
            }
        } catch (IOException e) {
            createAlertDialog("Erreur","Impossible de recuperer les objets dans la bdd");
            e.printStackTrace();
        }
        //new ArrayList<EquipementItemComponent>();


    }

    private void createAlertDialog(String titre,String texte) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(texte);
        dialog.setTitle(titre);
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        dialog.create().show();
    }

    /*A CHANGER LES TYPES ETC*/
    private void inflate(EquipementItemComponent item) {
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.inflatable_layout_object, container,false);
        v.setId(View.generateViewId());

        int nbMesItems = getNombreMesItems(item);

        TextView label = (TextView) v.getChildAt(0);
        TextView nbDispo = (TextView) v.getChildAt(1);
        ImageButton buttonDown = (ImageButton)v.getChildAt(2);
        EditText numberSelected = (EditText) v.getChildAt(3);
        ImageButton buttonUp =(ImageButton)v.getChildAt(4);

        label.setText(item.getName());
        nbDispo.setText(item.getValeur()+"/"+item.getMax());
        buttonDown.setOnClickListener(listenerDown);
        numberSelected.setText(nbMesItems);
        buttonUp.setOnClickListener(listenerUp);


        v.setVisibility(View.VISIBLE);
        container.addView(v);
        listeObjetView.put(v,item);

    }

    private int getNombreMesItems(EquipementItemComponent item) {
        return 666;
    }

}
