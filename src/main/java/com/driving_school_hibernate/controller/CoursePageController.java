package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CoursePageController {

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
    private TableColumn<?, ?> colCourseId;

    @FXML
    private TableColumn<?, ?> colCourseName;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colFee;

    @FXML
    private TableView<?> tblCourses;

    @FXML
    private TextField txtCourseId;

    @FXML
    private TextField txtCourseName;

    @FXML
    private TextField txtDuration;

    @FXML
    private TextField txtFee;

    @FXML
    private TextField txtSearch;

    @FXML
    void handleAddCourse(ActionEvent event) {

    }

    @FXML
    void handleClearForm(ActionEvent event) {

    }

    @FXML
    void handleDeleteCourse(ActionEvent event) {

    }

    @FXML
    void handleSearchCourse(ActionEvent event) {

    }

    @FXML
    void handleUpdateCourse(ActionEvent event) {

    }

    @FXML
    void handleViewAllCourses(ActionEvent event) {

    }

}
