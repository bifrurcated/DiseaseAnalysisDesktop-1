package dao;

import java.io.File;
import java.sql.*;

public class DatabaseRepository {
    private Connection connection;
    /**
     * Я думаю надо будет поместить БД не внутрь проекта, чтобы была возможность выбирать её.
     * Ещё надо будет создать config.ini, где будет записываться путь до БД.
     * Тогда можно сделать так, чтобы этот путь задавался и в самом приложении через интерфейс.
     */
    private final String PATH = new File("").getAbsolutePath()+"\\src\\main\\java\\db\\test_db";
    private final String URI = "jdbc:sqlite:" + PATH;

    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URI);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    public boolean insert(String sqlQuery){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);
            statement.close();
            System.out.println("Запись добавлена");
            return true;
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * Так как select может возвращать разные типы данных в зависимости от запроса,
     * то решено было сделать так для более гибкого использования.
     * @param sqlQuery передаём строку запроса SELECT
     * @return resultSet возвращает результат запроса. Значение может быть null, поэтому стоит делать проверку.
     */
    public ResultSet select(String sqlQuery){
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return resultSet;
    }
}