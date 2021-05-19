package ru.vvsu.diseaseanalysisdes.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.vvsu.diseaseanalysisdes.helpers.FileHelper;
import ru.vvsu.diseaseanalysisdes.managers.SQLiteManager;
import ru.vvsu.diseaseanalysisdes.models.AlgoSearch;
import ru.vvsu.diseaseanalysisdes.models.Human;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UsersController implements Initializable {
    private final SQLiteManager dataBase;
    @FXML private Button proceedButton;
    @FXML private Tab tabResult;

    @FXML private TableView<Human> tableViewEnterData;
    @FXML private TableView<Human> tableViewResultSearch;
    @FXML private ToggleGroup genderToggleGroup,vegesToggleGroup,sweetsToggleGroup,
            meatToggleGroup,fishToggleGroup,curdToggleGroup,cheeseToggleGroup,
            zasnutToggleGroup,vozderzhToggleGroup,headachesToggleGroup,restlessToggleGroup;

    @FXML private TextField waistTextField, hipTextField, dreamTextField,
            ageTextField, heightTextField, weightTextField, walkTextField,
            exerciseStressTextField, exerciseStressOnWorkTextField, cigarettesTextField,
            averageSystolicTextField, averageDiastolicTextField, averageHeartRateTextField,
            totalCholesterolTextField, hdlTextField, lpaTextField, probnpTextField, apobTextField,
            glucoseTextField, creatinineTextField, uricAcidcreatinineTextField, crpTextField, insulinTextField,
            tshTextField;
    @FXML private CheckBox statisticCheckBox, allSimilarCheckBox;

    private static Pattern pattern;
    private static Pattern onlyNumbersPattern;

    static {
        pattern = Pattern.compile("[\\d]+[\\.]?[\\d]*");
        onlyNumbersPattern = Pattern.compile("[1-10]");
    }

    private ObservableList<Human> enterDataList;
    private ObservableList<Human> resultSearchList;
    private Human user;
    private AlgoSearch algoSearch;
    private Map<String, Double> probabilityMap;
    private Map<String,String> scaleMap;
    private SingleSelectionModel<Tab> selectionModel;

    public UsersController(){
        dataBase = new SQLiteManager();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, String> namesColumns = new LinkedHashMap<>(50);
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
        namesColumns.put("abstinence_from_sleep","Как часто вы испытывали трудности в том,\nчтобы воздерживаться от засыпания когда ситуация этого требует?");
        namesColumns.put("fall_asleep","Насколько часто Вам было трудно заснуть в течение 30 мин\nпосле того как вы легли в постель?");
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
        namesColumns.put("osteochondrosis","Остеохондроз");
        namesColumns.put("rheumatoid_arthritis","Ревматоидный артрит");
        namesColumns.put("stroke","Инсульт");
        namesColumns.put("myocardial_infarction","Инфаркт миокарда");
        namesColumns.put("coronary_heart_disease","Ишемическая болезнь сердца");
        namesColumns.put("arrhythmia","Аритмия");
        namesColumns.put("kidney_disease","Заболевания почек");
        namesColumns.put("thyroid_disease","Заболевания щитовидной железы");
        namesColumns.put("id","Идентификатор");

        enterDataList = FXCollections.observableArrayList();
        resultSearchList = FXCollections.observableArrayList();
        tableViewEnterData.setItems(enterDataList);
        tableViewResultSearch.setItems(resultSearchList);
        Platform.runLater(() -> {
            tableViewResultSearch.getColumns().get(tableViewResultSearch.getColumns().size()-1).setVisible(false);
            tableViewResultSearch.getColumns().get(tableViewResultSearch.getColumns().size()-1).setVisible(true);
        });
        selectionModel = tabResult.getTabPane().getSelectionModel();

        Callback<TableColumn<Human, String>, TableCell<Human, String>> defaultCellFactory
                = TextFieldTableCell.forTableColumn();
        namesColumns.forEach((key, val) -> {
            if(!key.equals("id")){
                TableColumn<Human,String> tableColumnEnterData = new TableColumn<>(val);
                tableColumnEnterData.setCellValueFactory(new PropertyValueFactory<>(key));
                tableColumnEnterData.setCellFactory(col -> {
                    TableCell<Human, String> cell = defaultCellFactory.call(col);
                    cell.setAlignment(Pos.CENTER);
                    cell.setPrefHeight(60);
                    return cell ;
                });
                tableViewEnterData.getColumns().add(tableColumnEnterData);
            }

            TableColumn<Human,String> tableColumnResultSearch = new TableColumn<>(val);
            tableColumnResultSearch.setCellValueFactory(new PropertyValueFactory<>(key));
            tableColumnResultSearch.setCellFactory(col -> {
                TableCell<Human, String> cell = defaultCellFactory.call(col);
                cell.setAlignment(Pos.CENTER);
                return cell ;
            });
            tableViewResultSearch.getColumns().add(tableColumnResultSearch);
            if(key.equals("weight") || key.equals("fall_asleep") ||
                    key.equals("hips") || key.equals("probnp") || key.equals("thyroid_disease")){
                TableColumn<Human,String> tableColumnEnterData = new TableColumn<>(".........");
                tableViewEnterData.getColumns().add(tableColumnEnterData);
                tableColumnResultSearch = new TableColumn<>(".........");
                tableViewResultSearch.getColumns().add(tableColumnResultSearch);
            }
        });

        probabilityMap = new HashMap<>(9);
        algoSearch = new AlgoSearch();
        user = new Human();

        user.sex = "1";
        user.cigarettes = "";
        //----------------\\
        scaleMap = new HashMap<>(25);
        scaleMap.put("Мужской","1");
        scaleMap.put("Женский","2");
        scaleMap.put("Каждый день","4");
        scaleMap.put("Несколько раз в неделю","3");
        scaleMap.put("Один или два раза в неделю","2");
        scaleMap.put("Редко","1");

        scaleMap.put("Да","2");
        scaleMap.put("Нет","1");
        scaleMap.put("Очень часто","4");
        scaleMap.put("Довольно часто","3");
        scaleMap.put("Довольно редко","2");
        scaleMap.put("Очень редко","1");
        scaleMap.put("Верно","3");              // Вкладка "Параметры", параметр "Я неусидчив"
        scaleMap.put("Скорее да, чем нет","2"); // ↑
        scaleMap.put("Скорее нет, чем да","1"); // ↑↑
        scaleMap.put("Неверно","0");            // ↑↑↑
        //для каждой группы создаём своего слушателя
        genderToggleGroup.selectedToggleProperty().addListener(genderListener);
        meatToggleGroup.selectedToggleProperty().addListener(meatListener);
        fishToggleGroup.selectedToggleProperty().addListener(fishListener);
        vegesToggleGroup.selectedToggleProperty().addListener(vegesListener);
        sweetsToggleGroup.selectedToggleProperty().addListener(sweetsListener);
        curdToggleGroup.selectedToggleProperty().addListener(curdListener);
        cheeseToggleGroup.selectedToggleProperty().addListener(cheeseListener);
        zasnutToggleGroup.selectedToggleProperty().addListener(zasnutListener);
        vozderzhToggleGroup.selectedToggleProperty().addListener(vozderzhListener);
        headachesToggleGroup.selectedToggleProperty().addListener(headachesListener);
        restlessToggleGroup.selectedToggleProperty().addListener(restlessListener);

        //textfield listener
        waistTextField.textProperty().addListener(waistListener);
        hipTextField.textProperty().addListener(hipsListener);
        dreamTextField.textProperty().addListener(dreamListener);
        ageTextField.textProperty().addListener(ageListener);
        heightTextField.textProperty().addListener(heightListener);
        weightTextField.textProperty().addListener(weightListener);
        walkTextField.textProperty().addListener(walkListener);
        exerciseStressTextField.textProperty().addListener(exerciseStressListener);
        exerciseStressOnWorkTextField.textProperty().addListener(exerciseStressOnWorkListener);
        cigarettesTextField.textProperty().addListener(cigarettesListener);
        averageSystolicTextField.textProperty().addListener(averageSystolicListener);
        averageDiastolicTextField.textProperty().addListener(averageDiastolicListener);
        averageHeartRateTextField.textProperty().addListener(averageHeartRateListener);
        totalCholesterolTextField.textProperty().addListener(totalCholesterolListener);
        hdlTextField.textProperty().addListener(hdlListener);
        lpaTextField.textProperty().addListener(lpaListener);
        probnpTextField.textProperty().addListener(probnpListener);
        apobTextField.textProperty().addListener(apobListener);
        glucoseTextField.textProperty().addListener(glucoseListener);
        creatinineTextField.textProperty().addListener(creatinineListener);
        uricAcidcreatinineTextField.textProperty().addListener(uricAcidcreatinineListener);
        crpTextField.textProperty().addListener(crpListener);
        insulinTextField.textProperty().addListener(insulinListener);
        tshTextField.textProperty().addListener(tshListener);
    }

    ChangeListener<String> tshListener = (ov, old_toggle, new_toggle) -> {
        user.tsh = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.tsh, tshTextField);
            }
        });
        System.out.println(user.tsh);
    };

    ChangeListener<String> insulinListener = (ov, old_toggle, new_toggle) -> {
        user.insulin = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.insulin, insulinTextField);
            }
        });
        System.out.println(user.insulin);
    };

    ChangeListener<String> crpListener = (ov, old_toggle, new_toggle) -> {
        user.crp = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.crp, crpTextField);
            }
        });
        System.out.println(user.crp);
    };

    ChangeListener<String> uricAcidcreatinineListener = (ov, old_toggle, new_toggle) -> {
        user.uric_acid = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.uric_acid, uricAcidcreatinineTextField);
            }
        });
        System.out.println(user.uric_acid);
    };

    ChangeListener<String> creatinineListener = (ov, old_toggle, new_toggle) -> {
        user.creatinine = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.creatinine, creatinineTextField);
            }
        });
        System.out.println(user.creatinine);
    };

    ChangeListener<String> glucoseListener = (ov, old_toggle, new_toggle) -> {
        user.glucose = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.glucose, glucoseTextField);
            }
        });
        System.out.println(user.glucose);
    };

    ChangeListener<String> apobListener = (ov, old_toggle, new_toggle) -> {
        user.apob = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.apob, apobTextField);
            }
        });
        System.out.println(user.apob);
    };

    ChangeListener<String> probnpListener = (ov, old_toggle, new_toggle) -> {
        user.probnp = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.probnp, probnpTextField);
            }
        });
        System.out.println(user.probnp);
    };

    ChangeListener<String> lpaListener = (ov, old_toggle, new_toggle) -> {
        user.lpa = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.lpa, lpaTextField);
            }
        });
        System.out.println(user.lpa);
    };

    ChangeListener<String> hdlListener = (ov, old_toggle, new_toggle) -> {
        user.hdl = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.hdl, hdlTextField);
            }
        });
        System.out.println(user.hdl);
    };

    ChangeListener<String> totalCholesterolListener = (ov, old_toggle, new_toggle) -> {
        user.total_cholesterol = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.total_cholesterol, totalCholesterolTextField);
            }
        });
        System.out.println(user.total_cholesterol);
    };

    ChangeListener<String> averageHeartRateListener = (ov, old_toggle, new_toggle) -> {
        user.average_heart_rate = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.average_heart_rate, averageHeartRateTextField);
            }
        });
        System.out.println(user.average_heart_rate);
    };

    ChangeListener<String> averageDiastolicListener = (ov, old_toggle, new_toggle) -> {
        user.average_diastolic = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.average_diastolic, averageDiastolicTextField);
            }
        });
        System.out.println(user.average_diastolic);
    };

    ChangeListener<String> averageSystolicListener = (ov, old_toggle, new_toggle) -> {
        user.average_systolic = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.average_systolic, averageSystolicTextField);
            }
        });
        System.out.println(user.average_systolic);
    };

    ChangeListener<String> cigarettesListener = (ov, old_toggle, new_toggle) -> {
        user.cigarettes = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.cigarettes, cigarettesTextField);
            }
        });
        System.out.println(user.cigarettes);
    };

    ChangeListener<String> exerciseStressOnWorkListener = (ov, old_toggle, new_toggle) -> {
        createNumbersPattern(6);
        user.exercise_stress_on_work = readString(new_toggle, ValueType.ONLY_NUMBERS);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.exercise_stress_on_work, exerciseStressOnWorkTextField);
            }
        });
        System.out.println(user.exercise_stress_on_work);
    };

    ChangeListener<String> exerciseStressListener = (ov, old_toggle, new_toggle) -> {
        createNumbersPattern(10);
        user.exercise_stress = readString(new_toggle, ValueType.ONLY_NUMBERS);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.exercise_stress, exerciseStressTextField);
            }
        });
        System.out.println(user.exercise_stress);
    };

    ChangeListener<String> walkListener = (ov, old_toggle, new_toggle) -> {
        user.walk = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.walk, walkTextField);
            }
        });
        System.out.println(user.walk);
    };

    ChangeListener<String> weightListener = (ov, old_toggle, new_toggle) -> {
        user.weight = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.weight, weightTextField);
            }
        });
        System.out.println(user.weight);
    };

    ChangeListener<String> heightListener = (ov, old_toggle, new_toggle) -> {
        user.height = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.height, heightTextField);
            }
        });
        System.out.println(user.height);
    };

    ChangeListener<String> ageListener = (ov, old_toggle, new_toggle) -> {
        user.age = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.age, ageTextField);
            }
        });
        System.out.println(user.age);
    };

    ChangeListener<String> hipsListener = (ov, old_toggle, new_toggle) -> {
        user.hips = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.hips, hipTextField);
            }
        });
        System.out.println(user.hips);
    };

    ChangeListener<String> waistListener = (ov, old_toggle, new_toggle) -> {
        user.waist = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.waist, waistTextField);
            }
        });
        System.out.println(user.waist);
    };

    ChangeListener<String> dreamListener = (ov, old_toggle, new_toggle) -> {
        user.sleep = readString(new_toggle, ValueType.DOUBLE_DIGEST);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                checkEmptyInput(user.sleep, dreamTextField);
            }
        });
        System.out.println(user.sleep);
    };

    ChangeListener<Toggle> genderListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.sex = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.sex );
    };

    ChangeListener<Toggle> meatListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_meat = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_meat );
    };

    ChangeListener<Toggle> fishListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_fish = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_fish );
    };

    ChangeListener<Toggle> vegesListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_vegatables = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_vegatables );
    };

    ChangeListener<Toggle> sweetsListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_sweets = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_sweets );
    };

    ChangeListener<Toggle> curdListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_cottage_cheese = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_cottage_cheese );
    };

    ChangeListener<Toggle> cheeseListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.freq_cheese = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.freq_cheese );
    };

    ChangeListener<Toggle> headachesListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.headaches = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.headaches );
    };

    ChangeListener<Toggle> vozderzhListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.abstinence_from_sleep = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.abstinence_from_sleep );
    };

    ChangeListener<Toggle> zasnutListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.fall_asleep = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.fall_asleep );
    };

    ChangeListener<Toggle> restlessListener = (ov, old_toggle, new_toggle) -> {
        RadioButton selectRadioButton = (RadioButton) new_toggle;
        user.restless = scaleMap.get(selectRadioButton.getText());
        System.out.println( user.restless );
    };

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

    private void errors(String descriptionError, Button parentError){
        Stage stage = (Stage) parentError.getScene().getWindow();
        Label secondLabel = new Label(descriptionError);

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 300, 100);

        Stage newWindow = new Stage();
        newWindow.setResizable(false);
        newWindow.setTitle("Error");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(stage);
        newWindow.setX(stage.getX() + 300);
        newWindow.setY(stage.getY() + 300);
        newWindow.show();
    }

    private boolean checkInputData(){
        if(user.height == null || user.height.equals("")){
            String textError = "Поле `Рост` не заполнено!";
            errors(textError, proceedButton);
            return false;
        }
        else if(user.weight == null || user.weight.equals("")){
            String textError = "Поле `Вес` не заполнено!";
            errors(textError, proceedButton);
            return false;
        }
        return true;
    }

    public void handleBtnContinue(ActionEvent actionEvent) {
        if(!checkInputData()){
            return;
        }
        if(!enterDataList.isEmpty()){ enterDataList.clear(); }
        enterDataList.add(user);
        if(!resultSearchList.isEmpty()){ resultSearchList.clear(); }
        proceedButton.setDisable(true);
        algoSearch.setPercent(0);// задаём начальный процент выборки
        Runnable searchEqualUser2 = () -> {
            StringBuilder sb1 = new StringBuilder();
            int countFound = 0;
            while (countFound < 1){
                sb1 = algoSearch.getQueryNonSelections(user);
                try{
                    ResultSet resultSet = dataBase.getResultSet(
                            "SELECT *,10000*weight/(height*height) as imb FROM med_card where "+sb1+";");
                    while (resultSet.next()) {
                        countFound++;
                    }
                    algoSearch.nextSearch(countFound);
                    System.out.println(sb1);
                    System.out.println("nextSearch = "+algoSearch.isNextSearch());
                    resultSet.close();
                    resultSet.getStatement().close();
                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            System.out.println("countFound = "+countFound);
            System.out.println("sb1 = "+sb1);
            int max = (int)Math.ceil(countFound/3f);
            System.out.println("max = "+max);
            while (resultSearchList.size() < max){
                StringBuilder sb2 = new StringBuilder();
                if(countFound > 2){
                    sb2 = algoSearch.getQuerySelections(user);
                    if(sb2.length() == 0){
                        sb2.append(sb1);
                        max=countFound;
                    }
                    else {
                        sb2.append(" and ").append(sb1);
                    }
                } else {
                    sb2.append(sb1);
                }
                System.out.println("size = "+resultSearchList.size());
                System.out.println("sb2 = "+sb2);
                try{
                    ResultSet resultSet = dataBase.getResultSet(
                            "SELECT *,10000*weight/(height*height) as imb FROM med_card where "+sb2+";");
                    while (resultSet.next()) {
                        Human human = new Human();
                        for(Field field: human.getClass().getFields()){
                            try {
                                field.set(human,resultSet.getString(field.getName()));
                            } catch (IllegalAccessException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        resultSearchList.add(human);
                    }
                    if(resultSearchList.size() < max){
                        algoSearch.setPercent(algoSearch.getPercent()+1); //увеличиваем процент выборки
                        if(algoSearch.getPercent() >= 500){
                            break; // порог на всякий случай
                        }
                        resultSearchList.clear();
                    }
                    resultSet.close();
                    resultSet.getStatement().close();
                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            System.out.println("percent = "+algoSearch.getPercent());
            if(resultSearchList.size() > 2){
                probabilityMap = algoSearch.getProbabilityHealthy(resultSearchList);
                System.out.println(probabilityMap);
            }
            autoResizeColumns(tableViewEnterData);
            autoResizeColumns(tableViewResultSearch);
            selectionModel.select(tabResult);
            proceedButton.setDisable(false);
        };
        //Platform.runLater(searchEqualUser2);

        //Algo algo = new Algo();
        //algo.setPercent(15);
        /*Runnable searchEqualUser = () -> {
            StringBuilder sb1 = new StringBuilder();
            int countFound = 0;
            while (countFound < 1){
                sb1 = algo.getQuerySelections(user);
                try{
                    ResultSet resultSet = dataBase.getResultSet(
                            "SELECT *,10000*weight/(height*height) as imb FROM med_card where "+sb1+";");
                    while (resultSet.next()) {
                        countFound++;
                    }
                    System.out.println(sb1);
                    if(countFound < 1){
                        algo.setPercent(algo.getPercent()+1); //увеличиваем процент выборки
                        if(algo.getPercent() >= 500){
                            break; // порог на всякий случай
                        }
                    }
                    resultSet.close();
                    resultSet.getStatement().close();
                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            System.out.println("countFound = "+countFound);
            System.out.println("sb1 = "+sb1);
            if(sb1.length()!=0){
                return;
            }
            int max = (int)Math.ceil(countFound/3f);
            System.out.println("max = "+max);
            while (resultSearchList.size() < max){
                StringBuilder sb2 = new StringBuilder();
                if(countFound > 2){
                    sb2 = algo.getQueryNonSelections(user);
                    if(sb2.length() == 0){
                        sb2.append(sb1);
                    }
                    else {
                        sb2.append(" and ").append(sb1);
                    }
                } else {
                    sb2.append(sb1);
                }
                System.out.println("sb2 = "+sb2);
                try{
                    ResultSet resultSet = dataBase.getResultSet(
                            "SELECT *,10000*weight/(height*height) as imb FROM med_card where "+sb2+";");
                    while (resultSet.next()) {
                        Human human = new Human();
                        for(Field field: human.getClass().getFields()){
                            try {
                                field.set(human,resultSet.getString(field.getName()));
                            } catch (IllegalAccessException | SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if(!resultSearchList.contains(human)) {
                            resultSearchList.add(human);
                        }
                    }
                    algo.nextSearch(resultSearchList.size());
                    System.out.println(sb1);
                    System.out.println("nextSearch = "+algo.isNextSearch());
                    resultSet.close();
                    resultSet.getStatement().close();
                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                }
            }
            System.out.println("percent = "+algo.getPercent());
            if(resultSearchList.size() > 2){
                probabilityMap = algo.getProbabilityHealthy(resultSearchList);
                System.out.println(probabilityMap);
            }
            autoResizeColumns(tableViewEnterData);
            autoResizeColumns(tableViewResultSearch);
            selectionModel.select(tabResult);
            proceedButton.setDisable(false);
        };*/
        Platform.runLater(searchEqualUser2);
    }

    public static void autoResizeColumns( TableView<?> table )
    {
        //Set the right policy
        table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach( (column) ->
        {
            //Minimal width = columnheader
            Text t = new Text( column.getText() );
            double max = t.getLayoutBounds().getWidth();
            for ( int i = 0; i < table.getItems().size(); i++ )
            {
                //cell must not be empty
                if ( column.getCellData( i ) != null )
                {
                    t = new Text( column.getCellData( i ).toString() );
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if ( calcwidth > max )
                    {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth( max + 50.0d );
        } );
    }

    public void handleBtnSave(ActionEvent actionEvent) {
        createFileSave(user);
    }

    public void handleBtnLoad(ActionEvent actionEvent) {
        Optional<Human> optionalHuman = Optional.ofNullable((Human) openFileSave());
        optionalHuman.ifPresent(human -> {
            try {
                user = (Human) human.clone();
                if(user.sex != null){
                    genderToggleGroup.selectToggle(genderToggleGroup.getToggles().get(Integer.parseInt(user.sex)-1));
                }
                if(user.freq_vegatables != null){
                    vegesToggleGroup.selectToggle(vegesToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_vegatables)));
                }
                if(user.freq_sweets != null){
                    sweetsToggleGroup.selectToggle(sweetsToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_sweets)));
                }
                if(user.freq_meat != null){
                    meatToggleGroup.selectToggle(meatToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_meat)));
                }
                if(user.freq_fish != null){
                    fishToggleGroup.selectToggle(fishToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_fish)));
                }
                if(user.freq_cottage_cheese != null){
                    curdToggleGroup.selectToggle(curdToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_cottage_cheese)));
                }
                if(user.freq_cheese != null){
                    cheeseToggleGroup.selectToggle(cheeseToggleGroup.getToggles().get(4-Integer.parseInt(user.freq_cheese)));
                }
                if(user.fall_asleep != null){
                    zasnutToggleGroup.selectToggle(zasnutToggleGroup.getToggles().get(4-Integer.parseInt(user.fall_asleep)));
                }
                if(user.abstinence_from_sleep != null){
                    vozderzhToggleGroup.selectToggle(vozderzhToggleGroup.getToggles().get(4-Integer.parseInt(user.abstinence_from_sleep)));
                }
                if(user.headaches != null){
                    headachesToggleGroup.selectToggle(headachesToggleGroup.getToggles().get(2-Integer.parseInt(user.headaches)));
                }
                if(user.restless != null){
                    restlessToggleGroup.selectToggle(restlessToggleGroup.getToggles().get(3-Integer.parseInt(user.restless)));
                }

                waistTextField.setText(user.waist);
                hipTextField.setText(user.hips);
                dreamTextField.setText(user.sleep);
                ageTextField.setText(user.age);
                heightTextField.setText(user.height);
                weightTextField.setText(user.weight);
                walkTextField.setText(user.walk);
                exerciseStressTextField.setText(user.exercise_stress);
                exerciseStressOnWorkTextField.setText(user.exercise_stress_on_work);
                cigarettesTextField.setText(user.cigarettes);
                averageSystolicTextField.setText(user.average_systolic);
                averageDiastolicTextField.setText(user.average_diastolic);
                averageHeartRateTextField.setText(user.average_heart_rate);
                totalCholesterolTextField.setText(user.total_cholesterol);
                hdlTextField.setText(user.hdl);
                lpaTextField.setText(user.lpa);
                probnpTextField.setText(user.probnp);
                apobTextField.setText(user.apob);
                glucoseTextField.setText(user.glucose);
                creatinineTextField.setText(user.creatinine);
                uricAcidcreatinineTextField.setText(user.uric_acid);
                crpTextField.setText(user.crp);
                insulinTextField.setText(user.insulin);
                tshTextField.setText(user.tsh);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
    }

    public static String readString(String s, ValueType type) {
        Matcher matcher;
        switch (type) {
            case ONLY_NUMBERS:
                matcher = onlyNumbersPattern.matcher(s);
                if(matcher.matches()) {
                    return s;
                }
                break;
            case DOUBLE_DIGEST:
                matcher = pattern.matcher(s);
                if(matcher.matches()) {
                    return s;
                }
                break;
        }
        return "";
    }

    private static void createNumbersPattern(int endPosition) {
        String p;
        if(endPosition < 10) {
            p = "[" + 1 + "-" + endPosition + "]";
        } else {
            p = "[" + 1 +"-9][0" + "-" + String.valueOf(endPosition).toCharArray()[1] + "]*";
        }
        onlyNumbersPattern = Pattern.compile(p);
    }

    private static void checkEmptyInput(String s, TextField textField) {
        if(s.equals("")) {
            textField.clear();
        }
    }

    private enum ValueType {

        ONLY_NUMBERS, DOUBLE_DIGEST

    }
}