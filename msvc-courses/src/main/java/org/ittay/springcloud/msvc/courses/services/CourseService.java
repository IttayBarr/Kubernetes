package org.ittay.springcloud.msvc.courses.services;

import org.ittay.springcloud.msvc.courses.models.User;
import org.ittay.springcloud.msvc.courses.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> getAll();

    Optional<Course> findById(Long id);

    Optional<Course> findByIdWithUsers(Long id);
    Course addCourse(Course curse);

    void delete(Long id);

    void deleteCourseUser(Long id);
    Optional<User> addUserToCourse(User user, Long courseId);

    Optional<User> createUser(User user, Long courseId);

    Optional<User> removeUserFromCourse(User user, Long courseId);
}
