package ru.vvsu.diseaseanalysisdes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.vvsu.diseaseanalysisdes.helpers.FileHelper;

public class Main extends Application {

    public static Main instance;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/views/sample.fxml"));
        primaryStage.setTitle("Disease Analysis Tool");
        primaryStage.setScene(new Scene(root, 830, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public Main() {
        instance = this;
        FileHelper.setupDefault();
        FileHelper.loadFile(Settings.DB_FILE_NAME, Settings.DB_PATH_SOURCE);
    }


    public static void main(String[] args) {
        new Main();
        launch(args);
    }
}
