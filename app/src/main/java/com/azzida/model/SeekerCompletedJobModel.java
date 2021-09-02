package com.azzida.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeekerCompletedJobModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<SeekerCompletedJobModelDatum> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SeekerCompletedJobModelDatum> getData() {
        return data;
    }

    public void setData(ArrayList<SeekerCompletedJobModelDatum> data) {
        this.data = data;
    }

}
