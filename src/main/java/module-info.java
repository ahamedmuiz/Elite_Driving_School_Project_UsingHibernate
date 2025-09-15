module com.driving_school_hibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires static lombok;
    requires net.sf.jasperreports.core;
    requires java.mail;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;
    requires java.sql;

    // ================= Hibernate Reflection =================
    // Hibernate needs access to @Entity classes via reflection
    opens com.driving_school_hibernate.entity
            to org.hibernate.orm.core, javafx.base;

    // ================= JavaFX FXML Controllers =================
    opens com.driving_school_hibernate.controller to javafx.fxml;

    // ================= JavaFX Table Models =================
    // If you use TableView models (DTO/TM classes) with bindings
//    opens com.driving_school_hibernate.dto.tm to javafx.base;

    // ================= Exports (for public usage) =================
    exports com.driving_school_hibernate;
    exports com.driving_school_hibernate.dto;
    exports com.driving_school_hibernate.entity;
    exports com.driving_school_hibernate.bo;
    exports com.driving_school_hibernate.dao;
}
