/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poe.level.fx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import poe.level.PreloadNotification.GemDownloadNotification;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author Christos
 */
public class NewFXPreloader extends Preloader {

    ProgressBar bar;
    Stage stage;
    Loading_Controller controller;

    private Scene createPreloaderScene() {
        bar = new ProgressBar();
        //BorderPane p = new BorderPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loading.fxml"));
        AnchorPane p = null;
        try {
            p = loader.load();

        } catch (IOException ex) {
            Logger.getLogger(MainApp_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        //at this point this class has done enough, and we need to contact with the 
        //appropriate controllers.
        controller = loader.<Loading_Controller>getController();
        Scene pr = new Scene(p);
        pr.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        return pr;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(createPreloaderScene());
        stage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification scn) {
        if (scn.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }

    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        //System.out.println(pn.getProgress());
        //controller.notify(pn.getProgress());
        //bar.setProgress(pn.getProgress());
    }

    @Override
   public void handleApplicationNotification(PreloaderNotification arg0) {
          if (arg0 instanceof ProgressNotification) {
             ProgressNotification pn= (ProgressNotification) arg0;
                controller.notify(pn.getProgress());
          } else if (arg0 instanceof ErrorNotification) {
              new Alert(Alert.AlertType.ERROR, ((ErrorNotification) arg0).getDetails(), ButtonType.OK).showAndWait();
              Platform.exit();
              System.exit(1);
          } else if (arg0 instanceof GemDownloadNotification){
              GemDownloadNotification err = (GemDownloadNotification) arg0;
              controller.gemDownload(err.getGemName());
          }
    }

}
