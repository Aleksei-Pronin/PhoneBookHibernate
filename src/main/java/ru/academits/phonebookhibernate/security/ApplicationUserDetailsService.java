package ru.academits.phonebookhibernate.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.academits.phonebookhibernate.dao.UserRepository;
import ru.academits.phonebookhibernate.service.MessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MessageService messageService;

    @Transactional(readOnly = true)
    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username)
                .map(user -> new ApplicationUserDetails(
                        user.getId(),
                        user.getUserName(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole().getName()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageService.getMessage("user.not-found")
                ));
    }
}