package com.bogdan.chat.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DBRowMapper<T> {
    T rowMap(ResultSet resultSet) throws SQLException;
}
