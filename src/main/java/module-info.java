module com.driving_school_hibernate {
    requires javafx.controls;
    requires javafx.fxml;

    // Hibernate & JPA
    requires org.hibernate.orm.core;
    requires java.persistence; // for javax.persistence
    requires java.sql;

    // Optional logging
    requires org.jboss.logging;
    requires javafx.graphics;
    requires static lombok;
    requires jbcrypt;

    // JavaFX exports/opens
    opens com.driving_school_hibernate to javafx.fxml;
    opens com.driving_school_hibernate.controller to javafx.fxml;
    opens com.driving_school_hibernate.dto to org.hibernate.orm.core;
    opens com.driving_school_hibernate.entity to org.hibernate.orm.core;

    exports com.driving_school_hibernate;
    exports com.driving_school_hibernate.controller;
}
