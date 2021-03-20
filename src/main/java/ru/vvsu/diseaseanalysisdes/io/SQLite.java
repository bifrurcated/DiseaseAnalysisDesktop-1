package ru.vvsu.diseaseanalysisdes.io;

import ru.vvsu.diseaseanalysisdes.Settings;
import ru.vvsu.diseaseanalysisdes.helpers.FileHelper;

import java.io.File;
import java.sql.*;

public class SQLite {

    private Connection connection;

    public SQLite() {
        connect();
    }

    public Connection getConnection() throws SQLException {
        if(connection == null) {
            connect();
        } else if(!connection.isValid(3)) {
            connect();
        }
        return connection;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + FileHelper.getDir() + File.separator + Settings.DB_FILE_NAME);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean executeQuery(String sql, Object... args) {
        try(PreparedStatement ps = getConnection().prepareStatement(sql)) {
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet getResultSet(String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(sql);
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean close() {
        if(connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
