
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipModelData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("TippingAmount")
    @Expose
    private String tippingAmount;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private String modifyDate;
    @SerializedName("SeekerId")
    @Expose
    private Integer seekerId;
    @SerializedName("SeekerRate")
    @Expose
    private String seekerRate;

    @SerializedName("Status")
    @Expose
    private String Status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getTippingAmount() {
        return tippingAmount;
    }

    public void setTippingAmount(String tippingAmount) {
        this.tippingAmount = tippingAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Integer seekerId) {
        this.seekerId = seekerId;
    }

    public String getSeekerRate() {
        return seekerRate;
    }

    public void setSeekerRate(String seekerRate) {
        this.seekerRate = seekerRate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
