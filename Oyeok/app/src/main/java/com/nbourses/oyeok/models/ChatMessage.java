package com.nbourses.oyeok.models;

import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserType;

/**
 * Created by rohit on 17/02/16.
 */
public class ChatMessage {

    private String messageText;
    private ChatMessageUserType userType;
    private ChatMessageStatus messageStatus;
    private String userName;
    private Long messageTime;

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }



    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setUserType(ChatMessageUserType userType) {
        this.userType = userType;
    }

    public void setMessageStatus(ChatMessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageText() {
        return messageText;
    }

    public ChatMessageUserType getUserType() {
        return userType;
    }

    public ChatMessageStatus getMessageStatus() {
        return messageStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
