package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalModel {
    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("count")
    @Expose
    private int count;

    public TotalModel(int price, int count) {
        this.price = price;
        this.count = count;
    }

    public TotalModel() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
