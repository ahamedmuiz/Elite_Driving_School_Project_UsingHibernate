package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.util.AuthUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML private Button btnClear;
    @FXML private Button btnLogin;
    @FXML private CheckBox chkShowPassword;
    @FXML private Label lblError;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsername;
    @FXML private TextField txtVisiblePassword;

    @FXML
    public void initialize() {
        // Password visibility toggle
        chkShowPassword.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtVisiblePassword.setText(txtPassword.getText());
                txtVisiblePassword.setVisible(true);
                txtPassword.setVisible(false);
            } else {
                txtPassword.setText(txtVisiblePassword.getText());
                txtPassword.setVisible(true);
                txtVisiblePassword.setVisible(false);
            }
        });

        // Clear button action
        btnClear.setOnAction(event -> clearForm());

        // Login button action
        btnLogin.setOnAction(event -> handleLogin());

        // Enter key support for login
        txtPassword.setOnAction(event -> handleLogin());
        txtVisiblePassword.setOnAction(event -> handleLogin());
        txtUsername.setOnAction(event -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = chkShowPassword.isSelected() ?
                txtVisiblePassword.getText() : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in both fields.").show();
            return;

        }

        if (AuthUtil.authenticate(username, password)) {
            lblError.setVisible(false);
            navigateToDashboard();
        } else {
            new Alert(Alert.AlertType.ERROR, "Wrong username or password").show();
        }
    }

    private void navigateToDashboard() {
        try {
            // For now, navigate to UsersPage for both roles since we don't have separate dashboards
            // You can create separate dashboard FXML files later
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardPage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Elite Driving School - User Management");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            // Fallback: Show success message and close login
            showSuccessAndClose();
        }
    }

    private void showSuccessAndClose() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Successful");
        alert.setHeaderText("Welcome, " + AuthUtil.getCurrentUser());
        alert.setContentText("Role: " + AuthUtil.getCurrentRole() +
                "\n\nNote: Dashboard navigation failed. Please contact administrator. 0769094100");
        alert.showAndWait();
    }

    private void clearForm() {
        txtUsername.clear();
        txtPassword.clear();
        txtVisiblePassword.clear();
        lblError.setVisible(false);
        chkShowPassword.setSelected(false);
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}