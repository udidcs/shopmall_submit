package com.example.demo;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    static HikariDataSource ds;

    static {
        ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3307/udidc");
        ds.setUsername("root");
        ds.setPassword("1234");
        ds.setMaximumPoolSize(15);
    }
    public static Connection getConnection() {

//        try {
//            Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/udidc?characterEncoding=utf8&autoReconnect=true", "root", "1234");
//            return connection1;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        try {
            Connection connection = ds.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
