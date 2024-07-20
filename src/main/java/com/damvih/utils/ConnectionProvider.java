package com.damvih.utils;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ConnectionProvider {

    private static final HikariDataSource INSTANCE = new HikariDataSource();
    private static final String FILE_NAME = "/db.properties";
    private static final String DRIVER = "db.driver.name";
    private static final String URL = "db.url";
    private static final String DATABASE_NAME = "db.name";

    static {
        try (InputStream inputStream = ConnectionProvider.class.getResourceAsStream(FILE_NAME)) {
            Properties properties = new Properties();
            properties.load(inputStream);

            String driver = properties.getProperty(DRIVER);
            String url = properties.getProperty(URL);
            String databaseName = properties.getProperty(DATABASE_NAME);
            String path = Objects.requireNonNull(ConnectionProvider.class.getResource(databaseName)).getPath();

            INSTANCE.setDriverClassName(driver);
            INSTANCE.setJdbcUrl(url + path);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ConnectionProvider() {

    }

    public static DataSource getInstance() {
        return INSTANCE;
    }

}
