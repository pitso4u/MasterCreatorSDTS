/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.schedule;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CustomCellValueFactory implements Callback<TableColumn<Schedule, String>, TableCell<Schedule, String>> {

    @Override
    public TableCell<Schedule, String> call(TableColumn<Schedule, String> param) {
        return new TableCell<Schedule, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        };
    }
}

