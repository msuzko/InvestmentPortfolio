package com.mec.ip.controllers;

import com.mec.ip.objects.Stock;
import com.mec.ip.utils.DialogManager;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditDialogController implements Initializable {

    @FXML
    public Button btnOk;
    @FXML
    public Button btnCancel;
    @FXML
    private TextField txtTicker;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtCount;
    @FXML
    private TextField txtCost;
    private ResourceBundle bundle;

    private Stock stock;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
    private Pattern pattern;
    private boolean isCancel;

    public boolean isCancel() {
        return isCancel;
    }

    public EditDialogController() {
        pattern = Pattern.compile(DATE_PATTERN);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bundle = resources;
        txtTicker.textProperty().addListener((observable, oldValue, newValue) -> txtTicker.setText(newValue.toUpperCase()));
        txtCount.textProperty().addListener(this::listenCount);
        txtCost.textProperty().addListener(this::listenPrice);
        txtDate.textProperty().addListener(this::listenDate);
    }

    public void setStock(Stock stock) {
        if (stock == null) return;

        this.stock = stock;
        String date = stock.getDateStr();
        if (date.isEmpty())
            txtDate.setText(formatter.format(new Date()));
        else
            txtDate.setText(date);
        txtTicker.setText(stock.getTicker());
        if (stock.getCount() != 0) {
            txtCount.setText(String.valueOf(stock.getCount()));
            txtCost.setText(String.valueOf(stock.getCost()));
        }
    }

    public void actionClose(ActionEvent actionEvent) {
        isCancel = true;
        close(actionEvent);
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        isCancel = false;
        if (valuesEmpty()) {
            return;
        }
        stock.setTicker(txtTicker.getText().trim());
        stock.setCount(Integer.parseInt(txtCount.getText()));
        stock.setCost(Double.parseDouble(txtCost.getText()));
        try {
            stock.setDate(formatter.parse(txtDate.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        close(actionEvent);
    }

    private boolean valuesEmpty() {
        boolean isValuesEmpty = false;
        StringBuilder message = new StringBuilder();

        String date = txtDate.getText().trim().replaceAll("\\.", "/");
        if (date.isEmpty())
            message.append(bundle.getString("fill_date")).append("\n");
        else if (isNotValidate(date)) {
            message.append(bundle.getString("invalid_date")).append("\n");
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

        double cost = 0;
        try {
            cost = Double.parseDouble(txtCost.getText().trim());
        } catch (NumberFormatException ignored) {
        }
        if (cost == 0) {
            message.append(bundle.getString("fill_price"));
            isValuesEmpty = true;
        }

        if (isValuesEmpty)
            DialogManager.showErrorDialog("error", message.toString());

        return isValuesEmpty;
    }

    public boolean isNotValidate(final String date) {

        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            matcher.reset();
            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") ||
                                month.equals("9") || month.equals("11") ||
                                month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return true;//only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        return day.equals("30") || day.equals("31");
                    } else {
                        return day.equals("29") || day.equals("30") || day.equals("31");
                    }
                } else return false;
            } else return true;
        } else return true;
    }

    public Stock getStock() {
        return stock;
    }

    private void listenDate(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,2}([.]\\d{0,2}([.]\\d{0,4})?)?")) {
            txtDate.setText(oldValue);
        }
    }

    private void listenPrice(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d{0,7}([.]\\d{0,4})?")) {
            txtCost.setText(oldValue);
        }
    }

    private void listenCount(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
            txtCount.setText(oldValue);
        }
    }
}
