
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeekerInprogressJobModelDatum {

    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("JobDescription")
    @Expose
    private String jobDescription;
    @SerializedName("JobPicture")
    @Expose
    private String jobPicture;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("HowLong")
    @Expose
    private String howLong;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("ApplicantCount")
    @Expose
    private Integer applicantCount;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("ListerId")
    @Expose
    private Integer listerId;
    @SerializedName("IsApply")
    @Expose
    private Boolean isApply;
    @SerializedName("ApplicationAccepted")
    @Expose
    private Boolean applicationAccepted;
    @SerializedName("OfferAccepted")
    @Expose
    private Boolean offerAccepted;
    @SerializedName("SeekerId")
    @Expose
    private Object seekerId;
    @SerializedName("ListerName")
    @Expose
    private Object listerName;
    @SerializedName("ListerProfilePicture")
    @Expose
    private Object listerProfilePicture;
    @SerializedName("ListerCompleteJob")
    @Expose
    private Integer listerCompleteJob;
    @SerializedName("IsComplete")
    @Expose
    private Boolean isComplete;
    @SerializedName("imageList")
    @Expose
    private Object imageList;
    @SerializedName("JobCategory")
    @Expose
    private String jobCategory;

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getApplicantCount() {
        return applicantCount;
    }

    public void setApplicantCount(Integer applicantCount) {
        this.applicantCount = applicantCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getListerId() {
        return listerId;
    }

    public void setListerId(Integer listerId) {
        this.listerId = listerId;
    }

    public Boolean getIsApply() {
        return isApply;
    }

    public void setIsApply(Boolean isApply) {
        this.isApply = isApply;
    }

    public Boolean getApplicationAccepted() {
        return applicationAccepted;
    }

    public void setApplicationAccepted(Boolean applicationAccepted) {
        this.applicationAccepted = applicationAccepted;
    }

    public Boolean getOfferAccepted() {
        return offerAccepted;
    }

    public void setOfferAccepted(Boolean offerAccepted) {
        this.offerAccepted = offerAccepted;
    }

    public Object getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Object seekerId) {
        this.seekerId = seekerId;
    }

    public Object getListerName() {
        return listerName;
    }

    public void setListerName(Object listerName) {
        this.listerName = listerName;
    }

    public Object getListerProfilePicture() {
        return listerProfilePicture;
    }

    public void setListerProfilePicture(Object listerProfilePicture) {
        this.listerProfilePicture = listerProfilePicture;
    }

    public Integer getListerCompleteJob() {
        return listerCompleteJob;
    }

    public void setListerCompleteJob(Integer listerCompleteJob) {
        this.listerCompleteJob = listerCompleteJob;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Object getImageList() {
        return imageList;
    }

    public void setImageList(Object imageList) {
        this.imageList = imageList;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

}