module com.driving_school_hibernate {
    requires javafx.controls;
    requires javafx.fxml;

    // Hibernate & JPA
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;

    // Optional logging
    requires org.jboss.logging;
    requires javafx.graphics;

    // JavaFX exports/opens
    opens com.driving_school_hibernate to javafx.fxml;
    opens com.driving_school_hibernate.controller to javafx.fxml;

    exports com.driving_school_hibernate;
    exports com.driving_school_hibernate.controller;
}
