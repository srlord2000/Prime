package com.example.prime.Model;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

public class ServerResponseModel {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}