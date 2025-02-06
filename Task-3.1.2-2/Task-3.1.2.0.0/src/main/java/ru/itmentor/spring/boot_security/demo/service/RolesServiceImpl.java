package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.repositories.RolesRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;
    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    @Override
    public Role save(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public Set<Role> findAll() {
        return  new HashSet<>(rolesRepository.findAll());
    }

    @Override
    public Optional<Role> findById(Long id) {
        return rolesRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        rolesRepository.deleteById(id);

    }

    @Override
    public Optional<Role> findByName(String name) {
        return rolesRepository.findByName(name);
    }
}
