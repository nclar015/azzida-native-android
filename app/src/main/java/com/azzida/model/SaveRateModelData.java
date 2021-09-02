
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveRateModelData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("Rate1")
    @Expose
    private Integer rate1;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("SeekerId")
    @Expose
    private Object seekerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getRate1() {
        return rate1;
    }

    public void setRate1(Integer rate1) {
        this.rate1 = rate1;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Object getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Object modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Object getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Object seekerId) {
        this.seekerId = seekerId;
    }

}
