package pl.edu.wat.msk.communication.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Created by Kamil Przyborowski
 * Wojskowa Akademia Techniczna im. Jarosława Dąbrowskiego, Warszawa 2018.
 */
public class GuiApplication extends Application {

    private GuiFederate guiFederate;

    @FXML public MenuItem startBtn;
    @FXML public MenuItem stopBtn;

    @FXML public Label simTime;

    @FXML public Label tankRodzaj;
    @FXML public Label tankWielkosc;
    @FXML public Label tankPolozenie;
    @FXML public Label tankWRuchu;
    @FXML public Label tankWystrzeleniePocisku;

    @FXML public Label bulletRodzaj;
    @FXML public Label bulletWielkosc;
    @FXML public Label bulletPolozenie;
    @FXML public Label bulletWRuchu;
    @FXML public Label bulletZestrzelony;

    @FXML public Label targetPolozenie;
    @FXML public Label targetPoziomUszkodzen;
    @FXML public Label targetNiezdatny;

    @FXML public Label defenseSystemBulletPolozenie;
    @FXML public Label defenseSystemBulletWRuchu;

    @FXML public Label weatherWielkoscOpadow;
    @FXML public Label weatherSilaWiatru;
    @FXML public Label weatherKierunekWiatru;


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("msk.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setOnCloseRequest((windowEvent) -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void startSimBtn(ActionEvent event) {
        this.startBtn.setDisable(true);
        this.stopBtn.setDisable(false);
        this.guiFederate = new GuiFederate(this);
        new Thread(() -> {
            try {
                this.guiFederate.runFederate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void stopSimBtn(ActionEvent event) {
        this.startBtn.setDisable(false);
        this.stopBtn.setDisable(true);
        this.guiFederate.stopSimulation();
        this.guiFederate = null;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
