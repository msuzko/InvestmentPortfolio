package com.mec.ip;

import com.mec.ip.controllers.MainController;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("resource/main.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);

        primaryStage.setTitle("Investment portfolio");
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(300);
        primaryStage.setScene(new Scene(fxmlMain, 750, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
