package com.example.mspr_java;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import model.EquipementItemComponent;

public class OnClickUpListener implements View.OnClickListener {
    Map<View, EquipementItemComponent> listeObjetView;

    public OnClickUpListener(Map<View, EquipementItemComponent> listeObjetView) {
        this.listeObjetView = listeObjetView;
    }

    @Override
    public void onClick(View v) {
        EquipementItemComponent item;
        item = listeObjetView.get(v.getParent());
        int oldValue = (Integer.valueOf(((EditText) ((LinearLayout) v.getParent()).getChildAt(3)).getText().toString()));
        if(oldValue < item.getMax() && oldValue >0) {
            if (item.allowedClickedUp()) {
                String newvalue = "" + (oldValue + 1);
                ((TextView) ((LinearLayout) v.getParent()).getChildAt(1)).setText(item.getValeur() + "/" + item.getMax());
                ((EditText) ((LinearLayout) v.getParent()).getChildAt(3)).setText(newvalue);
            }
        }
    }
}
