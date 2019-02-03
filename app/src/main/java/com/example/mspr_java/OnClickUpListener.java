package com.example.mspr_java;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import model.Equipment;
import model.EquipmentItemComponent;

public class OnClickUpListener implements View.OnClickListener {
    Map<View, Equipment> listeObjetView;

    public OnClickUpListener(Map<View, Equipment> listeObjetView) {
        this.listeObjetView = listeObjetView;
    }

    @Override
    public void onClick(View v) {
        Equipment equipment = listeObjetView.get(v.getParent());
        EquipmentItemComponent item;
        item = equipment.getEquipment();

        int oldValue = equipment.getBorrowed();
        int newvalue =  (oldValue + 1);
        if(item.getQuantity()>0) {
            item.setQuantity(item.getQuantity()-1);
            equipment.setBorrowed(newvalue);
            ((TextView)((LinearLayout)v.getParent()).getChildAt(1)).setText((item.getQuantity())+" libre");
            ((EditText) ((LinearLayout) v.getParent()).getChildAt(3)).setText(newvalue+"");

        }
    }
}
