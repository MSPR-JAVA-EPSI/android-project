package com.example.mspr_java;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import model.EquipmentItemComponent;

public class OnClickUpListener implements View.OnClickListener {
    Map<View, EquipmentItemComponent> listeObjetView;

    public OnClickUpListener(Map<View, EquipmentItemComponent> listeObjetView) {
        this.listeObjetView = listeObjetView;
    }

    @Override
    public void onClick(View v) {
        EquipmentItemComponent item;
        item = listeObjetView.get(v.getParent());

        int oldValue = (Integer.valueOf(((EditText) ((LinearLayout) v.getParent()).getChildAt(3)).getText().toString()));
        if(item.getQuantity()>0) {

                String newvalue = "" + (oldValue + 1);
                item.setQuantity(item.getQuantity()-1);
                ((TextView) ((LinearLayout) v.getParent()).getChildAt(1)).setText(item.getQuantity() + " libre");
                ((EditText) ((LinearLayout) v.getParent()).getChildAt(3)).setText(newvalue);

        }
    }
}
