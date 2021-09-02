package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usercard {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("CardNumber")
    @Expose
    private String cardNumber;
    @SerializedName("ExpiryMonth")
    @Expose
    private String expiryMonth;
    @SerializedName("ExpiryYear")
    @Expose
    private String expiryYear;
    @SerializedName("CardType")
    @Expose
    private String cardType;
    @SerializedName("CreateDate")
    @Expose
    private Object createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

}