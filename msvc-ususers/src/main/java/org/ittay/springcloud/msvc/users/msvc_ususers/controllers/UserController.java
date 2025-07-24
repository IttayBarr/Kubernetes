package org.ittay.springcloud.msvc.users.msvc_ususers.controllers;


import jakarta.validation.Valid;
import org.ittay.springcloud.msvc.users.msvc_ususers.models.entity.User;
import org.ittay.springcloud.msvc.users.msvc_ususers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById (@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if(userOptional.isPresent()){
           return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

   @PostMapping("/")
   @ResponseStatus(HttpStatus.CREATED)
   public ResponseEntity<?> addUser(@RequestBody @Valid User user, BindingResult result) {
        if (result.hasErrors()){
           return getMapResponseEntityErrors(result);
       }

        if( userService.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Collections.singletonMap("Message:","Email is Already in use")
            );
        }

       return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
   }




    @PutMapping("/{id}")

    public ResponseEntity<?>  editUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id){
        Optional<User> userOptional = userService.findById(id);
        if(result.hasErrors()){
            return getMapResponseEntityErrors(result);
        }
        if(userOptional.isPresent()){
            User userBd = userOptional.get();
            if(!userBd.getEmail().equalsIgnoreCase(user.getEmail()) && userService.findByEmail(user.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Collections.singletonMap("Message:","Email is Already in use")
                );
            }

            userBd.setName(user.getName());
            userBd.setEmail(user.getEmail());
            userBd.setPassword(user.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userBd));

        }

        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        if (user.isPresent()){
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return  ResponseEntity.notFound().build();

    }

    @GetMapping("/course-users")
    public ResponseEntity<?> getUserByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(userService.listAllById(ids));
    }


    private static ResponseEntity<Map<String, String>> getMapResponseEntityErrors(BindingResult result) {
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Idk error",
                        (msg1, msg2) -> msg1 + "; " + msg2
                ));

        return ResponseEntity.badRequest().body(errors);
    }




}
