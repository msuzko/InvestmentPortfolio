package com.mec.ip.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddDialogController {

    @FXML
    public Button btnOk;
    @FXML
    public Button btnCancel;
    @FXML
    private TextField tickerTxt;
    @FXML
    private TextField dateTxt;
    @FXML
    private TextField countTxt;
    @FXML
    private TextField costTxt;
    @FXML
    private TextField commissionTxt;

    public void actionClose(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
