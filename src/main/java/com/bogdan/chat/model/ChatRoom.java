package com.bogdan.chat.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatRoom {
    private Long roomId;
    private String name;
    private String description;
    private User createBy;//one to many
    private Set<User> participants;//many to many

    private ChatRoom(ChatRoomBuilder builder){
        this.roomId = builder.roomId;
        this.name = builder.name;
        this.description = builder.description;
        this.createBy = builder.createBy;
        this.participants = builder.participants;

    }
    //default constructor for ORM
    public ChatRoom(){}

    public Long getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getCreateBy() {
        return createBy;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void addChatCreator(User user){
        this.createBy = user;
    }

    public void addParticipants(Set<User> participants){
        if(Objects.isNull(this.participants)){
            this.participants = new HashSet<>();
        }
        this.participants.addAll(participants);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createBy=" + createBy +
                ", participants=" + participants +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatRoom chatRoom)) return false;

        if (getRoomId() != null ? !getRoomId().equals(chatRoom.getRoomId()) : chatRoom.getRoomId() != null)
            return false;
        if (getName() != null ? !getName().equals(chatRoom.getName()) : chatRoom.getName() != null) return false;
        if (getDescription() != null ? !getDescription().equals(chatRoom.getDescription()) : chatRoom.getDescription() != null)
            return false;
        if (getCreateBy() != null ? !getCreateBy().equals(chatRoom.getCreateBy()) : chatRoom.getCreateBy() != null)
            return false;
        return getParticipants() != null ? getParticipants().equals(chatRoom.getParticipants()) : chatRoom.getParticipants() == null;
    }

    @Override
    public int hashCode() {
        int result = getRoomId() != null ? getRoomId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getCreateBy() != null ? getCreateBy().hashCode() : 0);
        result = 31 * result + (getParticipants() != null ? getParticipants().hashCode() : 0);
        return result;
    }

    public static class ChatRoomBuilder {
        private Long roomId;
        private String name;
        private String description;
        private User createBy;
        private Set<User> participants;

        public ChatRoomBuilder setRoomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public ChatRoomBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ChatRoomBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ChatRoomBuilder setCreateBy(User createBy) {
            this.createBy = createBy;
            return this;
        }

        public ChatRoomBuilder setParticipants(Set<User> participants) {
            this.participants = participants;
            return this;
        }

        public ChatRoom build(){
            return new ChatRoom(this);
        }
    }
}
