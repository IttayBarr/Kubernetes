package org.ittay.springcloud.msvc.users.msvc_ususers.services;

import org.ittay.springcloud.msvc.users.msvc_ususers.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void delete(Long id);

    Optional<User> findByEmail(String email);

    List<User> listAllById(Iterable<Long> ids);
}
