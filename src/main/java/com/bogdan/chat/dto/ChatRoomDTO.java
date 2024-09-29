package com.bogdan.chat.dto;

import java.util.HashSet;
import java.util.Set;

public class ChatRoomDTO {
    private Long roomId;
    private String name;
    private String description;
    private UserInfoDTO createBy;
    private Set<UserInfoDTO> participants;

    private ChatRoomDTO(ChatRoomDTOBuilder builder){
        this.roomId = builder.roomId;
        this.name = builder.name;
        this.description = builder.description;
        this.createBy = builder.createBy;
        this.participants = builder.participants;
    }

    public ChatRoomDTO(){}

    public Long getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UserInfoDTO getCreateBy() {
        return createBy;
    }

    public Set<UserInfoDTO> getParticipants() {
        return participants;
    }

    @Override
    public String toString() {
        return "ChatRoomDTO{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createBy=" + createBy +
                ", participants=" + participants +
                '}';
    }

    public static class ChatRoomDTOBuilder {
        private Long roomId;
        private String name;
        private String description;
        private UserInfoDTO createBy;
        private Set<UserInfoDTO> participants = new HashSet<>();

        public ChatRoomDTOBuilder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public ChatRoomDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ChatRoomDTOBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ChatRoomDTOBuilder setCreateBy(UserInfoDTO createBy) {
            this.createBy = createBy;
            return this;
        }

        public ChatRoomDTOBuilder setParticipants(Set<UserInfoDTO> participants) {
            this.participants = participants;
            return this;
        }

        public ChatRoomDTO build(){
            return new ChatRoomDTO(this);
        }
    }
}
