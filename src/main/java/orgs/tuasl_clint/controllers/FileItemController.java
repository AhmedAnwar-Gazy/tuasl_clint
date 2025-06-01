package orgs.tuasl_clint.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import orgs.tuasl_clint.utils.FilesHelper;

import java.io.File;

public class FileItemController {

    @FXML
    private Button deleteItem;

    @FXML
    private Label fileNameLBL;

    @FXML
    private Label fileSizeLBL;

    @FXML
    private Label fileTypeLBL;

    @FXML
    void deleteItemHandler(ActionEvent event) {
        try {
            Button deleteButton = (Button) event.getSource();
            HBox itemContainer = (HBox) deleteButton.getParent();
            HBox itemsParentContainer = (HBox) itemContainer.getParent();
            itemsParentContainer.getChildren().clear();
        } catch (ClassCastException | NullPointerException e) {
            System.err.println("Error deleting item: " + e.getMessage());
        }
    }

    public void setFileInfo(File file){
        this.fileNameLBL.setText(file.getName());
        this.fileSizeLBL.setText(FilesHelper.formatFileSize(FilesHelper.getFileSize(file)));
        this.fileTypeLBL.setText(FilesHelper.getFileExtension(file));
    }

}
