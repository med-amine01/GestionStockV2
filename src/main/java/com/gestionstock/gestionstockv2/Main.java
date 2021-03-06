package com.gestionstock.gestionstockv2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)  {launch();}

    //-------- TRAVAIL DÉVELOPPÉ PAR ----------- :
    // CHEBBI MOHAMED AMINE : camine500@gmail.com
    //EYA BOUAJILA : ebouajila20001304@gmail.com
}