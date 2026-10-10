package ru.academits.phonebookhibernate.service;

import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.entity.ApplicationUser;
import ru.academits.phonebookhibernate.security.UserRole;

public interface UserService {
    BaseResponse register(ApplicationUser user, UserRole userRole);
}