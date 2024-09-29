package com.bogdan.chat.mapping;

import com.bogdan.chat.dto.ChatRoomInfoDTO;
import com.bogdan.chat.dto.ParticipantDTO;
import com.bogdan.chat.dto.UserInfoDTO;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.model.User;

public class ParticipantMapper implements Mapper<Participation, ParticipantDTO> {

    @Override
    public Participation create(ParticipantDTO entity) {
        User user = new User.UserBuilder()
                .setUserId(entity.getUserInfo().getId())
                .setName(entity.getUserInfo().getUsername())
                .setEmail(entity.getUserInfo().getEmail())
                .setRole(entity.getUserInfo().getRole())
                .build();
        ChatRoom chatRoom = new ChatRoom.ChatRoomBuilder()
                .setRoomId(entity.getChatRoomInfo().getId())
                .setName(entity.getChatRoomInfo().getName())
                .setDescription(entity.getChatRoomInfo().getDescription())
                .build();
        return new Participation.ParticipationBuilder()
                .setUser(user)
                .setChatRoom(chatRoom)
                .build();
    }

    @Override
    public ParticipantDTO map(Participation entity) {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setId(entity.getUser().getUserId());
        userInfo.setUsername(entity.getUser().getName());
        userInfo.setEmail(entity.getUser().getEmail());
        userInfo.setRole(entity.getUser().getRole());

        ChatRoomInfoDTO chatRoomInfo = new ChatRoomInfoDTO();
        chatRoomInfo.setId(entity.getChatRoom().getRoomId());
        chatRoomInfo.setName(entity.getChatRoom().getName());
        chatRoomInfo.setDescription(entity.getChatRoom().getDescription());

        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setUserInfo(userInfo);
        participantDTO.setChatRoomInfo(chatRoomInfo);
        return participantDTO;

    }
}
