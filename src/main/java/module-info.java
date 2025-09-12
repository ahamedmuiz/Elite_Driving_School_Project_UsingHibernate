module com.driving_school_hibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires net.sf.jasperreports.core;
    requires java.mail;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens com.driving_school_hibernate.config to jakarta.persistence;
//    opens com.driving_school_hibernate.entity to org.hibernate.orm.core;

    opens com.driving_school_hibernate.controller to javafx.fxml;
//    opens com.driving_school_hibernate.dto.tm to javafx.base;

    exports com.driving_school_hibernate;
}