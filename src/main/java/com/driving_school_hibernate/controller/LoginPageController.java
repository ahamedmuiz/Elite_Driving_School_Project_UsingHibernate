package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.driving_school_hibernate.dto.UserDTO;
import com.driving_school_hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnLogin;

    @FXML
    private CheckBox chkShowPassword;

    @FXML
    private Label lblError;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtVisiblePassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Toggle password visibility
        chkShowPassword.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtVisiblePassword.setText(txtPassword.getText());
                txtVisiblePassword.setVisible(true);
                txtVisiblePassword.setManaged(true);
                txtPassword.setVisible(false);
                txtPassword.setManaged(false);
            } else {
                txtPassword.setText(txtVisiblePassword.getText());
                txtPassword.setVisible(true);
                txtPassword.setManaged(true);
                txtVisiblePassword.setVisible(false);
                txtVisiblePassword.setManaged(false);
            }
        });

        // Sync text between fields
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!chkShowPassword.isSelected()) {
                txtVisiblePassword.setText(newVal);
            }
        });
        txtVisiblePassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (chkShowPassword.isSelected()) {
                txtPassword.setText(newVal);
            }
        });
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = chkShowPassword.isSelected() ? txtVisiblePassword.getText() : txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in both fields.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<UserDTO> query = session.createQuery("FROM UserDTO WHERE username = :username", UserDTO.class);
            query.setParameter("username", username);
            UserDTO user = query.uniqueResult();

            if (user == null) {
                showError("Invalid username or password.");
                return;
            }

            // Verify hashed password using BCrypt
            if (!BCrypt.checkpw(password, user.getPassword())) {
                showError("Invalid username or password.");
                return;
            }

            // ✅ Successful login → Load Dashboard
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            AnchorPane root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
            stage.setScene(new Scene(root));
            stage.centerOnScreen();

        } catch (IOException e) {
            showError("Failed to load dashboard.");
            e.printStackTrace();
        } catch (Exception e) {
            showError("Login failed. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onClear(ActionEvent event) {
        txtUsername.clear();
        txtPassword.clear();
        txtVisiblePassword.clear();
        lblError.setVisible(false);
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }
}
