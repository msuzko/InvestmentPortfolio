package com.mec.ip.utils.tablecell;

import com.mec.ip.objects.Stock;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public class PaintTableCellRedOrGreen extends TableCell<Stock, Double> {
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

    private static String decimalFormat(double value) {
        DecimalFormat formatter = new DecimalFormat("###,###.##");
        return formatter.format(value);
    }
}
