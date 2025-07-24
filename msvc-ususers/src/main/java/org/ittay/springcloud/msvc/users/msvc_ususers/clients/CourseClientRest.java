package org.ittay.springcloud.msvc.users.msvc_ususers.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-courses", url = "msvc-courses:8002")
public interface CourseClientRest {

    @DeleteMapping("/course-user/{id}")
    void deleteCourseUser(@PathVariable Long id);

}
