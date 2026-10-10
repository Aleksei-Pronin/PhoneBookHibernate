package ru.academits.phonebookhibernate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.academits.phonebookhibernate.dao.RoleRepository;
import ru.academits.phonebookhibernate.dao.UserRepository;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.security.UserRole;
import ru.academits.phonebookhibernate.entity.ApplicationUser;
import ru.academits.phonebookhibernate.entity.Role;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public BaseResponse register(ApplicationUser user, UserRole userRole) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return BaseResponse.error(messageService.getMessage("user.already-exists"));
        }

        Role role = roleRepository.findByName(userRole.name()).orElse(null);

        if (role == null) {
            return BaseResponse.error(messageService.getMessage("role.not-found"));
        }

        user.setRole(role);
        userRepository.save(user);

        return BaseResponse.ok();
    }
}