package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Applied {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ApplierId")
    @Expose
    private Integer applierId;
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
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Howlong")
    @Expose
    private String howlong;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplierId() {
        return applierId;
    }

    public void setApplierId(Integer applierId) {
        this.applierId = applierId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHowlong() {
        return howlong;
    }

    public void setHowlong(String howlong) {
        this.howlong = howlong;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

}
