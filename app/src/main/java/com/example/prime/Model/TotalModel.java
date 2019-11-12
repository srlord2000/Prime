package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalModel {
    @SerializedName("price")
    @Expose
    private String price;

    public TotalModel(String price) {
        this.price = price;
    }

    public TotalModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
