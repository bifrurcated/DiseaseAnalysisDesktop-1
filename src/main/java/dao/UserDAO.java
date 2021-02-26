package dao;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private static Connection connection;
    private static final String PATH = "C:\\Users\\kerya\\Documents\\DiseaseAnalysisDesktop\\src\\main\\java\\db\\test_db";
    private static final String URI = "jdbc:sqlite:" + PATH ;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URI);
            System.out.println("Connected");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<User> showAll() {
        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM users;";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User user = new User();

                user.setName(resultSet.getString("name"));


                users.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }
}
