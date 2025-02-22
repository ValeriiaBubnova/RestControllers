package ru.itmentor.spring.boot_security.demo.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User,Long> {
@EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);
}
