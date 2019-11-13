package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StationTotalModel {
    @SerializedName("station_name")
    @Expose
    private String stationName;

    private ArrayList<TotalModel> totalModelArrayList;

    public StationTotalModel(String stationName, ArrayList<TotalModel> totalModelArrayList) {
        this.stationName = stationName;
        this.totalModelArrayList = totalModelArrayList;
    }

    public StationTotalModel() {
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public ArrayList<TotalModel> getTotalModelArrayList() {
        return totalModelArrayList;
    }

    public void setTotalModelArrayList(ArrayList<TotalModel> totalModelArrayList) {
        this.totalModelArrayList = totalModelArrayList;
    }
}
