package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewPaymentTransactionModelDatum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ListerId")
    @Expose
    private String listerId;
    @SerializedName("PaidTo")
    @Expose
    private String paidTo;
    @SerializedName("JobId")
    @Expose
    private String jobId;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("TotalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ReceivedFrom")
    @Expose
    private String receivedFrom;
    @SerializedName("SenderName")
    @Expose
    private String senderName;
    @SerializedName("SenderProfilePicture")
    @Expose
    private String senderProfilePicture;
    @SerializedName("MyId")
    @Expose
    private String myId;
    @SerializedName("ToName")
    @Expose
    private String toName;
    @SerializedName("ToProfilePicture")
    @Expose
    private String toProfilePicture;

    @SerializedName("SeekerPaymentAmount")
    @Expose
    private String SeekerPaymentAmount;

    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("IsListerPaymentDone")
    @Expose
    private Boolean isListerPaymentDone;
    @SerializedName("IsSeekerPaymentDone")
    @Expose
    private Boolean isSeekerPaymentDone;

    @SerializedName("JobAmount")
    @Expose
    private String JobAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getListerId() {
        return listerId;
    }

    public void setListerId(String listerId) {
        this.listerId = listerId;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfilePicture() {
        return senderProfilePicture;
    }

    public void setSenderProfilePicture(String senderProfilePicture) {
        this.senderProfilePicture = senderProfilePicture;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToProfilePicture() {
        return toProfilePicture;
    }

    public void setToProfilePicture(String toProfilePicture) {
        this.toProfilePicture = toProfilePicture;
    }

    public String getSeekerPaymentAmount() {
        return SeekerPaymentAmount;
    }

    public void setSeekerPaymentAmount(String seekerPaymentAmount) {
        SeekerPaymentAmount = seekerPaymentAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Boolean getListerPaymentDone() {
        return isListerPaymentDone;
    }

    public void setListerPaymentDone(Boolean listerPaymentDone) {
        isListerPaymentDone = listerPaymentDone;
    }

    public Boolean getSeekerPaymentDone() {
        return isSeekerPaymentDone;
    }

    public void setSeekerPaymentDone(Boolean seekerPaymentDone) {
        isSeekerPaymentDone = seekerPaymentDone;
    }

    public String getJobAmount() {
        return JobAmount;
    }

    public void setJobAmount(String jobAmount) {
        JobAmount = jobAmount;
    }
}
