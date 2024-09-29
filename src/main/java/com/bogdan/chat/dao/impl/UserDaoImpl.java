package com.bogdan.chat.dao.impl;

import com.bogdan.chat.common.util.PropertiesHandlerUtil;
import com.bogdan.chat.dao.UserDao;
import com.bogdan.chat.dao.db.DatabaseConnectionManager;
import com.bogdan.chat.dao.mapper.DBRowMapper;
import com.bogdan.chat.dao.mapper.UserDBRowMapper;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);
    private final DBRowMapper<User> mapper;
    private DataSource dataSource;

    public UserDaoImpl(){
        this.mapper = new UserDBRowMapper();
    }

    public UserDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
        this.mapper = new UserDBRowMapper();
    }

    public void setDataSource(String typeDB){
        DatabaseConnectionManager.setType(typeDB);
        DatabaseConnectionManager.createProdConfig();
        this.dataSource = DatabaseConnectionManager.getDataSource();
    }

    @Override
    public Optional<User> findById(long id) {
        Optional<User> user = Optional.empty();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("getUserById"))){

            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                User temp = mapper.rowMap(resultSet);
                user = Optional.of(temp);
            }

        } catch (SQLException ex){
            LOGGER.debug(ex.getMessage());
        }
        return user;
    }

    @Override
    public Set<ChatRoom> getCreatedChats(long id){
        return getChats(id,PropertiesHandlerUtil.getValue("getCreatedChats"));
    }

    @Override
    public Set<ChatRoom> getSubscriptions(long id) {
        return getChats(id, PropertiesHandlerUtil.getValue("getSubscriptions"));
    }

    private Set<ChatRoom> getChats(long id, String query){
        Set<ChatRoom> chats = new HashSet<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                ChatRoom chatRoom = new ChatRoom.ChatRoomBuilder()
                        .setRoomId(resultSet.getLong("roomid"))
                        .setName(resultSet.getString("chat_name"))
                        .setDescription(resultSet.getString("chat_description"))
                        .build();
                chats.add(chatRoom);
            }


        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return chats;
    }

    @Override
    public User save(User user) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            saveUser(user,connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return user;
    }

    private void saveUser(User user,Connection connection){
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("saveUser"))){

            setValueToDataBase(user,preparedStatement);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }

    private void setValueToDataBase(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getRole());
    }

    private void tryRollback(Connection connection) {
        try{
            connection.rollback();
        } catch (SQLException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @Override
    public User update(User user) {
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            updateUser(user,connection);
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return user;
    }

    private void updateUser(User user, Connection connection) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("updateUser"))){

            setValueToDataBase(user,preparedStatement);
            preparedStatement.setLong(4,user.getUserId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }


    @Override
    public User delete(User user) {
        try(Connection connection = dataSource.getConnection()){
            deleteUser(user,connection);
            connection.setAutoCommit(true);
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
        }
        return user;
    }

    private void deleteUser(User user,Connection connection) {
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("deleteUser"))){
            connection.setAutoCommit(false);
            preparedStatement.setLong(1,user.getUserId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(PropertiesHandlerUtil.getValue("getAllUsers"))){

            ResultSet set = preparedStatement.executeQuery();
            while(set.next()){
                users.add(mapper.rowMap(set));
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        return users;
    }
}
