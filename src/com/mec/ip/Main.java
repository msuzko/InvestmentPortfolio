package com.mec.ip;

import com.mec.ip.controllers.MainController;
import com.mec.ip.objects.Lang;
import com.mec.ip.utils.LocaleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Main extends Application implements Observer {

    public static final String MAIN_FXML = "resource/main.fxml";
    public static final String BUNDLES_FOLDER = "com.mec.ip.bundles.Locale";

    private Stage primaryStage;
    private MainController mainController;

    private VBox currentRoot;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createGUI();
    }

    private void createGUI() {
        currentRoot = loadFXML(LocaleManager.RU_LOCALE);
        mainController.setMainStage(primaryStage);
        primaryStage.setScene(new Scene(currentRoot, 750, 400));
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Lang lang = (Lang) arg;
        VBox newNode = loadFXML(lang.getLocale());
        currentRoot.getChildren().setAll(newNode.getChildren());
    }

    private VBox loadFXML(Locale locale) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(MAIN_FXML));
        fxmlLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));

        VBox node = null;

        try {
            node = fxmlLoader.load();
            mainController = fxmlLoader.getController();
            mainController.addObserver(this);
            primaryStage.setTitle(fxmlLoader.getResources().getString("portfolio"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
