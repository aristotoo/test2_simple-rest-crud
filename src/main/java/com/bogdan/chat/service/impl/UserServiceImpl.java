package com.bogdan.chat.service.impl;

import com.bogdan.chat.dao.UserDao;
import com.bogdan.chat.dao.factory.DaoProviderFactory;
import com.bogdan.chat.dto.UserDTO;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.mapping.UserMapper;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import com.bogdan.chat.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private DaoProviderFactory daoFactory;
    private UserDao dao;
    private Mapper<User,UserDTO> mapper;
    public UserServiceImpl(){
        this.daoFactory = new DaoProviderFactory();
        this.dao = daoFactory.getUserDao();
        this.mapper = new UserMapper();
    }

    @Override
    public Optional<UserDTO> findById(long id) {
        Optional<User> byId = dao.findById(id);
        Optional<UserDTO> result = Optional.empty();
        if(byId.isPresent()){
            User user = byId.get();
            Set<ChatRoom> chatRooms = dao.getCreatedChats(user.getUserId());
            Set<ChatRoom> subscriptions = dao.getSubscriptions(user.getUserId());
            user.setCreatedChats(chatRooms);
            user.setChatRooms(subscriptions);
            result = Optional.of(mapper.map(user));
        }
        return result;
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        User toSave = mapper.create(userDTO);
        User saved = dao.save(toSave);
        return mapper.map(saved);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        User toUpd = mapper.create(userDTO);
        User updated = dao.update(toUpd);
        return mapper.map(updated);
    }

    @Override
    public UserDTO delete(UserDTO userDTO) {
        User toDel = mapper.create(userDTO);
        User deleted = dao.delete(toDel);
        return mapper.map(deleted);
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = dao.getAll();
        if(!users.isEmpty()) {
            for (User user : users) {
                user.setCreatedChats(dao.getCreatedChats(user.getUserId()));
                user.setChatRooms(dao.getSubscriptions(user.getUserId()));
            }
        }
        return users.stream()
                .map(mapper::map)
                .toList();
    }
}
