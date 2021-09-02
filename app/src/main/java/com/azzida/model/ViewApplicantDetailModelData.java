
package com.azzida.model;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewApplicantDetailModelData {

    @SerializedName("SeekerName")
    @Expose
    private String seekerName;
    @SerializedName("GetRecentActivity")
    @Expose
    private ArrayList<GetrecentactivityModel> getRecentActivity = null;
    @SerializedName("JobCompleteCount")
    @Expose
    private String jobCompleteCount;
    @SerializedName("Joindate")
    @Expose
    private String joindate;
    @SerializedName("LName")
    @Expose
    private String lName;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("UserProfile")
    @Expose
    private String userProfile;
    @SerializedName("RatingUserCount")
    @Expose
    private String ratingUserCount;
    @SerializedName("SeekerId")
    @Expose
    private Integer seekerId;
    @SerializedName("SkillExperience")
    @Expose
    private String skillExperience;
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

    @SerializedName("RateAvg")
    @Expose
    private String RateAvg;
    @SerializedName("AzzidaVarified")
    @Expose
    private Boolean azzidaVerified;


    public String getSeekerName() {
        return seekerName;
    }

    public void setSeekerName(String seekerName) {
        this.seekerName = seekerName;
    }

    public ArrayList<GetrecentactivityModel> getGetRecentActivity() {
        return getRecentActivity;
    }

    public void setGetRecentActivity(ArrayList<GetrecentactivityModel> getRecentActivity) {
        this.getRecentActivity = getRecentActivity;
    }

    public String getJobCompleteCount() {
        return jobCompleteCount;
    }

    public void setJobCompleteCount(String jobCompleteCount) {
        this.jobCompleteCount = jobCompleteCount;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getRatingUserCount() {
        return ratingUserCount;
    }

    public void setRatingUserCount(String ratingUserCount) {
        this.ratingUserCount = ratingUserCount;
    }

    public Integer getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Integer seekerId) {
        this.seekerId = seekerId;
    }

    public String getSkillExperience() {
        return skillExperience;
    }

    public void setSkillExperience(String skillExperience) {
        this.skillExperience = skillExperience;
    }

    public String getRateAvg() {
        return RateAvg;
    }

    public void setRateAvg(String rateAvg) {
        RateAvg = rateAvg;
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
