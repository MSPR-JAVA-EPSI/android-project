package com.example.mspr_java;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import model.EquipmentItemComponent;

public class EasterEgg {
    private final TextView nom;
    public ImageView photoImageButton;
    public static boolean clicked = false;
    public static Main_Activity context;
    public static LinearLayout container;
    public static EasterEgg ee;

    public EasterEgg(Main_Activity context, LinearLayout container, TextView nom) {
        this.context = context;
        this.container = container;
        this.nom=nom;
        this.ee = this;
    }

    public void longClickListener(ImageView photoImageButton) {
        this.photoImageButton = photoImageButton;
        photoImageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(clicked) {
                    start();
                }
                return false;
            }
        });
    }

    public void firstItemListener(View childAt) {
        ((LinearLayout)childAt).getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;
            }
        });
    }

    public void start(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Sheh",Toast.LENGTH_LONG).show();
            }
        };
        nom.setText("Sheh");
        container.removeAllViews();
        RotateAnimation rotate;

        String base64img=context.getString(R.string.imgEasterEgg);
        try {
            byte[] decodedString = Base64.decode(base64img, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photoImageButton.setImageBitmap(decodedByte);
            rotate = new RotateAnimation(
                    0, 360,
                    Animation.RELATIVE_TO_SELF, 0.7f,
                    Animation.RELATIVE_TO_SELF, 0.2f
            );
            rotate.setDuration(5000);
            rotate.setRepeatCount(Animation.INFINITE);
            photoImageButton.startAnimation(rotate);

        }catch(Exception e){

        }

        rotate = new RotateAnimation(
                0, -720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(7000);
        rotate.setRepeatCount(Animation.INFINITE);
        nom.startAnimation(rotate);


        for(int i=0 ; i<100; i++){
            LinearLayout v = (LinearLayout) ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.inflatable_layout_object, container,false);
            v.setId(View.generateViewId());


            TextView label = (TextView) v.getChildAt(0);
            TextView nbDispo = (TextView) v.getChildAt(1);
            ImageButton buttonDown = (ImageButton)v.getChildAt(2);
            EditText numberSelected = (EditText) v.getChildAt(3);
            ImageButton buttonUp =(ImageButton)v.getChildAt(4);
            label.setText("Sheh");
            String txt = "Sheh";
            nbDispo.setText(txt);
            rotate = new RotateAnimation(
                    0, (float)random(0.1,1000),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2)
            );
            rotate.setDuration((long)random(500,1500));
            rotate.setRepeatCount(Animation.INFINITE);
            buttonUp.startAnimation(rotate);
            buttonUp.setOnClickListener(listener);
            rotate = new RotateAnimation(
                    0, (float)random(0.1,1000),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2)
            );
            rotate.setDuration((long)random(500,1500));
            rotate.setRepeatCount(Animation.INFINITE);
            nbDispo.startAnimation(rotate);
            numberSelected.setText("Sheh");
            rotate = new RotateAnimation(
                    0, (float)random(0.1,1000),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2)
            );
            rotate.setDuration((long)random(500,1500));
            rotate.setRepeatCount(Animation.INFINITE);
            buttonUp.startAnimation(rotate);
            buttonUp.setOnClickListener(listener);
            rotate = new RotateAnimation(
                    0, (float)random(0.1,1000),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2),
                    Animation.RELATIVE_TO_SELF, (float)random(0.1,2)
            );
            rotate.setDuration((long)random(500,1500));
            rotate.setRepeatCount(Animation.INFINITE);
            buttonDown.startAnimation(rotate);
            buttonDown.setOnClickListener(listener);
            v.setVisibility(View.VISIBLE);
            container.addView(v);
        }
    }

    public double random(double min,double max){
        return min + (Math.random() * (max - min));
    }

}