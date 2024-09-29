package com.bogdan.chat.mapping;

import com.bogdan.chat.dto.ChatRoomInfoDTO;
import com.bogdan.chat.dto.UserDTO;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper implements Mapper<User, UserDTO> {
    @Override
    public User create(UserDTO entity) {
        Set<ChatRoom> createdChats = new HashSet<>();
        if (entity.getCreatedChats() != null){
             createdChats = entity.getCreatedChats().stream()
                    .map(chatInfo -> new ChatRoom.ChatRoomBuilder()
                            .setRoomId(chatInfo.getId())
                            .setName(chatInfo.getName())
                            .setDescription(chatInfo.getDescription())
                            .build())
                    .collect(Collectors.toSet());
        }
        Set<ChatRoom> chatRooms = new HashSet<>();
        if(entity.getChatRooms() != null) {
            chatRooms = entity.getChatRooms().stream()
                    .map(chat ->
                            new ChatRoom.ChatRoomBuilder()
                                    .setRoomId(chat.getId())
                                    .setName(chat.getName())
                                    .setDescription(chat.getDescription())
                                    .build())
                    .collect(Collectors.toSet());
        }
        return new User.UserBuilder()
                .setUserId(entity.getUserId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setRole(entity.getRole())
                .setCreatedChats(createdChats)
                .setChatRooms(chatRooms)
                .build();
    }

    @Override
    public UserDTO map(User entity) {
        Set<ChatRoomInfoDTO> createdChats = new HashSet<>();
        if(entity.getCreatedChats() != null) {
            createdChats = entity.getCreatedChats().stream()
                    .map(chat -> {
                        ChatRoomInfoDTO chatRoomInfoDTO = new ChatRoomInfoDTO();
                        chatRoomInfoDTO.setId(chat.getRoomId());
                        chatRoomInfoDTO.setName(chat.getName());
                        chatRoomInfoDTO.setDescription(chat.getDescription());
                        return chatRoomInfoDTO;
                    })
                    .collect(Collectors.toSet());
        }
        Set<ChatRoomInfoDTO> chatRooms = new HashSet<>();
        if(entity.getChatRooms() != null) {
             chatRooms = entity.getChatRooms().stream()
                    .map(chat -> {
                        ChatRoomInfoDTO chats = new ChatRoomInfoDTO();
                        chats.setId(chat.getRoomId());
                        chats.setName(chat.getName());
                        chats.setDescription(chat.getDescription());
                        return chats;
                    })
                    .collect(Collectors.toSet());
        }
        return new UserDTO.UserDTOBuilder()
                .setUserId(entity.getUserId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setRole(entity.getRole())
                .setCreatedChats(createdChats)
                .setChatRooms(chatRooms)
                .build();
    }
}
