package com.mec.ip.utils;

import javafx.scene.control.Alert;

public class DialogManager {

    public static void showInfoDialog(String title, String text) {
        showDialog(title, text, Alert.AlertType.INFORMATION);
    }

    public static void showErrorDialog(String title, String text) {
        showDialog(title, text, Alert.AlertType.ERROR);
    }

    private static void showDialog(String title, String text, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }
}
