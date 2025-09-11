package com.driving_school_hibernate.config;

import com.driving_school_hibernate.entity.Student;
import com.driving_school_hibernate.entity.Course;
import com.driving_school_hibernate.entity.Instructor;
import com.driving_school_hibernate.entity.Lesson;
import com.driving_school_hibernate.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate SessionFactory singleton for desktop app (Hibernate 5.6)
 */
public class FactoryConfiguration {

    private static FactoryConfiguration factoryConfiguration;
    private SessionFactory sessionFactory;

    private FactoryConfiguration() {
        Configuration configuration = new Configuration();
        configuration.configure(); // loads hibernate.cfg.xml

        // Register entity classes
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(Instructor.class);
        configuration.addAnnotatedClass(Lesson.class);
        configuration.addAnnotatedClass(Payment.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() {
        if (factoryConfiguration == null) {
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }

    // Open a new session (not thread-safe)
    public Session getSession() {
        return sessionFactory.openSession();
    }

    // Get current session (thread-bound)
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    // Close SessionFactory
    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
