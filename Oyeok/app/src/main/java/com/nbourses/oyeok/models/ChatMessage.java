package com.nbourses.oyeok.models;

import com.nbourses.oyeok.enums.ChatMessageStatus;
import com.nbourses.oyeok.enums.ChatMessageUserSubtype;
import com.nbourses.oyeok.enums.ChatMessageUserType;

/**
 * Created by rohit on 17/02/16.
 */
public class ChatMessage {

    private String messageText;
    private ChatMessageUserType userType;
    private ChatMessageUserSubtype userSubtype;
    private ChatMessageStatus messageStatus;
    private String userName;
    private Long messageTime;
    private String imagePath;
    private String imageUrl;
    private String imageName;
    private String user_id;


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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ChatMessageUserSubtype getUserSubtype() {
        return userSubtype;
    }

    public void setUserSubtype(ChatMessageUserSubtype userSubtype) {
        this.userSubtype = userSubtype;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
