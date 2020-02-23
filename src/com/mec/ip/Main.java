package com.mec.ip;

import com.mec.ip.controllers.MainController;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("resource/main.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("com.mec.ip.bundles.Locale",new Locale("en")));
        Parent fxmlMain = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);

        primaryStage.setTitle(fxmlLoader.getResources().getString("portfolio"));
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(300);
        primaryStage.setScene(new Scene(fxmlMain, 750, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
