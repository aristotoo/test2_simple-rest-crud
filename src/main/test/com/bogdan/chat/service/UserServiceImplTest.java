package com.bogdan.chat.service;

import com.bogdan.chat.dao.impl.UserDaoImpl;
import com.bogdan.chat.dto.UserDTO;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import com.bogdan.chat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDaoImpl dao;
    @Mock
    private Mapper<User, UserDTO> mapper;
    @InjectMocks
    private UserServiceImpl service;
    private User user;
    private User user2;
    private UserDTO userDTO;
    private UserDTO user2DTO;

    @BeforeEach
    void setUp() {
        user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("johndoe@mail.ru")
                .setRole("admin")
                .setCreatedChats(new HashSet<>())
                .setChatRooms(new HashSet<>())
                .build();
        userDTO = new UserDTO.UserDTOBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("johndoe@mail.ru")
                .setRole("admin")
                .setCreatedChats(new HashSet<>())
                .setChatRooms(new HashSet<>())
                .build();
        user2 = new User.UserBuilder()
                .setUserId(2L)
                .setName("alekscross")
                .setEmail("alekscross@mail.ru")
                .setRole("user")
                .setCreatedChats(new HashSet<>())
                .setChatRooms(new HashSet<>())
                .build();
        user2DTO = new UserDTO.UserDTOBuilder()
                .setUserId(2L)
                .setName("alekscross")
                .setEmail("alekscross@mail.ru")
                .setRole("user")
                .setCreatedChats(new HashSet<>())
                .setChatRooms(new HashSet<>())
                .build();

    }

    @Test
    void testFindById_ShouldBeReturnUserDTO() {
        Set<ChatRoom> createdChats = new HashSet<>();
        Set<ChatRoom> subscriptions = new HashSet<>();
        user.setChatRooms(subscriptions);
        user.setCreatedChats(createdChats);

        Optional<User> optional = Optional.of(user);
        when(dao.findById(anyLong())).thenReturn(optional);
        Optional<User> byId1 = dao.findById(1L);
        assertTrue(byId1.isPresent());
        when(dao.getCreatedChats(anyLong())).thenReturn(createdChats);
        when(dao.getSubscriptions(anyLong())).thenReturn(subscriptions);
        when(mapper.map(optional.get())).thenReturn(userDTO);

        Optional<UserDTO> byId = service.findById(1L);
        assertTrue(byId.isPresent());
        assertEquals(userDTO, byId.get());
    }

    @Test
    void testSave_shouldSaveUserAndReturnSavedUser() {
        when(mapper.create(userDTO)).thenReturn(user);
        when(dao.save(user)).thenReturn(user);
        when(mapper.map(user)).thenReturn(userDTO);

        UserDTO save = service.save(userDTO);

        assertNotNull(save);
        verify(mapper, times(1)).create(userDTO);
        verify(dao, times(1)).save(user);
        verify(mapper, times(1)).map(user);

        assertEquals(userDTO, save);
    }

    @Test
    void testUpdate_shouldUpdateUserAndReturnUpdatedUser() {
        when(mapper.create(userDTO)).thenReturn(user);
        when(dao.update(user)).thenReturn(user);
        when(mapper.map(user)).thenReturn(userDTO);

        UserDTO upd = service.update(userDTO);

        assertNotNull(upd);
        verify(mapper, times(1)).create(userDTO);
        verify(dao, times(1)).update(user);
        verify(mapper, times(1)).map(user);

        assertEquals(userDTO, upd);
    }

    @Test
    void testDeleteUser_shouldDeleteUserAndReturnDeletedUser() {
        when(mapper.create(userDTO)).thenReturn(user);
        when(dao.delete(user)).thenReturn(user);
        when(mapper.map(user)).thenReturn(userDTO);

        UserDTO delete = service.delete(userDTO);

        assertNotNull(delete);
        verify(mapper, times(1)).create(userDTO);
        verify(dao, times(1)).delete(user);
        verify(mapper, times(1)).map(user);

        assertEquals(userDTO, delete);
    }

    @Test
    void testGetAllUser_shouldReturnAllUsers() {
        Set<ChatRoom> createdChats = new HashSet<>();
        Set<ChatRoom> subscriptions = new HashSet<>();
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        List<UserDTO> usersDTOtestList = new ArrayList<>();
        usersDTOtestList.add(userDTO);
        usersDTOtestList.add(user2DTO);

        when(dao.getAll()).thenReturn(users);
        for (User us : users) {
            when(dao.getCreatedChats(us.getUserId())).thenReturn(createdChats);
            when(dao.getSubscriptions(us.getUserId())).thenReturn(subscriptions);
        }
        when(mapper.map(user)).thenReturn(userDTO);
        when(mapper.map(user2)).thenReturn(user2DTO);

        List<UserDTO> all = service.getAll();

        assertEquals(usersDTOtestList.size(), all.size());
        verify(dao, times(2)).getCreatedChats(anyLong());
        verify(dao, times(2)).getSubscriptions(anyLong());
        verify(mapper, times(2)).map(any(User.class));

        assertEquals(usersDTOtestList, all);
    }
}

