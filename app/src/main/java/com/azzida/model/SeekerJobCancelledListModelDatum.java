package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeekerJobCancelledListModelDatum {

    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("JobCategory")
    @Expose
    private String jobCategory;
    @SerializedName("JobDescription")
    @Expose
    private String jobDescription;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("HowLong")
    @Expose
    private String howLong;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("CancelDate")
    @Expose
    private String CancelDate;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getHowLong() {
        return howLong;
    }

    public void setHowLong(String howLong) {
        this.howLong = howLong;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCancelDate() {
        return CancelDate;
    }

    public void setCancelDate(String cancelDate) {
        CancelDate = cancelDate;
    }
}