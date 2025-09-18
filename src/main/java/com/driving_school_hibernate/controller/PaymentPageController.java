package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.custom.PaymentBO;
import com.driving_school_hibernate.dto.PaymentDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.entity.StudentEntity;
import com.driving_school_hibernate.config.FactoryConfiguration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaymentPageController {

    @FXML
    private ComboBox<StudentEntity> cmbStudent;   // FIXED: was String
    @FXML
    private ComboBox<CourseEntity> cmbCourse;     // FIXED: was String

    @FXML
    private ChoiceBox<String> choiceMethod, choiceStatus;

    @FXML
    private TableView<PaymentDTO> tblPayments;

    @FXML
    private TableColumn<PaymentDTO, String> colPaymentId, colStudent, colCourse, colMethod, colStatus;

    @FXML
    private TableColumn<PaymentDTO, Double> colAmount;

    @FXML
    private TableColumn<PaymentDTO, LocalDate> colDate;

    @FXML
    private TextField txtPaymentId, txtAmount, txtSearch;

    @FXML
    private DatePicker datePayment;

    private final PaymentBO paymentBO = BOFactory.getInstance().getBO(com.driving_school_hibernate.bo.BOTypes.PAYMENT);

    @FXML
    public void initialize() {
        txtPaymentId.setEditable(false);
        txtPaymentId.setDisable(true);

        loadNewPaymentId();

        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadStudents();
        loadCourses();

        try {
            loadAllPayments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNewPaymentId() {
        try {
            txtPaymentId.setText(paymentBO.generateNewPaymentId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudents() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<StudentEntity> students = session.createQuery("from StudentEntity", StudentEntity.class).list();
        session.close();

        ObservableList<StudentEntity> list = FXCollections.observableArrayList(students);
        cmbStudent.setItems(list);

        // Display "ID - Name"
        cmbStudent.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(StudentEntity student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getStudentId() + " - " + student.getFirstName() + " " + student.getLastName());
                }
            }
        });

        cmbStudent.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StudentEntity student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getStudentId() + " - " + student.getFirstName() + " " + student.getLastName());
                }
            }
        });
    }

    private void loadCourses() {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<CourseEntity> courses = session.createQuery("from CourseEntity", CourseEntity.class).list();
        session.close();

        ObservableList<CourseEntity> list = FXCollections.observableArrayList(courses);
        cmbCourse.setItems(list);

        // Display "ID - Name"
        cmbCourse.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CourseEntity course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getCourseId() + " - " + course.getName());
                }
            }
        });

        cmbCourse.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CourseEntity course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getCourseId() + " - " + course.getName());
                }
            }
        });
    }

    private void loadAllPayments() throws SQLException {
        List<PaymentDTO> payments = paymentBO.findAllPayments();
        tblPayments.setItems(FXCollections.observableArrayList(payments));
    }

    @FXML
    void handleAddPayment(ActionEvent event) {
        try {
            StudentEntity student = cmbStudent.getValue();
            CourseEntity course = cmbCourse.getValue();

            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    student.getStudentId(),
                    course.getCourseId(),
                    Double.parseDouble(txtAmount.getText()),
                    datePayment.getValue(),
                    choiceMethod.getValue(),
                    choiceStatus.getValue()
            );

            if (paymentBO.savePayment(dto)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Added!").show();
                loadAllPayments();
                loadNewPaymentId();
                handleClearForm(null);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdatePayment(ActionEvent event) {
        try {
            StudentEntity student = cmbStudent.getValue();
            CourseEntity course = cmbCourse.getValue();

            PaymentDTO dto = new PaymentDTO(
                    txtPaymentId.getText(),
                    student.getStudentId(),
                    course.getCourseId(),
                    Double.parseDouble(txtAmount.getText()),
                    datePayment.getValue(),
                    choiceMethod.getValue(),
                    choiceStatus.getValue()
            );

            if (paymentBO.updatePayment(dto)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Updated!").show();
                loadAllPayments();
                handleClearForm(null);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeletePayment(ActionEvent event) {
        try {
            if (paymentBO.deletePayment(txtPaymentId.getText())) {
                new Alert(Alert.AlertType.CONFIRMATION, "Payment Deleted!").show();
                loadAllPayments();
                loadNewPaymentId();
                handleClearForm(null);
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void handleSearchPayment(ActionEvent event) {
        try {
            List<PaymentDTO> list = paymentBO.searchPaymentsByStudentName(txtSearch.getText());
            tblPayments.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleViewAllPayments(ActionEvent event) {
        try {
            loadAllPayments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        cmbStudent.setValue(null);
        cmbCourse.setValue(null);
        txtAmount.clear();
        datePayment.setValue(null);
        choiceMethod.setValue(null);
        choiceStatus.setValue(null);
    }
}
