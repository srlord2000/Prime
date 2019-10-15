package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UnitModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("time_added")
    @Expose
    private String timeAdded;

    private ArrayList<ServiceModel> serviceModels;

    public UnitModel(String id, String unitName, String groupId, String timeAdded, ArrayList<ServiceModel> serviceModels) {
        this.id = id;
        this.unitName = unitName;
        this.groupId = groupId;
        this.timeAdded = timeAdded;
        this.serviceModels = serviceModels;
    }

    public UnitModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public ArrayList<ServiceModel> getServiceModels() {
        return serviceModels;
    }

    public void setServiceModels(ArrayList<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
    }

}