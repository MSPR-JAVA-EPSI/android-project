package com.example.mspr_java;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EquipmentItemComponent;
import model.ListeEquipment;

public class Main_Activity extends AppCompatActivity {

    public String token;
    LinearLayout container;
    LayoutInflater inflater;
    Map<View, EquipmentItemComponent> listeObjetView;
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
                borrow();
            }
        });
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toast.makeText(this, "Connecté",Toast.LENGTH_LONG).show();
        //Passage de la liste par reference
        listeObjetView = new HashMap<View,EquipmentItemComponent>();

        listenerDown = new OnClickDownListener(listeObjetView);
        listenerUp = new OnClickUpListener(listeObjetView);
        ///////////////////////////////////////////////////////

        container = (LinearLayout) findViewById(R.id.linear_container_scroll);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        retrieveObjects();
    }

    public void retrieveObjects(){
        ComServerMain comServeurMain = new ComServerMain(this);
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization","Bearer "+token);
            Log.e("header","Bearer "+token);
            comServeurMain.request("item/getAll", headers,"body","getAll");
        }catch(Exception e){
            Log.e("TOUT CASSSé","erreur lors de la requete getAllItems (retrieveObjects in Main_activity)");
            e.printStackTrace();
        }

        //new ArrayList<EquipementItemComponent>();


    }

    private AlertDialog createAlertDialog(String titre,String texte) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(texte);
        dialog.setTitle(titre);
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        AlertDialog alertDialog= dialog.create();
        alertDialog.show();
        return alertDialog;
    }

    /*A CHANGER LES TYPES ETC*/
    private void inflate(EquipmentItemComponent item) {
        Log.e("Inflating Item",item.toString());
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.inflatable_layout_object, container,false);
        v.setId(View.generateViewId());
        String nbMesItems = getNombreMesItems(item)+"";

        TextView label = (TextView) v.getChildAt(0);
        TextView nbDispo = (TextView) v.getChildAt(1);
        ImageButton buttonDown = (ImageButton)v.getChildAt(2);
        EditText numberSelected = (EditText) v.getChildAt(3);
        ImageButton buttonUp =(ImageButton)v.getChildAt(4);
        label.setText(item.getName());
        String txt = item.getQuantity()+" libre";
        nbDispo.setText(txt);
        buttonDown.setOnClickListener(listenerDown);
        numberSelected.setText(nbMesItems);
        buttonUp.setOnClickListener(listenerUp);

        v.setVisibility(View.VISIBLE);
        container.addView(v);
        listeObjetView.put(v,item);


    }

    private int getNombreMesItems(EquipmentItemComponent item) {
        return 0;
    }

    public void retourComGetAll(int status, String body) {
        if(status!=200){
            createAlertDialog("Erreur","Impossible de recuperer les Equipements");
        }else {
            Gson gson = new Gson();
            List<EquipmentItemComponent> listeObject = (gson.fromJson(body, ListeEquipment.class)).getEquipments();
            try {
                for (EquipmentItemComponent object : listeObject) {
                    inflate(object);
                }
            } catch (Exception e) {
                createAlertDialog("Erreur", "Impossible de recuperer les Equipements");
                Log.e("Exception",e.toString());
            }
        }
    }

    public void retourComBorrow(int status){
        if(status==200){
            Toast.makeText(this, "Validé",Toast.LENGTH_LONG).show();
        }else{
            final AlertDialog alert = createAlertDialog("Erreur","Il semblerais que vous selectionnez trop d'objets, veuillez recommencer");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    alert.cancel();
                    recreate();
                }
            }, 4000);

        }
    }
    private void borrow() {
        String path = "/item/borrow";
        Collection coll = listeObjetView.values();
        List list;
        if (coll instanceof List)
            list = (List)coll;
        else
            list = new ArrayList(coll);
        ComServerMain comServeurMain = new ComServerMain(this);
        try {
            Gson gson = new Gson();
            String json = gson.toJson(new ListeEquipment(list));
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization","Bearer "+token);
            Log.e("header","Bearer "+token);
            comServeurMain.request(path, headers,json,"borrow");
        }catch(Exception e){
            createAlertDialog("Erreur","Erreur lors de l'envoi de la requette au serveur, réessayez");
            Log.e("TOUT CASSSé","erreur lors de la requete borrow (borrow() in Main_activity)");
            e.printStackTrace();
        }
    }
}
