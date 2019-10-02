package com.example.prime.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyModel {

//    @SerializedName("result")
//    @Expose
//    private ArrayList results;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("ci_sessionId")
    @Expose
    private String headers;

//    public ArrayList getResults() {
//        return results;
//    }
//
//    public void setResults(ArrayList results) {
//        this.results = results;
//    }


    public MyModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }
}
