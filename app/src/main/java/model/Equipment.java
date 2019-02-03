package model;

public class Equipment {
    private EquipmentItemComponent equipment;
    private int borrowed = 0;
    private int oldBorrowed = 0;

    public int getOldBorrowed() {
        return oldBorrowed;
    }

    public void setOldBorrowed(int oldBorrowed) {
        this.oldBorrowed = oldBorrowed;
    }

    public EquipmentItemComponent getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentItemComponent equipment) {
        this.equipment = equipment;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }
}
