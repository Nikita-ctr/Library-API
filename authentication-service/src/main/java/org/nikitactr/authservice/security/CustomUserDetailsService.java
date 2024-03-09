package org.nikitactr.authservice.security;

import lombok.RequiredArgsConstructor;
import org.nikitactr.authservice.exception.UserNotFoundException;
import org.nikitactr.authservice.model.User;
import org.nikitactr.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User Name " + username + " not found"));

        return new CustomUserDetails(user);
    }
}
