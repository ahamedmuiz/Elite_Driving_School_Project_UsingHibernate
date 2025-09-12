package com.driving_school_hibernate.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LessonPageController {

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
    private ChoiceBox<?> choiceStatus;

    @FXML
    private ComboBox<?> cmbCourse;

    @FXML
    private ComboBox<?> cmbInstructor;

    @FXML
    private ComboBox<?> cmbStudent;

    @FXML
    private TableColumn<?, ?> colCourse;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colInstructor;

    @FXML
    private TableColumn<?, ?> colLessonId;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colStudent;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private DatePicker dateLesson;

    @FXML
    private TableView<?> tblLessons;

    @FXML
    private TextField txtLessonId;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTime;

    @FXML
    void handleAddLesson(ActionEvent event) {

    }

    @FXML
    void handleClearForm(ActionEvent event) {

    }

    @FXML
    void handleDeleteLesson(ActionEvent event) {

    }

    @FXML
    void handleSearchLesson(ActionEvent event) {

    }

    @FXML
    void handleUpdateLesson(ActionEvent event) {

    }

    @FXML
    void handleViewAllLessons(ActionEvent event) {

    }

}
