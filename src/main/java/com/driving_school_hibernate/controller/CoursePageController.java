package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.BOTypes;
import com.driving_school_hibernate.bo.custom.CourseBO;
import com.driving_school_hibernate.dto.CourseDTO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class CoursePageController {

    @FXML private TextField txtCourseId;
    @FXML private TextField txtCourseName;
    @FXML private TextField txtDuration;
    @FXML private TextField txtFee;
    @FXML private TextField txtSearch;

    @FXML private TableView<CourseDTO> tblCourses;
    @FXML private TableColumn<CourseDTO, String> colCourseId;
    @FXML private TableColumn<CourseDTO, String> colCourseName;
    @FXML private TableColumn<CourseDTO, String> colDuration;
    @FXML private TableColumn<CourseDTO, Number> colFee;

    private final ObservableList<CourseDTO> courseList = FXCollections.observableArrayList();
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);

    @FXML
    public void initialize() {
        // CourseId field disabled
        txtCourseId.setEditable(false);
        txtCourseId.setDisable(true);

        colCourseId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseId()));
        colCourseName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colDuration.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDuration()));
        colFee.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFee()));

        tblCourses.setItems(courseList);

        loadCourses();
        generateCourseId();

        tblCourses.setOnMouseClicked(e -> {
            CourseDTO selected = tblCourses.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtCourseId.setText(selected.getCourseId());
                txtCourseName.setText(selected.getName());
                txtDuration.setText(selected.getDuration());
                txtFee.setText(String.valueOf(selected.getFee()));
            }
        });
    }

    @FXML
    private void handleAddCourse() {
        if (!validateForm()) return;

        CourseDTO dto = new CourseDTO(
                txtCourseId.getText(),
                txtCourseName.getText().trim(),
                txtDuration.getText().trim(),
                Double.parseDouble(txtFee.getText().trim())
        );

        try {
            courseBO.saveCourse(dto);
            courseList.add(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully.");
            clearForm();
            generateCourseId();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save course: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateCourse() {
        CourseDTO selected = tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a course.");
            return;
        }

        if (!validateForm()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Update course?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                selected.setName(txtCourseName.getText().trim());
                selected.setDuration(txtDuration.getText().trim());
                selected.setFee(Double.parseDouble(txtFee.getText().trim()));
                try {
                    courseBO.updateCourse(selected);
                    tblCourses.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Updated", "Course updated successfully.");
                    clearForm();
                    generateCourseId();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update course: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleDeleteCourse() {
        CourseDTO selected = tblCourses.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a course.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete course?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    courseBO.deleteCourse(selected.getCourseId());
                    courseList.remove(selected);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Course deleted successfully.");
                    clearForm();
                    generateCourseId();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete course: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleSearchCourse() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Search", "Please enter a course name.");
            return;
        }
        try {
            List<CourseDTO> found = courseBO.searchCoursesByName(keyword);
            courseList.setAll(found);
            if (found.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No courses found for: " + keyword);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAllCourses() {
        loadCourses();
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        generateCourseId();
    }

    // Helpers
    private void loadCourses() {
        try {
            courseList.setAll(courseBO.findAllCourses());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load courses: " + e.getMessage());
        }
    }

    private void generateCourseId() {
        try {
            txtCourseId.setText(courseBO.generateNewCourseId());
        } catch (SQLException e) {
            txtCourseId.setText("C001");
        }
    }

    private void clearForm() {
        txtCourseName.clear();
        txtDuration.clear();
        txtFee.clear();
        txtSearch.clear();
    }

    private boolean validateForm() {
        if (txtCourseName.getText().isEmpty() || txtDuration.getText().isEmpty() || txtFee.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return false;
        }
        try {
            Double.parseDouble(txtFee.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Fee must be a valid number.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
