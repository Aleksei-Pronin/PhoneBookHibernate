package ru.academits.phonebookhibernate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.academits.phonebookhibernate.converter.UserDtoToUserConverter;
import ru.academits.phonebookhibernate.dto.ApplicationUserDto;
import ru.academits.phonebookhibernate.dto.BaseResponse;
import ru.academits.phonebookhibernate.security.UserRole;
import ru.academits.phonebookhibernate.service.MessageService;
import ru.academits.phonebookhibernate.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final MessageService messageService;

    @Value("${app.admin-registration-token:}")
    private String adminRegistrationToken;

    @PostMapping("/registration")
    public BaseResponse register(@Valid @RequestBody ApplicationUserDto user) {
        return userService.register(userDtoToUserConverter.convert(user), UserRole.ROLE_USER);
    }

    @PostMapping("/admin")
    public BaseResponse registerAdmin(@Valid @RequestBody ApplicationUserDto user,
                                      @RequestParam String token) {
        if (adminRegistrationToken.isBlank()) {
            return BaseResponse.error(messageService.getMessage("auth.admin-registration-off"));
        }

        if (!adminRegistrationToken.equals(token)) {
            return BaseResponse.error(messageService.getMessage("auth.invalid-admin-token"));
        }

        return userService.register(userDtoToUserConverter.convert(user), UserRole.ROLE_ADMIN);
    }
}