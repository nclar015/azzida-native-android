package com.azzida.perfrences;


public class DataManager {
    private static DataManager dManager;

    private String JobID;
    private String SeekerId;
    private String paymentId;
    private String Amount;
    private String TipId;
    private String Address;
    private String Lat;
    private String Long;

    private String ChatUserId;
    private String ChatUserName;
    private String ChatUserProfile;

    private String AddressMain;
    private String LatMain;
    private String LongMain;

    private String LongInt;
    private String LatInt;

    private String StripeCode;

    private Boolean Payment = false;


    public static DataManager getInstance() {

        if (dManager == null) {
            dManager = new DataManager();
            return dManager;
        } else {
            return dManager;
        }

    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getSeekerId() {
        return SeekerId;
    }

    public void setSeekerId(String seekerId) {
        SeekerId = seekerId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getAddressMain() {
        return AddressMain;
    }

    public void setAddressMain(String addressMain) {
        AddressMain = addressMain;
    }

    public String getLatMain() {
        return LatMain;
    }

    public void setLatMain(String latMain) {
        LatMain = latMain;
    }

    public String getLongMain() {
        return LongMain;
    }

    public void setLongMain(String longMain) {
        LongMain = longMain;
    }

    public String getLongInt() {
        return LongInt;
    }

    public void setLongInt(String longInt) {
        LongInt = longInt;
    }

    public String getLatInt() {
        return LatInt;
    }

    public void setLatInt(String latInt) {
        LatInt = latInt;
    }

    public String getTipId() {
        return TipId;
    }

    public void setTipId(String tipId) {
        TipId = tipId;
    }

    public Boolean getPayment() {
        return Payment;
    }

    public void setPayment(Boolean payment) {
        Payment = payment;
    }

    public String getStripeCode() {
        return StripeCode;
    }

    public void setStripeCode(String stripeCode) {
        StripeCode = stripeCode;
    }

    public String getChatUserId() {
        return ChatUserId;
    }

    public void setChatUserId(String chatUserId) {
        ChatUserId = chatUserId;
    }

    public String getChatUserName() {
        return ChatUserName;
    }

    public void setChatUserName(String chatUserName) {
        ChatUserName = chatUserName;
    }

    public String getChatUserProfile() {
        return ChatUserProfile;
    }

    public void setChatUserProfile(String chatUserProfile) {
        ChatUserProfile = chatUserProfile;
    }
}