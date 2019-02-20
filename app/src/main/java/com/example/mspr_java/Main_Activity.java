package com.example.mspr_java;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Equipment;
import model.EquipmentItemComponent;
import model.ListeEquipment;
import utils.ComServerMain;

public class Main_Activity extends AppCompatActivity {

    public String token;
    private String nom;
    private String image;
    LinearLayout container;
    LayoutInflater inflater;
    Map<View, Equipment> listeObjetView;
    List<EquipmentItemComponent> listOldBorrows;
    OnClickDownListener listenerDown;
    OnClickUpListener listenerUp;
    boolean attenteBorrow = false;
    boolean attenteReturn = false;
    List<EquipmentItemComponent> listeAttenteBorrowed;
    List<EquipmentItemComponent> listeAttenteReturned;
    private TextView textViewNom;
    private ImageView photoImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_scrolling);
        token = getIntent().getStringExtra("token");
        nom = getIntent().getStringExtra("nom");
        image = getIntent().getStringExtra("image");
        //
        textViewNom = (TextView) findViewById(R.id.NomMainActivity);
        photoImageButton = (ImageView) findViewById(R.id.imageViewMainActivity);



        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
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

        textViewNom.setText(""+nom);
        try {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photoImageButton.setImageBitmap(decodedByte);
        }catch(Exception e){
            photoImageButton.setImageDrawable(getDrawable(R.drawable.logo));
        }
        //Passage de la liste par reference
        listeObjetView = new HashMap<View,Equipment>();

        listenerDown = new OnClickDownListener(listeObjetView);
        listenerUp = new OnClickUpListener(listeObjetView);
        ///////////////////////////////////////////////////////

        container = (LinearLayout) findViewById(R.id.linear_container_scroll);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        retrieveBorrows();
    }

    public void retrieveBorrows(){
        ComServerMain comServeurMain = new ComServerMain(this);
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization","Bearer "+token);
            Log.e("header","Bearer "+token);
            comServeurMain.request("item/getBorrows", headers,"body","getBorrows");
        }catch(Exception e){
            Log.e("Exception","erreur lors de la requete getBorrows (retrieveBorrows in Main_activity)"+e.toString());
            e.printStackTrace();
        }
    }

    public void retourComGetBorrows(int status, String body){
        if(status!=200){
            createAlertDialog("Erreur","Impossible de recuperer les Emprunts");
        }else {
            Gson gson = new Gson();
            listOldBorrows = (gson.fromJson(body, ListeEquipment.class)).getEquipments();
            retrieveObjects();
        }

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
            Log.e("Exception","erreur lors de la requete getAllItems (retrieveObjects in Main_activity)"+e.toString());
            e.printStackTrace();
        }

        //new ArrayList<EquipementItemComponent>();


    }

    public void retourComGetAll(int status, String body) {
        if(status!=200){
            createAlertDialog("Erreur","Impossible de recuperer les Equipements");
        }else {
            Gson gson = new Gson();
            List<EquipmentItemComponent> listeObject = (gson.fromJson(body, ListeEquipment.class)).getEquipments();
            try {
                for (EquipmentItemComponent object : listeObject) {
                    Equipment e = match(object);
                    inflate(e);
                }
            } catch (Exception e) {
                createAlertDialog("Erreur", "Impossible de recuperer les Equipements");
                Log.e("Exception",e.toString());
            }
        }
    }

    private Equipment match(EquipmentItemComponent object) {
        Equipment equipment = new Equipment();
        equipment.setEquipment(object);
        for (EquipmentItemComponent e : listOldBorrows){
            if(e.getId() == object.getId()){
                equipment.setOldBorrowed(e.getQuantity());
                break;
            }
        }
        equipment.setBorrowed(equipment.getOldBorrowed());
        return equipment;
    }

    private void inflate(Equipment equipment) {
        EquipmentItemComponent item=equipment.getEquipment();
        Log.e("Inflating Item",item.toString());
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.inflatable_layout_object, container,false);
        v.setId(View.generateViewId());
        String nbMesItems = equipment.getBorrowed()+"";

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
        listeObjetView.put(v,equipment);


    }

    private void borrow() {
        if(attenteReturn || attenteBorrow){
            Snackbar.make(container, "Veuillez attendre l'execution de la requête précédente", Snackbar.LENGTH_LONG)
                    .show();
            Log.e("ATTENTE","Attente borrow "+attenteBorrow+" Attente Return "+attenteReturn);
            return;
        }

        Collection<Equipment> coll = listeObjetView.values();
        List<EquipmentItemComponent> listBorrowed = new ArrayList<>();
        List<EquipmentItemComponent> listReturned = new ArrayList<>();
        for (View view : listeObjetView.keySet()){
            Equipment equipment = listeObjetView.get(view);
            EquipmentItemComponent e =equipment.getEquipment();

            e.setQuantity(equipment.getBorrowed()-equipment.getOldBorrowed());
            if(e.getQuantity()>0){
                listBorrowed.add(e);
            }
            else if (e.getQuantity()<0){
                e.setQuantity(-(e.getQuantity()));
                listReturned.add(e);
            }
        }
        if(listBorrowed.size()>0){
            attenteBorrow=true;
            listeAttenteBorrowed = listBorrowed;
        }
        if(listReturned.size()>0){
            attenteReturn=true;
            listeAttenteReturned = listReturned;
        }
        if (attenteBorrow) {
            requeteBorrow();
        }else if(attenteReturn) {
            requeteReturn();
        }
    }

    public void requeteBorrow(){
        try {
            String pathBorrow = "/item/borrow";
            ComServerMain comServerMain = new ComServerMain(this);
            Gson gson = new Gson();
            String json = gson.toJson(new ListeEquipment(listeAttenteBorrowed));
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + token);
            Log.e("borrow", "Bearer " + token);
            comServerMain.request(pathBorrow, headers, json, "borrow");
        } catch (Exception e) {
            createAlertDialog("Erreur", "Erreur lors de l'envoi de la requette au serveur, réessayez");
            Log.e("logErreurDialog", "erreur lors de la requete borrow (borrow() in Main_activity)");
            e.printStackTrace();
            attenteBorrow=false;
            if(!attenteReturn){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recreate();
                    }
                }, 2000);
            }
        }
    }
    public void requeteReturn(){
        try {
            String pathReturn = "/item/returnBorrows";
            ComServerMain comServerMain = new ComServerMain(this);
            Gson gson = new Gson();
            String json = gson.toJson(new ListeEquipment(listeAttenteReturned));
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + token);
            Log.e("return", "Bearer " + token);
            comServerMain.request(pathReturn, headers, json, "return");
        } catch (Exception e) {
            createAlertDialog("Erreur", "Erreur lors de l'envoi de la requette au serveur, réessayez");
            Log.e("logErrorDialog", "erreur lors de la requete return (return() in Main_activity)");
            e.printStackTrace();
            attenteReturn=false;
            if(!attenteBorrow){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recreate();
                    }
                }, 2000);
            }
        }
    }

    public void retourComBorrow(int status){
        attenteBorrow=false;
        if(status==200){
            Toast.makeText(this, "Emprunt Validé",Toast.LENGTH_LONG).show();
            if(!attenteReturn){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recreate();
                    }
                }, 2000);
            }else{
                requeteReturn();
            }
        }else{
            final AlertDialog alert = createAlertDialog("Erreur","Erreur lors de l'emprunt, veuillez ressaisir les objets empruntés");
            if(!attenteReturn) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            alert.cancel();
                        }catch(Exception e){
                            //Si l'alerte n'existe plus
                        }
                        recreate();
                    }
                }, 4000);
            }else{
                requeteReturn();
            }

        }
    }

    public void retourComReturn(int status){
        attenteReturn=false;
        if(status==200){
            Toast.makeText(this, "Retour Validé",Toast.LENGTH_LONG).show();
            if(!attenteBorrow){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recreate();
                    }
                }, 2000);
            }else{
                requeteBorrow();
            }
        }else{
            final AlertDialog alert = createAlertDialog("Erreur","Erreur lors du retour, veuillez ressaisir les objets retournés");
            if(!attenteBorrow) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            alert.cancel();
                        }catch(Exception e){
                            //Si l'alerte n'existe plus
                        }
                        recreate();
                    }
                }, 4000);
            }else{
                requeteBorrow();
            }

        }
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
}
