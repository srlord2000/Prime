package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("service_level")
    @Expose
    private String serviceLevel;
    @SerializedName("time_added")
    @Expose
    private String timeAdded;

    public CardsModel(String id, String serviceType, String serviceLevel, String timeAdded) {
        this.id = id;
        this.serviceType = serviceType;
        this.serviceLevel = serviceLevel;
        this.timeAdded = timeAdded;
    }

    public CardsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}