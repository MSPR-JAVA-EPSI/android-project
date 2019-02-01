package model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class EquipementItemComponent implements Parcelable {
    private int quantity;
    private String name;
    private int id;

    public EquipementItemComponent(JSONObject jObject) {
        this.quantity = jObject.optInt("max");
        this.name = jObject.optString("name");
        this.id = jObject.optInt("id");

    }

    protected EquipementItemComponent(Parcel in) {
        quantity = in.readInt();
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

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
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
        dest.writeInt(quantity);
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


