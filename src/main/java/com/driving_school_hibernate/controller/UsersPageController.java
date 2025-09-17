package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.custom.UsersBO;
import com.driving_school_hibernate.dto.UsersDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class UsersPageController {

    @FXML private TextField txtUserId;
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private TableView<UsersDTO> tblUsers;
    @FXML private TableColumn<UsersDTO, String> colUserId;
    @FXML private TableColumn<UsersDTO, String> colUsername;
    @FXML private TableColumn<UsersDTO, String> colEmail;
    @FXML private TableColumn<UsersDTO, String> colRole;
    @FXML private TextField txtSearch;

    private final UsersBO usersBO = BOFactory.getInstance().getBO(com.driving_school_hibernate.bo.BOTypes.USERS);
    private UsersDTO selectedUser;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{5,}$");

    @FXML
    public void initialize() {
        txtUserId.setEditable(false);
        txtUserId.setDisable(true);

        cmbRole.setItems(FXCollections.observableArrayList("Admin", "User"));

        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadAllUsers();
        generateNewId();

        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedUser = newVal;
                fillForm(newVal);
            }
        });
    }

    private void fillForm(UsersDTO dto) {
        txtUserId.setText(dto.getUserId());
        txtUsername.setText(dto.getUsername());
        txtEmail.setText(dto.getEmail());
        txtPassword.setText(dto.getPassword());
        cmbRole.setValue(dto.getRole());
    }

    private void clearForm() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
        tblUsers.getSelectionModel().clearSelection();
        selectedUser = null;
        generateNewId();
    }

    private void generateNewId() {
        try {
            txtUserId.setText(usersBO.generateNewUserId());
        } catch (SQLException e) {
            showError("Failed to generate User ID");
        }
    }

    private boolean validateFields() {
        if (!NAME_PATTERN.matcher(txtUsername.getText()).matches()) {
            showError("Invalid username (3-50 chars, letters/numbers/underscore).");
            return false;
        }
        if (!EMAIL_PATTERN.matcher(txtEmail.getText()).matches()) {
            showError("Invalid email format.");
            return false;
        }
        if (!PASSWORD_PATTERN.matcher(txtPassword.getText()).matches()) {
            showError("Password must be at least 5 characters.");
            return false;
        }
        if (cmbRole.getValue() == null) {
            showError("Select a role.");
            return false;
        }
        return true;
    }

    private UsersDTO getFormDTO() {
        return new UsersDTO(
                txtUserId.getText(),
                txtUsername.getText(),
                txtEmail.getText(),
                txtPassword.getText(),
                cmbRole.getValue()
        );
    }

    private void loadAllUsers() {
        try {
            List<UsersDTO> list = usersBO.findAllUsers();
            tblUsers.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError("Failed to load users.");
        }
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        try {
            if (!validateFields()) return;
            UsersDTO dto = getFormDTO();
            if (usersBO.saveUser(dto)) {
                showInfo("User added successfully.");
                loadAllUsers();
                clearForm();
            }
        } catch (Exception e) {
            showError("Failed to add user: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateUser(ActionEvent event) {
        try {
            if (selectedUser == null) {
                showError("No user selected.");
                return;
            }
            if (!validateFields()) return;

            UsersDTO dto = getFormDTO();

            boolean changed =
                    !dto.getUsername().equals(selectedUser.getUsername()) ||
                            !dto.getEmail().equals(selectedUser.getEmail()) ||
                            !dto.getPassword().equals(selectedUser.getPassword()) ||
                            !dto.getRole().equals(selectedUser.getRole());

            if (!changed) {
                showError("Nothing to update.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Update user?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (usersBO.updateUser(dto)) {
                showInfo("User updated.");
                loadAllUsers();
                clearForm();
            }
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteUser(ActionEvent event) {
        try {
            if (selectedUser == null) {
                showError("No user selected.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete user?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (usersBO.deleteUser(selectedUser.getUserId())) {
                showInfo("User deleted.");
                loadAllUsers();
                clearForm();
            }
        } catch (Exception e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    void handleSearchUser(ActionEvent event) {
        try {
            List<UsersDTO> list = usersBO.searchUsersByName(txtSearch.getText());
            tblUsers.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            showError("Search failed.");
        }
    }

    @FXML
    void handleViewAllUsers(ActionEvent event) {
        loadAllUsers();
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        clearForm();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
