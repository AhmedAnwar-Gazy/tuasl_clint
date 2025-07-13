package orgs.tuasl_clint.controllers;

import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import orgs.tuasl_clint.models2.Media;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class LoadItemController implements Initializable {

    @FXML
    private HBox buttonsContainer;

    @FXML
    private Button cancelDownloadButton;

    @FXML
    private Button downloadButton;

    @FXML
    private ImageView downloadImg;

    @FXML
    private ProgressIndicator downloadProgressPar;

    @FXML
    private Label fileInfoLabel;

    @FXML
    private Label fileNameLabel;

    @FXML
    private HBox readyFileContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HBox p = (HBox) this.cancelDownloadButton.getParent();
        p.getChildren().remove(this.cancelDownloadButton);
    }

    public static enum FileState{
        READY,
        IS_ON_DOWNLOADING,
        NOT_DOWNLOADED,
        FINISHED_NOW,
        TERMENATED

    }

    File file;
    Media media;

    private interface OnReadyItemListiner{
        public void onReadyItem(HBox fileItemContainer);
    }
    private interface OnDownloadingItemListiner{
        public void onDownloadingItem(long currentSize,long fileSize ,ProgressIndicator progressIndicator);
    }
    private interface OnDownloadButtonClickedListiner{
        public void onDownloadButtonClicked(Media media , File file);
    }
    private interface OnCancelDownloadButtonClickedListiner{
        public void onCancelDownloadButtonClicked(Media media , File file);
    }
    private OnDownloadingItemListiner onDownloadingItemListiner;
    private OnReadyItemListiner onReadyItemListiner;
    private OnDownloadButtonClickedListiner onDownloadButtonClickedListiner;
    private OnCancelDownloadButtonClickedListiner onCancelDownloadButtonClickedListiner;
    Thread updateDownloadingStatusThread;

    private FileState state;

    @FXML
    void handleCancelDownloadItemClicked(ActionEvent event) {

        if(this.onCancelDownloadButtonClickedListiner != null)
            this.onCancelDownloadButtonClickedListiner.onCancelDownloadButtonClicked(this.media,this.file);
        this.state = FileState.NOT_DOWNLOADED;
        HBox p = (HBox) this.cancelDownloadButton.getParent();
        p.getChildren().remove(this.cancelDownloadButton);
        p.getChildren().add(this.downloadButton);
        if(this.updateDownloadingStatusThread != null && !(this.updateDownloadingStatusThread.getState() == Thread.State.TIMED_WAITING || this.updateDownloadingStatusThread.getState() == Thread.State.WAITING)){
            try {
                this.updateDownloadingStatusThread.wait();
            } catch (InterruptedException e) {
                System.out.println("from LoadItemController: Cannot make thread Wait Error: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    void handleCancelDownloadItemClickedI(MouseEvent event) {

        if(this.onCancelDownloadButtonClickedListiner != null)
            this.onCancelDownloadButtonClickedListiner.onCancelDownloadButtonClicked(this.media,this.file);
        this.state = FileState.NOT_DOWNLOADED;
        HBox p = (HBox) this.cancelDownloadButton.getParent();
        p.getChildren().remove(this.cancelDownloadButton);
        p.getChildren().add(this.downloadButton);
        if(this.updateDownloadingStatusThread != null && !(this.updateDownloadingStatusThread.getState() == Thread.State.TIMED_WAITING || this.updateDownloadingStatusThread.getState() == Thread.State.WAITING)){
            try {
                this.updateDownloadingStatusThread.wait();
            } catch (InterruptedException e) {
                System.out.println("from LoadItemController: Cannot make thread Wait Error: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }
    @FXML
    void handleDownloadItemClicked(ActionEvent event) {
        if(this.onCancelDownloadButtonClickedListiner != null)
            this.onCancelDownloadButtonClickedListiner.onCancelDownloadButtonClicked(this.media,this.file);
        this.state = FileState.IS_ON_DOWNLOADING;
        if(this.updateDownloadingStatusThread != null){
            if(this.updateDownloadingStatusThread.getState() == Thread.State.WAITING || this.updateDownloadingStatusThread.getState() == Thread.State.TIMED_WAITING)
                this.updateDownloadingStatusThread.notify();
            else if(this.updateDownloadingStatusThread.getState() == Thread.State.NEW)
                this.updateDownloadingStatusThread.start();
            else if (this.updateDownloadingStatusThread.getState() == Thread.State.TERMINATED) {
                this.state = FileState.TERMENATED;
            }
        }

        HBox p = (HBox) this.downloadButton.getParent();
        p.getChildren().remove(this.downloadButton);
        p.getChildren().add(this.cancelDownloadButton);
    }

    public void setOnDownloadingItemListiner(OnDownloadingItemListiner onDownloadingItemListiner) {
        this.onDownloadingItemListiner = onDownloadingItemListiner;
    }

    public void setOnReadyItemListiner(OnReadyItemListiner onReadyItemListiner) {
        this.onReadyItemListiner = onReadyItemListiner;
    }

    public void setOnDownloadButtonClickedListiner(OnDownloadButtonClickedListiner onDownloadButtonClickedListiner) {
        this.onDownloadButtonClickedListiner = onDownloadButtonClickedListiner;
        this.updateDownloadingStatusThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted() &&
                        state == FileState.IS_ON_DOWNLOADING) {

                    // Safe null check
                    long currentSize = (file != null && file.exists()) ? file.length() : 0;

                    // Call listener
                    if (onDownloadingItemListiner != null) {
                        onDownloadingItemListiner.onDownloadingItem(
                                currentSize,
                                media.getFileSizeBytes(),
                                downloadProgressPar
                        );
                    }

                    // Controlled sleep with interrupt handling
                    TimeUnit.SECONDS.sleep(1);
                }
                if(this.state == FileState.READY)
                    Thread.currentThread().interrupt();
                else if( this.state == FileState.NOT_DOWNLOADED)
                    Thread.currentThread().wait();
            } catch (InterruptedException e) {
                System.out.println("----------From LoadItemController: Download status thread interrupted gracefully Error: "+e.getMessage());
                e.printStackTrace();
                Thread.currentThread().interrupt(); // Restore interrupt flag
            } catch (Exception e) {
                System.err.println("Error in download status thread: " + e.getMessage());
                e.printStackTrace();
            } finally {
                // Cleanup resources if needed
            }
        });
        updateDownloadingStatusThread.setDaemon(true);
        updateDownloadingStatusThread.start();

//        if(updateDownloadingStatusThread.getState() == Thread.State.WAITING)
//            this.updateDownloadingStatusThread.notify();
//        else {
//
//        }
            this.updateDownloadingStatusThread.start();
    }

    public void setOnCancelDownloadButtonClickedListiner(OnCancelDownloadButtonClickedListiner onCancelDownloadButtonClickedListiner) {
        this.onCancelDownloadButtonClickedListiner = onCancelDownloadButtonClickedListiner;
        try {
            this.updateDownloadingStatusThread.wait();
        } catch (InterruptedException e) {
            System.out.println("------------From LoadItemController : Cannot Do Wait For Update Item Status Thread Error: "+ e.getMessage());
            e.printStackTrace();
        }
    }

}
