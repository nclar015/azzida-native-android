package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePaymentModelData {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("JobId")
    @Expose
    private Integer jobId;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("RefBalance")
    @Expose
    private String RefBalance;
    @SerializedName("Tax")
    @Expose
    private String tax;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("WorkDuration")
    @Expose
    private String workDuration;
    @SerializedName("PaymentToken")
    @Expose
    private String paymentToken;
    @SerializedName("PaymentId")
    @Expose
    private String paymentId;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifyDate")
    @Expose
    private Object modifyDate;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @Expose
    private String PaymentType;
    @SerializedName("PaymentType")

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getRefBalance() {
        return RefBalance;
    }

    public void setRefBalance(String refBalance) {
        RefBalance = refBalance;
    }
}
