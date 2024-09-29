package com.bogdan.chat.dao.mapper;

import com.bogdan.chat.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBRowMapper implements DBRowMapper<User> {

    @Override
    public User rowMap(ResultSet resultSet) throws SQLException {
        return new User.UserBuilder()
                .setUserId(resultSet.getLong("userid"))
                .setName(resultSet.getString("username"))
                .setEmail(resultSet.getString("email"))
                .setRole(resultSet.getString("role"))
                .build();
    }
}
