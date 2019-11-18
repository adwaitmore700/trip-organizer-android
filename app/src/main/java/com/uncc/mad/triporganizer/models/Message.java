package com.uncc.mad.triporganizer.models;

import android.graphics.Bitmap;

import java.util.Date;

public class Message {
    private String Id;
    private String ChatRoomId;
    private String SenderId;
    private String ReceiverId;
    private String MessageType;
    private String MessageBody; //null if MessageType is Image
    private String MessageAttachmentUri; //null if MessageType is Text
    private Date MessageDateTime;
    private boolean MessageReadStatus;
    private boolean MessageDeliveredStatus;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public void setMessageBody(String messageBody) {
        MessageBody = messageBody;
    }

    public String getMessageAttachment() {
        return MessageAttachmentUri;
    }

    public void setMessageAttachment(String messageAttachment) {
        MessageAttachmentUri = messageAttachment;
    }

    public Date getMessageDateTime() {
        return MessageDateTime;
    }

    public void setMessageDateTime(Date messageDateTime) {
        MessageDateTime = messageDateTime;
    }

    public boolean isMessageReadStatus() {
        return MessageReadStatus;
    }

    public void setMessageReadStatus(boolean messageReadStatus) {
        MessageReadStatus = messageReadStatus;
    }

    public boolean isMessageDeliveredStatus() {
        return MessageDeliveredStatus;
    }

    public void setMessageDeliveredStatus(boolean messageDeliveredStatus) {
        MessageDeliveredStatus = messageDeliveredStatus;
    }
}
