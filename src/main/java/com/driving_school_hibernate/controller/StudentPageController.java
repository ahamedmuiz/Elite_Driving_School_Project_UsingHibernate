package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dto.StudentDTO;
import com.driving_school_hibernate.entity.StudentEntity;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.util.AuthUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.regex.Pattern;

public class StudentPageController {

    // ===================== Form Fields =====================
    @FXML private TextField txtStudentId;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtNIC;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextArea txtAddress;
    @FXML private TextField txtSearch;

    // ===================== Table =====================
    @FXML private TableView<StudentDTO> tblStudents;
    @FXML private TableColumn<StudentDTO, String> colId;
    @FXML private TableColumn<StudentDTO, String> colFirstName;
    @FXML private TableColumn<StudentDTO, String> colLastName;
    @FXML private TableColumn<StudentDTO, String> colNIC;
    @FXML private TableColumn<StudentDTO, String> colPhone;
    @FXML private TableColumn<StudentDTO, String> colEmail;
    @FXML private TableColumn<StudentDTO, String> colAddress;

    @FXML private Button btnDelete;

    // ===================== Data =====================
    private final ObservableList<StudentDTO> studentList = FXCollections.observableArrayList();

    // ===================== Regex Patterns =====================
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");

    // ===================== Initialization =====================
    @FXML
    public void initialize() {
        txtStudentId.setEditable(false);
        txtStudentId.setDisable(true);

        applyRoleBasedAccess();

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentId()));
        colFirstName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirstName()));
        colLastName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLastName()));
        colNIC.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNic()));
        colPhone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colAddress.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAddress()));

        tblStudents.setItems(studentList);

        loadStudentsFromDB();
        generateNewStudentId();

        tblStudents.setOnMouseClicked(this::handleRowSelect);
    }

    private void applyRoleBasedAccess() {
        if (!AuthUtil.isAdmin()) {
            btnDelete.setDisable(true);
        }
    }

    // ===================== Add Student =====================
    @FXML
    private void handleAddStudent() {
        if (!validateForm()) return;

        if (isNicExists(txtNIC.getText().trim(), null)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "NIC already exists in database.");
            return;
        }

        String newId = txtStudentId.getText();
        StudentDTO dto = new StudentDTO(
                newId,
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                txtNIC.getText().trim(),
                txtPhone.getText().trim(),
                txtEmail.getText().trim(),
                txtAddress.getText().trim()
        );

        StudentEntity entity = EntityDTOConvertor.toEntity(dto);

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction tx = session.beginTransaction();
        session.save(entity);
        tx.commit();
        session.close();

        studentList.add(dto);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");

        clearForm();
        generateNewStudentId();
    }

    // ===================== Update Student =====================
    @FXML
    private void handleUpdateStudent() {
        StudentDTO selectedDTO = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedDTO == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to update.");
            return;
        }

        if (!validateForm()) return;

        if (isNicExists(txtNIC.getText().trim(), selectedDTO.getStudentId())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "NIC already exists in database.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to update this student?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                selectedDTO.setFirstName(txtFirstName.getText().trim());
                selectedDTO.setLastName(txtLastName.getText().trim());
                selectedDTO.setNic(txtNIC.getText().trim());
                selectedDTO.setPhone(txtPhone.getText().trim());
                selectedDTO.setEmail(txtEmail.getText().trim());
                selectedDTO.setAddress(txtAddress.getText().trim());

                StudentEntity entity = EntityDTOConvertor.toEntity(selectedDTO);

                Session session = FactoryConfiguration.getInstance().getSession();
                Transaction tx = session.beginTransaction();
                session.update(entity);
                tx.commit();
                session.close();

                tblStudents.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Updated", "Student updated successfully.");
                clearForm();
                generateNewStudentId();
            }
        });
    }

    // ===================== Delete Student =====================
    @FXML
    private void handleDeleteStudent() {
        StudentDTO selectedDTO = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedDTO == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                StudentEntity entity = EntityDTOConvertor.toEntity(selectedDTO);

                Session session = FactoryConfiguration.getInstance().getSession();
                Transaction tx = session.beginTransaction();
                session.delete(entity);
                tx.commit();
                session.close();

                studentList.remove(selectedDTO);
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Student deleted successfully.");
                clearForm();
                generateNewStudentId();
            }
        });
    }

    // ===================== Search & View All =====================
    @FXML
    private void handleSearchStudent() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a name to search.");
            return;
        }

        ObservableList<StudentDTO> filtered = FXCollections.observableArrayList();
        for (StudentDTO s : studentList) {
            if (s.getFirstName().toLowerCase().contains(keyword) ||
                    s.getLastName().toLowerCase().contains(keyword)) {
                filtered.add(s);
            }
        }

        tblStudents.setItems(filtered);

        if (filtered.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No students found with name: " + keyword);
        }
    }

    @FXML
    private void handleViewAllStudents() {
        tblStudents.setItems(studentList);
    }

    // ===================== Row Select =====================
    private void handleRowSelect(MouseEvent event) {
        StudentDTO selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtStudentId.setText(selected.getStudentId());
            txtFirstName.setText(selected.getFirstName());
            txtLastName.setText(selected.getLastName());
            txtNIC.setText(selected.getNic());
            txtPhone.setText(selected.getPhone());
            txtEmail.setText(selected.getEmail());
            txtAddress.setText(selected.getAddress());
        }
    }

    // ===================== Helpers =====================
    @FXML
    private void handleClearForm() {
        clearForm();
        generateNewStudentId();
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        txtNIC.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
    }

    private void generateNewStudentId() {
        int maxId = 0;
        for (StudentDTO s : studentList) {
            try {
                int num = Integer.parseInt(s.getStudentId().substring(1));
                if (num > maxId) maxId = num;
            } catch (NumberFormatException ignored) {}
        }
        txtStudentId.setText("S00" + (maxId + 1));
    }

    private boolean validateForm() {
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                txtNIC.getText().isEmpty() || txtPhone.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields.");
            return false;
        }

        if (!NAME_PATTERN.matcher(txtFirstName.getText().trim()).matches() ||
                !NAME_PATTERN.matcher(txtLastName.getText().trim()).matches()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "First or Last name is invalid.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Email format is invalid.");
            return false;
        }

        if (!PHONE_PATTERN.matcher(txtPhone.getText().trim()).matches()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Phone number is invalid.");
            return false;
        }

        return true;
    }

    private boolean isNicExists(String nic, String excludeStudentId) {
        Session session = FactoryConfiguration.getInstance().getSession();
        String hql = "FROM StudentEntity s WHERE s.nic = :nic";
        if (excludeStudentId != null) hql += " AND s.studentId != :id";

        var query = session.createQuery(hql, StudentEntity.class);
        query.setParameter("nic", nic);
        if (excludeStudentId != null) query.setParameter("id", excludeStudentId);

        boolean exists = !query.list().isEmpty();
        session.close();
        return exists;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void loadStudentsFromDB() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<StudentEntity> entities = session.createQuery("FROM StudentEntity", StudentEntity.class).list();
        for (StudentEntity e : entities) studentList.add(EntityDTOConvertor.fromEntity(e));
        session.close();
    }
}
