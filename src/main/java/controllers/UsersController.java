package controllers;
import java.net.URL;
import java.util.ResourceBundle;

import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import models.User;


public class UsersController {
    private final UserDAO userDAO;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Pane pane;
    @FXML private ListView<String> listNames;

    private ObservableList<String> names;

    @FXML
    void initialize() {
        //assert pane != null : "fx:id=\"textField\" was not injected: check your FXML file 'sample.fxml'.";
        //assert listNames != null : "fx:id=\"listNames\" was not injected: check your FXML file 'sample.fxml'.";
    }

    public UsersController(){
        this.userDAO = new UserDAO();
        names = FXCollections.observableArrayList();


    }

    public void onClick(MouseEvent mouseEvent) {
        int count = 0;
        for(User element:userDAO.showAll()){
            if(!names.contains(element.getName())) {
                names.add(element.getName());
                ++count;
            }
        }
        if (count!=0) {
            ObservableList<String> additionNames = FXCollections.observableArrayList(names.subList(names.size()-count, names.size()));
            listNames.setItems(additionNames);
        }
    }
}
