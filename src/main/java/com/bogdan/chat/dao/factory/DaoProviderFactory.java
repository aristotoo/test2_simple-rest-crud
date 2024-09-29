package com.bogdan.chat.dao.factory;

import com.bogdan.chat.dao.impl.ChatRoomDaoImpl;
import com.bogdan.chat.dao.impl.ParticipantsDaoImpl;
import com.bogdan.chat.dao.impl.UserDaoImpl;

public class DaoProviderFactory {

    public ChatRoomDaoImpl getChatDao() {
        ChatRoomDaoImpl chatRoomDao = new ChatRoomDaoImpl();
        chatRoomDao.setDataSource("prod");
        return chatRoomDao;
    }

    public UserDaoImpl getUserDao(){
        UserDaoImpl dao = new UserDaoImpl();
        dao.setDataSource("prod");
        return dao;
    }

    public ParticipantsDaoImpl getParticipantDao(){
        ParticipantsDaoImpl dao = new ParticipantsDaoImpl();
        dao.setDataSource("prod");
        return dao;
    }
}
