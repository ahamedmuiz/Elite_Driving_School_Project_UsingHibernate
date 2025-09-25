package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.BOTypes;
import com.driving_school_hibernate.bo.custom.PaymentBO;
import com.driving_school_hibernate.bo.custom.StudentBO;
import com.driving_school_hibernate.bo.custom.CourseBO;
import com.driving_school_hibernate.dto.PaymentDTO;
import com.driving_school_hibernate.dto.StudentDTO;
import com.driving_school_hibernate.dto.CourseDTO;
import com.driving_school_hibernate.util.AuthUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {

    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSearch;
    @FXML private Button btnUpdate;
    @FXML private Button btnViewAll;

    @FXML private ChoiceBox<String> choiceMethod;
    @FXML private ChoiceBox<String> choiceStatus;

    @FXML private ComboBox<String> cmbCourse;
    @FXML private ComboBox<String> cmbStudent;

    @FXML private TableColumn<PaymentDTO, String> colAmount;
    @FXML private TableColumn<PaymentDTO, String> colCourse;
    @FXML private TableColumn<PaymentDTO, String> colDate;
    @FXML private TableColumn<PaymentDTO, String> colMethod;
    @FXML private TableColumn<PaymentDTO, String> colPaymentId;
    @FXML private TableColumn<PaymentDTO, String> colStatus;
    @FXML private TableColumn<PaymentDTO, String> colStudent;

    @FXML private DatePicker datePayment;
    @FXML private TableView<PaymentDTO> tblPayments;
    @FXML private TextField txtAmount;
    @FXML private TextField txtPaymentId;
    @FXML private TextField txtSearch;



    private final PaymentBO paymentBO = BOFactory.getInstance().getBO(BOTypes.PAYMENT);
    private final StudentBO studentBO = BOFactory.getInstance().getBO(BOTypes.STUDENT);
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);

    private ObservableList<PaymentDTO> paymentList = FXCollections.observableArrayList();
    private ObservableList<String> studentList = FXCollections.observableArrayList();
    private ObservableList<String> courseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUI();
        loadStudents();
        loadCourses();
        loadAllPayments();
        generateNextPaymentId();
        setCurrentDate();
        applyRoleBasedAccess();
    }

    private void initializeUI() {
        // Setup table columns
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Setup choice boxes
        choiceMethod.setItems(FXCollections.observableArrayList("Cash", "Card"));
        choiceStatus.setItems(FXCollections.observableArrayList("Advance Payment", "Full Payment"));

        // Set default values
        choiceMethod.setValue("Cash");
        choiceStatus.setValue("Advance Payment");


        // Make payment ID non-editable and disabled
        txtPaymentId.setEditable(false);
        txtPaymentId.setDisable(true);

        // Set current date
        datePayment.setValue(LocalDate.now());

        // Add table selection listener
        tblPayments.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        setPaymentData(newValue);
                    }
                }
        );
    }

    private void applyRoleBasedAccess() {
        if (!AuthUtil.isAdmin()) {
            btnDelete.setDisable(true);
        }
    }

    private void loadStudents() {
        try {
            List<StudentDTO> students = studentBO.findAllStudents();
            studentList.clear();
            for (StudentDTO student : students) {
                studentList.add(student.getStudentId() + " - " + student.getFirstName() + " " + student.getLastName());
            }
            cmbStudent.setItems(studentList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading students: " + e.getMessage());
        }
    }

    private void loadCourses() {
        try {
            List<CourseDTO> courses = courseBO.findAllCourses();
            courseList.clear();
            for (CourseDTO course : courses) {
                courseList.add(course.getCourseId() + " - " + course.getName() + " (LKR " + course.getFee() + ")");
            }
            cmbCourse.setItems(courseList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading courses: " + e.getMessage());
        }
    }

    private void loadAllPayments() {
        try {
            List<PaymentDTO> payments = paymentBO.findAllPayments();
            paymentList.setAll(payments);
            tblPayments.setItems(paymentList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading payments: " + e.getMessage());
        }
    }

    private void generateNextPaymentId() {
        try {
            String nextId = paymentBO.generateNextPaymentId();
            txtPaymentId.setText(nextId);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error generating payment ID: " + e.getMessage());
        }
    }

    private void setCurrentDate() {
        datePayment.setValue(LocalDate.now());
    }

    @FXML
    void handleAddPayment(ActionEvent event) {
        try {
            if (!validateInput()) return;

            PaymentDTO paymentDTO = getPaymentDataFromForm();

            // Check for duplicate payment
            if (paymentBO.isDuplicatePayment(paymentDTO.getStudentId(), paymentDTO.getCourseId())) {
                showAlert(Alert.AlertType.WARNING, "This student has already paid for the selected course.");
                return;
            }

            boolean saved = paymentBO.savePayment(paymentDTO);
            if (saved) {
                showAlert(Alert.AlertType.INFORMATION, "Payment added successfully!");
                clearForm();
                loadAllPayments();
                generateNextPaymentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to add payment.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error adding payment: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdatePayment(ActionEvent event) {
        try {
            if (!validateInput()) return;
            if (txtPaymentId.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please select a payment to update.");
                return;
            }

            PaymentDTO paymentDTO = getPaymentDataFromForm();
            boolean updated = paymentBO.updatePayment(paymentDTO);

            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Payment updated successfully!");
                clearForm();
                loadAllPayments();
                generateNextPaymentId();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to update payment.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error updating payment: " + e.getMessage());
        }
    }

    @FXML
    void handleDeletePayment(ActionEvent event) {
        try {
            String paymentId = txtPaymentId.getText();
            if (paymentId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Please select a payment to delete.");
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Delete");
            confirmation.setHeaderText("Delete Payment");
            confirmation.setContentText("Are you sure you want to delete this payment?");

            if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                boolean deleted = paymentBO.deletePayment(paymentId);
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Payment deleted successfully!");
                    clearForm();
                    loadAllPayments();
                    generateNextPaymentId();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed to delete payment.");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting payment: " + e.getMessage());
        }
    }

    @FXML
    void handleSearchPayment(ActionEvent event) {
        try {
            String searchText = txtSearch.getText().trim();
            if (searchText.isEmpty()) {
                loadAllPayments();
                return;
            }

            List<PaymentDTO> payments = paymentBO.searchPaymentsByStudentName(searchText);
            paymentList.setAll(payments);
            tblPayments.setItems(paymentList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error searching payments: " + e.getMessage());
        }
    }

    @FXML
    void handleViewAllPayments(ActionEvent event) {
        txtSearch.clear();
        loadAllPayments();
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        clearForm();
    }

    private PaymentDTO getPaymentDataFromForm() {
        String studentId = cmbStudent.getValue() != null ?
                cmbStudent.getValue().split(" - ")[0] : "";
        String courseId = cmbCourse.getValue() != null ?
                cmbCourse.getValue().split(" - ")[0] : "";

        return new PaymentDTO(
                txtPaymentId.getText(),
                studentId,
                courseId,
                Double.parseDouble(txtAmount.getText()),
                datePayment.getValue(),
                choiceMethod.getValue(),
                choiceStatus.getValue()
        );
    }

    private void setPaymentData(PaymentDTO payment) {
        txtPaymentId.setText(payment.getPaymentId());

        // Set student combo box
        for (String studentItem : studentList) {
            if (studentItem.startsWith(payment.getStudentId())) {
                cmbStudent.setValue(studentItem);
                break;
            }
        }

        // Set course combo box
        for (String courseItem : courseList) {
            if (courseItem.startsWith(payment.getCourseId())) {
                cmbCourse.setValue(courseItem);
                break;
            }
        }

        txtAmount.setText(String.valueOf(payment.getAmount()));
        datePayment.setValue(payment.getPaymentDate());
        choiceMethod.setValue(payment.getMethod());
        choiceStatus.setValue(payment.getStatus());
    }

    private boolean validateInput() {
        if (cmbStudent.getValue() == null || cmbStudent.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a student.");
            return false;
        }
        if (cmbCourse.getValue() == null || cmbCourse.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please select a course.");
            return false;
        }
        if (txtAmount.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter the amount.");
            return false;
        }
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.WARNING, "Amount must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Please enter a valid amount.");
            return false;
        }
        if (datePayment.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a payment date.");
            return false;
        }
        if (choiceMethod.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a payment method.");
            return false;
        }
        if (choiceStatus.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a payment status.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtPaymentId.clear();
        cmbStudent.getSelectionModel().clearSelection();
        cmbCourse.getSelectionModel().clearSelection();
        txtAmount.clear();
        datePayment.setValue(LocalDate.now());
        choiceMethod.setValue("Cash");
        choiceStatus.setValue("Pending");
        generateNextPaymentId();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Payment Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}