package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.custom.UsersBO;
import com.driving_school_hibernate.dto.UsersDTO;
import com.driving_school_hibernate.util.AuthUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePageController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnUpdateProfile;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtRole;
    @FXML private TextField txtUserId;
    @FXML private TextField txtUsername;

    private UsersBO usersBO;
    private UsersDTO currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usersBO = BOFactory.getInstance().getBO(com.driving_school_hibernate.bo.BOTypes.USERS);
        loadUserProfile();
        setupFieldRestrictions();
    }

    private void loadUserProfile() {
        try {
            String currentUserId = AuthUtil.getCurrentUserId();

            if ("ADMIN001".equals(currentUserId)) {
                loadDefaultAdminProfile();
                return;
            }

            currentUser = usersBO.findUserById(currentUserId);
            if (currentUser != null) {
                populateForm(currentUser);
            } else {
                showError("User profile not found!");
            }
        } catch (Exception e) {
            showError("Failed to load user profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDefaultAdminProfile() {
        currentUser = new UsersDTO();
        currentUser.setUserId("ADMIN001");
        currentUser.setUsername(AuthUtil.getCurrentUser());
        currentUser.setEmail("defaultAdmin@gmail.com");
        currentUser.setPassword("");
        currentUser.setRole("Admin");

        populateForm(currentUser);

        txtUsername.setEditable(false);
        txtUsername.setStyle("-fx-background-color: #f0f0f0;");
    }

    private void populateForm(UsersDTO user) {
        txtUserId.setText(user.getUserId());
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtRole.setText(user.getRole());

        txtPassword.clear();
        txtConfirmPassword.clear();

        applyFieldRestrictions();
    }

    private void setupFieldRestrictions() {

        txtUserId.setEditable(false);
        txtEmail.setEditable(false);
        txtRole.setEditable(false);

        txtUserId.setStyle("-fx-background-color: #f0f0f0;");
        txtEmail.setStyle("-fx-background-color: #f0f0f0;");
        txtRole.setStyle("-fx-background-color: #f0f0f0;");
    }

    private void applyFieldRestrictions() {

        if (!AuthUtil.isAdmin() || "ADMIN001".equals(currentUser.getUserId())) {
            txtUsername.setEditable(false);
            txtUsername.setStyle("-fx-background-color: #f0f0f0;");
        } else {
            txtUsername.setEditable(true);
            txtUsername.setStyle("-fx-background-color: white;");
        }
    }

    @FXML
    void handleUpdateProfile(ActionEvent event) {
        try {
            if (!validateForm()) {
                return;
            }

            if (currentUser == null) {
                showError("No user data loaded!");
                return;
            }

            boolean changesMade = checkForChanges();
            if (!changesMade) {
                showInfo("No changes detected.");
                return;
            }

            updateUserFromForm();

            if ("ADMIN001".equals(currentUser.getUserId())) {
                updateDefaultAdmin();
            } else {
                updateDatabaseUser();
            }

        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateForm() {

        if (txtUsername.isEditable()) {
            String username = txtUsername.getText().trim();
            if (username.isEmpty()) {
                showError("Username cannot be empty!");
                txtUsername.requestFocus();
                return false;
            }
            if (username.length() < 3 || username.length() > 50) {
                showError("Username must be between 3 and 50 characters!");
                txtUsername.requestFocus();
                return false;
            }
        }

        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (!password.isEmpty()) {
            if (password.length() < 5) {
                showError("Password must be at least 5 characters long!");
                txtPassword.requestFocus();
                return false;
            }
            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match!");
                txtConfirmPassword.requestFocus();
                return false;
            }
        }

        return true;
    }

    private boolean checkForChanges() {
        boolean usernameChanged = txtUsername.isEditable() &&
                !txtUsername.getText().equals(currentUser.getUsername());
        boolean passwordChanged = !txtPassword.getText().isEmpty();

        return usernameChanged || passwordChanged;
    }

    private void updateUserFromForm() {
        if (txtUsername.isEditable()) {
            currentUser.setUsername(txtUsername.getText().trim());
        }

        if (!txtPassword.getText().isEmpty()) {
            currentUser.setPassword(txtPassword.getText());
        }
    }

    private void updateDatabaseUser() throws Exception {
        boolean success = usersBO.updateUser(currentUser);
        if (success) {
            showInfo("Profile updated successfully!");
            if (txtUsername.isEditable()) {
            }
            loadUserProfile();
        } else {
            showError("Failed to update profile in database!");
        }
    }

    private void updateDefaultAdmin() {

        showInfo("Default admin profile updated (changes will persist until logout).");

        if (!txtPassword.getText().isEmpty()) {

            showInfo("Password changed for current session. Default password will be restored after logout.");
        }

        txtPassword.clear();
        txtConfirmPassword.clear();
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {

        if (txtUsername.isEditable()) {
            txtUsername.setText(currentUser != null ? currentUser.getUsername() : "");
        }
        txtPassword.clear();
        txtConfirmPassword.clear();

        showInfo("Form cleared. Non-editable fields remain unchanged.");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}