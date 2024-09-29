package com.bogdan.chat.dao.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class DatabaseConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnectionManager.class);
    private static final Map<String,HikariConfig> configMap = new HashMap<>();
    private static Properties properties;
    private static String type;
    private static String url;

    public static void setUrl(String url) {
        DatabaseConnectionManager.url = url;
    }

    public static String getType() {
        return type;
    }
    public static void setType(String type) {
        DatabaseConnectionManager.type = type;
    }
    public static void createProdConfig(){
        properties = getProperties("/app-config.properties");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("datasource.url"));
        config.setUsername(properties.getProperty("datasource.username"));
        config.setPassword(properties.getProperty("datasource.password"));
        config.setDriverClassName(properties.getProperty("datasource.driverClassName"));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("datasource.maximumPoolSize")));
        configMap.put("prod", config);
    }

    public static void createTestConfig(){
        properties = getProperties("/test-config.properties");
        HikariConfig forTestConfig = new HikariConfig();
        forTestConfig.setJdbcUrl(url);
        forTestConfig.setUsername(properties.getProperty("datasource.t_username"));
        forTestConfig.setPassword(properties.getProperty("datasource.t_password"));
        forTestConfig.setDriverClassName(properties.getProperty("datasource.t_driverClassName"));
        forTestConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("datasource.t_maximumPoolSize")));
        configMap.put("test", forTestConfig);
    }

    private DatabaseConnectionManager(){}

    public static HikariDataSource getDataSource() {
        return DataSourceHolder.dataSource;
    }
    public static void close(){
        DataSourceHolder.dataSource.close();
    }

    private static Properties getProperties(String resourcePath) {
        Properties properties = new Properties();
        try(InputStream is = DatabaseConnectionManager.class.getResourceAsStream(resourcePath)){
            properties.load(is);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
        return properties;
    }

    private static final class DataSourceHolder {
        private static final HikariConfig config = configMap.get(getType());
        private static final HikariDataSource dataSource = new HikariDataSource(config);
    }
}