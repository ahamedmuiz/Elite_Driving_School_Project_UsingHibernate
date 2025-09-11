package com.driving_school_hibernate.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController {

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

}
