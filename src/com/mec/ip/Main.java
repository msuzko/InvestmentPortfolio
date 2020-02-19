package com.mec.ip;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resource/main.fxml"));
        primaryStage.setTitle("Investment portfolio");
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(300);
        primaryStage.setScene(new Scene(root, 750, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
