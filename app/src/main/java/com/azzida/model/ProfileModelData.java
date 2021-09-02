package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileModelData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("UserPassword")
    @Expose
    private String userPassword;
    @SerializedName("UserEmail")
    @Expose
    private String userEmail;
    @SerializedName("Skills")
    @Expose
    private String skills;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("DeviceType")
    @Expose
    private String deviceType;
    @SerializedName("GoogleEmail")
    @Expose
    private Object googleEmail;
    @SerializedName("FaceBookEmail")
    @Expose
    private String faceBookEmail;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("IsActive")
    @Expose
    private Object isActive;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("ProfilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("EmailType")
    @Expose
    private String emailType;
    @SerializedName("UserName")
    @Expose
    private Object userName;
    @SerializedName("JobType")
    @Expose
    private String JobType;
    @SerializedName("UserRatingAvg")
    @Expose
    private String UserRatingAvg;

    @SerializedName("RefCode")
    @Expose
    private String RefCode;

    @SerializedName("Balance")
    @Expose
    private String Balance;

    @SerializedName("StripeAccId")
    @Expose
    private String StripeAccId;

    @SerializedName("receivedAmount")
    @Expose
    private String receivedAmount;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Object getGoogleEmail() {
        return googleEmail;
    }

    public void setGoogleEmail(Object googleEmail) {
        this.googleEmail = googleEmail;
    }

    public String getFaceBookEmail() {
        return faceBookEmail;
    }

    public void setFaceBookEmail(String faceBookEmail) {
        this.faceBookEmail = faceBookEmail;
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

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public String getUserRatingAvg() {
        return UserRatingAvg;
    }

    public void setUserRatingAvg(String userRatingAvg) {
        UserRatingAvg = userRatingAvg;
    }

    public String getRefCode() {
        return RefCode;
    }

    public void setRefCode(String refCode) {
        RefCode = refCode;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getStripeAccId() {
        return StripeAccId;
    }

    public void setStripeAccId(String stripeAccId) {
        StripeAccId = stripeAccId;
    }

    public String getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        this.receivedAmount = receivedAmount;
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
