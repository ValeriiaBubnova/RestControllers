package ru.itmentor.spring.boot_security.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return usersRepository.findAll();
    }
    @Transactional
    @Override
    public User findById(long id) {
        Optional<User> foundUser = usersRepository.findById(id);
        return foundUser.orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    @Transactional
    @Override
    public void save(User user) {
        log.info(user.toString());
        usersRepository.save(user);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Transactional
    @Override
    public void update(long id, User updatedUser) {
        User user = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
//        String newPassword = updatedUser.getPassword();
//        if (!updatedUser.getPassword().equals(newPassword)) {
//            updatedUser.setPassword(passwordEncoder.encode(newPassword));
//        } else {
            updatedUser.setPassword(user.getPassword());
        usersRepository.save(updatedUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        usersRepository.deleteById(id);
    }
}
