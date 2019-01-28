package model;

import org.json.JSONObject;

public class EquipementItemComponent {
    private int valeur;
    private int max;
    private String name;
    private int id;
    public EquipementItemComponent(JSONObject jObject) {
        this.valeur = jObject.optInt("valeur");
        this.max = jObject.optInt("max");
        this.name = jObject.optString("name");
        this.id = jObject.optInt("id");
    }
    public int getValeur() {
            return valeur;
    }
    public int getId() {
            return id;
    }
    public void setId(int id) {
            this.id = id;
    }
    public int getMax() {
            return max;
    }
    public String getName() {
            return name;
    }
    public void setValeur(int valeur){
            this.valeur = valeur;
    }
    public void setMax(int max) {
            this.max = max;
    }
    public void setName(String name) {
            this.name = name;
    }
    public boolean allowedClickedDown() {
        return false;
    }
    public boolean allowedClickedUp() {
        return false;
    }
}


