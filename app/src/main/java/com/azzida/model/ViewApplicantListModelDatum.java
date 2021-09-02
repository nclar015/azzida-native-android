
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewApplicantListModelDatum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ListerId")
    @Expose
    private Integer listerId;
    @SerializedName("SeekerId")
    @Expose
    private Integer seekerId;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("ProfilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("JobCompleteCount")
    @Expose
    private Integer jobCompleteCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListerId() {
        return listerId;
    }

    public void setListerId(Integer listerId) {
        this.listerId = listerId;
    }

    public Integer getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Integer seekerId) {
        this.seekerId = seekerId;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Integer getJobCompleteCount() {
        return jobCompleteCount;
    }

    public void setJobCompleteCount(Integer jobCompleteCount) {
        this.jobCompleteCount = jobCompleteCount;
    }

}
