package ru.itmentor.spring.boot_security.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RolesService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;
    private final RolesService rolesService;

    @Autowired
    public UserController(UserService userService, RolesService rolesService) {
        this.userService = userService;
        this.rolesService = rolesService;
    }

//просмотр всех юзеров
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<User>> showUsers() {
        log.info("Запрос на отображение юзера");
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users); //200
    }

//страница юзера
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<User> indexOfUser(@PathVariable("id") long id) {
        log.info("Запрос на доступ к пользователю с id = {}", id);
        User user = userService.findById(id);
        if (user == null) {
        return  ResponseEntity.notFound().build(); //404 - не найден
        }
        return  ResponseEntity.ok(user);  //200 - все ок  //return new ResponseEntity<>(user, HttpStatus.OK);
    }
    //сохранение юзера
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/new")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Юзер успешно сохранен с id {} ", user.getId());
        Set<Role> roles = user.getRoles();
        if (roles != null && !roles.isEmpty()) {
            user.setRoles(roles);
        }
        userService.save(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(user); //201 успешно сохранен
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("admin/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @Valid @RequestBody User updatedUser) {
       User existingUser = userService.findById(id);
       if (existingUser == null) {
           return  ResponseEntity.notFound().build(); //404
       }
       updatedUser.setId(id);
        Set<Role> roles = updatedUser.getRoles();
        if (roles != null && !roles.isEmpty()) {
            updatedUser.setRoles(roles);
        }
       userService.save(updatedUser);
       return  ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("admin/{id}/delete")
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return  ResponseEntity.notFound().build(); //404
        }
        userService.delete(id);
        log.info("Пользователь с id = {} удален", id);
        return ResponseEntity.noContent().build(); //204 -успешно удален пустой ответ

    }
}
