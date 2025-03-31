package com.maurooyhanart.surveyq.session.user;

import com.maurooyhanart.surveyq.shared.HttpClient;
import com.maurooyhanart.surveyq.shared.log.HttpLogger;
import com.maurooyhanart.surveyq.session.user.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final HttpLogger httpLogger;
    private final UserRepository userRepository;
    private final HttpClient backendClient;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, HttpClient backendClient, HttpLogger httpLogger
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.backendClient = backendClient;
        this.httpLogger = httpLogger;
    }

    public User createUser(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEnabled(true);

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(userRole);

        userRepository.save(user);

        logger.info("User saved: {}", user);
        httpLogger.httpLog("session", "User saved: " + user, "INFO");
        return user;
    }
}