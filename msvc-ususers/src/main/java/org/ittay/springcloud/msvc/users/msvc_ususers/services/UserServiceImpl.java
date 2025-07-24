package org.ittay.springcloud.msvc.users.msvc_ususers.services;

import org.ittay.springcloud.msvc.users.msvc_ususers.clients.CourseClientRest;
import org.ittay.springcloud.msvc.users.msvc_ususers.models.entity.User;
import org.ittay.springcloud.msvc.users.msvc_ususers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClientRest courseClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) this.userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String Email) {
        return this.userRepository.findByEmail(Email);
    }

    @Override

    @Transactional(readOnly = true)
    public List<User> listAllById(Iterable<Long> ids) {
        return (List<User>) userRepository.findAllById(ids);
    }


    @Transactional
    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }


    @Transactional
    @Override
    public void delete(Long id) {
        this.userRepository.deleteById(id);
        courseClientRest.deleteCourseUser(id);
    }


}
