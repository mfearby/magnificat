package com.marcfearby.widgets;

import com.marcfearby.components.PlainTabController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FilesTableController implements Initializable {

    private PlainTabController tab;

    @FXML
    private TableView<File> table;

    @FXML
    private TableColumn<File, String> colName;

    @FXML
    private TableColumn<File, String> colSize;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }


    private void setupTable() {
        colName.setCellValueFactory((TableColumn.CellDataFeatures<File, String> param) -> {
            return new SimpleStringProperty(param.getValue().getName());
        });
        colSize.setCellValueFactory((TableColumn.CellDataFeatures<File, String> param) -> {
            String fancy = FileUtils.byteCountToDisplaySize(param.getValue().length());
            return new SimpleStringProperty(fancy);
        });
    }


    public void init(PlainTabController tab, File directory) {
        this.tab = tab;
        selectFolder(directory);
    }


    public void selectFolder(File directory) {
        ObservableList<File> data = FXCollections.observableArrayList();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory() && !f.getName().startsWith(".") && f.getName().endsWith("mp3")) {
                    data.add(f);
                }
            }
        }

        table.setItems(data);
    }

}
