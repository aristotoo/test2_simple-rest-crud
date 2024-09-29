package com.bogdan.chat.dto;

public class ParticipantDTO {
    private UserInfoDTO userInfo;
    private ChatRoomInfoDTO chatRoomInfo;

    public ParticipantDTO(){}

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public ChatRoomInfoDTO getChatRoomInfo() {
        return chatRoomInfo;
    }

    public void setChatRoomInfo(ChatRoomInfoDTO chatRoomInfo) {
        this.chatRoomInfo = chatRoomInfo;
    }

    @Override
    public String toString() {
        return "ParticipantDTO{" +
                "userInfo=" + userInfo +
                ", chatRoomInfo=" + chatRoomInfo +
                '}';
    }
}
