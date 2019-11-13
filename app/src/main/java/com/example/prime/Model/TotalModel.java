package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalModel {
    @SerializedName("price")
    @Expose
    private int price;

    public TotalModel(int price) {
        this.price = price;
    }

    public TotalModel() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
