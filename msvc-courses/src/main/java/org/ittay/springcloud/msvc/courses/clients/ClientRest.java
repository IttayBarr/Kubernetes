package org.ittay.springcloud.msvc.courses.clients;


import org.ittay.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users",url = "${msvc.users.url}")
public interface ClientRest {

    @GetMapping("/{id}")
    User getById(@PathVariable Long id);

    @PostMapping("/")
    User addUser(@RequestBody User user);


    @GetMapping("/course-users")
    List<User> getUserByCourse(@RequestParam Iterable<Long> ids);




}
