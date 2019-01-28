package model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class EquipementItemComponent implements Parcelable {
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

    protected EquipementItemComponent(Parcel in) {
        valeur = in.readInt();
        max = in.readInt();
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<EquipementItemComponent> CREATOR = new Creator<EquipementItemComponent>() {
        @Override
        public EquipementItemComponent createFromParcel(Parcel in) {
            return new EquipementItemComponent(in);
        }

        @Override
        public EquipementItemComponent[] newArray(int size) {
            return new EquipementItemComponent[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(valeur);
        dest.writeInt(max);
        dest.writeString(name);
        dest.writeInt(id);
    }
    public boolean allowedClickedDown() {
        return false;
    }
    public boolean allowedClickedUp() {
        return false;
    }
}


