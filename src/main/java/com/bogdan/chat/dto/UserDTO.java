package com.bogdan.chat.dto;

import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private String role;
    private Set<ChatRoomInfoDTO> chatRooms;//many to many
    private Set<ChatRoomInfoDTO> createdChats;//one to many

    private UserDTO(UserDTOBuilder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.role = builder.role;
        this.createdChats = builder.createdChats;
        this.chatRooms = builder.chatRooms;
    }

    public UserDTO(){}

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

    public Set<ChatRoomInfoDTO> getChatRooms() {
        return chatRooms;
    }

    public Set<ChatRoomInfoDTO> getCreatedChats() {
        return createdChats;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public static class UserDTOBuilder {
        private Long userId;
        private String name;
        private String email;
        private String role;
        private Set<ChatRoomInfoDTO> chatRooms = new HashSet<>();
        private Set<ChatRoomInfoDTO> createdChats = new HashSet<>();

        public UserDTOBuilder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserDTOBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserDTOBuilder setRole(String role) {
            this.role = role;
            return this;
        }

        public UserDTOBuilder setChatRooms(Set<ChatRoomInfoDTO> chatRooms) {
            this.chatRooms = chatRooms;
            return this;
        }

        public UserDTOBuilder setCreatedChats(Set<ChatRoomInfoDTO> createdChats) {
            this.createdChats = createdChats;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }
}
