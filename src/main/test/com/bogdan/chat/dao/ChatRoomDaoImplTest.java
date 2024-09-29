package com.bogdan.chat.dao;

import com.bogdan.chat.dao.impl.ChatRoomDaoImpl;
import com.bogdan.chat.dao.impl.UserDaoImpl;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class ChatRoomDaoImplTest extends BaseDaoTest {
    private static ChatRoomDao chatDao;
    private static UserDao userDao;

    @BeforeAll
    static void setup() throws SQLException {
        chatDao = BaseDaoTest.getChatRoomDao();
        userDao = BaseDaoTest.getUserDao();
    }

    @BeforeEach
    void refresh(){
        resetSequence("service.chat_users","userid");
        resetSequence("service.chat_room","roomid");
    }

    @Test
    void testFindById() {
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();

        ChatRoom save = chatDao.save(chat);

        Optional<ChatRoom> foundUser = chatDao.findById(save.getRoomId());
        assertTrue(foundUser.isPresent());
        assertEquals(save, foundUser.get());
    }

    @Test
    void testSaveChat(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();
        ChatRoom save = chatDao.save(chat);
        assertEquals(chat,save);
    }

    @Test
    void testUpdateChatRoom(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();
        chatDao.save(chat);
        ChatRoom upd = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test2")
                .setDescription("Test2 test2")
                .setCreateBy(user)
                .build();
        ChatRoom updated = chatDao.update(upd);
        Optional<ChatRoom> byId = chatDao.findById(updated.getRoomId());
        assertTrue(byId.isPresent());
        assertEquals(upd,byId.get());
    }

    @Test
    void testDeleteChatRoom(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();
        ChatRoom save = chatDao.save(chat);
        Optional<ChatRoom> byId = chatDao.findById(save.getRoomId());
        assertTrue(byId.isPresent());
        ChatRoom delete = chatDao.delete(byId.get());
        Optional<ChatRoom> del = chatDao.findById(delete.getRoomId());
        assertTrue(del.isEmpty());
    }

    @Test
    void testGetAllChatRoom(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        User user2 = new User.UserBuilder()
                .setUserId(2L)
                .setName("Alex")
                .setEmail("alex.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user2);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();
        chatDao.save(chat);
        ChatRoom chat2 = new ChatRoom.ChatRoomBuilder()
                .setRoomId(2L)
                .setName("Test2")
                .setDescription("Test2 test2")
                .setCreateBy(user2)
                .build();
        chatDao.save(chat2);
        List<ChatRoom> test = new ArrayList<>(2);
        test.add(chat);
        test.add(chat2);
        List<ChatRoom> all = chatDao.getAll();
        assertEquals(test.size(),all.size());
        assertEquals(test,all);
    }
}
