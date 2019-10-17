package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("display_number")
    @Expose
    private String displayNumber;
    @SerializedName("group_description")
    @Expose
    private Object groupDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisplayNumber() {
        return displayNumber;
    }

    public void setDisplayNumber(String displayNumber) {
        this.displayNumber = displayNumber;
    }

    public Object getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(Object groupDescription) {
        this.groupDescription = groupDescription;
    }

}