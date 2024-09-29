package com.bogdan.chat.dao.impl;

import com.bogdan.chat.common.util.PropertiesHandlerUtil;
import com.bogdan.chat.dao.ChatRoomDao;
import com.bogdan.chat.dao.db.DatabaseConnectionManager;
import com.bogdan.chat.dao.mapper.ChatRomDBRowMapper;
import com.bogdan.chat.dao.mapper.DBRowMapper;
import com.bogdan.chat.model.ChatRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ChatRoomDaoImpl implements ChatRoomDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomDaoImpl.class);
    private final DBRowMapper<ChatRoom> mapper;
    private DataSource dataSource;

    public ChatRoomDaoImpl(){
        this. mapper = new ChatRomDBRowMapper();
    }

    public ChatRoomDaoImpl(DataSource dataSource){
        this.mapper = new ChatRomDBRowMapper();
        this.dataSource = dataSource;
    }

    public void setDataSource(String typeDB){
        DatabaseConnectionManager.setType(typeDB);
        this.dataSource = DatabaseConnectionManager.getDataSource();
    }

    @Override
    public Optional<ChatRoom> findById(Long id){
        Optional<ChatRoom> chatRoom = Optional.empty();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("getChatById"))){

            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                ChatRoom chat = mapper.rowMap(resultSet);
                chatRoom = Optional.of(chat);
            }

        } catch (SQLException ex){
            LOGGER.debug(ex.getMessage());
        }
        return chatRoom;
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom){
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            saveChat(chatRoom,connection);
            addAdminToChat(chatRoom,connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return chatRoom;
    }

    private int saveChat(ChatRoom chatRoom, Connection connection){
        int result = -1;
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("saveChat"))){
            setValueToDataBase(chatRoom,preparedStatement);
            result = preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
        return result;
    }

    private int addAdminToChat(ChatRoom chatRoom, Connection connection) {
        int result = -1;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(PropertiesHandlerUtil.getValue("saveParticipants"))) {
            preparedStatement.setLong(1, chatRoom.getCreateBy().getUserId());
            preparedStatement.setLong(2, chatRoom.getRoomId());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
        return result;
    }

    private void setValueToDataBase(ChatRoom chatRoom, PreparedStatement statement) throws SQLException {
        statement.setString(1,chatRoom.getName());
        statement.setString(2,chatRoom.getDescription());
        statement.setLong(3,chatRoom.getCreateBy().getUserId());
    }
    private void tryRollback(Connection connection) {
        try{
            connection.rollback();
        } catch (SQLException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @Override
    public ChatRoom update(ChatRoom chatRoom){
        try(Connection connection = dataSource.getConnection();){
            connection.setAutoCommit(false);
            updateChat(chatRoom,connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return chatRoom;
    }

    private void updateChat(ChatRoom chatRoom, Connection connection) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("updateChat"))){
            setValueToDataBase(chatRoom,preparedStatement);
            preparedStatement.setLong(4,chatRoom.getRoomId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }


    @Override
    public ChatRoom delete(ChatRoom chatRoom) {
        try(Connection connection = dataSource.getConnection();){
            connection.setAutoCommit(false);
            deleteChat(chatRoom,connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return chatRoom;
    }

    private void deleteChat(ChatRoom chatRoom,Connection connection) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("deleteChat"))){

            preparedStatement.setLong(1,chatRoom.getRoomId());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }


    @Override
    public List<ChatRoom> getAll() {
        List<ChatRoom> chats = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("getAllChats"))){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ChatRoom chatRoom = mapper.rowMap(resultSet);
                chats.add(chatRoom);
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        return chats;
    }
}
