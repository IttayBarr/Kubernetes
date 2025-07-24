package org.ittay.springcloud.msvc.courses.models.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@EqualsAndHashCode(of = "userId")
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "courses_users")
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_id", unique = true )
    private Long userId;



}
