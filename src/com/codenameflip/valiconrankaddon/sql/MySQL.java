package com.codenameflip.valiconrankaddon.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/***************************************************************************************************
 * This class was created by CodenameFlip on 12/24/15 under the package net.minefury.core.mysql
 ***************************************************************************************************/
public class MySQL extends Database {
    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    public MySQL(String hostname, String port, String username,
                 String password) {
        this(hostname, port, null, username, password);
    }

    public MySQL(String hostname, String port, String database,
                 String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
    }

    @Override
    public Connection openConnection() throws SQLException,
            ClassNotFoundException {
        if (checkConnection()) {
            return connection;
        }

        String connectionURL = "jdbc:mysql://"
                + this.hostname + ":" + this.port;
        if (database != null) {
            connectionURL = connectionURL + "/" + this.database;
        }

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(connectionURL,
                this.user, this.password);
        return connection;
    }
}
