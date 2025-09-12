package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class InstructorPageController {

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
    private ChoiceBox<?> choiceAvailability;

    @FXML
    private TableColumn<?, ?> colAvailability;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colInstructorId;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableColumn<?, ?> colSpecialization;

    @FXML
    private TableView<?> tblInstructors;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtInstructorId;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtNIC;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSpecialization;

    @FXML
    void handleAddInstructor(ActionEvent event) {

    }

    @FXML
    void handleClearForm(ActionEvent event) {

    }

    @FXML
    void handleDeleteInstructor(ActionEvent event) {

    }

    @FXML
    void handleSearchInstructor(ActionEvent event) {

    }

    @FXML
    void handleUpdateInstructor(ActionEvent event) {

    }

    @FXML
    void handleViewAllInstructors(ActionEvent event) {

    }

}
