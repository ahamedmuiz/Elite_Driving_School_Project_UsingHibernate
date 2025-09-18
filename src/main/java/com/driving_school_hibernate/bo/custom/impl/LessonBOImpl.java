package com.driving_school_hibernate.bo.custom.impl;

import com.driving_school_hibernate.bo.custom.LessonBO;
import com.driving_school_hibernate.bo.util.EntityDTOConvertor;
import com.driving_school_hibernate.config.FactoryConfiguration;
import com.driving_school_hibernate.dto.LessonDTO;
import com.driving_school_hibernate.entity.CourseEntity;
import com.driving_school_hibernate.entity.InstructorEntity;
import com.driving_school_hibernate.entity.LessonEntity;
import com.driving_school_hibernate.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonBOImpl implements LessonBO {

    @Override
    public boolean saveLesson(LessonDTO dto) throws Exception {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();

            // load associations in same session
            StudentEntity student = session.get(StudentEntity.class, dto.getStudentId());
            CourseEntity course = session.get(CourseEntity.class, dto.getCourseId());
            InstructorEntity instructor = session.get(InstructorEntity.class, dto.getInstructorId());

            if (student == null || course == null || instructor == null) {
                throw new IllegalArgumentException("Student/Course/Instructor not found for given IDs.");
            }

            LessonEntity entity = EntityDTOConvertor.toEntity(dto, student, course, instructor);
            session.persist(entity);

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean updateLesson(LessonDTO dto) throws Exception {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();

            LessonEntity existing = session.get(LessonEntity.class, dto.getLessonId());
            if (existing == null) return false;

            StudentEntity student = session.get(StudentEntity.class, dto.getStudentId());
            CourseEntity course = session.get(CourseEntity.class, dto.getCourseId());
            InstructorEntity instructor = session.get(InstructorEntity.class, dto.getInstructorId());

            if (student == null || course == null || instructor == null) {
                throw new IllegalArgumentException("Student/Course/Instructor not found for given IDs.");
            }

            existing.setStudent(student);
            existing.setCourse(course);
            existing.setInstructor(instructor);
            existing.setDate(dto.getDate());
            existing.setTime(dto.getTime());
            existing.setStatus(dto.getStatus());

            session.merge(existing);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteLesson(String id) throws Exception {
        Transaction tx = null;
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            tx = session.beginTransaction();
            LessonEntity e = session.get(LessonEntity.class, id);
            if (e == null) return false;
            session.remove(e);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<LessonDTO> findAllLessons() throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<LessonEntity> entities = session.createQuery("FROM LessonEntity", LessonEntity.class).list();
            List<LessonDTO> list = new ArrayList<>();
            for (LessonEntity e : entities) {
                list.add(EntityDTOConvertor.fromEntity(e));
            }
            return list;
        }
    }

    @Override
    public List<LessonDTO> searchLessons(String studentName) throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            String param = "%" + studentName.toLowerCase() + "%";
            List<LessonEntity> entities = session.createQuery(
                            "select l from LessonEntity l where lower(l.student.firstName) like :nm or lower(l.student.lastName) like :nm",
                            LessonEntity.class)
                    .setParameter("nm", param)
                    .list();
            List<LessonDTO> list = new ArrayList<>();
            for (LessonEntity e : entities) list.add(EntityDTOConvertor.fromEntity(e));
            return list;
        }
    }

    @Override
    public String generateNewLessonId() throws Exception {
        try (Session session = FactoryConfiguration.getInstance().getSession()) {
            List<String> list = session.createQuery("select l.lessonId from LessonEntity l order by l.lessonId desc", String.class)
                    .setMaxResults(1)
                    .list();
            if (list.isEmpty()) {
                return "L001";
            } else {
                String last = list.get(0);
                // Extract digits
                String digits = last.replaceAll("\\D+", "");
                int num = 0;
                if (!digits.isEmpty()) {
                    num = Integer.parseInt(digits);
                }
                int next = num + 1;
                return String.format("L%03d", next);
            }
        }
    }
}
