package com.example.horseraceapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HorseBetApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HorseBetApplication.class.getResource("hello-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load(), 320, 300);
        stage.setTitle("Horse Bet");
        stage.setScene(loginScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}