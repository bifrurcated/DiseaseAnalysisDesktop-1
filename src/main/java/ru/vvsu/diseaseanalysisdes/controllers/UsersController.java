package ru.vvsu.diseaseanalysisdes.controllers;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    @FXML private TableView<Human> tableViewEnterData;
    @FXML private TableView<Human> tableViewResultSearch;

    @FXML private ToggleGroup genderToggleGroup;

    private ObservableList<Human> enterDataList;
    private ObservableList<Human> resultSearchList;
    private List<Human> humanList;
    private Human user;
    private Algo algo;
    private Map<String, Double> probabilityMap;
    private Map<String, String> namesColumns;

    public UsersController(){
        dataBase = new SQLiteManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        namesColumns = new LinkedHashMap<>(40);
        namesColumns.put("sex","Пол");
        namesColumns.put("age","Возраст");
        namesColumns.put("height","Рост");
        namesColumns.put("weight","Вес");
        namesColumns.put("freq_meat","Как часто вы употребляете мясо?");
        namesColumns.put("freq_fish","Как часто вы употребляете рыбу?");
        namesColumns.put("freq_vegatables","Как часто вы употребляете свежие овощи?");
        namesColumns.put("freq_sweets","Как часто вы употребляете сладости?");
        namesColumns.put("freq_cottage_cheese","Как часто вы употребляете творог?");
        namesColumns.put("freq_cheese","Как часто вы употребляете сыр?");
        namesColumns.put("exercise_stress_on_work","Уровень физ. нагрузки во время работы");
        namesColumns.put("walk","Сколько в среднем минут в день ходите пешком");
        namesColumns.put("exercise_stress","Сколько раз в неделю физ. нагрузка?");
        namesColumns.put("cigarettes","Сколько в среднем сигарет вы выкуриваете в день?");
        namesColumns.put("headaches","Головные боли бывают?");
        namesColumns.put("sleep","Длительность ежедневного сна?");
        namesColumns.put("abstinence_from_sleep","Как часто вы испытывали трудности в том, чтобы воздерживаться от засыпания когда ситуация этого требует?");
        namesColumns.put("fall_asleep","Насколько часто Вам было трудно заснуть в течение 30 мин после того как вы легли в постель?");
        namesColumns.put("restless","Я неусидчив");
        namesColumns.put("waist","Окружность талии");
        namesColumns.put("hips","Окружность бедер");
        namesColumns.put("average_systolic","Среднее систолическое артериальное давление");
        namesColumns.put("average_diastolic","Среднее диастолическое артериальное давление");
        namesColumns.put("average_heart_rate","Средняя частота сердечных сокращений");
        namesColumns.put("total_cholesterol","Общий холестерин");
        namesColumns.put("hdl","ЛПВП");
        namesColumns.put("lpa","ЛП(a)");
        namesColumns.put("apob","APOB");
        namesColumns.put("glucose","Глюкоза");
        namesColumns.put("creatinine","Креатинин");
        namesColumns.put("uric_acid","Мочевая кислота");
        namesColumns.put("crp","C-реактивный белок");
        namesColumns.put("insulin","Инсулин");
        namesColumns.put("tsh","ТТГ");
        namesColumns.put("probnp","PROBNP");

        enterDataList = FXCollections.observableArrayList();
        resultSearchList = FXCollections.observableArrayList();
        tableViewEnterData.setItems(enterDataList);
        tableViewResultSearch.setItems(resultSearchList);

        namesColumns.forEach((key,val) -> {
            TableColumn<Human,String> tableColumnEnterData = new TableColumn<>(val);
            tableColumnEnterData.setCellValueFactory(new PropertyValueFactory<>(key));
            tableViewEnterData.getColumns().add(tableColumnEnterData);
            TableColumn<Human,String> tableColumnResultSearch = new TableColumn<>(val);
            tableColumnResultSearch.setCellValueFactory(new PropertyValueFactory<>(key));
            tableViewResultSearch.getColumns().add(tableColumnResultSearch);
            if(key.equals("weight") || key.equals("fall_asleep") || key.equals("hips")){
                tableColumnEnterData = new TableColumn<>(".........");
                tableViewEnterData.getColumns().add(tableColumnEnterData);
                tableColumnResultSearch = new TableColumn<>(".........");
                tableViewResultSearch.getColumns().add(tableColumnResultSearch);
            }
        });

        humanList = new ArrayList<>();
        probabilityMap = new HashMap<>(9);
        algo = new Algo();
        user = new Human();

        //для теста
        user.height = "160";
        user.age = "28";
        user.weight = "60";
        user.sex = "1";
        //----------------\\
        //для каждой группы создаём своего слушателя
        genderToggleGroup.selectedToggleProperty().addListener(genderListener);
    }

    ChangeListener<Toggle> genderListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Мужской": user.sex = "1"; break;
            case "Женский": user.sex = "2"; break;
        }
        System.out.println( user.sex );
    };

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

    public void handleBtnContinue(ActionEvent actionEvent) {
        if(!enterDataList.isEmpty()){ enterDataList.remove(0); }
        enterDataList.add(user);

        if(!resultSearchList.isEmpty()){ resultSearchList.remove(0, resultSearchList.size()); }

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
                        resultSearchList.add(human);
                        count++;
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
}