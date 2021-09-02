
package com.azzida.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeekerInprogressJobModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<SeekerInprogressJobModelDatum> data = null;

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

    public ArrayList<SeekerInprogressJobModelDatum> getData() {
        return data;
    }

    public void setData(ArrayList<SeekerInprogressJobModelDatum> data) {
        this.data = data;
    }

}
