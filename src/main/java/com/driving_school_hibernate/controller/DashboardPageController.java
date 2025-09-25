package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.util.AuthUtil;
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

    @FXML private Button btnCourses;
    @FXML private Button btnProfile;
    @FXML private Button btnInstructors;
    @FXML private Button btnLessons;
    @FXML private Button btnLogout;
    @FXML private Button btnPayments;
    @FXML private Button btnStudents;
    @FXML private Button btnUsers;
    @FXML private Label lblLoggedUser;
    @FXML private StackPane mainContent;

    @FXML
    public void initialize() {
        // Display logged-in user info
        lblLoggedUser.setText("Welcome, " + AuthUtil.getCurrentUser() +
                " (" + AuthUtil.getCurrentRole() + ")");

        // Apply role-based access control
        applyRoleBasedAccess();

        // Load default page based on role
        loadDefaultPage();
    }

    private void applyRoleBasedAccess() {
        if (!AuthUtil.isAdmin()) {
            // Disable admin-only features for regular users
            btnUsers.setDisable(true);
            btnUsers.setVisible(false); // Or just disable: btnUsers.setDisable(true);

            // You can also disable other admin-only features if needed
            // btnStudents.setDisable(true);
            // btnInstructors.setDisable(true);
        }

        // Example: Only admin can manage users and view all payments
        if (AuthUtil.isUser()) {
            // Regular users might have limited access
            btnUsers.setDisable(true);
            btnUsers.setVisible(false);

            // Optionally disable other features for users
            // btnPayments.setDisable(true);
            // btnInstructors.setDisable(true);
        }
    }

    private void loadDefaultPage() {
       if(AuthUtil.isAdmin() || AuthUtil.isUser()) {
           loadUI("/view/ProfilePage.fxml");
       }
       else  {
           loadUI("/view/LoginPage.fxml");

       }
    }

    // ------------------ HANDLERS -------------------
    @FXML
    void handleProfile(ActionEvent event) {
        loadUI("/view/ProfilePage.fxml");
    }

    @FXML
    void handleStudents(ActionEvent event) {
        if (checkAccess("Student Management")) {
            loadUI("/view/StudentPage.fxml");
        }
    }

    @FXML
    void handleCourses(ActionEvent event) {
        if (checkAccess("Course Management")) {
            loadUI("/view/CoursePage.fxml");
        }
    }

    @FXML
    void handleInstructors(ActionEvent event) {
        if (checkAccess("Instructor Management")) {
            loadUI("/view/InstructorPage.fxml");
        }
    }

    @FXML
    void handleLessons(ActionEvent event) {
        if (checkAccess("Lesson Management")) {
            loadUI("/view/LessonPage.fxml");
        }
    }

    @FXML
    void handlePayments(ActionEvent event) {
        if (checkAccess("Payment Management")) {
            loadUI("/view/PaymentPage.fxml");
        }
    }

    @FXML
    void handleUsers(ActionEvent event) {
        if (checkAccess("User Management")) {
            loadUI("/view/UsersPage.fxml");
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Do you really want to logout?");
        alert.setContentText("Click OK to return to the login screen.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AuthUtil.logout();
            navigateToLogin(event);
        }
    }

    // ------------------ HELPER METHODS -------------------
    private boolean checkAccess(String feature) {
        if (feature.equals("User Management") && !AuthUtil.isAdmin()) {
            showAccessDenied("User Management is only available for administrators.");
            return false;
        }

        // Add more access checks as needed
        if (feature.equals("Student Management") && AuthUtil.isUser()) {
            // Example: Only admin and instructors can manage students
            // if (!AuthUtil.isAdmin() && !AuthUtil.isInstructor()) {
            //     showAccessDenied("Student management requires admin or instructor privileges.");
            //     return false;
            // }
        }

        return true;
    }

    private void showAccessDenied(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Permissions");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUI(String fxmlPath) {
        try {
            Parent node = FXMLLoader.load(getClass().getResource(fxmlPath));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(node);
        } catch (IOException e) {
            showErrorPage("Failed to load: " + fxmlPath);
        }
    }

    private void showErrorPage(String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        mainContent.getChildren().clear();
        mainContent.getChildren().add(errorLabel);
    }

    private void navigateToLogin(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
            Scene loginScene = new Scene(loginPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}