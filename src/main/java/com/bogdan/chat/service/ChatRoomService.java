package com.bogdan.chat.service;

import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    Optional<ChatRoomDTO> findById(Long id);
    ChatRoomDTO save(ChatRoomDTO chatDTO);
    ChatRoomDTO update(ChatRoomDTO chatDTO);
    ChatRoomDTO delete(ChatRoomDTO chatDTO);
    List<ChatRoomDTO> getAll();
}
