package com.driving_school_hibernate.bo;

import com.driving_school_hibernate.bo.custom.impl.*;
import com.driving_school_hibernate.dao.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? (boFactory = new BOFactory()) : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <Hello extends SuperBO> Hello getBO(BOTypes boType) {
        return switch (boType) {

            case STUDENT -> (Hello) new StudentBOImpl();
            case INSTRUCTOR -> (Hello) new InstructorBOImpl();
            case COURSE -> (Hello) new CourseBOImpl();
            case LESSON -> (Hello) new LessonBOImpl();
            case PAYMENT -> (Hello) new PaymentBOImpl();
            case USERS -> (Hello) new UsersBOImpl();


        };
    }

}