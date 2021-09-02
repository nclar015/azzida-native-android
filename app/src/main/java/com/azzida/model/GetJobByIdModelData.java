
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetJobByIdModelData {


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
    private Integer userId;
    @SerializedName("IsComplete")
    @Expose
    private Boolean isComplete;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("CompletedDate")
    @Expose
    private String completedDate;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("HowLong")
    @Expose
    private String howLong;
    @SerializedName("ApplicationAccept")
    @Expose
    private Boolean applicationAccept;
    @SerializedName("OfferAccept")
    @Expose
    private Boolean offerAccept;
    @SerializedName("imglist")
    @Expose
    private ArrayList<ImageList> imglist ;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getHowLong() {
        return howLong;
    }

    public void setHowLong(String howLong) {
        this.howLong = howLong;
    }

    public Boolean getApplicationAccept() {
        return applicationAccept;
    }

    public void setApplicationAccept(Boolean applicationAccept) {
        this.applicationAccept = applicationAccept;
    }

    public Boolean getOfferAccept() {
        return offerAccept;
    }

    public void setOfferAccept(Boolean offerAccept) {
        this.offerAccept = offerAccept;
    }

    public ArrayList<ImageList> getImglist() {
        return imglist;
    }

    public void setImglist(ArrayList<ImageList> imglist) {
        this.imglist = imglist;
    }

}
