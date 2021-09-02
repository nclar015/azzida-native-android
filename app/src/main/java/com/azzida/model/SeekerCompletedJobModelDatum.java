package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeekerCompletedJobModelDatum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("HowLong")
    @Expose
    private String howLong;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("JobCategory")
    @Expose
    private String jobCategory;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("JobDescription")
    @Expose
    private String jobDescription;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("IsComplete")
    @Expose
    private Boolean isComplete;
    @SerializedName("CompletedDate")
    @Expose
    private String completedDate;
    @SerializedName("JobPicture")
    @Expose
    private Object jobPicture;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("AssignSeekerId")
    @Expose
    private Integer assignSeekerId;
    @SerializedName("IscompleteUser")
    @Expose
    private Boolean iscompleteUser;
    @SerializedName("IsCancel")
    @Expose
    private Object isCancel;

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

    public String getHowLong() {
        return howLong;
    }

    public void setHowLong(String howLong) {
        this.howLong = howLong;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Object modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public Object getJobPicture() {
        return jobPicture;
    }

    public void setJobPicture(Object jobPicture) {
        this.jobPicture = jobPicture;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAssignSeekerId() {
        return assignSeekerId;
    }

    public void setAssignSeekerId(Integer assignSeekerId) {
        this.assignSeekerId = assignSeekerId;
    }

    public Boolean getIscompleteUser() {
        return iscompleteUser;
    }

    public void setIscompleteUser(Boolean iscompleteUser) {
        this.iscompleteUser = iscompleteUser;
    }

    public Object getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Object isCancel) {
        this.isCancel = isCancel;
    }

}