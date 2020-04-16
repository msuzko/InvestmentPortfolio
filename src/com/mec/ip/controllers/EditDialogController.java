package com.mec.ip.controllers;

import com.mec.ip.objects.Stock;
import com.mec.ip.utils.DialogManager;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;


public class EditDialogController implements Initializable {

    @FXML
    public Button btnOk;
    @FXML
    public Button btnCancel;
    @FXML
    private TextField txtTicker;
    @FXML
    private DatePicker txtDate;
    @FXML
    private TextField txtCount;
    @FXML
    private TextField txtPrice;
    private ResourceBundle bundle;

    private Stock stock;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private boolean isSave;


    public boolean isSave() {
        return isSave;
    }

    public EditDialogController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        txtTicker.textProperty().addListener((observable, oldValue, newValue) -> txtTicker.setText(newValue.toUpperCase()));
        txtCount.textProperty().addListener(this::listenCount);
        txtPrice.textProperty().addListener(this::listenPrice);
    }

    public void setStock(Stock stock) {
        if (stock == null) return;

        this.stock = stock;
        String date = stock.getDateStr();
        if (date.isEmpty())
            txtDate.setValue(LocalDate.now());
        else {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(formatter.parse(date));
                txtDate.setValue(LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txtTicker.setText(stock.getTicker());
        if (stock.getCount() != 0) {
            txtCount.setText(String.valueOf(stock.getCount()));
            txtPrice.setText(String.valueOf(stock.getPurchasePrice()));
        } else {
            txtCount.setText("");
            txtPrice.setText("");
        }
    }

    public void actionCancel(ActionEvent actionEvent) {
        isSave = false;
        close(actionEvent);
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        isSave = true;
        if (valuesEmpty()) {
            return;
        }
        stock.setTicker(txtTicker.getText().trim());
        stock.setCount(Integer.parseInt(txtCount.getText()));
        stock.setPurchasePrice(Double.parseDouble(txtPrice.getText()));

        try {
            stock.setDate(formatter.parse(txtDate.getEditor().getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        close(actionEvent);
    }


    private boolean valuesEmpty() {
        boolean isValuesEmpty = false;
        StringBuilder message = new StringBuilder();
        if (txtDate.getValue() == null) {
            message.append(bundle.getString("fill_date")).append("\n");
            isValuesEmpty = true;
        }

        if (txtTicker.getText().trim().isEmpty()) {
            message.append(bundle.getString("fill_ticker")).append("\n");
            isValuesEmpty = true;
        }

        int count = 0;
        try {
            count = Integer.parseInt(txtCount.getText().trim());
        } catch (NumberFormatException ignored) {
        }
        if (count == 0) {
            message.append(bundle.getString("fill_count")).append("\n");
            isValuesEmpty = true;
        }

        double price = 0;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
        } catch (NumberFormatException ignored) {
        }
        if (price == 0) {
            message.append(bundle.getString("fill_price"));
            isValuesEmpty = true;
        }

        if (isValuesEmpty)
            DialogManager.showErrorDialog("error", message.toString());

        return isValuesEmpty;
    }

    public Stock getStock() {
        return stock;
    }

    private void listenPrice(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
            txtPrice.setText(oldValue);
        }
    }

    private void listenCount(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            txtCount.setText(oldValue);
        }
    }
}
