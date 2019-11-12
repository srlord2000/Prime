package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DryStationCountModel {

    @SerializedName("service_name")
    @Expose
    private String serviceName;

    private ArrayList<DryCountModel> dryCountModels;

    public DryStationCountModel(String serviceName, ArrayList<DryCountModel> dryCountModels) {
        this.serviceName = serviceName;
        this.dryCountModels = dryCountModels;
    }

    public DryStationCountModel() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ArrayList<DryCountModel> getDryCountModels() {
        return dryCountModels;
    }

    public void setDryCountModels(ArrayList<DryCountModel> dryCountModels) {
        this.dryCountModels = dryCountModels;
    }
}