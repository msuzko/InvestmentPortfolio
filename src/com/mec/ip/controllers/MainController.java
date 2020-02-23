package com.mec.ip.controllers;

import com.mec.ip.interfaces.impls.CollectionPortfolio;
import com.mec.ip.objects.Stock;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    public static final String EDIT_STOCK = "Изменить позицию";
    public static final String ADD_STOCK = "Добавить позицию";
    private CollectionPortfolio portfolio = new CollectionPortfolio();

    @FXML
    public CustomTextField txtSearch;
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

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditDialogController editDialogController;
    private Stage editDialogStage;
    private Stage mainStage;
    private ResourceBundle bundle;
    private ObservableList<Stock> backupList;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        tablePortfolio.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initColumns();
        setupClearButtonField(txtSearch);
        initListeners();
        fillData();
        initLoaders();
    }

    private void setupClearButtonField(CustomTextField customTextField) {
        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, customTextField, customTextField.rightProperty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData() {
        portfolio.fillTestData();
        backupList = FXCollections.observableArrayList();
        backupList.addAll(portfolio.getStockList());
        tablePortfolio.setItems(portfolio.getStockList());
    }

    private void initColumns() {
        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
        columnTicker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        columnCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
    }

    private void initLoaders() {
        try {
            fxmlLoader.setLocation((getClass().getResource("../resource/edit.fxml")));
            fxmlLoader.setResources(ResourceBundle.getBundle("com.mec.ip.bundles.Locale", new Locale("en")));
            fxmlEdit = fxmlLoader.load();
            editDialogController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        portfolio.getStockList().addListener(
                (ListChangeListener<Stock>) c -> updateCommonMarketPrice());
        tablePortfolio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editDialogController.setStock(tablePortfolio.getSelectionModel().getSelectedItem());
                showDialog(bundle.getString("editPosition"));
            }
        });
    }

    private void updateCommonMarketPrice() {
        marketPrice.setText(String.valueOf(decimalFormat(portfolio.getSum())));
    }

    private String decimalFormat(double value) {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        return formatter.format(value);
    }

    public void actionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) return;

        Button button = (Button) source;
        switch (button.getId()) {
            case "btnAdd":
                editDialogController.setStock(new Stock());
                showDialog(bundle.getString("addPosition"));
                if (!editDialogController.isCancel()) {
                    portfolio.add(editDialogController.getStock());
                    backupList.add(editDialogController.getStock());
                }
                break;
            case "btnEdit": {
                Stock stock = tablePortfolio.getSelectionModel().getSelectedItems().get(0);
                if (stock == null) return;
                editDialogController.setStock(stock);
                showDialog(bundle.getString("editPosition"));
                updateCommonMarketPrice();
                break;
            }
            case "btnDelete": {
                List<Stock> selectedItems = new ArrayList<>(tablePortfolio.getSelectionModel().getSelectedItems());
                for (Stock stock : selectedItems) {
                    portfolio.getStockList().remove(stock);
                    backupList.remove(stock);
                }
                break;
            }
        }
    }

    public void showDialog(String title) {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setMinHeight(115);
            editDialogStage.setMinWidth(350);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }
        editDialogStage.setTitle(title);
        editDialogStage.showAndWait();
    }

    public void actionSearch(ActionEvent actionEvent) {
        portfolio.getStockList().clear();

        for (Stock stock : backupList) {
            if (stock.getTicker().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                    stock.getName().toLowerCase().contains(txtSearch.getText().toLowerCase())) {
                portfolio.getStockList().add(stock);
            }
        }


    }

}
