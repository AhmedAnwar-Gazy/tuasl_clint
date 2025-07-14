package orgs.tuasl_clint.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import orgs.tuasl_clint.utils.FilesHelper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FileItemController implements Initializable {

    File file;

    @FXML
    private Button deleteItem;
    @FXML
    private Button openButton;
    @FXML
    private HBox buttonsContainer;
    @FXML
    private HBox mainAllContainer;
    @FXML
    private Label fileNameLBL;

    @FXML
    private Label fileSizeLBL;

    @FXML
    private Label fileTypeLBL;

    @FXML
    void deleteItemHandler(ActionEvent event) {
        this.deleteItem();
    }

    @FXML
    void openFileButtonClicked(ActionEvent event) {
        if(action != null){
            action.OnClickItem();
        }
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.out.println("---------------------------Cannot Open The File-----------------------------");
        }
    }
    public HBox getMainContainer(){
        return this.mainAllContainer;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonsContainer.getChildren().remove(openButton);
    }

    public interface Action{
        public void OnActionDelete();
        public void OnActionCleared();
        public void OnClickItem();
    }
    public Action action;

    public void initialize() {
        this.state = State.NEW;
    }

    public void setFile(File file, Action StateChangedActions){
        if(file != null){
            this.file = file;
            this.fileNameLBL.setText(file.getName());
            this.fileSizeLBL.setText(FilesHelper.formatFileSize(FilesHelper.getFileSize(file)));
            this.fileTypeLBL.setText(FilesHelper.getFileExtension(file));
        }
        this.action = StateChangedActions;
    }
    public File getFile(){
        return this.file;
    }

    public void clear(){
        this.state = State.CLEARED;
        if(this.action != null){
            action.OnActionCleared();
        }
        this.file = null;
        Button deleteButton = this.deleteItem;
        HBox itemContainer = (HBox) deleteButton.getParent();
        HBox itemsParentContainer = (HBox) itemContainer.getParent();
        if(itemsParentContainer != null)
            itemsParentContainer.getChildren().clear();
    }
    public void deleteItem(){
        try {
            this.state = State.DELETED;
            HBox itemsParentContainer = (HBox) mainAllContainer.getParent();
            itemsParentContainer.getChildren().clear();

        } catch (ClassCastException | NullPointerException e) {
            System.err.println("Error deleting item: " + e.getMessage());
        }
        if(action != null)
            action.OnActionDelete();
    }
    public void desableCloseButton(){
        this.deleteItem.setVisible(false);
    }
    public void enableCloseButton(){
        this.deleteItem.setVisible(true);
    }

    public enum State{CLOSED,CLEARED,DELETED,NEW}
    private State state;

    public State getState(){
        return this.state;
    }
    @FXML
    void FileItemClicked(MouseEvent event) {
        System.out.println("FileItem clicked");
        if(action != null){
            action.OnClickItem();
        }
    }
}
