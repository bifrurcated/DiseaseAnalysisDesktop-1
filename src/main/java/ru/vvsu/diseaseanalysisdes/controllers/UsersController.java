package ru.vvsu.diseaseanalysisdes.controllers;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import ru.vvsu.diseaseanalysisdes.managers.SQLiteManager;


public class UsersController implements Initializable {
    private final SQLiteManager dataBase;

    @FXML private Pane pane;
    @FXML private ListView<String> listNames;

    private ObservableList<String> names;

    public UsersController(){
        dataBase = new SQLiteManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert pane != null : "fx:id=\"textField\" was not injected: check your FXML file 'sample.fxml'.";
        assert listNames != null : "fx:id=\"listNames\" was not injected: check your FXML file 'sample.fxml'.";
        names = FXCollections.observableArrayList();
        listNames.setItems(names);
    }

    public void onClick(MouseEvent mouseEvent) {
        try{
            ResultSet resultSet = dataBase.getResultSet("SELECT * FROM users;");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                if(!names.contains(name)){
                    names.add(name);
                }
            }
            resultSet.close();
            resultSet.getStatement().close();
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}