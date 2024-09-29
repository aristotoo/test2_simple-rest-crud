package com.bogdan.chat.dao;

import com.bogdan.chat.dao.db.DatabaseConnectionManager;
import com.bogdan.chat.dao.impl.ChatRoomDaoImpl;
import com.bogdan.chat.dao.impl.ParticipantsDaoImpl;
import com.bogdan.chat.dao.impl.UserDaoImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class BaseDaoTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.1")
            .withDatabaseName("aston_crud_task")
            .withUsername("testuser")
            .withReuse(true)
            .withPassword("admin")
            .withInitScript("db/create_tables.sql");
    private static DataSource dataSource;

    private static UserDao userDao;
    private static ChatRoomDao chatRoomDao;
    private static ParticipantsDao participantsDao;

    public static UserDao getUserDao() {
        return userDao;
    }

    public static ChatRoomDao getChatRoomDao() {
        return chatRoomDao;
    }

    public static ParticipantsDao getParticipantsDao() {
        return participantsDao;
    }

    @BeforeAll
    static void setupDatabase() {
        postgreSQLContainer.start();
        String host = postgreSQLContainer.getHost();
        var jdbcContainer = (JdbcDatabaseContainer<?>)postgreSQLContainer;
        DatabaseConnectionManager.setUrl(jdbcContainer.getJdbcUrl());
        DatabaseConnectionManager.setType("test");
        DatabaseConnectionManager.createTestConfig();
        dataSource = DatabaseConnectionManager.getDataSource();
        userDao = new UserDaoImpl(dataSource);
        chatRoomDao = new ChatRoomDaoImpl(dataSource);
        participantsDao = new ParticipantsDaoImpl(dataSource);
    }

        @AfterAll
    static void teardownDatabase() {
        clearDatabase();
        DatabaseConnectionManager.close();
        postgreSQLContainer.stop();
    }

    protected static void resetSequence(String tableName, String columnName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("SELECT pg_get_serial_sequence(?, ?) AS sequence_name")){
                statement.setString(1,tableName);
                statement.setString(2,columnName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String sequenceName = resultSet.getString("sequence_name");
                resetSequence(connection, sequenceName);
            } else {
                throw new IllegalStateException("Sequence not found for table " + tableName + ", column " + columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset sequence for table " + tableName + ", column " + columnName, e);
        }
    }

    private static void resetSequence(Connection connection, String sequenceName) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT setval(?, 1, false)")) {
            statement.setString(1,sequenceName);
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset sequence: " + sequenceName, e);
        }
    }
    private static void clearDatabase() {
        List<String> tablesToTruncate = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            // Получить список таблиц, которые нужно очистить
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'service'");
                while (resultSet.next()) {
                    tablesToTruncate.add(resultSet.getString("table_name"));
                }
            }
            // Очистить каждую таблицу
            try (Statement statement = connection.createStatement()) {
                for (String tableName : tablesToTruncate) {
                    statement.executeUpdate("TRUNCATE TABLE service." + tableName + " CASCADE;");
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear database", e);
        }
    }
}
