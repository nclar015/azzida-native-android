
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobApplicationModelData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("SeekerId")
    @Expose
    private Integer seekerId;
    @SerializedName("ListerId")
    @Expose
    private Integer listerId;
    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("IsAcceptedByLister")
    @Expose
    private Boolean isAcceptedByLister;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private Boolean modifyDate;
    @SerializedName("IsAccteptedBySeeker")
    @Expose
    private Boolean isAccteptedBySeeker;
    @SerializedName("IsApply")
    @Expose
    private Boolean isApply;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Integer seekerId) {
        this.seekerId = seekerId;
    }

    public Integer getListerId() {
        return listerId;
    }

    public void setListerId(Integer listerId) {
        this.listerId = listerId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Boolean getIsAcceptedByLister() {
        return isAcceptedByLister;
    }

    public void setIsAcceptedByLister(Boolean isAcceptedByLister) {
        this.isAcceptedByLister = isAcceptedByLister;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Boolean modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Boolean getIsAccteptedBySeeker() {
        return isAccteptedBySeeker;
    }

    public void setIsAccteptedBySeeker(Boolean isAccteptedBySeeker) {
        this.isAccteptedBySeeker = isAccteptedBySeeker;
    }

    public Boolean getIsApply() {
        return isApply;
    }

    public void setIsApply(Boolean isApply) {
        this.isApply = isApply;
    }

}
