package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WashCountModel {
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
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("group_name")
    @Expose
    private String groupName;

    public WashCountModel(String stationName, String serviceType, String serviceLevel, String serviceName, String count, String groupName) {
        this.stationName = stationName;
        this.serviceType = serviceType;
        this.serviceLevel = serviceLevel;
        this.serviceName = serviceName;
        this.count = count;
        this.groupName = groupName;
    }

    public WashCountModel() {
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private boolean isChecked = true;

    public boolean isChecked() {
        return isChecked;
    }

}