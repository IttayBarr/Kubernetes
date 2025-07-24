package org.ittay.springcloud.msvc.courses.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.ittay.springcloud.msvc.courses.models.User;
import org.ittay.springcloud.msvc.courses.models.entity.Course;
import org.ittay.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Course>> getAll(){
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        Optional<Course> course = courseService.findById(id);
        if(course.isPresent()){

            return ResponseEntity.ok(course.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody
                                           @Valid
                                           Course curse, BindingResult validateObj){

        if(validateObj.hasErrors()){
            return getMapResponseEntityErrors(validateObj);
        }
        Course c = courseService.addCourse(curse);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@RequestBody
                                              @Valid Course course, BindingResult validObject, @PathVariable Long id){
        if (validObject.hasErrors()){
            return getMapResponseEntityErrors(validObject);
        }
        Optional<Course> courseDb = courseService.findById(id);
        if (courseDb.isPresent()){
            courseDb.get().setName(course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    courseService.addCourse(courseDb.get())
            );
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")

    public ResponseEntity<?> deleteCourse(@PathVariable Long id){
        Optional<Course> courseDb = courseService.findById(id);
        if (courseDb.isPresent()){
            courseService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignCourse(@RequestBody User user,
                                          @PathVariable Long courseId){
        Optional<User> o;
        try {
            o = courseService.addUserToCourse(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message:",e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody User user,
                                          @PathVariable Long courseId){
        Optional<User> o;
        try {
            o = courseService.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message:",e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }
    @DeleteMapping("/assign-user/{courseId}")
    public ResponseEntity<?> unAssignCourse(@RequestBody User user,
                                          @PathVariable Long courseId){
        Optional<User> o;
        try {
            o = courseService.removeUserFromCourse(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Message:",e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/usersByCourse/{id}")
    public ResponseEntity<?> courseWithUsers(@PathVariable Long id){
        Optional<Course> course = courseService.findByIdWithUsers(id);
        if(course.isPresent()){

            return ResponseEntity.ok(course.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/course-user/{id}")
    public ResponseEntity<?> deleteCourseUser(@PathVariable Long id){
        courseService.deleteCourseUser(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> getMapResponseEntityErrors(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Unknown error",
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        return ResponseEntity.badRequest().body(errors);
    }






}
