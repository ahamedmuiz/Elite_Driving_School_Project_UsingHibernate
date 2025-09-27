package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.BOTypes;
import com.driving_school_hibernate.bo.custom.InstructorBO;
import com.driving_school_hibernate.dto.InstructorDTO;
import com.driving_school_hibernate.util.AuthUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class InstructorPageController {

    @FXML private TableView<InstructorDTO> tblInstructors;
    @FXML private TableColumn<InstructorDTO, String> colInstructorId, colFirstName, colLastName, colNIC, colPhone, colEmail, colSpecialization, colAvailability;
    @FXML private TextField txtInstructorId, txtFirstName, txtLastName, txtNIC, txtPhone, txtEmail, txtSpecialization, txtSearch;
    @FXML private ChoiceBox<String> choiceAvailability;
    @FXML private Button btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnViewAll;

    private final InstructorBO instructorBO = BOFactory.getInstance().getBO(BOTypes.INSTRUCTOR);
    private final ObservableList<InstructorDTO> instructorList = FXCollections.observableArrayList();

    private InstructorDTO selectedInstructor = null;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
    private static final Pattern NIC_PATTERN = Pattern.compile("^[0-9]{9}[vVxX]$|^[0-9]{12}$");

    @FXML
    public void initialize() {
        txtInstructorId.setEditable(false);
        txtInstructorId.setDisable(true);

        applyRoleBasedAccess();

        colInstructorId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getInstructorId()));
        colFirstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));
        colLastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));
        colNIC.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNic()));
        colPhone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPhone()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colSpecialization.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSpecialization()));
        colAvailability.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAvailability()));

        tblInstructors.setItems(instructorList);

        tblInstructors.setOnMouseClicked(e -> {
            InstructorDTO selected = tblInstructors.getSelectionModel().getSelectedItem();
            if (selected != null) {
                setFormData(selected);
                selectedInstructor = selected;
            }
        });

        try {
            loadAllInstructors();
            setNewInstructorId();
        } catch (SQLException e) {
            showError("Initialization failed: " + e.getMessage());
        }
    }

    private void applyRoleBasedAccess() {

        if (!AuthUtil.isAdmin()) {
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        }

    }

    private void setNewInstructorId() throws SQLException {
        txtInstructorId.setText(instructorBO.generateNewInstructorId());
    }

    private void loadAllInstructors() throws SQLException {
        instructorList.setAll(instructorBO.findAllInstructors());
    }

    private void setFormData(InstructorDTO dto) {
        txtInstructorId.setText(dto.getInstructorId());
        txtFirstName.setText(dto.getFirstName());
        txtLastName.setText(dto.getLastName());
        txtNIC.setText(dto.getNic());
        txtPhone.setText(dto.getPhone());
        txtEmail.setText(dto.getEmail());
        txtSpecialization.setText(dto.getSpecialization());
        choiceAvailability.setValue(dto.getAvailability());
    }

    private boolean validateFields() {
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                txtNIC.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtSpecialization.getText().isEmpty() ||
                choiceAvailability.getValue() == null) {
            showError("All fields must be filled!");
            return false;
        }
        if (!NAME_PATTERN.matcher(txtFirstName.getText()).matches() ||
                !NAME_PATTERN.matcher(txtLastName.getText()).matches()) {
            showError("Invalid name format.");
            return false;
        }
        if (!NIC_PATTERN.matcher(txtNIC.getText()).matches()) {
            showError("Invalid NIC format.");
            return false;
        }
        if (!PHONE_PATTERN.matcher(txtPhone.getText()).matches()) {
            showError("Invalid phone number.");
            return false;
        }
        if (!EMAIL_PATTERN.matcher(txtEmail.getText()).matches()) {
            showError("Invalid email format.");
            return false;
        }
        return true;
    }

    private InstructorDTO getFormDTO() {
        return new InstructorDTO(
                txtInstructorId.getText(),
                txtFirstName.getText(),
                txtLastName.getText(),
                txtNIC.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                txtSpecialization.getText(),
                choiceAvailability.getValue()
        );
    }

    @FXML
    void handleAddInstructor(ActionEvent event) {
        try {
            if (!validateFields()) return;

            if (selectedInstructor != null && selectedInstructor.getInstructorId().equals(txtInstructorId.getText())) {
                showError("Cannot add: A table row is selected. Use Update instead.");
                return;
            }

            InstructorDTO dto = getFormDTO();
            if (instructorBO.saveInstructor(dto)) {
                showInfo("Instructor added successfully.");
                loadAllInstructors();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateInstructor(ActionEvent event) {
        try {
            if (!validateFields()) return;

            if (selectedInstructor == null) {
                showError("No instructor selected to update.");
                return;
            }

            InstructorDTO dto = getFormDTO();

            boolean changed =
                    !dto.getFirstName().equals(selectedInstructor.getFirstName()) ||
                            !dto.getLastName().equals(selectedInstructor.getLastName()) ||
                            !dto.getNic().equals(selectedInstructor.getNic()) ||
                            !dto.getPhone().equals(selectedInstructor.getPhone()) ||
                            !dto.getEmail().equals(selectedInstructor.getEmail()) ||
                            !dto.getSpecialization().equals(selectedInstructor.getSpecialization()) ||
                            !dto.getAvailability().equals(selectedInstructor.getAvailability());

            if (!changed) {
                showError("Nothing to update. No changes detected.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Update instructor?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (instructorBO.updateInstructor(dto)) {
                showInfo("Instructor updated successfully.");
                loadAllInstructors();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }


    @FXML
    void handleDeleteInstructor(ActionEvent event) {
        try {
            if (selectedInstructor == null) {
                showError("No instructor selected to delete.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete instructor?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (instructorBO.deleteInstructor(txtInstructorId.getText())) {
                showInfo("Instructor deleted successfully.");
                loadAllInstructors();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    void handleSearchInstructor(ActionEvent event) {
        try {
            List<InstructorDTO> results = instructorBO.searchInstructors(txtSearch.getText());
            instructorList.setAll(results);
        } catch (Exception e) {
            showError("Search failed: " + e.getMessage());
        }
    }

    @FXML
    void handleViewAllInstructors(ActionEvent event) {
        try {
            loadAllInstructors();
        } catch (Exception e) {
            showError("Failed to load instructors: " + e.getMessage());
        }
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        txtFirstName.clear();
        txtLastName.clear();
        txtNIC.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtSpecialization.clear();
        txtSearch.clear();
        choiceAvailability.setValue(null);

        selectedInstructor = null;

        try {
            setNewInstructorId();
        } catch (SQLException e) {
            showError("Failed to generate ID: " + e.getMessage());
        }
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }
}
