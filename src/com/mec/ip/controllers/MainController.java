package com.mec.ip.controllers;

import com.mec.ip.interfaces.PortfolioDAO;
import com.mec.ip.interfaces.Strategy;
import com.mec.ip.interfaces.impls.portfolio.DBPortfolio;
import com.mec.ip.interfaces.impls.FinvizStrategy;
import com.mec.ip.objects.Lang;
import com.mec.ip.objects.Stock;
import com.mec.ip.utils.DialogManager;
import com.mec.ip.utils.LocaleManager;
import com.mec.ip.utils.Math;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class MainController extends Observable implements Initializable {

    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en";
    private PortfolioDAO portfolio = new DBPortfolio();
    private Runnable updater = new UpdateData();
    private Strategy strategy = new FinvizStrategy(portfolio);


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
    public TableColumn<Stock, String> columnTitle;
    @FXML
    public TableColumn<Stock, Integer> columnCount;
    @FXML
    public TableColumn<Stock, Double> columnPrice;
    @FXML
    public TableColumn<Stock, Double> columnCurrentPrice;
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
    public Label commonPL;
    @FXML
    public ComboBox<Lang> comboLocales;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditDialogController editDialogController;
    private Stage editDialogStage;
    private Stage mainStage;
    private ResourceBundle bundle;

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
        new Thread(new Indicators()).start();
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
        fillTable();
        fillLangComboBox();
    }

    private void fillTable() {
        //  portfolio.fillTestData();
        tablePortfolio.setItems(portfolio.getStockList());
    }

    private void fillLangComboBox() {
        Lang langRU = new Lang(0, RU_CODE, bundle.getString("ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(1, EN_CODE, bundle.getString("en"), LocaleManager.EN_LOCALE);

        comboLocales.getItems().add(langRU);
        comboLocales.getItems().add(langEN);

        if (LocaleManager.getCurrentLang() == null)
            comboLocales.getSelectionModel().select(0);
        else
            comboLocales.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
    }

    private void initColumns() {
        columnDate.setCellValueFactory(new PropertyValueFactory<>("dateStr"));
        columnTicker.setCellValueFactory(new PropertyValueFactory<>("ticker"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        columnPE.setCellValueFactory(new PropertyValueFactory<>("pe"));
        columnGoal.setCellValueFactory(new PropertyValueFactory<>("goal"));
        columnCurrentPrice.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        columnPL.setCellValueFactory(new PropertyValueFactory<>("pl"));
        columnPercentPL.setCellValueFactory(new PropertyValueFactory<>("plPercent"));

        columnPL.setCellFactory(param -> new PLTableCell());
        columnPercentPL.setCellFactory(param -> new PLTableCell());

        columnGoal.setCellFactory(param -> new GoalTableCell());
    }

    private static class PLTableCell extends TableCell<Stock, Double> {
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                Stock stock = ((Stock) this.getTableRow().getItem());
                if (item != null && stock != null) {
                    if (item <= 0)
                        this.setTextFill(Color.RED);
                    else
                        this.setTextFill(Color.GREEN);
                    this.setText(decimalFormat(item));
                } else this.setText(null);
            } else this.setText(null);
        }
    }

    private static class GoalTableCell extends TableCell<Stock, Double> {

        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (!isEmpty()) {
                Stock stock = ((Stock) this.getTableRow().getItem());
                if (item != null && stock != null) {
                    if (item <= stock.getCurrentPrice())
                        this.setTextFill(Color.RED);
                    else
                        this.setTextFill(Color.GREEN);
                    this.setText(decimalFormat(item));
                } else this.setText(null);

            } else this.setText(null);
        }
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

        tablePortfolio.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editDialogController.setStock(tablePortfolio.getSelectionModel().getSelectedItem());
                showDialog(bundle.getString("editPosition"));
            }
        });
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> actionSearch());


        comboLocales.setOnAction(event -> {
            Lang selectedLang = comboLocales.getSelectionModel().getSelectedItem();
            LocaleManager.setCurrentLang(selectedLang);

            setChanged();
            notifyObservers(selectedLang);
        });
    }

    private void updateCommonMarketPrice() {
        marketPrice.setText(decimalFormat(portfolio.getCurrentSum()) + " $");
        double pl = Math.round(portfolio.getCurrentSum() - portfolio.getSum(), 2);
        double percent = Math.round(portfolio.getCurrentSum() / portfolio.getSum() * 100 - 100, 2);
        commonPL.setText(decimalFormat(pl) + " $ / " + percent + "%");
        if (pl < 0)
            commonPL.setTextFill(Paint.valueOf("red"));
        else
            commonPL.setTextFill(Paint.valueOf("green"));

    }

    private static String decimalFormat(double value) {
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
                    updateData(editDialogController.getStock());
                }

                break;
            case "btnEdit": {
                if (positionNotSelected()) return;
                Stock stock = tablePortfolio.getSelectionModel().getSelectedItems().get(0);
                editDialogController.setStock(stock);
                showDialog(bundle.getString("editPosition"));
                portfolio.update(stock);
                updateData(stock);
                break;
            }
            case "btnDelete": {
                if (positionNotSelected()) return;
                List<Stock> selectedItems = new ArrayList<>(tablePortfolio.getSelectionModel().getSelectedItems());
                for (Stock stock : selectedItems) {
                    portfolio.delete(stock);
                }
                break;
            }
        }
    }

    private boolean positionNotSelected() {
        if (tablePortfolio.getSelectionModel().getSelectedItems().size() == 0) {
            DialogManager.showErrorDialog(bundle.getString("error"), bundle.getString("select_position"));
            return true;
        }
        return false;
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

    public void actionSearch() {
        tablePortfolio.setItems(portfolio.find(txtSearch.getText()));
        updateCommonMarketPrice();
    }

    private void updateData(Stock stock) {
        new Thread(() -> {
            strategy.updateStock(stock);
            Platform.runLater(updater);
        }).start();

    }

    private class Indicators extends Observable implements Runnable {


        @Override
        public void run() {

            while (true) {
                long start = System.currentTimeMillis();
                strategy.updateDataInList(new ArrayList<>(portfolio.getStockList()));
                Platform.runLater(updater);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    break;
                }
                System.out.println("time: " + (System.currentTimeMillis() - start));
            }
        }

    }

    private class UpdateData implements Runnable {

        @Override
        public void run() {
            updateCommonMarketPrice();
            portfolio.recalculate();
        }
    }

}
