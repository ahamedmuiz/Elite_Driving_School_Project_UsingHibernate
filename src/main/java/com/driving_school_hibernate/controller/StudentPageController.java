package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StudentPageController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnViewAll;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableView<?> tblStudents;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtNIC;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtStudentId;

    @FXML
    void handleAddStudent(ActionEvent event) {

    }

    @FXML
    void handleClearForm(ActionEvent event) {

    }

    @FXML
    void handleDeleteStudent(ActionEvent event) {

    }

    @FXML
    void handleSearchStudent(ActionEvent event) {

    }

    @FXML
    void handleUpdateStudent(ActionEvent event) {

    }

    @FXML
    void handleViewAllStudents(ActionEvent event) {

    }

}
