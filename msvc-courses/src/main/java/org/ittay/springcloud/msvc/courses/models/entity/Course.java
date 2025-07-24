package org.ittay.springcloud.msvc.courses.models.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.ittay.springcloud.msvc.courses.models.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(unique = true)
    private String name;


    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CourseUser> courseUsers;


    @Transient
    private List<User> users;

    public Course() {
        courseUsers =  new ArrayList<CourseUser>() ;
        users = new ArrayList<User>();
    }


    public void addCourseUser(CourseUser courseUser){
        courseUsers.add(courseUser);
    }

    public void deleteCourseUser(CourseUser courseUser){
        courseUsers.remove(courseUser);
    }

}
