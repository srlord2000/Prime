package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListModel {
    @SerializedName("station_name")
    @Expose
    private String stationName;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("service_level")
    @Expose
    private String serviceLevel;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("time_added")
    @Expose
    private String timeAdded;
    @SerializedName("group_name")
    @Expose
    private String groupName;

    public ListModel(String stationName, String serviceType, String serviceLevel, String serviceName, String timeAdded, String groupName) {
        this.stationName = stationName;
        this.serviceType = serviceType;
        this.serviceLevel = serviceLevel;
        this.serviceName = serviceName;
        this.timeAdded = timeAdded;
        this.groupName = groupName;
    }

    public ListModel() {
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}