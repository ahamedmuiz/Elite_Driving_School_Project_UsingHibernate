package com.driving_school_hibernate;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static void main(String[] args) {
//        Session currentSession1 = FactoryConfiguration.getInstance().getCurrentSession();
//        Session currentSession2 = FactoryConfiguration.getInstance().getCurrentSession();
//
//        System.out.println("=========== Current session");
//        System.out.println(currentSession1 == currentSession2);
//        System.out.println(currentSession1.equals(currentSession2));
//
//        System.out.println("==============================");
//        Session session1 = FactoryConfiguration.getInstance().getSession();
//        Session session2 = FactoryConfiguration.getInstance().getSession();
//
//        System.out.println(session1 == session2);
//        System.out.println(session1.equals(session2));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(
                new FXMLLoader(getClass().getResource("/view/DashboardPage.fxml")).load()
        ));
        primaryStage.show();

        Task<Scene> loadingTask = new Task<>() {
            @Override
            protected Scene call() throws Exception {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
                return new Scene(fxmlLoader.load());
            }
        };

        loadingTask.setOnSucceeded(event -> {
            Scene value = loadingTask.getValue();

            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(value);
        });

        loadingTask.setOnFailed(event -> {
            System.out.println("Fail to load application");
        });

        new Thread(loadingTask).start();

//        Parent parent = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
//        primaryStage.setScene(new Scene(parent));
//        primaryStage.show();
    }
}
