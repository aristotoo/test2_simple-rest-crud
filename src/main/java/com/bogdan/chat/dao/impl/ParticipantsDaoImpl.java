package com.bogdan.chat.dao.impl;

import com.bogdan.chat.common.util.PropertiesHandlerUtil;
import com.bogdan.chat.dao.ParticipantsDao;
import com.bogdan.chat.dao.db.DatabaseConnectionManager;
import com.bogdan.chat.dao.mapper.DBRowMapper;
import com.bogdan.chat.dao.mapper.UserDBRowMapper;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class ParticipantsDaoImpl implements ParticipantsDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsDaoImpl.class);
    private final DBRowMapper<User> userMapper;
    private DataSource dataSource;

    public ParticipantsDaoImpl(){
        this.userMapper = new UserDBRowMapper();
    }

    public ParticipantsDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
        this.userMapper = new UserDBRowMapper();
    }

    public void setDataSource(String typeDB){
        DatabaseConnectionManager.setType(typeDB);
        this.dataSource = DatabaseConnectionManager.getDataSource();
    }
    @Override
    public Set<User> getParticipants(long id) {
        Set<User> participants = new LinkedHashSet<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(PropertiesHandlerUtil.getValue("getParticipants"))) {

            statement.setLong(1, id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                participants.add(userMapper.rowMap(set));
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        return participants;
    }

    @Override
    public Participation save(Participation participation) {
        try (Connection connection = dataSource.getConnection()) {
            saveParticipants(connection, participation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participation;
    }

    private int saveParticipants(Connection connection, Participation participant) {
        int result = -1;
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(PropertiesHandlerUtil.getValue("saveParticipants"))) {

            preparedStatement.setLong(1, participant.getUser().getUserId());
            preparedStatement.setLong(2, participant.getChatRoom().getRoomId());
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
        return result;
    }

    private void tryRollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @Override
    public Participation delete(Participation participant) {
        try (Connection connection = dataSource.getConnection()) {
            deleteParticipants(participant, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participant;
    }

    private void deleteParticipants(Participation participation, Connection connection) {
        try (PreparedStatement stmt =
                     connection.prepareStatement(PropertiesHandlerUtil.getValue("deleteParticipants"))) {
            stmt.setLong(1, participation.getChatRoom().getRoomId());
            stmt.setLong(2,participation.getUser().getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            tryRollback(connection);
        }
    }

    @Override
    public Participation deleteAll(Participation participation){
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt =
                     connection.prepareStatement(PropertiesHandlerUtil.getValue("deleteAllParticipants"))) {
            stmt.setLong(1, participation.getChatRoom().getRoomId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        return participation;
    }
}