package com.bogdan.chat.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private Set<ChatRoom> chatRooms;//many to many
    private Set<ChatRoom> createdChats;//one to many

    private User(UserBuilder builder){
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.chatRooms = builder.chatRooms;
        this.createdChats = builder.createdChats;
    }

    //default constructor for ORM
    public User(){}

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Set<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public Set<ChatRoom> getCreatedChats() {
        return createdChats;
    }

    public void setCreatedChats(Set<ChatRoom> chatRoom){
        this.createdChats = chatRoom;
    }
    public void setChatRooms(Set<ChatRoom> chatRooms){
        this.chatRooms = chatRooms;
    }

    public void addChatRoom(ChatRoom chat){
        if(chatRooms == null){
            chatRooms = new HashSet<>();
        }
        chatRooms.add(chat);
        chat.getParticipants().add(this);
    }

    public void removeChatRoom(ChatRoom chat){
        if(chatRooms != null){
            chatRooms.remove(chat);
            chat.getParticipants().remove(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", chatRooms=" + chatRooms +
                ", createdChats=" + createdChats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        if (getUserId() != null ? !getUserId().equals(user.getUserId()) : user.getUserId() != null) return false;
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) return false;
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        if (getRole() != null ? !getRole().equals(user.getRole()) : user.getRole() != null) return false;
        if (getChatRooms() != null ? !getChatRooms().equals(user.getChatRooms()) : user.getChatRooms() != null)
            return false;
        return getCreatedChats() != null ? getCreatedChats().equals(user.getCreatedChats()) : user.getCreatedChats() == null;
    }

    @Override
    public int hashCode() {
        int result = getUserId() != null ? getUserId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        result = 31 * result + (getChatRooms() != null ? getChatRooms().hashCode() : 0);
        result = 31 * result + (getCreatedChats() != null ? getCreatedChats().hashCode() : 0);
        return result;
    }

    public static class UserBuilder {
        private Long userId;
        private String name;
        private String email;
        private String role;
        private Set<ChatRoom> chatRooms = new HashSet<>();//many to many
        private Set<ChatRoom> createdChats = new HashSet<>();//one to many


        public UserBuilder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setRole(String role) {
            this.role = role;
            return this;
        }

        public UserBuilder setChatRooms(Set<ChatRoom> chatRooms) {
            this.chatRooms = chatRooms;
            return this;
        }

        public UserBuilder setCreatedChats(Set<ChatRoom> createdChats) {
            this.createdChats = createdChats;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
