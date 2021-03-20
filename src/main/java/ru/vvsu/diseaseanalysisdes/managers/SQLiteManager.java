package ru.vvsu.diseaseanalysisdes.managers;

import ru.vvsu.diseaseanalysisdes.Settings;
import ru.vvsu.diseaseanalysisdes.helpers.FileHelper;

import java.io.File;
import java.sql.*;

public class SQLiteManager {

    private Connection connection;

    public SQLiteManager() {
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

    public int executeQuery(String sql, Object... args) {
        try(PreparedStatement ps = getConnection().prepareStatement(sql)) {
            for(int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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
