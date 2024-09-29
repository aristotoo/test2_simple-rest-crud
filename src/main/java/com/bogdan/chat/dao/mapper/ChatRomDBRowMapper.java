package com.bogdan.chat.dao.mapper;

import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRomDBRowMapper implements DBRowMapper<ChatRoom> {
    @Override
    public ChatRoom rowMap(ResultSet resultSet) throws SQLException {
        User creator = new User.UserBuilder()
                .setUserId(resultSet.getLong("creator_id"))
                .setName(resultSet.getString("username"))
                .setEmail(resultSet.getString("email"))
                .setRole(resultSet.getString("role"))
                .build();
        return new ChatRoom.ChatRoomBuilder()
                .setRoomId(resultSet.getLong("roomid"))
                .setName(resultSet.getString("chat_name"))
                .setDescription(resultSet.getString("chat_description"))
                .setCreateBy(creator)
                .build();
    }
}