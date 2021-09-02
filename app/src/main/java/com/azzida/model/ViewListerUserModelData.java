
package com.azzida.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewListerUserModelData {

    @SerializedName("Getrecentactivity")
    @Expose
    private ArrayList<GetrecentactivityModel> getrecentactivity = null;
    @SerializedName("JobCompleteCount")
    @Expose
    private String jobCompleteCount;
    @SerializedName("JobPostingcount")
    @Expose
    private String jobPostingcount;
    @SerializedName("joinDate")
    @Expose
    private String joinDate;
    @SerializedName("ListerId")
    @Expose
    private Integer listerId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ProfilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("ReportStatus")
    @Expose
    private String reportStatus;

    @SerializedName("NationalStatus")
    @Expose
    private String nationalStatus;

    @SerializedName("GlobalStatus")
    @Expose
    private String globalStatus;

    @SerializedName("SexOffenderStatus")
    @Expose
    private String sexOffenderStatus;

    @SerializedName("SsnTraceStatus")
    @Expose
    private String ssnTraceStatus;

    @SerializedName("AzzidaVerified")
    @Expose
    private Boolean azzidaVerified;
    @SerializedName("RateAvg")
    @Expose
    private String RateAvg;
    @SerializedName("RatingUserCount")
    @Expose
    private String ratingUserCount;

    public ArrayList<GetrecentactivityModel> getGetrecentactivity() {
        return getrecentactivity;
    }

    public void setGetrecentactivity(ArrayList<GetrecentactivityModel> getrecentactivity) {
        this.getrecentactivity = getrecentactivity;
    }

    public String getJobCompleteCount() {
        return jobCompleteCount;
    }

    public void setJobCompleteCount(String jobCompleteCount) {
        this.jobCompleteCount = jobCompleteCount;
    }

    public String getJobPostingcount() {
        return jobPostingcount;
    }

    public void setJobPostingcount(String jobPostingcount) {
        this.jobPostingcount = jobPostingcount;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public Integer getListerId() {
        return listerId;
    }

    public void setListerId(Integer listerId) {
        this.listerId = listerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getAzzidaVerified() {
        return azzidaVerified;
    }

    public void setAzzidaVerified(Boolean azzidaVerified) {
        this.azzidaVerified = azzidaVerified;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getRateAvg() {
        return RateAvg;
    }

    public void setRateAvg(String rateAvg) {
        RateAvg = rateAvg;
    }

    public String getRatingUserCount() {
        return ratingUserCount;
    }

    public void setRatingUserCount(String ratingUserCount) {
        this.ratingUserCount = ratingUserCount;
    }

    public String getNationalStatus() {
        return nationalStatus;
    }

    public void setNationalStatus(String nationalStatus) {
        this.nationalStatus = nationalStatus;
    }

    public String getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(String globalStatus) {
        this.globalStatus = globalStatus;
    }

    public String getSexOffenderStatus() {
        return sexOffenderStatus;
    }

    public void setSexOffenderStatus(String sexOffenderStatus) {
        this.sexOffenderStatus = sexOffenderStatus;
    }

    public String getSsnTraceStatus() {
        return ssnTraceStatus;
    }

    public void setSsnTraceStatus(String ssnTraceStatus) {
        this.ssnTraceStatus = ssnTraceStatus;
    }
}
