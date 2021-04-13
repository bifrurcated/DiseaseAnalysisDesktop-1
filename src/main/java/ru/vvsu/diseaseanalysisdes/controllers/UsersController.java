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
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class UsersController implements Initializable {
    private final SQLiteManager dataBase;

    @FXML private Pane pane;
    @FXML private ListView<String> listNames;

    private ObservableList<String> names;
    private List<Human> humanList;
    private Human user;
    private Algo algo;
    private Map<String, Double> probabilityMap;

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
        probabilityMap = new HashMap<>(9);
        algo = new Algo();
        user = new Human();
        //для теста
        user.height = "160";
        user.age = "28";
        user.weight = "60";
        user.sex = "1";
    }
    //Пример создание и загрузка сохранений
    /*  Human sad = new Human();
        sad.id = "142142";
        createFileSave(sad);
        Optional<Human> optionalHuman = Optional.ofNullable((Human) openFileSave());
        optionalHuman.ifPresent(human -> System.out.println(human.id));*/

    public void onClick(MouseEvent mouseEvent) {
        algo.setPercent(5); // задаём начальный процент выборки
        System.out.println(algo.getIndexMassBody(user.height, user.weight));
        Runnable searchEqualUser = () -> {
            int count = 0;
            while (count < 1){
                StringBuilder sb = algo.getQuerySelections(user); System.out.println(sb);
                try{
                    ResultSet resultSet = dataBase.getResultSet("SELECT * FROM med_card where "+sb+";");
                    while (resultSet.next()) {
                        Human human = new Human();
                        Arrays.stream(human.getClass().getFields()).forEach(val -> {
                            try {
                                val.set(human,resultSet.getString(val.getName()));
                            } catch (IllegalAccessException | SQLException e) {
                                e.printStackTrace();
                            }
                        }); // каждой public переменной присваиваем значение из выборки
                        humanList.add(human);
                        count++;
                        if(!names.contains(human.height)){
                            names.add(human.height); //этот список для примера, не нужен - удалить
                        }
                    }
                    if(count < 1){
                        algo.setPercent(algo.getPercent()+1); //увеличиваем процент выборки
                    }
                    if(count > 2){
                        probabilityMap = algo.getProbabilityHealthy(humanList);
                        System.out.println(probabilityMap);
                    }
                    resultSet.close();
                    resultSet.getStatement().close();

                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            System.out.println("percent = "+algo.getPercent());
        };
        Thread thread = new Thread(searchEqualUser);
        thread.setDaemon(true);
        thread.start();
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
            Thread serializeThread = new Thread(() -> FileHelper.serialize(userData, configFile.getName()));
            serializeThread.start();
            System.out.println(configFile.getParent());
        }
    }
    public Object openFileSave() {
        FileChooser configFileChooser = new FileChooser();
        configFileChooser.setTitle("Выбрать файл");
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