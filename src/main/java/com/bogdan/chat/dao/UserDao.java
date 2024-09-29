package com.bogdan.chat.dao;

import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import jakarta.servlet.ServletContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    Optional<User> findById(long id);
    User save(User user);
    User update(User user);
    User delete(User user);
    List<User> getAll();
    Set<ChatRoom> getCreatedChats(long id);
    Set<ChatRoom> getSubscriptions(long id);
}
