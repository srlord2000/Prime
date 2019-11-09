package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SummaryModel {
    @SerializedName("unitid")
    @Expose
    private String unitid;

    @SerializedName("unitname")
    @Expose
    private String unitname;

    private ArrayList<WashSummaryModel> washSummaryModels;

    private ArrayList<DrySummaryModel> drySummaryModels;

    private ArrayList<DryStationModel> dryStationModels;

    private ArrayList<WashStationModel> washStationModels;


    public SummaryModel(String unitid, String unitname, ArrayList<WashSummaryModel> washSummaryModels, ArrayList<DrySummaryModel> drySummaryModels) {
        this.unitid = unitid;
        this.unitname = unitname;
        this.washSummaryModels = washSummaryModels;
        this.drySummaryModels = drySummaryModels;
    }

    public SummaryModel() {
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public ArrayList<WashSummaryModel> getWashSummaryModels() {
        return washSummaryModels;
    }

    public void setWashSummaryModels(ArrayList<WashSummaryModel> washSummaryModels) {
        this.washSummaryModels = washSummaryModels;
    }

    public ArrayList<DrySummaryModel> getDrySummaryModels() {
        return drySummaryModels;
    }

    public void setDrySummaryModels(ArrayList<DrySummaryModel> drySummaryModels) {
        this.drySummaryModels = drySummaryModels;
    }

    public ArrayList<DryStationModel> getDryStationModels() {
        return dryStationModels;
    }

    public void setDryStationModels(ArrayList<DryStationModel> dryStationModels) {
        this.dryStationModels = dryStationModels;
    }

    public ArrayList<WashStationModel> getWashStationModels() {
        return washStationModels;
    }

    public void setWashStationModels(ArrayList<WashStationModel> washStationModels) {
        this.washStationModels = washStationModels;
    }
}
