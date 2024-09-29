package com.bogdan.chat.dao;

import com.bogdan.chat.model.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomDao {
    Optional<ChatRoom> findById(Long id);
    ChatRoom save(ChatRoom chatRoom);
    ChatRoom update(ChatRoom chatRoom);
    ChatRoom delete(ChatRoom chatRoom);
    List<ChatRoom> getAll();
}
