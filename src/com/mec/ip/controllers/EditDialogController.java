package com.mec.ip.controllers;

import com.mec.ip.objects.Stock;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditDialogController {

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
        stock.setTicker(txtTicker.getText());
        stock.setCount(Integer.parseInt(txtCount.getText()));
        stock.setCost(Double.parseDouble(txtCost.getText()));
        try {
            stock.setDate(formatter.parse(txtDate.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        close(actionEvent);
    }

    @FXML
    private void initialize() {
        txtTicker.textProperty().addListener((observable, oldValue, newValue) -> {
            txtTicker.setText(newValue.toUpperCase());

        });
        txtCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCount.setText(oldValue);
            }
        });
        txtCost.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                txtCost.setText(oldValue);
            }
        });
        txtDate.textProperty().addListener((observable, oldValue, newValue) -> {
            String date = newValue.replaceAll("\\.", "/");
            if (!validate(date)) {
                txtDate.setText(oldValue);
            }
        });
    }

    public boolean validate(final String date) {

        Matcher matcher = pattern.matcher(date);
        if (matcher.matches()) {
            matcher.reset();
            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false;//only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public Stock getStock() {
        return stock;
    }
}
