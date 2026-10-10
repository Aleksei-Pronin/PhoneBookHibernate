package ru.academits.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academits.phonebookhibernate.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}