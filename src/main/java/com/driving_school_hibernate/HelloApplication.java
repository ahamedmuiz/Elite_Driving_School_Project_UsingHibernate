package com.driving_school_hibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

        @Override
        public void start(Stage stage) throws IOException {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("BookHive Management System");
            stage.setResizable(true);
            stage.show();
        }


    }

