package com.sidprice.android.baking_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private int   id ;
    private double quantity ;
    private String  measure ;
    private String  ingredient ;

    public Ingredient() {

    }

    protected Ingredient(Parcel in) {
        id = in.readInt();
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
