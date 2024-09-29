package com.bogdan.chat.dao;

import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class ParticipantsDaoImplTest extends BaseDaoTest {
    private static ParticipantsDao participantsDao;
    private static ChatRoomDao chatDao;
    private static UserDao userDao;

    @BeforeAll
    static void setup() throws SQLException {
        participantsDao = BaseDaoTest.getParticipantsDao();
        chatDao = BaseDaoTest.getChatRoomDao();
        userDao = BaseDaoTest.getUserDao();
    }

    private User user;
    private ChatRoom chat;

    @BeforeEach
    void init() {
        user = new User.UserBuilder()
                .setUserId(1L)
                .setName("johndoe")
                .setEmail("john.doe@example.com")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        chat = new ChatRoom.ChatRoomBuilder()
                .setRoomId(1L)
                .setName("Test")
                .setDescription("Test test")
                .setCreateBy(user)
                .build();
        chatDao.save(chat);
    }

    @Test
    void testSave() {
        User user = new User.UserBuilder()
                .setUserId(2L)
                .setName("testuser")
                .setEmail("testmail")
                .setRole("ADMIN")
                .build();
        userDao.save(user);
        Participation participation = new Participation.ParticipationBuilder()
                .setUser(user)
                .setChatRoom(chat)
                .build();

        Participation save = participantsDao.save(participation);
        assertEquals(participation, save);
        participantsDao.delete(save);
    }

    @Test
    void testGetParticipants() {
        Set<User> participants = participantsDao.getParticipants(user.getUserId());
        assertFalse(participants.isEmpty());
        assertTrue(participants.contains(user));
    }

    @Test
    void testDelete() {
        Participation participation = new Participation.ParticipationBuilder()
                .setUser(user)
                .setChatRoom(chat)
                .build();
        Participation delete = participantsDao.delete(participation);
        Set<User> participants = participantsDao.getParticipants(delete.getChatRoom().getRoomId());
        assertFalse(participants.contains(delete.getUser()));
    }

    @Test
    void testDeleteAll() {
        User user2 = new User.UserBuilder().setUserId(2L).setName("testuser").setEmail("testmail").setRole("ADMIN")
                .build();
        userDao.save(user2);
        Participation participation2 = new Participation.ParticipationBuilder().setUser(user2).setChatRoom(chat)
                .build();
        participantsDao.save(participation2);
        Set<User> participants = participantsDao.getParticipants(chat.getRoomId());
        assertEquals(2, participants.size());
        participantsDao.deleteAll(participation2);
        Set<User> participants1 = participantsDao.getParticipants(chat.getRoomId());
        assertTrue(participants1.isEmpty());
    }
}
