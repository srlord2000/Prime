package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StringModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("host_id")
    @Expose
    private String hostId;
    @SerializedName("station_name")
    @Expose
    private String stationName;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("ipaddress")
    @Expose
    private String ipaddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
}
