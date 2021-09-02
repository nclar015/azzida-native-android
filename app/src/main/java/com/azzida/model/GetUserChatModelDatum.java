
package com.azzida.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserChatModelDatum {

    @SerializedName("SenderId")
    @Expose
    private Integer senderId;
    @SerializedName("SenderName")
    @Expose
    private String senderName;
    @SerializedName("SenderProfilePic")
    @Expose
    private String senderProfilePic;
    @SerializedName("ReceiverId")
    @Expose
    private Integer receiverId;
    @SerializedName("ReceiverName")
    @Expose
    private String receiverName;
    @SerializedName("ReceiverProfilePic")
    @Expose
    private String receiverProfilePic;
    @SerializedName("UserMessage")
    @Expose
    private String userMessage;
    @SerializedName("MessageDateTime")
    @Expose
    private String messageDateTime;
    @SerializedName("IsTyping")
    @Expose
    private Boolean isTyping;

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfilePic() {
        return senderProfilePic;
    }

    public void setSenderProfilePic(String senderProfilePic) {
        this.senderProfilePic = senderProfilePic;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverProfilePic() {
        return receiverProfilePic;
    }

    public void setReceiverProfilePic(String receiverProfilePic) {
        this.receiverProfilePic = receiverProfilePic;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getMessageDateTime() {
        return messageDateTime;
    }

    public void setMessageDateTime(String messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public Boolean getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(Boolean isTyping) {
        this.isTyping = isTyping;
    }

}
