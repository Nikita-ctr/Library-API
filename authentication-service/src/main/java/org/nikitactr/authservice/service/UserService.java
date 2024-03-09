package org.nikitactr.authservice.service;

import lombok.extern.log4j.Log4j2;
import org.nikitactr.authservice.jwt.JwtUtils;
import org.nikitactr.authservice.model.ERole;
import org.nikitactr.authservice.model.Role;
import org.nikitactr.authservice.model.User;
import org.nikitactr.authservice.payload.request.LoginRequest;
import org.nikitactr.authservice.payload.request.SignUpRequest;
import org.nikitactr.authservice.payload.response.JWTResponse;
import org.nikitactr.authservice.payload.response.MessageResponse;
import org.nikitactr.authservice.repository.UserRepository;
import org.nikitactr.authservice.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        Set<Role> roles = new HashSet<>();

        if (checkUserExists(username, email)) {
          return  ResponseEntity.badRequest().body(new MessageResponse("User already exists!"));
        }
        User user = User.builder()
                .email(email)
                .username(username)
                .password(encoder.encode(password))
                .build();

        roles.add(new Role(ERole.ROLE_USER));
        user.setRoles(roles);
        userRepository.save(user);
        log.info("User registered successfully: {}", username);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        log.info("User authenticated successfully: {}", username);

        return ResponseEntity.ok(createJwtResponse(userDetails));
    }

    private Boolean checkUserExists(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            log.error("Username already taken: {}", username);
            return true;
        }
        if (userRepository.existsByEmail(email)) {
            log.error("Email already taken: {}", email);
            return true;
        }
        return false;
    }

    private JWTResponse createJwtResponse(CustomUserDetails userDetails) {
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return JWTResponse.builder()
                .type("Bearer")
                .email(userDetails.getEmail())
                .username(userDetails.getUsername())
                .id(userDetails.getId())
                .token(jwt)
                .roles(roles)
                .build();
    }
}
