package ru.vvsu.diseaseanalysisdes.controllers;
import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.vvsu.diseaseanalysisdes.Settings;
import ru.vvsu.diseaseanalysisdes.managers.SQLiteManager;
import ru.vvsu.diseaseanalysisdes.models.Human;
import ru.vvsu.diseaseanalysisdes.models.SaveUserData;


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
        /*createFileSave(new Human("a","w","ac"));
        Human human = (Human) openFileSave();
        System.out.println(human.toString());*/
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

    public void createFileSave(Serializable userData) {
        FileChooser configFileChooser = new FileChooser();
        configFileChooser.setTitle("Сохранить");
        SaveUserData saveUserData = new SaveUserData();
        saveUserData.createDir(Paths.get(Settings.DEFAULT_SAVE_PATH));
        configFileChooser.setInitialDirectory(
                new File(Settings.DEFAULT_SAVE_PATH)
        );
        configFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.ser")
        );
        File configFile = configFileChooser.showSaveDialog(null);
        if(configFile!=null){
            System.out.println(configFile.getAbsolutePath());
            saveUserData.exportSave(userData, configFile.getAbsolutePath());
            System.out.println(configFile.getParent());
        }
    }
    public Object openFileSave() {
        FileChooser configFileChooser = new FileChooser();
        configFileChooser.setTitle("Выбор сохранения");
        SaveUserData saveUserData = new SaveUserData();
        saveUserData.createDir(Paths.get(Settings.DEFAULT_SAVE_PATH));
        configFileChooser.setInitialDirectory(
                new File(Settings.DEFAULT_SAVE_PATH)
        );
        configFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.ser"),
                new FileChooser.ExtensionFilter("ALL", "*.*")
        );
        File configFile = configFileChooser.showOpenDialog(null);
        if(configFile!=null){
            System.out.println(configFile.getAbsolutePath());
            System.out.println(configFile.getParent());
            return saveUserData.importSave(configFile.getAbsolutePath());
        }
        return null;
    }
}