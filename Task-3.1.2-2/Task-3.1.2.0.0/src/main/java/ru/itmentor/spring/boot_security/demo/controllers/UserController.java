package ru.itmentor.spring.boot_security.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repositories.RolesRepository;
import ru.itmentor.spring.boot_security.demo.service.RolesService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
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
    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        log.info("Запрос на отображение юзера");
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

//страница юзера
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public String indexOfUser(@PathVariable("id") long id, Model model) {
        log.info("Запрос на доступ к пользователю с id = {}", id);
        model.addAttribute("user", userService.findById(id));
        return "user/show";
    }
    //добавление юзера
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/new")
    public String createUserForm (@ModelAttribute("user") User user, Model model){
        log.info("Форма для создания юзера");
        model.addAttribute("roles", rolesService.findAll());
        return "admin/form";
    }
    //сохранение юзера
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Ошибка при сохранении пользователя с данными: {}", user);
            return "admin/form";
        }
        log.info("Юзер успешно сохранен с id {} ", user.getId());
        userService.save(user);
        return "redirect:/admin/users";
    }

    // редактирование юзера
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model) {
        log.info("Форма для редактирования юзера");
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", rolesService.findAll());
        return "admin/form";
    }

    //
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid @ModelAttribute("user") User updatedUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Ошибка при обновлении пользователя с id = {}: {}", id, updatedUser);
            return "admin/form";
        }
        updatedUser.setId(id);
        userService.update(id, updatedUser);
        log.info("Юзер с id = {} обновлен", id);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        log.info("Пользователь с id = {} удален", id);
        return "redirect:/admin/users";

    }
}
