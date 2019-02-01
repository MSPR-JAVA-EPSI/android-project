package model;

import java.util.List;

public class ListeEquipement {
    private List<EquipementItemComponent> equipements;

    public ListeEquipement(List<EquipementItemComponent> equipements) {
        this.equipements = equipements;
    }
    public List<EquipementItemComponent> getEquipements() {
        return equipements;
    }

    public void setEquipements(List<EquipementItemComponent> equipements) {
        this.equipements = equipements;
    }

}
