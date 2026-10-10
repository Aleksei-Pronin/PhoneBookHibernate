package ru.academits.phonebookhibernate.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.academits.phonebookhibernate.entity.ApplicationUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUserName(String userName);

    boolean existsByUserName(String userName);
}