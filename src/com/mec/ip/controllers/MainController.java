package com.mec.ip.controllers;

import com.mec.ip.interfaces.impls.CollectionPortfolio;
import com.mec.ip.objects.Stock;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController {


    private CollectionPortfolio portfolio = new CollectionPortfolio();

    @FXML
    public Button btnAdd;
    @FXML
    public Button btnEdit;
    @FXML
    public Button btnDelete;
    @FXML
    public TableColumn<Stock, String> columnDate;
    @FXML
    public TableColumn<Stock, String> columnTicker;
    @FXML
    public TableColumn<Stock, String> columnName;
    @FXML
    public TableColumn<Stock, Integer> columnCount;
    @FXML
    public TableColumn<Stock, Double> columnCost;
    @FXML
    public TableColumn<Stock, Double> columnCurrentCost;
    @FXML
    public TableColumn<Stock, Double> columnAmount;
    @FXML
    public TableColumn<Stock, Double> columnWeight;
    @FXML
    public TableColumn<Stock, Double> columnPL;
    @FXML
    public TableColumn<Stock, Double> columnPercentPL;
    @FXML
    public TableColumn<Stock, Double> columnPE;
    @FXML
    public TableColumn<Stock, Double> columnGoal;
    @FXML
    public TableView<Stock> tablePortfolio;
    @FXML
    public Label marketPrice;

    


    @FXML
    private void initialize() {
        tablePortfolio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
        columnTicker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        columnCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        portfolio.getStockList().addListener(
                (ListChangeListener<Stock>) c -> updateCommonMarketPrice());

        portfolio.fillTestData();

        tablePortfolio.setItems(portfolio.getStockList());


    }

    private void updateCommonMarketPrice() {
        marketPrice.setText(String.valueOf(decimalFormat(portfolio.getSum())));
    }

    private String decimalFormat(double value) {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        return formatter.format(value);
    }

    public void showDialog(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) return;

        Button button = (Button) source;
        switch (button.getId()) {
            case "btnAdd":
                showDialog("Добавить позицию", actionEvent, "../resource/add.fxml");
                break;
            case "btnEdit":
                showDialog("Изменить позицию", actionEvent, "../resource/add.fxml");
                break;
            case "btnDelete":
                List<Stock> selectedItems = new ArrayList<>(tablePortfolio.getSelectionModel().getSelectedItems());
                for (Stock stock : selectedItems)
                    portfolio.getStockList().remove(stock);
                break;
        }
    }

    public void showDialog(String title, ActionEvent actionEvent, String fxml) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            stage.setTitle(title);
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


    private Window getOwner(ActionEvent actionEvent) {
        return ((Node) actionEvent.getSource()).getScene().getWindow();
    }
}
