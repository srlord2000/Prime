package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InventorySpinnerModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("time_added")
    @Expose
    private String timeAdded;

    public InventorySpinnerModel(String id, String itemName, String price, String timeAdded) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.timeAdded = timeAdded;
    }

    public InventorySpinnerModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }


}