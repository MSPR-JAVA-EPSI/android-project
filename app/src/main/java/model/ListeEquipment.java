package model;

import java.util.List;

public class ListeEquipment {
    private List<EquipmentItemComponent> equipments;

    public ListeEquipment(List<EquipmentItemComponent> equipments) {
        this.equipments = equipments;
    }
    public List<EquipmentItemComponent> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentItemComponent> equipments) {
        this.equipments = equipments;
    }

}
