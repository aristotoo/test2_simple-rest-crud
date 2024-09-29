package com.bogdan.chat.service;

import com.bogdan.chat.dao.impl.ChatRoomDaoImpl;
import com.bogdan.chat.dao.impl.ParticipantsDaoImpl;
import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.dto.UserInfoDTO;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import com.bogdan.chat.service.impl.ChatRoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private ChatRoomDaoImpl dao;
    @Mock
    private ParticipantsDaoImpl participantsDao;
    @Mock
    private Mapper<ChatRoom, ChatRoomDTO> mapper;
    @InjectMocks
    private ChatRoomServiceImpl service;

    private ChatRoom chatRoom;
    private ChatRoom chatRoom2;
    private ChatRoomDTO dto;
    private ChatRoomDTO dto2;

    @BeforeEach
    void setUp() {
        chatRoom = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Chat")
                .setDescription("test chat")
                .setCreateBy(new User())
                .build();
        chatRoom2 = new ChatRoom.ChatRoomBuilder()
                .setRoomId(2L)
                .setName("Chat2")
                .setDescription("test chat2")
                .setCreateBy(new User())
                .build();
        dto = new ChatRoomDTO.ChatRoomDTOBuilder()
                .setRoomId(1L)
                .setName("Chat")
                .setDescription("test chat")
                .setCreateBy(new UserInfoDTO())
                .build();
        dto2 = new ChatRoomDTO.ChatRoomDTOBuilder()
                .setRoomId(2L)
                .setName("Chat2")
                .setDescription("test chat2")
                .setCreateBy(new UserInfoDTO())
                .build();
    }

    @Test
    void testFindChatById_ShouldBeReturnChatDTO() {
        Set<User> participants = new HashSet<>();
        participants.add(new User());
        Optional<ChatRoom> optional = Optional.of(chatRoom);
        when(dao.findById(anyLong())).thenReturn(optional);
        Optional<ChatRoom> byId = dao.findById(1L);
        assertTrue(byId.isPresent());

        when(participantsDao.getParticipants(anyLong())).thenReturn(participants);
        when(mapper.map(chatRoom)).thenReturn(dto);

        Optional<ChatRoomDTO> chat = service.findById(1L);
        assertTrue(chat.isPresent());
        assertEquals(dto, chat.get());
    }

    @Test
    void testSave_shouldSaveChatAndReturnSavedChat() {
        when(mapper.create(dto)).thenReturn(chatRoom);
        when(dao.save(chatRoom)).thenReturn(chatRoom);
        when(mapper.map(chatRoom)).thenReturn(dto);

        ChatRoomDTO save = service.save(dto);

        assertNotNull(save);
        verify(mapper, times(1)).create(dto);
        verify(dao, times(1)).save(chatRoom);
        verify(mapper, times(1)).map(chatRoom);

        assertEquals(dto, save);
    }

    @Test
    void testUpdate_shouldUpdateChatAndReturnUpdatedChat() {
        when(mapper.create(dto)).thenReturn(chatRoom);
        when(dao.update(chatRoom)).thenReturn(chatRoom);
        when(mapper.map(chatRoom)).thenReturn(dto);

        ChatRoomDTO upd = service.update(dto);

        assertNotNull(upd);
        verify(mapper, times(1)).create(dto);
        verify(dao, times(1)).update(chatRoom);
        verify(mapper, times(1)).map(chatRoom);

        assertEquals(dto, upd);
    }

    @Test
    void testDeleteChat_shouldDeleteChatAndReturnDeletedChat() {
        when(mapper.create(dto)).thenReturn(chatRoom);
        when(dao.delete(chatRoom)).thenReturn(chatRoom);
        when(mapper.map(chatRoom)).thenReturn(dto);

        ChatRoomDTO delete = service.delete(dto);

        assertNotNull(delete);
        verify(mapper, times(1)).create(dto);
        verify(dao, times(1)).delete(chatRoom);
        verify(mapper, times(1)).map(chatRoom);

        assertEquals(dto, delete);
    }

    @Test
    void testGetAllChats_shouldReturnAllChats() {
        Set<User> participants = new HashSet<>();

        List<ChatRoom> chats = new ArrayList<>();
        chats.add(chatRoom);
        chats.add(chatRoom2);
        List<ChatRoomDTO> usersDTOtestList = new ArrayList<>();
        usersDTOtestList.add(dto);
        usersDTOtestList.add(dto2);

        when(dao.getAll()).thenReturn(chats);
        for (ChatRoom chat : chats) {
            when(participantsDao.getParticipants(chat.getRoomId())).thenReturn(participants);
        }
        when(mapper.map(chatRoom)).thenReturn(dto);
        when(mapper.map(chatRoom2)).thenReturn(dto2);

        List<ChatRoomDTO> all = service.getAll();

        assertEquals(usersDTOtestList.size(), all.size());
        verify(dao, times(1)).getAll();
        verify(participantsDao, times(2)).getParticipants(anyLong());
        verify(mapper, times(2)).map(any());

        assertEquals(usersDTOtestList, all);
    }
}
