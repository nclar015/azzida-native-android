
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyJobModelDatum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("JobCategory")
    @Expose
    private String jobCategory;
    @SerializedName("JobDescription")
    @Expose
    private String jobDescription;
    @SerializedName("JobPicture")
    @Expose
    private String jobPicture;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("IsComplete")
    @Expose
    private Object isComplete;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("CompletedDate")
    @Expose
    private Object completedDate;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("HowLong")
    @Expose
    private String HowLong;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("Distance")
    @Expose
    private String Distance;
    @SerializedName("ProfilePicture")
    @Expose
    private String ListerProfilePicture;
    @SerializedName("Status")
    @Expose
    private String Status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getJobPicture() {
        return jobPicture;
    }

    public void setJobPicture(String jobPicture) {
        this.jobPicture = jobPicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Object isComplete) {
        this.isComplete = isComplete;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public Object getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Object completedDate) {
        this.completedDate = completedDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHowLong() {
        return HowLong;
    }

    public void setHowLong(String howLong) {
        HowLong = howLong;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getListerProfilePicture() {
        return ListerProfilePicture;
    }

    public void setListerProfilePicture(String listerProfilePicture) {
        ListerProfilePicture = listerProfilePicture;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
