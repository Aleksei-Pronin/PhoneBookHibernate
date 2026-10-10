package ru.academits.phonebookhibernate.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.academits.phonebookhibernate.dto.ApplicationUserDto;
import ru.academits.phonebookhibernate.entity.ApplicationUser;

@Service
@RequiredArgsConstructor
public class UserDtoToUserConverter implements Converter<ApplicationUserDto, ApplicationUser> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApplicationUser convert(ApplicationUserDto source) {
        ApplicationUser user = new ApplicationUser();

        user.setUserName(source.getUserName());
        user.setPassword(passwordEncoder.encode(source.getPassword()));

        return user;
    }
}