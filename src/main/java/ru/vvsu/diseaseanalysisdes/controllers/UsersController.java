package ru.vvsu.diseaseanalysisdes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import ru.vvsu.diseaseanalysisdes.helpers.FileHelper;
import ru.vvsu.diseaseanalysisdes.managers.SQLiteManager;
import ru.vvsu.diseaseanalysisdes.models.Algo;
import ru.vvsu.diseaseanalysisdes.models.Human;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class UsersController implements Initializable {
    private final SQLiteManager dataBase;

    @FXML private Pane pane;
    @FXML private ListView<String> listNames;

    private ObservableList<String> names;
    private List<Human> humanList;
    private Field field;

    public UsersController(){
        dataBase = new SQLiteManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert pane != null : "fx:id=\"textField\" was not injected: check your FXML file 'sample.fxml'.";
        assert listNames != null : "fx:id=\"listNames\" was not injected: check your FXML file 'sample.fxml'.";
        names = FXCollections.observableArrayList();
        humanList = new ArrayList<>();
        listNames.setItems(names);
    }

    public void onClick(MouseEvent mouseEvent) {
        /*createFileSave(new Human("a","w","ac"));
        Optional<Human> human= Optional.ofNullable((Human) openFileSave());
        System.out.println(human.toString());*/
        Human user = new Human();
        user.height = "150";
        user.age = "28";
        user.weight = "55";
        user.sex = "1";
        user.sleep = "8";
        Algo algo = new Algo(15); // задаём процент выборки
        StringBuilder sb = algo.getQuerySelections(user);

        System.out.println(sb);
        try{
            // И в запросе используем
            ResultSet resultSet = dataBase.getResultSet(
                    "SELECT * FROM med_card where "+sb+";");
            while (resultSet.next()) {
                Human human = new Human();
                Arrays.stream(human.getClass().getFields()).forEach(val -> {
                    try {
                        val.set(human,resultSet.getString(val.getName()));
                    } catch (IllegalAccessException | SQLException e) {
                        e.printStackTrace();
                    }
                }); //каждой public переменной класса присваиваем значение из таблицы
                humanList.add(human);

                if(!names.contains(human.height)){
                    names.add(human.height);
                }
            }
            System.out.println(names);
            resultSet.close();
            resultSet.getStatement().close();

        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void createFileSave(Serializable userData) {
        FileChooser configFileChooser = new FileChooser();
        configFileChooser.setTitle("Сохранить");
        configFileChooser.setInitialDirectory(
                new File(FileHelper.getDir().getAbsolutePath())
        );
        configFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.ser")
        );
        File configFile = configFileChooser.showSaveDialog(null);
        if(configFile!=null){
            System.out.println(configFile.getName());
            FileHelper.serialize(userData, configFile.getName());
            System.out.println(configFile.getParent());
        }
    }
    public Object openFileSave() {
        FileChooser configFileChooser = new FileChooser();
        configFileChooser.setTitle("Выбор сохранения");
        configFileChooser.setInitialDirectory(
                new File(FileHelper.getDir().getAbsolutePath())
        );
        configFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SER", "*.ser"),
                new FileChooser.ExtensionFilter("ALL", "*.*")
        );
        File configFile = configFileChooser.showOpenDialog(null);

        if(configFile!=null){
            System.out.println(configFile.getName());
            System.out.println(configFile.getParent());
            return FileHelper.deserialize(configFile.getName());
        }
        return null;
    }
}