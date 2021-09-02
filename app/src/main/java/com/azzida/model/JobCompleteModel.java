
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobCompleteModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private JobCompleteModelData data;

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

    public JobCompleteModelData getData() {
        return data;
    }

    public void setData(JobCompleteModelData data) {
        this.data = data;
    }

}
