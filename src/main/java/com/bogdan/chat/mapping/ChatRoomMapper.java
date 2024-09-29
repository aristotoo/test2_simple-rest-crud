package com.bogdan.chat.mapping;

import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.dto.UserInfoDTO;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatRoomMapper implements Mapper<ChatRoom, ChatRoomDTO> {

    @Override
    public ChatRoom create(ChatRoomDTO entity) {
        User createdBy = new User.UserBuilder()
                .setUserId(entity.getCreateBy().getId())
                .setName(entity.getCreateBy().getUsername())
                .setEmail(entity.getCreateBy().getEmail())
                .setRole(entity.getCreateBy().getRole())
                .build();
        Set<User> participants = new HashSet<>();
        if(entity.getParticipants() != null) {
             participants = entity.getParticipants()
                    .stream()
                    .map(userInfo -> new User.UserBuilder()
                            .setUserId(userInfo.getId())
                            .setName(userInfo.getUsername())
                            .setEmail(userInfo.getEmail())
                            .setRole(userInfo.getRole())
                            .build())
                    .collect(Collectors.toSet());
        }
        return new ChatRoom.ChatRoomBuilder()
                .setRoomId(entity.getRoomId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setCreateBy(createdBy)
                .setParticipants(participants)
                .build();
    }

    @Override
    public ChatRoomDTO map(ChatRoom entity) {
        UserInfoDTO infoDTO = new UserInfoDTO();
        infoDTO.setId(entity.getCreateBy().getUserId());
        infoDTO.setUsername(entity.getCreateBy().getName());
        infoDTO.setEmail(entity.getCreateBy().getEmail());
        infoDTO.setRole(entity.getCreateBy().getRole());
        Set<UserInfoDTO> participants = new HashSet<>();
        if(entity.getParticipants() != null) {
             participants = entity.getParticipants()
                    .stream()
                    .map(user -> {
                        UserInfoDTO participant = new UserInfoDTO();
                        participant.setId(user.getUserId());
                        participant.setUsername(user.getName());
                        participant.setEmail(user.getEmail());
                        participant.setRole(user.getRole());
                        return participant;
                    })
                    .collect(Collectors.toSet());
        }
        return new ChatRoomDTO.ChatRoomDTOBuilder()
                .setRoomId(entity.getRoomId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setCreateBy(infoDTO)
                .setParticipants(participants)
                .build();
    }
}
