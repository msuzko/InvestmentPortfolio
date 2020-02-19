package com.mec.ip.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class MainController {
    public void showAddDialog(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../resource/add.fxml"));
            stage.setTitle("Добавиит позицию");
            stage.setMinHeight(115);
            stage.setMinWidth(350);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(getOwner(actionEvent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showEditDialog(ActionEvent actionEvent) {

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../resource/edit.fxml"));
            stage.setTitle("Изменить позицию");
            stage.setMinHeight(245);
            stage.setMinWidth(395);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(getOwner(actionEvent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Window getOwner(ActionEvent actionEvent) {
        return ((Node) actionEvent.getSource()).getScene().getWindow();
    }
}
