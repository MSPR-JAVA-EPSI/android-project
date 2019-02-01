package model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class EquipmentItemComponent implements Parcelable {
    private int quantity;
    private String name;
    private int id;

    public EquipmentItemComponent(JSONObject jObject) {
        this.quantity = jObject.optInt("max");
        this.name = jObject.optString("name");
        this.id = jObject.optInt("id");

    }

    protected EquipmentItemComponent(Parcel in) {
        quantity = in.readInt();
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<EquipmentItemComponent> CREATOR = new Creator<EquipmentItemComponent>() {
        @Override
        public EquipmentItemComponent createFromParcel(Parcel in) {
            return new EquipmentItemComponent(in);
        }

        @Override
        public EquipmentItemComponent[] newArray(int size) {
            return new EquipmentItemComponent[size];
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
}


