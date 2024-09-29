package com.bogdan.chat.dao;

import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplTest extends BaseDaoTest {
    private static UserDao userDao;
    private static ChatRoomDao chatRoomDao;

    @BeforeAll
    static void setup() {
        userDao = BaseDaoTest.getUserDao();
        chatRoomDao = BaseDaoTest.getChatRoomDao();
    }

    @BeforeEach
    void refresh(){
        resetSequence("service.chat_users","userid");
    }

    @Test
    void testFindById() {
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        User save = userDao.save(user);
        Optional<User> foundUser = userDao.findById(save.getUserId());
        assertTrue(foundUser.isPresent());
        assertEquals(save, foundUser.get());
    }

    @Test
    void testSaveUser(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        User save = userDao.save(user);
        assertEquals(user,save);
        userDao.delete(save);
    }

    @Test
    void testGetSubscriptions() {
        User user = new User.UserBuilder().setUserId(1L).setName("johndoe").setEmail("john.doe@example.com")
                .setRole("ADMIN").build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder().setRoomId(1L).setName("Test").setDescription("Test test")
                .setCreateBy(user).build();
        chatRoomDao.save(chat);
        ChatRoom testResult = new ChatRoom.ChatRoomBuilder().setRoomId(1L).setName("Test").setDescription("Test test")
                .build();
        Set<ChatRoom> subscriptions = userDao.getSubscriptions(user.getUserId());
        ChatRoom next = subscriptions.iterator().next();
        assertEquals(testResult.getName(),next.getName());
        assertEquals(testResult.getDescription(),next.getDescription());
    }

    @Test
    void testGetCreatedChats() {
        User user = new User.UserBuilder().setUserId(1L).setName("johndoe").setEmail("john.doe@example.com")
                .setRole("ADMIN").build();
        userDao.save(user);
        ChatRoom chat = new ChatRoom.ChatRoomBuilder().setRoomId(1L).setName("Test").setDescription("Test test")
                .setCreateBy(user).build();
        ChatRoom save = chatRoomDao.save(chat);
        Set<ChatRoom> createdChats = userDao.getCreatedChats(user.getUserId());
        ChatRoom next = createdChats.iterator().next();
        assertEquals(save.getName(),next.getName());
        assertEquals(save.getDescription(),next.getDescription());
    }

    @Test
    void testUpdateUser(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("asdasf")
                .setEmail("jasassaae@example.com")
                .setRole("User")
                .build();
        userDao.save(user);
        User upd = new User.UserBuilder()
                .setUserId(1L)
                .setName("Test")
                .setEmail("test@example.com")
                .setRole("ADMIN")
                .build();
        User updated = userDao.update(upd);
        Optional<User> byId = userDao.findById(updated.getUserId());
        assertTrue(byId.isPresent());
        assertEquals(updated,byId.get());
        userDao.delete(updated);

    }

    @Test
    void testDeleteUser(){
        User user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        User save = userDao.save(user);
        Optional<User> byId = userDao.findById(save.getUserId());
        assertTrue(byId.isPresent());
        User delete = userDao.delete(byId.get());
        Optional<User> del = userDao.findById(delete.getUserId());
        assertTrue(del.isEmpty());
    }

    @Test
    void testGetAllUsers(){
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
        List<User> test = new ArrayList<>(2);
        test.add(user);
        test.add(user2);
        List<User> all = userDao.getAll();
        assertEquals(test.size(),all.size());
        assertEquals(test,all);
        userDao.delete(user);
        userDao.delete(user2);
    }
}
