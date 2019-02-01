package model;

import java.util.List;

public class ListeEquipement {
    private List<EquipementItemComponent> liste;

    public ListeEquipement(List<EquipementItemComponent> liste) {
        this.liste = liste;
    }
    public List<EquipementItemComponent> getListe() {
        return liste;
    }

    public void setListe(List<EquipementItemComponent> liste) {
        this.liste = liste;
    }

}
