package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WashSummaryModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("service_level")
    @Expose
    private String serviceLevel;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("tap_pulse")
    @Expose
    private String tapPulse;
    @SerializedName("time_added")
    @Expose
    private String timeAdded;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("unit_id")
    @Expose
    private String unitId;

    private ArrayList<WashCountModel> washCountModels;

    public WashSummaryModel(String id, String serviceName, String serviceType, String serviceLevel, String price, String tapPulse, String timeAdded, String unitName, String unitId) {
        this.id = id;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.serviceLevel = serviceLevel;
        this.price = price;
        this.tapPulse = tapPulse;
        this.timeAdded = timeAdded;
        this.unitName = unitName;
        this.unitId = unitId;
        this.washCountModels = washCountModels;
    }

    public WashSummaryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTapPulse() {
        return tapPulse;
    }

    public void setTapPulse(String tapPulse) {
        this.tapPulse = tapPulse;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public ArrayList<WashCountModel> getWashCountModels() {
        return washCountModels;
    }

    public void setWashCountModels(ArrayList<WashCountModel> washCountModels) {
        this.washCountModels = washCountModels;
    }
}

