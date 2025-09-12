package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DashboardPageController {

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnInstructors;

    @FXML
    private Button btnLessons;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnPayments;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnUsers;

    @FXML
    private Label lblLoggedUser;

    @FXML
    private StackPane mainContent;

    // ------------------ HANDLERS -------------------

    @FXML
    void handleProfile(ActionEvent event) {
        loadUI("/view/ProfilePage.fxml"); // profile page
    }

    @FXML
    void handleStudents(ActionEvent event) {
        loadUI("/view/StudentPage.fxml");
    }

    @FXML
    void handleCourses(ActionEvent event) {
        loadUI("/view/CoursePage.fxml");
    }

    @FXML
    void handleInstructors(ActionEvent event) {
        loadUI("/view/InstructorPage.fxml");
    }

    @FXML
    void handleLessons(ActionEvent event) {
        loadUI("/view/LessonPage.fxml");
    }

    @FXML
    void handlePayments(ActionEvent event) {
        loadUI("/view/PaymentPage.fxml");
    }

    @FXML
    void handleUsers(ActionEvent event) {
        loadUI("/view/UsersPage.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Do you really want to logout?");
        alert.setContentText("Click OK to return to the login screen.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Parent loginPage = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
                Scene loginScene = new Scene(loginPage);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(loginScene);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------ HELPER -------------------
    private void loadUI(String fxmlPath) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
