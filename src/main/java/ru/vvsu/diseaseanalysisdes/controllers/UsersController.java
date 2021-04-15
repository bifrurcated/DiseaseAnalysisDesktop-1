package ru.vvsu.diseaseanalysisdes.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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

    @FXML private ToggleGroup genderToggleGroup,vegesToggleGroup,sweetsToggleGroup,
            meatToggleGroup,fishToggleGroup,curdToggleGroup,cheeseToggleGroup,
    physicalStressLevelToggleGroup,onFootToggleGroup,physicalStressFrequencyToggleGroup,
            smokingToggleGroup,sleepToggleGroup,zasnutToggleGroup,vozderzhToggleGroup;

    //private ObservableList<String> names = FXCollections.observableArrayList();
    private List<Human> humanList;
    private Human user;
    private Algo algo;
    private Map<String, Double> probabilityMap;

    public UsersController(){
        dataBase = new SQLiteManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        vegesToggleGroup.selectedToggleProperty().addListener(vegesListener);
        sweetsToggleGroup.selectedToggleProperty().addListener(sweetsListener);
        meatToggleGroup.selectedToggleProperty().addListener(meatListener);
        fishToggleGroup.selectedToggleProperty().addListener(fishListener);
        curdToggleGroup.selectedToggleProperty().addListener(curdListener);
        cheeseToggleGroup.selectedToggleProperty().addListener(cheeseListener);
        physicalStressLevelToggleGroup.selectedToggleProperty().addListener(physicalWorkListener);
        onFootToggleGroup.selectedToggleProperty().addListener(footListener);
        physicalStressFrequencyToggleGroup.selectedToggleProperty().addListener(physicalListener);
        smokingToggleGroup.selectedToggleProperty().addListener(smokeListener);
        sleepToggleGroup.selectedToggleProperty().addListener(sleepListener);
        zasnutToggleGroup.selectedToggleProperty().addListener(zasnutListener);
        vozderzhToggleGroup.selectedToggleProperty().addListener(vozderzhListener);
    }

    ChangeListener<Toggle> genderListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Мужской": user.sex = "1"; break;
            case "Женский": user.sex = "2"; break;
        }
        System.out.println( user.sex );
    };

    ChangeListener<Toggle> vegesListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_vegatables = "1"; break;
            case "Несколько раз в неделю": user.freq_vegatables = "2"; break;
            case "Один или два раза в неделю": user.freq_vegatables = "3"; break;
            case "Редко": user.freq_vegatables = "4"; break;
        }
        System.out.println( user.freq_vegatables );
    };

    ChangeListener<Toggle> sweetsListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_sweets = "1"; break;
            case "Несколько раз в неделю": user.freq_sweets = "2"; break;
            case "Один или два раза в неделю": user.freq_sweets = "3"; break;
            case "Редко": user.freq_sweets = "4"; break;
        }
        System.out.println( user.freq_sweets );
    };

    ChangeListener<Toggle> fishListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_fish = "1"; break;
            case "Несколько раз в неделю": user.freq_fish = "2"; break;
            case "Один или два раза в неделю": user.freq_fish = "3"; break;
            case "Редко": user.freq_fish = "4"; break;
        }
        System.out.println( user.freq_fish );
    };

    ChangeListener<Toggle> meatListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_meat = "1"; break;
            case "Несколько раз в неделю": user.freq_meat = "2"; break;
            case "Один или два раза в неделю": user.freq_meat = "3"; break;
            case "Редко": user.freq_meat = "4"; break;
        }
        System.out.println( user.freq_meat );
    };

    ChangeListener<Toggle> curdListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_cottage_cheese = "1"; break;
            case "Несколько раз в неделю": user.freq_cottage_cheese = "2"; break;
            case "Один или два раза в неделю": user.freq_cottage_cheese = "3"; break;
            case "Редко": user.freq_cottage_cheese = "4"; break;
        }
        System.out.println( user.freq_cottage_cheese );
    };

    ChangeListener<Toggle> cheeseListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.freq_cheese = "1"; break;
            case "Несколько раз в неделю": user.freq_cheese = "2"; break;
            case "Один или два раза в неделю": user.freq_cheese = "3"; break;
            case "Редко": user.freq_cheese = "4"; break;
        }
        System.out.println( user.freq_cheese );
    };

    ChangeListener<Toggle> physicalWorkListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.exercise_stress_on_work = "1"; break;
            case "Несколько раз в неделю": user.exercise_stress_on_work = "2"; break;
            case "Один или два раза в неделю": user.exercise_stress_on_work = "3"; break;
            case "Редко": user.exercise_stress_on_work = "4"; break;
        }
        System.out.println( user.exercise_stress_on_work );
    };

    ChangeListener<Toggle> footListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.walk = "1"; break;
            case "Несколько раз в неделю": user.walk = "2"; break;
            case "Один или два раза в неделю": user.walk = "3"; break;
            case "Редко": user.walk = "4"; break;
        }
        System.out.println( user.walk );
    };

    ChangeListener<Toggle> physicalListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.exercise_stress = "1"; break;
            case "Несколько раз в неделю": user.exercise_stress = "2"; break;
            case "Один или два раза в неделю": user.exercise_stress = "3"; break;
            case "Редко": user.exercise_stress = "4"; break;
        }
        System.out.println( user.exercise_stress );
    };

    ChangeListener<Toggle> smokeListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.cigarettes = "1"; break;
            case "Несколько раз в неделю": user.cigarettes = "2"; break;
            case "Один или два раза в неделю": user.cigarettes = "3"; break;
            case "Редко": user.cigarettes = "4"; break;
        }
        System.out.println( user.cigarettes );
    };

    ChangeListener<Toggle> sleepListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.sleep = "1"; break;
            case "Несколько раз в неделю": user.sleep = "2"; break;
            case "Один или два раза в неделю": user.sleep = "3"; break;
            case "Редко": user.sleep = "4"; break;
        }
        System.out.println( user.sleep );
    };

    ChangeListener<Toggle> zasnutListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.abstinence_from_sleep = "1"; break;
            case "Несколько раз в неделю": user.abstinence_from_sleep = "2"; break;
            case "Один или два раза в неделю": user.abstinence_from_sleep = "3"; break;
            case "Редко": user.abstinence_from_sleep = "4"; break;
        }
        System.out.println( user.abstinence_from_sleep );
    };

    ChangeListener<Toggle> vozderzhListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        switch (selectRadioButton.getText()){
            case "Каждый день": user.fall_asleep = "1"; break;
            case "Несколько раз в неделю": user.fall_asleep = "2"; break;
            case "Один или два раза в неделю": user.fall_asleep = "3"; break;
            case "Редко": user.fall_asleep = "4"; break;
        }
        System.out.println( user.fall_asleep );
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
}