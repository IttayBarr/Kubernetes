package org.ittay.springcloud.msvc.courses.services;

import org.ittay.springcloud.msvc.courses.clients.ClientRest;
import org.ittay.springcloud.msvc.courses.models.User;
import org.ittay.springcloud.msvc.courses.models.entity.Course;
import org.ittay.springcloud.msvc.courses.models.entity.CourseUser;
import org.ittay.springcloud.msvc.courses.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImp implements  CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAll() {
        return (List<Course>)courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByIdWithUsers(Long id) {
        Optional<Course> o = courseRepository.findById(id);

        if (o.isPresent()){
            Course course = o.get();

            if(!course.getCourseUsers().isEmpty()){
                List<Long> ids = course.getCourseUsers()
                        .stream()
                        .map(CourseUser::getUserId)
                        .toList();


                List<User> userByCourse = clientRest.getUserByCourse(ids);

                course.setUsers(userByCourse);

            }

            return Optional.of(course);



        }

        return Optional.empty();


    }

    @Override
    @Transactional
    public Course addCourse(Course curse) {
        return courseRepository.save(curse);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUser(Long id) {
      courseRepository.deleteCourseUser(id);
    }

    @Override
    @Transactional
    public Optional<User> addUserToCourse(User user, Long courseId) {
        Optional<Course> o = courseRepository.findById(courseId);
        if (o.isPresent()){
            User userMsvc = clientRest.getById(user.getId());
            Course course = o.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());
            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {
        Optional<Course> o = courseRepository.findById(courseId);
        if (o.isPresent()){
            User userMsvc = clientRest.addUser(user);
            Course course = o.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());
            course.addCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> removeUserFromCourse(User user, Long courseId) {
        Optional<Course> o = courseRepository.findById(courseId);
        if (o.isPresent()){
            User userMsvc = clientRest.getById(user.getId());
            Course course = o.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.deleteCourseUser(courseUser);
            courseRepository.save(course);

            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }
}
