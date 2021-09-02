
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignInModelData {

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
    private String googleEmail;
    @SerializedName("FaceBookEmail")
    @Expose
    private String faceBookEmail;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private String modifyDate;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
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
    private String userName;
    @SerializedName("RefCode")
    @Expose
    private String RefCode;
    @SerializedName("UserLongitude")
    @Expose
    private String UserLongitude;
    @SerializedName("UserLatitude")
    @Expose
    private String UserLatitude;

    @SerializedName("StripeAccId")
    @Expose
    private String StripeAccId;

    @SerializedName("AzzidaVerified")
    @Expose
    private Boolean azzidaVerified;

    @SerializedName("CandidateId")
    @Expose
    private String candidateId;

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

    public String getGoogleEmail() {
        return googleEmail;
    }

    public void setGoogleEmail(String googleEmail) {
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

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRefCode() {
        return RefCode;
    }

    public void setRefCode(String refCode) {
        RefCode = refCode;
    }


    public String getUserLongitude() {
        return UserLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        UserLongitude = userLongitude;
    }

    public String getUserLatitude() {
        return UserLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        UserLatitude = userLatitude;
    }

    public String getStripeAccId() {
        return StripeAccId;
    }

    public void setStripeAccId(String stripeAccId) {
        StripeAccId = stripeAccId;
    }

    public Boolean getAzzidaVerified() {
        return azzidaVerified;
    }

    public void setAzzidaVerified(Boolean azzidaVerified) {
        this.azzidaVerified = azzidaVerified;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
}
