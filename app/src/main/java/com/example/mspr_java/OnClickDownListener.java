package com.example.mspr_java;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import model.EquipmentItemComponent;

public class OnClickDownListener implements View.OnClickListener {
    Map<View, EquipmentItemComponent> listeObjetView;

    public OnClickDownListener(Map<View, EquipmentItemComponent> listeObjetView) {
        this.listeObjetView = listeObjetView;
    }

    @Override
    public void onClick(View v) {
        EquipmentItemComponent item;
        Log.e("SUCE MA QUEUE","PUTAIN DE JUIF"+listeObjetView.size());
        item = listeObjetView.get(v.getParent());
        int oldValue =(Integer.valueOf(((EditText) ((LinearLayout)v.getParent()).getChildAt(3)).getText().toString()));
        if(oldValue > 0){

                String newvalue = ""+(oldValue-1);
                item.setQuantity(item.getQuantity()+1);
                ((TextView)((LinearLayout)v.getParent()).getChildAt(1)).setText(item.getQuantity()+" libre");
                ((EditText)((LinearLayout)v.getParent()).getChildAt(3)).setText(newvalue);


        }


    }
}
