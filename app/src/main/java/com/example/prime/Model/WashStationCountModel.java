package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WashStationCountModel {

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    private ArrayList<WashCountModel> washCountModels;

    public WashStationCountModel(String serviceName, ArrayList<WashCountModel> washCountModels) {
        this.serviceName = serviceName;
        this.washCountModels = washCountModels;
    }

    public WashStationCountModel() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ArrayList<WashCountModel> getWashCountModels() {
        return washCountModels;
    }

    public void setWashCountModels(ArrayList<WashCountModel> washCountModels) {
        this.washCountModels = washCountModels;
    }
}