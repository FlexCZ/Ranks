package com.codenameflip.valiconrankaddon.sql;

import java.sql.Connection;
import java.sql.SQLException;

/***************************************************************************************************
 * This class was created by CodenameFlip on 1/29/16 under the package net.valiconnetwork.anticheat.sql
 ***************************************************************************************************/
public class DatabasePipeline {

    MySQL sql;
    Connection connection;

    public DatabasePipeline(String host, String port, String data, String user, String pass) throws SQLException, ClassNotFoundException {
        this.sql = new MySQL(host, port, data, user, pass);
        connection = sql.openConnection();
    }

    public MySQL getSql() {
        return sql;
    }

    public Connection getConnection() {
        return connection;
    }

}
