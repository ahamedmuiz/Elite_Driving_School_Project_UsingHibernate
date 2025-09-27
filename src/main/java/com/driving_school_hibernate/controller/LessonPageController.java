package com.driving_school_hibernate.controller;

import com.driving_school_hibernate.bo.BOFactory;
import com.driving_school_hibernate.bo.BOTypes;
import com.driving_school_hibernate.bo.custom.LessonBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dto.CourseDTO;
import com.driving_school_hibernate.dto.InstructorDTO;
import com.driving_school_hibernate.dto.LessonDTO;
import com.driving_school_hibernate.dto.StudentDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.entity.InstructorEntity;
import com.driving_school_hibernate.entity.StudentEntity;
import com.driving_school_hibernate.util.AuthUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.hibernate.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LessonPageController {

    @FXML private TableView<LessonDTO> tblLessons;
    @FXML private TableColumn<LessonDTO, String> colLessonId, colStudent, colCourse, colInstructor, colDate, colTime, colStatus;

    @FXML private TextField txtLessonId, txtTime, txtSearch;
    @FXML private DatePicker dateLesson;
    @FXML private ComboBox<StudentDTO> cmbStudent;
    @FXML private ComboBox<CourseDTO> cmbCourse;
    @FXML private ComboBox<InstructorDTO> cmbInstructor;
    @FXML private ChoiceBox<String> choiceStatus;

    @FXML private Button btnDelete;

    private final LessonBO lessonBO = BOFactory.getInstance().getBO(BOTypes.LESSON);
    private final ObservableList<LessonDTO> lessonList = FXCollections.observableArrayList();

    private final ObservableList<StudentDTO> studentItems = FXCollections.observableArrayList();
    private final ObservableList<CourseDTO> courseItems = FXCollections.observableArrayList();
    private final ObservableList<InstructorDTO> instructorItems = FXCollections.observableArrayList();

    private final Map<String, String> studentNameMap = new HashMap<>();
    private final Map<String, String> courseNameMap = new HashMap<>();
    private final Map<String, String> instructorNameMap = new HashMap<>();

    private LessonDTO selectedLesson = null;

    @FXML
    public void initialize() {

        txtLessonId.setEditable(false);
        txtLessonId.setDisable(true);

        applyRoleBasedAccess();

        colLessonId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLessonId()));
        colStudent.setCellValueFactory(c ->
                new SimpleStringProperty(studentNameMap.getOrDefault(c.getValue().getStudentId(), c.getValue().getStudentId())));
        colCourse.setCellValueFactory(c ->
                new SimpleStringProperty(courseNameMap.getOrDefault(c.getValue().getCourseId(), c.getValue().getCourseId())));
        colInstructor.setCellValueFactory(c ->
                new SimpleStringProperty(instructorNameMap.getOrDefault(c.getValue().getInstructorId(), c.getValue().getInstructorId())));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDate() == null ? "" : c.getValue().getDate().toString()));
        colTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTime()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus()));

        tblLessons.setItems(lessonList);

        choiceStatus.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));

        loadCombosAndMaps();
        loadLessons();

        tblLessons.setOnMouseClicked(e -> {
            LessonDTO dto = tblLessons.getSelectionModel().getSelectedItem();
            if (dto != null) {
                selectedLesson = dto;
                populateFormFromLesson(dto);
            }
        });

        cmbStudent.setConverter(new StringConverter<>() {
            @Override
            public String toString(StudentDTO s) {
                return s == null ? "" : s.getFirstName() + " " + s.getLastName() + " (" + s.getStudentId() + ")";
            }
            @Override
            public StudentDTO fromString(String string) { return null; }
        });
        cmbCourse.setConverter(new StringConverter<>() {
            @Override
            public String toString(CourseDTO c) {
                return c == null ? "" : c.getName() + " (" + c.getCourseId() + ")";
            }
            @Override
            public CourseDTO fromString(String string) { return null; }
        });
        cmbInstructor.setConverter(new StringConverter<>() {
            @Override
            public String toString(InstructorDTO i) {
                return i == null ? "" : i.getFirstName() + " " + i.getLastName() + " (" + i.getInstructorId() + ")";
            }
            @Override
            public InstructorDTO fromString(String string) { return null; }
        });
    }

    private void applyRoleBasedAccess() {
        if (!AuthUtil.isAdmin()) {
            btnDelete.setDisable(true);
        }
    }
    private void loadCombosAndMaps() {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {

            List<StudentEntity> students = session.createQuery("FROM StudentEntity", StudentEntity.class).list();
            studentItems.clear();
            studentNameMap.clear();
            for (StudentEntity s : students) {
                StudentDTO dto = EntityDTOConvertor.fromEntity(s);
                studentItems.add(dto);
                studentNameMap.put(dto.getStudentId(), dto.getFirstName() + " " + dto.getLastName());
            }
            cmbStudent.setItems(studentItems);


            List<CourseEntity> courses = session.createQuery("FROM CourseEntity", CourseEntity.class).list();
            courseItems.clear();
            courseNameMap.clear();
            for (CourseEntity c : courses) {
                CourseDTO dto = EntityDTOConvertor.fromEntity(c);
                courseItems.add(dto);
                courseNameMap.put(dto.getCourseId(), dto.getName());
            }
            cmbCourse.setItems(courseItems);


            List<InstructorEntity> instructors = session.createQuery("FROM InstructorEntity", InstructorEntity.class).list();
            instructorItems.clear();
            instructorNameMap.clear();
            for (InstructorEntity i : instructors) {
                InstructorDTO dto = EntityDTOConvertor.fromEntity(i);
                instructorItems.add(dto);
                instructorNameMap.put(dto.getInstructorId(), dto.getFirstName() + " " + dto.getLastName());
            }
            cmbInstructor.setItems(instructorItems);
        } catch (Exception e) {
            showError("Failed to load students/courses/instructors: " + e.getMessage());
        }

        try {
            txtLessonId.setText(lessonBO.generateNewLessonId());
        } catch (Exception e) {
            showError("Failed to generate Lesson ID: " + e.getMessage());
            txtLessonId.setText("L001");
        }
    }

    private void loadLessons() {
        try {
            List<LessonDTO> lessons = lessonBO.findAllLessons();
            lessonList.setAll(lessons);
        } catch (Exception e) {
            showError("Failed to load lessons: " + e.getMessage());
        }
    }

    private void populateFormFromLesson(LessonDTO dto) {
        txtLessonId.setText(dto.getLessonId());
        txtTime.setText(dto.getTime());
        dateLesson.setValue(dto.getDate() == null ? null : dto.getDate());
        choiceStatus.setValue(dto.getStatus());

        Optional<StudentDTO> sopt = studentItems.stream()
                .filter(s -> s.getStudentId().equals(dto.getStudentId())).findFirst();
        sopt.ifPresent(cmbStudent::setValue);

        Optional<CourseDTO> copt = courseItems.stream()
                .filter(c -> c.getCourseId().equals(dto.getCourseId())).findFirst();
        copt.ifPresent(cmbCourse::setValue);

        Optional<InstructorDTO> iopt = instructorItems.stream()
                .filter(i -> i.getInstructorId().equals(dto.getInstructorId())).findFirst();
        iopt.ifPresent(cmbInstructor::setValue);
    }

    private boolean validateForm() {
        if (cmbStudent.getValue() == null || cmbCourse.getValue() == null || cmbInstructor.getValue() == null
                || dateLesson.getValue() == null || txtTime.getText().trim().isEmpty() || choiceStatus.getValue() == null) {
            showError("All fields must be filled.");
            return false;
        }
        if (txtTime.getText().trim().length() < 3) {
            showError("Enter a valid time.");
            return false;
        }
        return true;
    }

    private LessonDTO getFormDTO() {
        String lid = txtLessonId.getText();
        String studentId = cmbStudent.getValue().getStudentId();
        String courseId = cmbCourse.getValue().getCourseId();
        String instructorId = cmbInstructor.getValue().getInstructorId();
        LocalDate date = dateLesson.getValue();
        String time = txtTime.getText().trim();
        String status = choiceStatus.getValue();
        return new LessonDTO(lid, studentId, courseId, instructorId, date, time, status);
    }

    @FXML
    void handleAddLesson(javafx.event.ActionEvent event) {
        try {
            if (!validateForm()) return;

            if (selectedLesson != null && selectedLesson.getLessonId().equals(txtLessonId.getText())) {
                showError("Cannot add while a table row is selected. Clear selection first or press Clear.");
                return;
            }

            LessonDTO dto = getFormDTO();
            if (lessonBO.saveLesson(dto)) {
                showInfo("Lesson added.");
                loadLessons();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Add failed: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdateLesson(javafx.event.ActionEvent event) {
        try {
            if (!validateForm()) return;

            if (selectedLesson == null) {
                showError("No lesson selected to update.");
                return;
            }

            LessonDTO dto = getFormDTO();

            boolean changed = !Objects.equals(dto.getStudentId(), selectedLesson.getStudentId())
                    || !Objects.equals(dto.getCourseId(), selectedLesson.getCourseId())
                    || !Objects.equals(dto.getInstructorId(), selectedLesson.getInstructorId())
                    || !Objects.equals(dto.getDate(), selectedLesson.getDate())
                    || !Objects.equals(dto.getTime(), selectedLesson.getTime())
                    || !Objects.equals(dto.getStatus(), selectedLesson.getStatus());

            if (!changed) {
                showInfo("Nothing to update. No changes detected.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to update this lesson?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (lessonBO.updateLesson(dto)) {
                showInfo("Lesson updated.");
                loadLessons();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteLesson(javafx.event.ActionEvent event) {
        try {
            if (selectedLesson == null) {
                showError("No lesson selected to delete.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this lesson?", ButtonType.YES, ButtonType.NO);
            if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.NO) return;

            if (lessonBO.deleteLesson(selectedLesson.getLessonId())) {
                showInfo("Lesson deleted.");
                loadLessons();
                handleClearForm(null);
            }
        } catch (Exception e) {
            showError("Delete failed: " + e.getMessage());
        }
    }

    @FXML
    void handleSearchLesson(javafx.event.ActionEvent event) {
        try {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                showError("Please enter a student name to search.");
                return;
            }
            List<LessonDTO> results = lessonBO.searchLessons(keyword);
            lessonList.setAll(results);
        } catch (Exception e) {
            showError("Search failed: " + e.getMessage());
        }
    }

    @FXML
    void handleViewAllLessons(javafx.event.ActionEvent event) {
        loadLessons();
    }

    @FXML
    void handleClearForm(javafx.event.ActionEvent event) {
        txtTime.clear();
        dateLesson.setValue(null);
        txtSearch.clear();
        choiceStatus.setValue(null);
        cmbStudent.setValue(null);
        cmbCourse.setValue(null);
        cmbInstructor.setValue(null);
        selectedLesson = null;
        try {
            txtLessonId.setText(lessonBO.generateNewLessonId());
        } catch (Exception e) {
            txtLessonId.setText("L001");
        }
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
